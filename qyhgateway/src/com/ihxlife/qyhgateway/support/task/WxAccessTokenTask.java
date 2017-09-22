package com.ihxlife.qyhgateway.support.task;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.ihxlife.qyhgateway.dto.AccesstokenInfo;
import com.ihxlife.qyhgateway.service.AccessTokenInfoService;
import com.ihxlife.qyhgateway.support.common.LoggerStartAndEnd;
import com.ihxlife.qyhgateway.support.constant.PropertiesConstants;
import com.ihxlife.qyhgateway.support.constant.RedisConstant;
import com.ihxlife.qyhgateway.support.redis.RedisUtil;
import com.ihxlife.qyhgateway.support.spring.SpringBeanRegisterUtil;
import com.ihxlife.qyhgateway.support.util.GenerateRandomUtils;
import com.ihxlife.qyhgateway.support.util.HttpUtil;
import com.ihxlife.qyhgateway.support.util.WeChatUtil;
import com.ihxlife.qyhgateway.support.variable.DataVariable;

/**
 * 微信AccessToken定时任务
 * @author Administrator
 *
 */
public class WxAccessTokenTask {
	
	// 日志打印
	private static final Logger logger = LoggerFactory.getLogger(WxAccessTokenTask.class);
	
	// access_token跑批成功标识
	private static boolean access_token_lisener = false;
	
	// ticket跑批成功标识
	private static boolean ticket_lisener = false;
	
	// 腾讯获取ticket接口地址
	private static final String QYWX_URL_GET_JSAPITICKET = PropertiesConstants.QYWX_URL_GET_JSAPITICKET;
	
	// 腾讯获取access_token接口地址
	private static final String QYWX_URL_GET_ACCESS_TOKEN = PropertiesConstants.QYWX_URL_GET_ACCESS_TOKEN;
	
	// 获取access_token的corpid
	private static final String ACCESSTOKEN_CORPID = PropertiesConstants.ACCESSTOKEN_CORPID;
	
	// 获取access_token的secret
	private static final String ACCESSTOKEN_SECRET = PropertiesConstants.ACCESSTOKEN_SECRET;
	
	// redis中获取access_token的key
	private static final String ACCESSTOKEN_KEY = RedisConstant.ACCESS_TOKEN_KEY;
	
	// redis中获取JSAPITICKET的key
	private static final String JSAPI_TICKET_KEY = RedisConstant.JSAPI_TICKET_KEY;
	
	//accessToken的主键id
	private static final String ACCESS_TOKEN_ID = "0000000001";
	
	@Autowired
	private AccessTokenInfoService tokenService;

	public static synchronized boolean isAccess_token_lisener() {
		return access_token_lisener;
	}

	public static synchronized void setAccess_token_lisener(boolean access_token_lisener) {
		WxAccessTokenTask.access_token_lisener = access_token_lisener;
	}

	public static synchronized boolean isTicket_lisener() {
		return ticket_lisener;
	}

	public static synchronized void setTicket_lisener(boolean ticket_lisener) {
		WxAccessTokenTask.ticket_lisener = ticket_lisener;
	}

