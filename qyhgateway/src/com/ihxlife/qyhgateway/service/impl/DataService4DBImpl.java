package com.ihxlife.qyhgateway.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.ihxlife.qyhgateway.dto.AccesstokenInfo;
import com.ihxlife.qyhgateway.dto.InterfaceConfig;
import com.ihxlife.qyhgateway.dto.TradeSource;
import com.ihxlife.qyhgateway.service.AccessTokenInfoService;
import com.ihxlife.qyhgateway.service.DataService;
import com.ihxlife.qyhgateway.service.InterfaceConfigService;
import com.ihxlife.qyhgateway.service.TradeSourceService;
import com.ihxlife.qyhgateway.support.spring.SpringUtils;

/**
 * mysql方式数据读取类
 * @author Administrator
 *
 */
public class DataService4DBImpl implements DataService {
	
	private static final Logger logger = LoggerFactory.getLogger(DataService4DBImpl.class);
	
	//accessToken的主键id
	private static final String ACCESS_TOKEN_ID = "0000000001";

	
	private static final InterfaceConfigService interfaceConfigService = SpringUtils.getBean(InterfaceConfigService.class);
	
	private static final TradeSourceService tradeSourceService = SpringUtils.getBean(TradeSourceService.class);
	
	private static final AccessTokenInfoService accessTokenService = SpringUtils.getBean(AccessTokenInfoService.class);
	
	/**
	 * 根据id读取接口配置信息
	 */
	@Override
	public String getInterfaceConfig(String id) {
		String redirectUrl = "";
		logger.info("以数据库的形式获取接口配置信息，入参ID为【{}】",id);
		InterfaceConfig interfaceConfig = interfaceConfigService.getById(id);
		if(interfaceConfig != null){
			redirectUrl = interfaceConfig.getRedirecturl();
			logger.info("以数据库的形式获取接口配置信息，获取的重定向地址为【{}】",redirectUrl);
		}
		return redirectUrl;
	}
	
	/**
	 * 根据交易来源读取交易来源详细信息
	 */
	@Override
	public String getTradeSource(String tradeSource) {
		String checkKey = "";
		logger.info("以数据库的形式获取交易来源信息，入参交易来源为【{}】",tradeSource);
		TradeSource tradeSourceBean = tradeSourceService.getTradeSource(tradeSource);
		if(tradeSourceBean != null){
			checkKey = tradeSourceBean.getCheckKey();
			logger.info("以数据库的形式获取交易来源信息，获取的交易来源秘钥为【{}】",checkKey);
		}
		return checkKey;
	}

	/**
	 * 获取AccessToken
	 */
	@Override
	public String getAccessToken() {
		JSONObject accessTokenJson = new JSONObject();
		logger.info("以数据库的形式获取access_token信息");
		
		logger.info("accessTokenService【{}】",accessTokenService);
		AccesstokenInfo accessToken = accessTokenService.getById(ACCESS_TOKEN_ID);
		accessTokenJson.put("access_token", accessToken.getAccessToken());
		accessTokenJson.put("expires_in", accessToken.getTokenExpiresIn());
		logger.info("以数据库形式获取access_token信息，获取的信息为：【{}】",accessTokenJson.toString());
		return accessTokenJson.toString();
	}

	/**
	 * 由于不存在ticket表并未实现此方式
	 */
	@Override
	public String getJsApiTicket() {
		JSONObject ticketJSON = new JSONObject();
		logger.info("以数据库的形式获取ticket信息");
		logger.info("accessTokenService【{}】",accessTokenService);
		AccesstokenInfo accessToken = accessTokenService.getById(ACCESS_TOKEN_ID);
		ticketJSON.put("ticket", accessToken.getTicket());
		ticketJSON.put("expires_in", accessToken.getTicketExpiresIn());
		logger.info("以数据库形式获取ticket信息，获取的信息为：【{}】",ticketJSON.toString());
		return ticketJSON.toString();
	}

}
