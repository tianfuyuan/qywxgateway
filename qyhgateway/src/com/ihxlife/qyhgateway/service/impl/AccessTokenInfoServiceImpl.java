package com.ihxlife.qyhgateway.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ihxlife.qyhgateway.dto.AccesstokenInfo;
import com.ihxlife.qyhgateway.dto.AccesstokenInfoExample;
import com.ihxlife.qyhgateway.mapper.AccesstokenInfoMapper;
import com.ihxlife.qyhgateway.service.AccessTokenInfoService;

/**
 * AccessToken配置信息service
 * @author Administrator
 *
 */
@Service
public class AccessTokenInfoServiceImpl implements AccessTokenInfoService{


	@Autowired
	private AccesstokenInfoMapper wxAccessTokenMapper;
	
	/**
	 * 获取所有AccessToken所需参数信息
	 */
	@Override
	public List<AccesstokenInfo> getAll() {
		List<AccesstokenInfo> list = null;
		AccesstokenInfoExample example = new AccesstokenInfoExample();
		list = wxAccessTokenMapper.selectByExample(example);
		return list;
	}

	/**
	 * 根据ID获取AccessToken所需参数信息
	 */
	@Override
	public AccesstokenInfo getById(String id) {
		return wxAccessTokenMapper.selectByPrimaryKey(id);
	}

	/**
	 * 修改AccessToken
	 */
	@Override
	public boolean updateToken(AccesstokenInfo wxAccessToken) {
		boolean result = false;
		int count = wxAccessTokenMapper.updateByPrimaryKey(wxAccessToken);
		if(count>0){
			result = true;
		}
		return result;
	}

}
