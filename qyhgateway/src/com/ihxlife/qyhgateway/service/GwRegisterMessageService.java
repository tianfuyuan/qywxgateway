package com.ihxlife.qyhgateway.service;

import java.util.List;

import com.ihxlife.qyhgateway.dto.GwRegisterMessage;


/**
 * 注册官微信息service
 * @author Administrator
 *
 */
public interface GwRegisterMessageService {
	
	/**
	 * 存储官微注册信息
	 * @param gwRegisterMessage 官微注册信息对象
	 * @return
	 */
	public boolean saveGwRegisterMessage(GwRegisterMessage gwRegisterMessage);
	
	/**
	 * 更新官微注册信息
	 * @param gwRegisterMessage 官微注册信息对象
	 * @return
	 */
	public boolean updateGwRegisterMessage(GwRegisterMessage gwRegisterMessage);
	
	/**
	 * 查询官微注册信息
	 * @return
	 */
	public List<GwRegisterMessage> queryGwRegisterMessage();
	
	/**
	 * 根据工号和状态查询官微注册信息
	 * @param userid 工号
	 * @param state 状态
	 * @return
	 */
	public GwRegisterMessage queryOneRegisterMessage(String userid,String state);
}
