package com.ihxlife.qyhgateway.service.impl;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.ihxlife.qyhgateway.dto.ReturnMessage;
import com.ihxlife.qyhgateway.service.RegisterService;
import com.ihxlife.qyhgateway.support.components.DataGetWayComponent;
import com.ihxlife.qyhgateway.support.constant.PropertiesConstants;
import com.ihxlife.qyhgateway.support.util.HttpUtil;


@Service
public class RegisterServiceImpl implements RegisterService {
	
	private static final Logger logger = LoggerFactory.getLogger(RegisterServiceImpl.class);
	
	// 创建人员接口
	private static final String PERSON_CREATE = PropertiesConstants.QYWX_URL_PERSON_CREATE;
	
	// 微课堂部门id
	private static final String WKT_DEPARTMENT_ID = PropertiesConstants.WKT_DEPARTMENT_ID;
	
	@Autowired
	private DataGetWayComponent dataGetWayComponent ;

	@Override
	public ReturnMessage registerUser(String data) throws Exception {
		ReturnMessage returnMessage = new ReturnMessage();
		returnMessage.setResult_code("fail");
		String userid = "";
		String name = "";
		//腾讯生成的部门编号
		String department = WKT_DEPARTMENT_ID;
		String position = "";
		String mobile = "";
		String gender = "";
		String email = "";
		String accessTokenInfo = dataGetWayComponent.getDataService().getAccessToken();
		JSONObject json = JSONObject.parseObject(accessTokenInfo);
		String accessToken = json.getString("access_token");
		logger.info("微课堂注册企业号：access_token为【{}】",accessToken);
		//data数据包转为json
		JSONObject dataJson = JSONObject.parseObject(data);
		//从数据包中获取用户信息
		userid = dataJson.getString("userid") == null ? "" : dataJson.getString("userid").trim();
		name = dataJson.getString("name") == null ? "" : dataJson.getString("name").trim();
		position = dataJson.getString("position") == null ? "" : dataJson.getString("position").trim();
		mobile = dataJson.getString("mobile") == null ? "" : dataJson.getString("mobile").trim();
		gender = dataJson.getString("gender") == null ? "" : dataJson.getString("gender").trim();
		email = dataJson.getString("email") == null ? "" : dataJson.getString("email").trim();
		logger.info("微课堂注册企业号信息参数为：userid：【{}】，name：【{}】，position：【{}】，mobile：【{}】，gender：【{}】，email：【{}】",userid,name,position,mobile,gender,email);
		
		if(StringUtils.isBlank(userid) || StringUtils.isBlank(name) || StringUtils.isBlank(mobile) || StringUtils.isBlank(gender)){
			logger.info("微课堂注册企业号失败：缺失参数，userid：【{}】,name:【{}】,mobile:【{}】,gender:【{}】",userid,name,mobile,gender);
			returnMessage.setResult_msg("微课堂注册企业号失败，缺失参数！");
			return returnMessage;
		}
		String requesturl = PERSON_CREATE.replace("ACCESS_TOKEN", accessToken);
		//请求腾讯数据
		JSONObject requestTX = new JSONObject();
		requestTX.put("userid",userid);//账号
		requestTX.put("name",name);//姓名
		requestTX.put("department",department);//部门
		requestTX.put("position",position);//职位
		requestTX.put("mobile",mobile);//手机号
		requestTX.put("gender",gender);//性别
		requestTX.put("email",email);//邮箱
		
		//调增加成员接口
		logger.info("调用腾讯接口，入参json为【{}】",requestTX);
		String result = HttpUtil.post(requesturl, requestTX.toString());
		logger.info("管理人员类，创建人员接口，增加成员，返回result为【{}】",result);
		
		if(result == null){
			logger.info("微课堂注册企业号失败：调用腾讯接口失败！");
			returnMessage.setResult_msg("微课堂注册企业号失败：调用腾讯接口失败！");
			return returnMessage;
		}
		//腾讯返回结果转JSONObject对象
		JSONObject txResultJson = JSONObject.parseObject(result);
		//获取错误码
		String errcode = txResultJson.getString("errcode");
		
		if("0".equals(errcode)){
			logger.info("微课堂注册企业号成功！");
			returnMessage.setResult_code("suc");
			returnMessage.setResult_msg("微课堂注册企业号成功！");
		}else if("60102".equals(errcode)){
			logger.info("微课堂注册企业号失败:userid已存在！");
			returnMessage.setResult_msg("微课堂注册企业号失败:userid已存在！");
		//手机号不合法置为4
		}else if("60103".equals(errcode)){
			logger.info("微课堂注册企业号失败:userid已存在！");
			returnMessage.setResult_msg("微课堂注册企业号失败:手机号码不合法！");
		//手机号重复置为3
		}else if("60104".equals(errcode)){
			logger.info("微课堂注册企业号失败:手机号重复！");
			returnMessage.setResult_msg("微课堂注册企业号失败:手机号重复！");
		//access_token超时
		}else if("40014".equals(errcode)){
			logger.info("微课堂注册企业号失败:access_token超时！");
			returnMessage.setResult_msg("微课堂注册企业号失败:access_token超时！请稍后再试");
		//创建失败系统异常
		}else if("-1".equals(errcode)){
			logger.info("微课堂注册企业号失败:腾讯系统异常！");
			returnMessage.setResult_msg("微课堂注册企业号失败:腾讯系统异常！");
		}else{
			logger.info("微课堂注册企业号失败:系统异常");
			returnMessage.setResult_msg("微课堂注册企业号失败:系统异常");
		}
		return returnMessage;
	}
	
	
	
	

}