	/**
	 * 更新access_token到数据变量类中
	 */
	public void readAccessToken2DataVariable() {
		// 生成随机数作为当前线程流水号
		String randomNum = GenerateRandomUtils.getCharAndNumr(20);
		Thread.currentThread().setName(randomNum);
		LoggerStartAndEnd.loggerStartOrFinish(logger, "WxAccessTokenTask", "readAccessToken2DataVariable", "开始");
		String result = "";
		JSONObject resultJson = null;
		String url = QYWX_URL_GET_ACCESS_TOKEN;
		try {
			// 修改请求腾讯获取access_token的地址参数
			url = url.replace("CorpID", ACCESSTOKEN_CORPID).replace("SECRET", ACCESSTOKEN_SECRET);
			logger.info("请求腾讯获取access_token链接【{}】", url);
			// 接收腾讯返回的结果
			result = HttpUtil.get(url);
			logger.info("更新access_token返回【{}】", result);
			
			if (StringUtils.isBlank(result)) {
				logger.info("请求腾讯返回为空！");
				// return前进入finally模块
				return;
			}
			
			resultJson = JSONObject.parseObject(result);
			// 参数为空标识
			if ( resultJson.get("access_token") == null
					|| StringUtils.isBlank(resultJson.getString("access_token"))) {
				logger.info("请求腾讯失败");
				// return前进入finally模块
				return;
			}
			
			String access_token = resultJson.getString("access_token");
			String expires_in = resultJson.getString("expires_in");
			logger.info("accesstoken为【{}】", access_token);
			
			if(SpringBeanRegisterUtil.isBeanNameUse("dataService4DBImpl")){
				AccesstokenInfo token = tokenService.getById(ACCESS_TOKEN_ID);
				token.setAccessToken(access_token);
				token.setTokenExpiresIn(expires_in);
				logger.info("数据库形式存储access_token");
				if(tokenService.updateToken(token)){
					logger.info("数据库形式存储access_token成功");
				}
			}else if(SpringBeanRegisterUtil.isBeanNameUse("dataService4RedisImpl")){
				if(RedisUtil.isExists(ACCESSTOKEN_KEY)){
					RedisUtil.updateResisInfo(ACCESSTOKEN_KEY, access_token);
				}else{
					RedisUtil.insertResisInfo(ACCESSTOKEN_KEY, access_token);
				}
				logger.info("redis形式存储access_token");
			}else{
				// 存储access_token
				DataVariable.access_token = access_token;
				// 存储access_token有效时间
				DataVariable.access_token_expires_time = expires_in;
				logger.info("成员变量形式存储access_token");
			}
			
			// 跑批成功标识
			setAccess_token_lisener(true);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("accesstoken更新异常！");
		} finally {
			if (!isAccess_token_lisener()) {
				logger.info("更新access_token失败，即将发送警告消息");
			}
			LoggerStartAndEnd.loggerStartOrFinish(logger, "WxAccessTokenTask", "readAccessToken2DataVariable", "结束");
		}
	}

	/**
	 * access_token跑批监控
	 */
	public void readAccessToken2DataVariableLis() {
		// 生成随机数作为当前线程流水号
		String randomNum = GenerateRandomUtils.getCharAndNumr(20);
		Thread.currentThread().setName(randomNum);
		LoggerStartAndEnd.loggerStartOrFinish(logger, "WxAccessTokenTask", "readAccessToken2DataVariableLis", "开始");

		if (isAccess_token_lisener()) {
			logger.info("ACCESS_TOKEN跑批顺利进行，无需发送警告");
			// 重置为跑批失败状态
			setAccess_token_lisener(false);
			LoggerStartAndEnd.loggerStartOrFinish(logger, "WxAccessTokenTask", "readAccessToken2DataVariableLis", "结束");

			return;
		}
		
		if (!WeChatUtil.sendWaring("ACCESS_TOKEN")) {
			logger.info("发送警告失败！");
			LoggerStartAndEnd.loggerStartOrFinish(logger, "WxAccessTokenTask", "readAccessToken2DataVariableLis", "结束");
			return;
		}
		
		logger.info("发送警告成功！");
		LoggerStartAndEnd.loggerStartOrFinish(logger, "WxAccessTokenTask", "readAccessToken2DataVariableLis", "结束");
	}

