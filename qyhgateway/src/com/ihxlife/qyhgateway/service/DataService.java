package com.ihxlife.qyhgateway.service;

/**
 * 数据获取方式service
 * @author Administrator
 *
 */
public interface DataService {
	
	/**
	 * 根据id读取接口配置信息
	 * 
	 * @param id 主键id
	 * @return
	 */
	public String getInterfaceConfig(String id);

	/**
	 * 根据交易来源读取交易来源详细信息
	 * 
	 * @param tradeSource 交易来源
	 * @return
	 */
	public String getTradeSource(String tradeSource);
	
	/**
	 * 获取AccessToken
	 * 
	 * @return
	 */
	public String getAccessToken();
	
	/**
	 * 获取jsApiTicket
	 * @return
	 */
	public String getJsApiTicket();

}
