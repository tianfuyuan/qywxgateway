package com.ihxlife.qyhgateway.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ihxlife.qyhgateway.dto.TradeSource;
import com.ihxlife.qyhgateway.dto.TradeSourceExample;
import com.ihxlife.qyhgateway.dto.TradeSourceExample.Criteria;
import com.ihxlife.qyhgateway.mapper.TradeSourceMapper;
import com.ihxlife.qyhgateway.service.TradeSourceService;

/**
 * 交易来源信息service
 * @author Administrator
 *
 */
@Service
public class TradeSourceServiceImpl implements TradeSourceService {

	private static final Logger logger = LoggerFactory.getLogger(TradeSourceServiceImpl.class);
	
	@Autowired
	private TradeSourceMapper tradeSourceMapper;
	
	/**
	 * 根据来源标识查找有无该来源配置
	 */
	public TradeSource getTradeSource(String trade_source) {
		TradeSource tradeSource = null;
		try{
			//去掉空格
			trade_source = trade_source.trim();
			//查询对应数据
			TradeSourceExample example = new TradeSourceExample();
			Criteria criteria = example.createCriteria();
			criteria.andTradeSourceEqualTo(trade_source);
			List<TradeSource> list = tradeSourceMapper.selectByExample(example);
			//如果查到则返回第一个
			if(list != null && list.size() >0){
				logger.info("该来源为【{}】在库中有所配置！",list.get(0).getRemark());
				tradeSource = list.get(0);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.info("查询来源在库中配置报错！");
		}
		return tradeSource;
	}

	/**
	 * 获取所有交易来源信息
	 */
	@Override
	public List<TradeSource> selectAllTradeSource() {
		List<TradeSource> tradeSources = null;
		try {
			TradeSourceExample example = new TradeSourceExample();
			//根据录入时间排序
			example.setOrderByClause("inputdate asc");
			tradeSources = tradeSourceMapper.selectByExample(example);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("查询来源信息异常【{}】",e.getMessage());
		}
		return tradeSources;
	}
}