	/**
	 * 更新JsApiTicket到数据变量类中
	 */
	public void readTicket2DataVariable() {
		// 生成随机数作为当前线程流水号
		String randomNum = GenerateRandomUtils.getCharAndNumr(20);
		Thread.currentThread().setName(randomNum);
		LoggerStartAndEnd.loggerStartOrFinish(logger, "WxAccessTokenTask", "readTicket2DataVariable", "开始");
		String access_token = DataVariable.access_token;
		try {
		
			if (StringUtils.isBlank(access_token)) {
				logger.info("刷新JsApiTicket失败:access_token为空");
				return;
			}
			// 拼接请求腾讯获取JsApiTicket地址
			String url = QYWX_URL_GET_JSAPITICKET.replace("ACCESS_TOKEN", access_token);
			logger.info("刷新JsApiTicket地址==【{}】", url);
			String result = HttpUtil.get(url);
			logger.info("刷新JsApiTicket返回==【{}】", result);
			JSONObject json = JSONObject.parseObject(result);
			// 腾讯返回errcode不为0刷新失败
			if (!"0".equals(json.getString("errcode"))) {
				logger.info("刷新JsApiTicket失败");
				return;
			}
			
			String ticket = json.getString("ticket");
			String expires_in = json.getString("expires_in");
			logger.info("取得JsApiTicket==【{}】,有效时间为【{}】", ticket, expires_in);
			
			if(SpringBeanRegisterUtil.isBeanNameUse("dataService4DBImpl")){
				AccesstokenInfo token = tokenService.getById(ACCESS_TOKEN_ID);
				token.setTicket(ticket);
				token.setTicketExpiresIn(expires_in);
				logger.info("数据库形式存储jsapiticket");
				if(tokenService.updateToken(token)){
					logger.info("数据库形式存储jsapiticket成功");
				}
			}else if(SpringBeanRegisterUtil.isBeanNameUse("dataService4RedisImpl")){
				if(RedisUtil.isExists(JSAPI_TICKET_KEY)){
					RedisUtil.updateResisInfo(JSAPI_TICKET_KEY, ticket);
				}else{
					RedisUtil.insertResisInfo(JSAPI_TICKET_KEY, ticket);
				}
				logger.info("redis形式存储jsapiticket");
			}else{
				// 存储JsApiTicket
				DataVariable.js_api_ticket = ticket;
				// 存储JsApiTicket有效时间
				DataVariable.js_api_ticket_expires_time = expires_in;
				logger.info("成员变量形式存储jsapiticket");
			}
			
			// 跑批顺利，标识为true
			setTicket_lisener(true);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("更新微信JsApiTicket异常", e.getMessage());
		} finally {
			if (!isTicket_lisener()) {
				logger.info("刷新JsApiTicket失败，即将发送警告消息。。。");
			}
			LoggerStartAndEnd.loggerStartOrFinish(logger, "WxAccessTokenTask", "readTicket2DataVariable", "结束");

		}

	}

	/**
	 * JsApiTicket跑批监控
	 */
	public void readTicket2DataVariableLis() {
		// 生成随机数作为当前线程流水号
		String randomNum = GenerateRandomUtils.getCharAndNumr(20);
		Thread.currentThread().setName(randomNum);
		LoggerStartAndEnd.loggerStartOrFinish(logger, "WxAccessTokenTask", "readTicket2DataVariableLis", "开始");
		
		if (isTicket_lisener()) {
			logger.info("JSAPITICKET跑批顺利进行，无需发送警告");
			// 重置ticket跑批成功标识为false
			setTicket_lisener(false);
			LoggerStartAndEnd.loggerStartOrFinish(logger, "WxAccessTokenTask", "readTicket2DataVariableLis", "结束");
			return;
		}
		
		logger.info("JSAPITICKET跑批停止，开始发送警告");
		if (!WeChatUtil.sendWaring("JsApiTicket")) {
			logger.info("发送警告失败！");
			LoggerStartAndEnd.loggerStartOrFinish(logger, "WxAccessTokenTask", "readTicket2DataVariableLis", "结束");
			return;
		}
		
		logger.info("发送警告成功！");
		LoggerStartAndEnd.loggerStartOrFinish(logger, "WxAccessTokenTask", "readTicket2DataVariableLis", "结束");
	}


}
