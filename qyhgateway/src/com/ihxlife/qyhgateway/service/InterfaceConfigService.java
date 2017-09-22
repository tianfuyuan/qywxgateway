package com.ihxlife.qyhgateway.service;

import java.util.List;

import com.ihxlife.qyhgateway.dto.InterfaceConfig;

/**
 * 接口配置信息service
 * @author Administrator
 *
 */
public interface InterfaceConfigService {
	
	/**
	 * 根据ID获取接口配置信息
	 * @param id 主键id
	 * @return
	 */
	public InterfaceConfig getById(String id);
	
	/**
	 * 获取所有接口配置信息
	 * @return
	 */
	public List<InterfaceConfig> selectAllInterfaceConf();
}
