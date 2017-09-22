package com.ihxlife.qyhgateway.service.impl;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.ihxlife.qyhgateway.service.DataService;
import com.ihxlife.qyhgateway.support.constant.PropertiesInit;
import com.ihxlife.qyhgateway.support.variable.DataVariable;

/**
 * 配置文件数据读取类
 * @author Administrator
 *
 */
public class DataService4PropertiesImpl implements DataService {
	
	private static final Logger logger = LoggerFactory.getLogger(DataService4PropertiesImpl.class);

	private Properties paramProperties = PropertiesInit.getParamProperties();

	/**
	 * 根据id读取接口配置信息
	 */
	@Override
	public String getInterfaceConfig(String id) {
		String redirectUrl = "";
		//拼接id
		String newId = "inter.conf."+id;
		logger.info("以配置文件方式读取接口配置信息，入参ID为【{}】", newId);
		redirectUrl = paramProperties.getProperty(newId);
		logger.info("以配置文件方式读取接口配置信息，获取的重定向地址为【{}】", redirectUrl);
		return redirectUrl;
	}

	/**
	 * 根据交易来源读取交易来源详细信息
	 */
	@Override
	public String getTradeSource(String tradeSource) {
		String checkKey = "";
		//拼接tradesource
		String newTradeSource = "tradesource."+tradeSource;
		logger.info("以配置文件方式读取交易来源信息，入参交易来源为【{}】", newTradeSource);
		checkKey = paramProperties.getProperty(newTradeSource);
		logger.info("以配置文件方式读取交易来源信息，获取的对应秘钥为【{}】", checkKey);
		return checkKey;
	}

	/**
	 * 获取AccessToken
	 */
	@Override
	public String getAccessToken() {
		logger.info("以数据常量的形式获取AccessToken");
		JSONObject json = new JSONObject();
		json.put("access_token", DataVariable.access_token);
		json.put("expires_in", DataVariable.access_token_expires_time);
		logger.info("以数据常量的形式获取AccessToken,获取的AccessToken为【{}】", json.toString());
		return json.toString();
	}

	/**
	 * 获取JsApiticket
	 */
	@Override
	public String getJsApiTicket() {
		logger.info("以数据常量的形式获取ticket");
		JSONObject json = new JSONObject();
		json.put("ticket", DataVariable.js_api_ticket);
		json.put("expires_in", DataVariable.js_api_ticket_expires_time);
		logger.info("以数据常量的形式获取ticket,获取的ticket为【{}】", json.toString());
		return json.toString();
	}

}
