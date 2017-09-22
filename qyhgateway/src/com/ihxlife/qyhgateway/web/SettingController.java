package com.ihxlife.qyhgateway.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.ihxlife.qyhgateway.dto.AccesstokenInfo;
import com.ihxlife.qyhgateway.service.AccessTokenInfoService;
import com.ihxlife.qyhgateway.service.DataService;
import com.ihxlife.qyhgateway.service.impl.DataService4DBImpl;
import com.ihxlife.qyhgateway.service.impl.DataService4PropertiesImpl;
import com.ihxlife.qyhgateway.service.impl.DataService4RedisImpl;
import com.ihxlife.qyhgateway.support.annotation.RequireMessage;
import com.ihxlife.qyhgateway.support.components.DataGetWayComponent;
import com.ihxlife.qyhgateway.support.constant.PropertiesConstants;
import com.ihxlife.qyhgateway.support.constant.RedisConstant;
import com.ihxlife.qyhgateway.support.redis.RedisUtil;
import com.ihxlife.qyhgateway.support.spring.SpringBeanRegisterUtil;
import com.ihxlife.qyhgateway.support.util.HttpUtil;
import com.ihxlife.qyhgateway.support.util.JsonUtil;
import com.ihxlife.qyhgateway.support.variable.DataVariable;


@Controller
@RequestMapping("/api/v1")
public class SettingController {
	private static final Logger logger = LoggerFactory.getLogger(SettingController.class);
	
	// 从redis中获取接口配置信息的key
	private static final String INTERFACE_CONF_KEY = RedisConstant.INTERFACE_CONF_KEY;
	
	// 从redis中获取交易来源信息的key
	private static final String TRADE_SOURCE_KEY = RedisConstant.TRADE_SOURCE_KEY;
	
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

	
	
	
	@RequestMapping("/redis/trade_source/create")
	@ResponseBody
	@RequireMessage(className="SettingController",msg="向redis中存入tradesource",callerTypeIsCustom=false)
	public String setTradeSource2redis(HttpServletRequest request) throws Exception{
		String tradesource = request.getParameter("tradesource");
		String checkKey = request.getParameter("check_key");
		logger.info("新增tradesource【{}】，key【{}】",tradesource,checkKey);
		Map<String, String> tradesources = new HashMap<String, String>();
		//------------------开发环境配置------开始-----------------
		tradesources.put("CSLY", "D32pO5555nn7011b7WQT8W591552532g");
		//------------------开发环境配置------结束-----------------
		if(!StringUtils.isBlank(tradesource) && !StringUtils.isBlank(checkKey)){
			tradesources.put(tradesource, checkKey);
		}
		String json = JsonUtil.object2Json(tradesources);
		
		if(RedisUtil.isExists(TRADE_SOURCE_KEY)){
			RedisUtil.updateResisInfo(TRADE_SOURCE_KEY, json);
			return "redis更新交易来源成功！";
		}
		RedisUtil.insertResisInfo(TRADE_SOURCE_KEY, json);
		return "redis添加交易来源成功！";
	}
	
