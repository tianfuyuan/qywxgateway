package com.ihxlife.qyhgateway.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ihxlife.qyhgateway.dto.GwRegisterMessage;
import com.ihxlife.qyhgateway.dto.GwRegisterMessageExample;
import com.ihxlife.qyhgateway.dto.GwRegisterMessageExample.Criteria;
import com.ihxlife.qyhgateway.mapper.GwRegisterMessageMapper;
import com.ihxlife.qyhgateway.service.GwRegisterMessageService;


@Service
public class GwRegisterMessageServiceImpl implements GwRegisterMessageService {
	
	private static final Logger logger = LoggerFactory.getLogger(GwRegisterMessageServiceImpl.class);
	
	@Autowired
	private GwRegisterMessageMapper gwRegisterMessageMapper;
	
	@Override
	public boolean saveGwRegisterMessage(GwRegisterMessage gwRegisterMessage) {
		boolean result = false;
		int resultcode = gwRegisterMessageMapper.insert(gwRegisterMessage);
		if(resultcode>0){
			result = true;
		}
		logger.info("存储官微注册记录，存储条数为【{}】",resultcode);
		return result;
	}

	@Override
	public boolean updateGwRegisterMessage(GwRegisterMessage gwRegisterMessage) {
		boolean result = false;
		int resultcode = gwRegisterMessageMapper.updateByPrimaryKey(gwRegisterMessage);
		if(resultcode>0){
			result = true;
		}
		logger.info("更新官微注册记录，更新条数为【{}】",resultcode);
		return result;
	}

	@Override
	public List<GwRegisterMessage> queryGwRegisterMessage() {
		List<GwRegisterMessage> returnList = null;
		GwRegisterMessageExample example = new GwRegisterMessageExample();
		Criteria criteria = example.createCriteria();
		criteria.andStateEqualTo("0");
		returnList = gwRegisterMessageMapper.selectByExample(example);
		logger.info("查询官微注册记录表，集合条数为【{}】",returnList.size());
		return returnList;
	}

	@Override
	public GwRegisterMessage queryOneRegisterMessage(String userid, String state) {
		GwRegisterMessage message = null;
		List<GwRegisterMessage> returnList = null;
		GwRegisterMessageExample example = new GwRegisterMessageExample();
		Criteria criteria = example.createCriteria();
		criteria.andStateEqualTo("0");
		criteria.andUseridEqualTo(userid);
		returnList = gwRegisterMessageMapper.selectByExample(example);
		if(returnList == null || returnList.size()<=0){
			return message;
		}
		message = returnList.get(0);
		logger.info("查询官微注册记录表，对象条数为【{}】",returnList.size());
		return message;
	}

}
