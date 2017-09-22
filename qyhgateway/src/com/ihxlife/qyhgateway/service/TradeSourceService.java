package com.ihxlife.qyhgateway.service;

import java.util.List;

import com.ihxlife.qyhgateway.dto.TradeSource;

/**
 * 交易来源信息service
 * @author Administrator
 *
 */
public interface TradeSourceService {
	
	/**
	 * 根据交易来源获取交易来源整体信息
	 * @param trade_source 交易来源
	 * @return
	 */
	public TradeSource getTradeSource(String trade_source);
	
	/**
	 * 获取所有交易来源信息
	 * @return
	 */
	public List<TradeSource> selectAllTradeSource();
}