	@RequestMapping("/redis/interface_conf/creat")
	@ResponseBody
	@RequireMessage(className="SettingController",msg="向redis中存入interfaceConfig",callerTypeIsCustom=false)
	public String setInterfaceConf2Redis(HttpServletRequest request) throws Exception{
		String id = request.getParameter("id");
		String redirectUrl = request.getParameter("redirectUrl");
		Map<String, String> interfaceConfigs = new HashMap<String, String>();
		//------------------开发环境配置------开始-----------------
		interfaceConfigs.put("0000000001", "http://../../../../../micro.html?");
		//------------------开发环境配置------结束-----------------
		if(!StringUtils.isBlank(id) && !StringUtils.isBlank(redirectUrl)){
			interfaceConfigs.put(id, redirectUrl);
		}
		String json = JsonUtil.object2Json(interfaceConfigs);
		
		if(RedisUtil.isExists(INTERFACE_CONF_KEY)){
			RedisUtil.updateResisInfo(INTERFACE_CONF_KEY, json);
			return "redis更新接口配置信息成功！";
		}
		RedisUtil.insertResisInfo(INTERFACE_CONF_KEY, json);
		return "redis添加接口配置信息成功！";
	}

	
	@RequestMapping("/storage/type/redis")
	@ResponseBody
	@RequireMessage(className="SettingController",msg="切换为redis获取方式",callerTypeIsCustom=false)
	public String dataGetWay4Redis() throws Exception{
		String result = "";
		JSONObject resultJson = null;
		String url = QYWX_URL_GET_ACCESS_TOKEN;
		String beanName = "";
		if(SpringBeanRegisterUtil.isBeanNameUse("dataService4DBImpl")){
			beanName = "dataService4DBImpl";
		}
		if(SpringBeanRegisterUtil.isBeanNameUse("dataService4PropertiesImpl")){
			beanName = "dataService4PropertiesImpl";
		}
		logger.info("beanName为【{}】",beanName);
		if(!SpringBeanRegisterUtil.isBeanNameUse("dataService4RedisImpl")){
			SpringBeanRegisterUtil.unregisterBean(beanName);
			SpringBeanRegisterUtil.registerBean("dataService4RedisImpl", DataService4RedisImpl.class.getName());
			DataGetWayComponent.updateDataService(SpringBeanRegisterUtil.getBean(DataService.class));
		}
		if(SpringBeanRegisterUtil.isBeanNameUse("dataService4RedisImpl")){
			logger.info("切换为redis获取方式。修改成功!");
			// 修改请求腾讯获取access_token的地址参数
			url = url.replace("CorpID", ACCESSTOKEN_CORPID).replace("SECRET", ACCESSTOKEN_SECRET);
			logger.info("请求腾讯获取access_token链接【{}】", url);
			// 接收腾讯返回的结果
			result = HttpUtil.get(url);
			logger.info("更新access_token返回【{}】", result);
			
			if (StringUtils.isBlank(result)) {
				logger.info("请求腾讯返回为空！");
				return "请求腾讯返回为空！";
			}
			
			resultJson = JSONObject.parseObject(result);
			
			// 参数为空标识
			if (resultJson.get("access_token") == null
					|| StringUtils.isBlank(resultJson.getString("access_token"))) {
				logger.info("请求腾讯失败");
				return "请求腾讯失败";
			}
			
			String token = resultJson.getString("access_token");
			String expires_in = resultJson.getString("expires_in");
			logger.info("accesstoken为【{}】,有效时间为【{}】", token, expires_in);
			if(RedisUtil.isExists(ACCESSTOKEN_KEY)){
				RedisUtil.updateResisInfo(ACCESSTOKEN_KEY, token);
			}else{
				RedisUtil.insertResisInfo(ACCESSTOKEN_KEY, token);
			}
			logger.info("redis形式存储access_token");
			// 获取JsApiTicket
			if (StringUtils.isBlank(token)) {
				logger.info("刷新JsApiTicket失败:access_token为空");
				return "刷新JsApiTicket失败:access_token为空";
			}
			// 修改腾讯获取JsApiTicket地址参数
			String ticketUrl = QYWX_URL_GET_JSAPITICKET.replace("ACCESS_TOKEN", token);
			logger.info("刷新JsApiTicket地址==【{}】", ticketUrl);
			String ticketResult = HttpUtil.get(ticketUrl);
			logger.info("刷新JsApiTicket返回==【{}】", ticketResult);
			JSONObject json = JSONObject.parseObject(ticketResult);
			
			if (!"0".equals(json.getString("errcode"))) {
				logger.info("刷新JsApiTicket失败");
				return "刷新JsApiTicket失败";
			}
			
			String ticket = json.getString("ticket");
			String ticket_expires_in = json.getString("expires_in");
			logger.info("取得JsApiTicket==【{}】,有效时间为【{}】", ticket, ticket_expires_in);
			if(RedisUtil.isExists(JSAPI_TICKET_KEY)){
				RedisUtil.updateResisInfo(JSAPI_TICKET_KEY, ticket);
			}else{
				RedisUtil.insertResisInfo(JSAPI_TICKET_KEY, ticket);
			}
			logger.info("redis形式存储jsapiticket");
			return "success";
		}
		logger.info("切换为redis获取方式。修改失败!");
		return "fail";
	}
	
