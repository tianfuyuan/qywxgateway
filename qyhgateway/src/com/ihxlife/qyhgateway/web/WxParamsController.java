package com.ihxlife.qyhgateway.web;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.ihxlife.qyhgateway.support.annotation.CheckParamType;
import com.ihxlife.qyhgateway.support.annotation.CheckRequestParam;
import com.ihxlife.qyhgateway.support.annotation.RequireMessage;
import com.ihxlife.qyhgateway.support.components.DataGetWayComponent;

/**
 * 获取企业号参数Controller
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/api/v1")
public class WxParamsController {
	
	private static final Logger logger = LoggerFactory.getLogger(WxParamsController.class);

	@Autowired
	private DataGetWayComponent dataGetWayComponent;
	
	

	/**
	 * 获取jsApiTicket接口
	 * @param request 请求
	 * @return
	 */
	@RequestMapping("/ticket/get")
	@ResponseBody
	@CheckRequestParam(checkRequestType=CheckParamType.CHECK_NO_DATA)
	@RequireMessage(msg="JSApiTicket",className="WechatParamsController",callerTypeIsCustom=false)
	public JSONObject getTicketParam(HttpServletRequest request) {
		JSONObject ticketParam = new JSONObject();
		String ticketInfo = dataGetWayComponent.getDataService().getJsApiTicket();
		JSONObject json = JSONObject.parseObject(ticketInfo);
		// 取得成功
		ticketParam.put("result_code", "suc");
		ticketParam.put("result_msg", "取得JSApiTicket成功");
		ticketParam.put("ticket", json.getString("ticket"));
		ticketParam.put("expires_in", json.getString("expires_in"));
		logger.info("获取JsApiTicket成功：返回的数据为【{}】", ticketParam.toString());
		return ticketParam;
	}

	/**
	 * 获取access_token接口
	 * @param request 请求
	 * @return
	 */
	@RequestMapping("/token/get")
	@ResponseBody
	@CheckRequestParam(checkRequestType=CheckParamType.CHECK_NO_DATA)
	@RequireMessage(msg="AccessToken",className="WechatParamsController",callerTypeIsCustom=false)
	public JSONObject getAccessToken(HttpServletRequest request) {
		JSONObject accessTokenParam = new JSONObject();
		String accessTokenInfo = dataGetWayComponent.getDataService().getAccessToken();
		JSONObject json = JSONObject.parseObject(accessTokenInfo);
		// 取得成功
		accessTokenParam.put("result_code", "suc");
		accessTokenParam.put("result_msg", "取得access_token成功");
		accessTokenParam.put("access_token", json.getString("access_token"));
		accessTokenParam.put("expires_in", json.getString("expires_in"));
		logger.info("获取access_token成功：返回的数据为【{}】", accessTokenParam.toString());
		return accessTokenParam;
	}

}
