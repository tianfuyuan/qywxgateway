package com.ihxlife.qyhgateway.service;

import java.util.List;

import com.ihxlife.qyhgateway.dto.AccesstokenInfo;

/**
 * AccessToken配置信息service
 * @author Administrator
 *
 */
public interface AccessTokenInfoService {
	
	/**
	 * 获取所有AccessToken所需参数信息
	 * @return
	 */
	public List<AccesstokenInfo> getAll();
	
	/**
	 * 根据ID获取AccessToken所需参数信息
	 * @param id
	 * @return
	 */
	public AccesstokenInfo getById(String id);
	
	/**
	 * 修改AccessToken
	 * @param wxAccessToken
	 * @return
	 */
	public boolean updateToken(AccesstokenInfo wxAccessToken);
}
