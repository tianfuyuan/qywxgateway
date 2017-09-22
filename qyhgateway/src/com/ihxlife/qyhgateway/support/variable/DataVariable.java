package com.ihxlife.qyhgateway.support.variable;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.ihxlife.qyhgateway.support.common.LoggerStartAndEnd;
import com.ihxlife.qyhgateway.support.constant.PropertiesConstants;
import com.ihxlife.qyhgateway.support.util.GenerateRandomUtils;
import com.ihxlife.qyhgateway.support.util.HttpUtil;

/**
 * 数据变量类
 * 
 * @author Administrator
 *
 */
public class DataVariable {

	// 日志打印
	private static final Logger logger = LoggerFactory.getLogger(DataVariable.class);

	// 腾讯获取ticket接口地址
	private static final String QYWX_URL_GET_JSAPITICKET = PropertiesConstants.QYWX_URL_GET_JSAPITICKET;
	
	// 腾讯获取access_token接口地址
	private static final String QYWX_URL_GET_ACCESS_TOKEN = PropertiesConstants.QYWX_URL_GET_ACCESS_TOKEN;
	
	// 获取access_token的corpid
	private static final String ACCESSTOKEN_CORPID = PropertiesConstants.ACCESSTOKEN_CORPID;
	
	// 获取access_token的secret
	private static final String ACCESSTOKEN_SECRET = PropertiesConstants.ACCESSTOKEN_SECRET;

	public static String access_token = "";
	
	public static String access_token_expires_time = "";
	
	public static String js_api_ticket = "";
	
	public static String js_api_ticket_expires_time = "";

	/**
	 * 初始化access_token和jsApiTicket（配置在application-task.xml中。项目启动时加载该方法）
	 */
	@SuppressWarnings("unused")
	private static void init() {
		// 生成随机数作为当前线程流水号
		String randomNum = GenerateRandomUtils.getCharAndNumr(20);
		Thread.currentThread().setName(randomNum);
		LoggerStartAndEnd.loggerStartOrFinish(logger, "DataVariable", "initAccessTokenAndTicket", "开始");
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
				return;
			}
			
			resultJson = JSONObject.parseObject(result);
			
			// 参数为空标识
			if (resultJson.get("access_token") == null
					|| StringUtils.isBlank(resultJson.getString("access_token"))) {
				logger.info("请求腾讯失败");
				return;
			}
			
			String token = resultJson.getString("access_token");
			String expires_in = resultJson.getString("expires_in");
			logger.info("accesstoken为【{}】,有效时间为【{}】", token, expires_in);
			// 存储access_token
			access_token = token;
			// 存储access_token有效时间
			access_token_expires_time = expires_in;
			
			// 获取JsApiTicket
			if (StringUtils.isBlank(access_token)) {
				logger.info("刷新JsApiTicket失败:access_token为空");
				return;
			}
			// 修改腾讯获取JsApiTicket地址参数
			String ticketUrl = QYWX_URL_GET_JSAPITICKET.replace("ACCESS_TOKEN", access_token);
			logger.info("刷新JsApiTicket地址==【{}】", ticketUrl);
			String ticketResult = HttpUtil.get(ticketUrl);
			logger.info("刷新JsApiTicket返回==【{}】", ticketResult);
			JSONObject json = JSONObject.parseObject(ticketResult);
			
			if (!"0".equals(json.getString("errcode"))) {
				logger.info("刷新JsApiTicket失败");
				return;
			}
			
			String ticket = json.getString("ticket");
			String ticket_expires_in = json.getString("expires_in");
			logger.info("取得JsApiTicket==【{}】,有效时间为【{}】", ticket, ticket_expires_in);
			// 存储JsApiTicket
			js_api_ticket = ticket;
			// 存储JsApiTicket有效时间
			js_api_ticket_expires_time = ticket_expires_in;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("accesstoken更新异常！");
		} finally {
			LoggerStartAndEnd.loggerStartOrFinish(logger, "DataVariable", "initAccessTokenAndTicket", "结束");

		}
	}
}
