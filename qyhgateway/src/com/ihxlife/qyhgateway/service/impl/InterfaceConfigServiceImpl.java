package com.ihxlife.qyhgateway.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ihxlife.qyhgateway.dto.InterfaceConfig;
import com.ihxlife.qyhgateway.dto.InterfaceConfigExample;
import com.ihxlife.qyhgateway.mapper.InterfaceConfigMapper;
import com.ihxlife.qyhgateway.service.InterfaceConfigService;

/**
 * 接口配置信息service
 * @author Administrator
 *
 */
@Service
public class InterfaceConfigServiceImpl implements InterfaceConfigService{
	
	private static final Logger logger = LoggerFactory.getLogger(InterfaceConfigServiceImpl.class);

	@Autowired
	private InterfaceConfigMapper interfaceConfigMapper;

	/**
	 * 根据ID获取接口配置信息
	 */
	@Override
	public InterfaceConfig getById(String id) {
		return interfaceConfigMapper.selectByPrimaryKey(id);
	}

	/**
	 * 获取所有接口配置信息
	 */
	@Override
	public List<InterfaceConfig> selectAllInterfaceConf() {
		List<InterfaceConfig> interfaceConfs = null;
		InterfaceConfigExample example;
		try {
			example = new InterfaceConfigExample();
			example.setOrderByClause("createtime asc");
			interfaceConfs = interfaceConfigMapper.selectByExample(example);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("查询配置信息异常【{}】",e.getMessage());
		}
		return interfaceConfs;
	}
}