	@RequestMapping("/storage/type/properties")
	@ResponseBody
	@RequireMessage(className="SettingController",msg="切换为properties获取方式",callerTypeIsCustom=false)
	public String dataGetWay4Properties() throws Exception{
		String result = "";
		JSONObject resultJson = null;
		String url = QYWX_URL_GET_ACCESS_TOKEN;
		String beanName = "";
		if(SpringBeanRegisterUtil.isBeanNameUse("dataService4DBImpl")){
			beanName = "dataService4DBImpl";
		}
		if(SpringBeanRegisterUtil.isBeanNameUse("dataService4RedisImpl")){
			beanName = "dataService4RedisImpl";
		}
		logger.info("beanName为【{}】",beanName);
		if(!SpringBeanRegisterUtil.isBeanNameUse("dataService4PropertiesImpl")){
			SpringBeanRegisterUtil.unregisterBean(beanName);
			SpringBeanRegisterUtil.registerBean("dataService4PropertiesImpl", DataService4PropertiesImpl.class.getName());
			DataGetWayComponent.updateDataService(SpringBeanRegisterUtil.getBean(DataService.class));
		}
		if(SpringBeanRegisterUtil.isBeanNameUse("dataService4PropertiesImpl")){
			logger.info("切换为properties获取方式。修改成功!");
			
			// 修改请求腾讯获取access_token的地址参数
			url = url.replace("CorpID", ACCESSTOKEN_CORPID).replace("SECRET", ACCESSTOKEN_SECRET);
			logger.info("请求腾讯获取access_token链接【{}】", url);
			// 接收腾讯返回的结果
			result = HttpUtil.get(url);
			logger.info("更新access_token返回【{}】", result);
			
			if (StringUtils.isBlank(result)) {
				logger.info("请求腾讯返回为空！");
				return "请求腾讯返回为空！";
			}
			
			resultJson = JSONObject.parseObject(result);
			
			// 参数为空标识
			if (resultJson.get("access_token") == null
					|| StringUtils.isBlank(resultJson.getString("access_token"))) {
				logger.info("请求腾讯失败");
				return "请求腾讯失败";
			}
			
			String token = resultJson.getString("access_token");
			String expires_in = resultJson.getString("expires_in");
			logger.info("accesstoken为【{}】,有效时间为【{}】", token, expires_in);
			DataVariable.access_token = token;
			DataVariable.access_token_expires_time = expires_in;
			logger.info("成员变量形式存储access_token");
			// 获取JsApiTicket
			if (StringUtils.isBlank(token)) {
				logger.info("刷新JsApiTicket失败:access_token为空");
				return "刷新JsApiTicket失败:access_token为空";
			}
			// 修改腾讯获取JsApiTicket地址参数
			String ticketUrl = QYWX_URL_GET_JSAPITICKET.replace("ACCESS_TOKEN", token);
			logger.info("刷新JsApiTicket地址==【{}】", ticketUrl);
			String ticketResult = HttpUtil.get(ticketUrl);
			logger.info("刷新JsApiTicket返回==【{}】", ticketResult);
			JSONObject json = JSONObject.parseObject(ticketResult);
			
			if (!"0".equals(json.getString("errcode"))) {
				logger.info("刷新JsApiTicket失败");
				return "刷新JsApiTicket失败";
			}
			
			String ticket = json.getString("ticket");
			String ticket_expires_in = json.getString("expires_in");
			logger.info("取得JsApiTicket==【{}】,有效时间为【{}】", ticket, ticket_expires_in);
			DataVariable.js_api_ticket = ticket;
			DataVariable.js_api_ticket_expires_time = ticket_expires_in;
			logger.info("成员变量形式存储jsapiticket");
			return "success";
		}
		logger.info("切换为properties获取方式。修改失败!");
		return "fail";
	}
	
