package com.ihxlife.qyhgateway.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.ihxlife.qyhgateway.dto.InterfaceConfig;
import com.ihxlife.qyhgateway.service.DataService;
import com.ihxlife.qyhgateway.support.constant.RedisConstant;
import com.ihxlife.qyhgateway.support.redis.RedisUtil;
import com.ihxlife.qyhgateway.support.util.JsonUtil;

/**
 * redis数据读取类
 * @author Administrator
 *
 */
public class DataService4RedisImpl implements DataService {
	private static final Logger logger = LoggerFactory.getLogger(DataService4RedisImpl.class);

	// 从redis中获取接口配置信息的key
	private static final String INTERFACE_CONF_KEY = RedisConstant.INTERFACE_CONF_KEY;
	
	// 从redis中获取JSAPITICKET的key
	private static final String JSAPI_TICKET_KEY = RedisConstant.JSAPI_TICKET_KEY;

	// 从redis中获取交易来源信息的key
	private static final String TRADE_SOURCE_KEY = RedisConstant.TRADE_SOURCE_KEY;
	
	//redis中获取access_token的key
	private static final String ACCESS_TOKEN_KEY = RedisConstant.ACCESS_TOKEN_KEY;

	/**
	 * 根据id读取接口配置信息
	 */
	@Override
	public String getInterfaceConfig(String id) {
		logger.info("以redis的形式获取接口配置信息，入参ID为【{}】", id);
		String redirectUrl = "";
		InterfaceConfig config = null;
		// 从redis中获取接口配置信息列表
		String confJson = RedisUtil.getResisInfo(INTERFACE_CONF_KEY);
		// 接口配置信息集合json转为map对象
		Map<String, String> map = (Map<String, String>) JsonUtil.json2Map(confJson);
		// redis中获取的接口配置信息集合中是否存在某个配置信息
		if (!map.containsKey(id)) {
			return redirectUrl;
		}
		// 存在则获取配置信息的json对象转为接口配置信息对象
		config = (InterfaceConfig) JsonUtil.json2Object(map.get(id), InterfaceConfig.class);
		redirectUrl = config.getRedirecturl();
		logger.info("以redis的形式获取接口配置信息，获取的重定向地址为【{}】", redirectUrl);
		return redirectUrl;
	}
	
	/**
	 * 根据交易来源读取交易来源详细信息
	 */
	@Override
	public String getTradeSource(String tradeSource) {
		logger.info("以redis的形式获取交易来源信息，入参交易来源为【{}】", tradeSource);
		String checkKey = "";
		// 从redis中获取交易来源信息集合
		String tradeSourcesJson = RedisUtil.getResisInfo(TRADE_SOURCE_KEY);
		// 交易来源信息集合转为map对象
		Map<String, String> tradeSourceMap = (Map<String, String>) JsonUtil.json2Map(tradeSourcesJson);
		// 交易来源信息集合中是否存在某个交易来源信息
		if (!tradeSourceMap.containsKey(tradeSource)) {
			return checkKey;
		}
		checkKey = tradeSourceMap.get(tradeSource);
		logger.info("以redis的形式获取交易来源信息，获取的交易来源秘钥为【{}】", checkKey);
		return checkKey;

	}

	/**
	 * 获取AccessToken
	 */
	@Override
	public String getAccessToken() {
		logger.info("以redis的形式获取AccessToken");
		String accessTokenInfo = RedisUtil.getResisInfo(ACCESS_TOKEN_KEY);
		logger.info("以redis的形式获取AccessToken，获取AccessToken的值为【{}】", accessTokenInfo);
		JSONObject json = new JSONObject();
		json.put("access_token", accessTokenInfo);
		json.put("expires_in", "7200");
		return json.toString();
	}

	/**
	 * 获取Ticket
	 */
	@Override
	public String getJsApiTicket() {
		logger.info("以redis的形式获取JsApiTicket");
		String ticket = RedisUtil.getResisInfo(JSAPI_TICKET_KEY);
		logger.info("以redis的形式获取ticket，获取ticket的值为【{}】", ticket);
		JSONObject json = new JSONObject();
		json.put("ticket", ticket);
		json.put("expires_in", "7200");
		return json.toString();
	}

}