	@RequestMapping("/storage/type/db")
	@ResponseBody
	@RequireMessage(className="SettingController",msg="切换为数据库获取方式",callerTypeIsCustom=false)
	public String dataGetWay4DB() throws Exception{
		String result = "";
		JSONObject resultJson = null;
		String url = QYWX_URL_GET_ACCESS_TOKEN;
		String beanName = "";
		if(SpringBeanRegisterUtil.isBeanNameUse("dataService4PropertiesImpl")){
			beanName = "dataService4PropertiesImpl";
		}
		if(SpringBeanRegisterUtil.isBeanNameUse("dataService4RedisImpl")){
			beanName = "dataService4RedisImpl";
		}
		logger.info("beanName为【{}】",beanName);
		if(!SpringBeanRegisterUtil.isBeanNameUse("dataService4DBImpl")){
			SpringBeanRegisterUtil.unregisterBean(beanName);
			SpringBeanRegisterUtil.registerBean("dataService4DBImpl", DataService4DBImpl.class.getName());
			DataGetWayComponent.updateDataService(SpringBeanRegisterUtil.getBean(DataService.class));
		}
		if(SpringBeanRegisterUtil.isBeanNameUse("dataService4DBImpl")){
			logger.info("切换为数据库获取方式。修改成功!");
			// 修改请求腾讯获取access_token的地址参数
			url = url.replace("CorpID", ACCESSTOKEN_CORPID).replace("SECRET", ACCESSTOKEN_SECRET);
			logger.info("请求腾讯获取access_token链接【{}】", url);
			// 接收腾讯返回的结果
			result = HttpUtil.get(url);
			logger.info("更新access_token返回【{}】", result);
			
			if (StringUtils.isBlank(result)) {
				logger.info("请求腾讯返回为空！");
				return "请求腾讯返回为空！";
			}
			
			resultJson = JSONObject.parseObject(result);
			
			// 参数为空标识
			if (resultJson.get("access_token") == null
					|| StringUtils.isBlank(resultJson.getString("access_token"))) {
				logger.info("请求腾讯失败");
				return "请求腾讯失败";
			}
			
			String token = resultJson.getString("access_token");
			String expires_in = resultJson.getString("expires_in");
			logger.info("accesstoken为【{}】,有效时间为【{}】", token, expires_in);
			AccesstokenInfo accessToken = tokenService.getById(ACCESS_TOKEN_ID);
			accessToken.setAccessToken(token);
			accessToken.setTokenExpiresIn(expires_in);
			// 获取JsApiTicket
			if (StringUtils.isBlank(token)) {
				logger.info("刷新JsApiTicket失败:access_token为空");
				return "刷新JsApiTicket失败:access_token为空";
			}
			// 修改腾讯获取JsApiTicket地址参数
			String ticketUrl = QYWX_URL_GET_JSAPITICKET.replace("ACCESS_TOKEN", token);
			logger.info("刷新JsApiTicket地址==【{}】", ticketUrl);
			String ticketResult = HttpUtil.get(ticketUrl);
			logger.info("刷新JsApiTicket返回==【{}】", ticketResult);
			JSONObject json = JSONObject.parseObject(ticketResult);
			
			if (!"0".equals(json.getString("errcode"))) {
				logger.info("刷新JsApiTicket失败");
				return "刷新JsApiTicket失败";
			}
			String ticket = json.getString("ticket");
			String ticket_expires_in = json.getString("expires_in");
			logger.info("取得JsApiTicket==【{}】,有效时间为【{}】", ticket, ticket_expires_in);
			accessToken.setTicket(ticket);
			accessToken.setTicketExpiresIn(expires_in);
			boolean resultFlag = tokenService.updateToken(accessToken);
			logger.info("数据库形式存储access_token，ticket");
			if(resultFlag){
				logger.info("数据库形式存储access_token，ticket成功");
			}
			return "success";
		}
		logger.info("切换为数据库获取方式。修改失败!");
		return "fail";
	}
	
}
