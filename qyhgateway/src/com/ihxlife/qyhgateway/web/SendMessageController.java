package com.ihxlife.qyhgateway.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.ihxlife.qyhgateway.dto.ReturnMessage;
import com.ihxlife.qyhgateway.support.annotation.CheckParamType;
import com.ihxlife.qyhgateway.support.annotation.CheckRequestParam;
import com.ihxlife.qyhgateway.support.annotation.RequireMessage;
import com.ihxlife.qyhgateway.support.components.DataGetWayComponent;
import com.ihxlife.qyhgateway.support.constant.PropertiesConstants;
import com.ihxlife.qyhgateway.support.exception.BusinessException;
import com.ihxlife.qyhgateway.support.util.HttpUtil;
import com.ihxlife.qyhgateway.support.util.JsonUtil;

/**
 * 发送消息Controller
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/api/v1/message")
public class SendMessageController {

	private static final Logger logger = LoggerFactory.getLogger(SendMessageController.class);

	// 微信发送消息URL
	private static final String QYWX_WX_SEND_MESSAGE_URL = PropertiesConstants.QYWX_WX_SEND_MESSAGE_URL;

	@Autowired
	private DataGetWayComponent dataGetWayComponent;

	/**
	 * 企业号推送文本消息接口
	 * @param request 请求
	 * @param response 响应
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/text/send", method = RequestMethod.POST)
	@ResponseBody
	@CheckRequestParam(checkRequestType = CheckParamType.CHECK_DATA)
	@RequireMessage(msg = "企业号发送消息", className = "SendMessageController", callerTypeIsCustom = false)
	public ReturnMessage sendText(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ReturnMessage returnMessage = new ReturnMessage();
		String data = request.getParameter("data");
		logger.info("参数和签名校验成功，data数据包内容为【{}】", data);
		// 获取AccessToken
		String accessTokenInfo = dataGetWayComponent.getDataService().getAccessToken();
		JSONObject json = JSONObject.parseObject(accessTokenInfo);
		String access_token = json.getString("access_token");
		logger.info("access_token【{}】", access_token);
		JSONObject dataMap = JSONObject.parseObject(data);
		// touser toparty totag 不能同时为空
		if ((!dataMap.containsKey("touser") || StringUtils.isBlank(dataMap.get("touser").toString()))
				&& (!dataMap.containsKey("toparty") || StringUtils.isBlank(dataMap.get("toparty").toString()))
				&& (!dataMap.containsKey("totag") || StringUtils.isBlank(dataMap.get("totag").toString()))) {
			throw new BusinessException("企业号发送消息失败:消息接收人为空");
		}
		// 消息类型不能为空
		if (!dataMap.containsKey("msgtype") || StringUtils.isBlank(dataMap.get("msgtype").toString())) {
			throw new BusinessException("企业号发送消息失败:消息类型为空");
		}

		// 获取消息类型
		String type = dataMap.get("msgtype").toString();
		if (!"text".equals(type)) {
			throw new BusinessException("企业号发送消息失败:消息类型错误");
		}
		// data数据包中text属性不能为空
		if (!dataMap.containsKey("text")|| StringUtils.isBlank(dataMap.getString("text"))) {
			throw new BusinessException("企业号发送消息失败:text消息内容为空");
		}
		
		JSONObject textMap = JSONObject.parseObject(dataMap.getString("text"));
		logger.info("textMap【{}】", textMap);
		// 需要发送的内容不能为空
		if (!textMap.containsKey("content") || StringUtils.isBlank(textMap.get("content").toString())) {
			throw new BusinessException("企业号发送消息失败:text消息content内容为空");
		}
		logger.info("text【{}】", dataMap.get("text"));
		// 腾讯企业号发送消息接口地址
		String url = QYWX_WX_SEND_MESSAGE_URL.replace("ACCESS_TOKEN", access_token);
		// 企业号发送消息返回结果
		String result = "";
		result = HttpUtil.post(url, data.toString());
		logger.info("企业号发送消息返回结果result【{}】", result);

		if (StringUtils.isBlank(result)) {
			throw new BusinessException("企业号发送消息失败:腾讯返回接口为空");
		}
		// 腾讯返回的结果转换为map对象
		Map<String, String> resultMap = (Map<String, String>) JsonUtil.json2Map(result);
		if (resultMap.get("errmsg").equals("ok") && !result.contains("invalid")) {
			logger.info("消息发送成功");
			returnMessage.setResult_code("suc");
			returnMessage.setResult_msg("消息发送成功");
		// 腾讯返回82001代表企业号不存在此业务员，返回60011代表此业务员不在应用可见范围内
		} else if ("82001".equals(resultMap.get("errcode"))||"60011".equals(resultMap.get("errcode"))) {
			throw new BusinessException("企业号发送消息失败:请查看对应管理组权限，收件人是否存在，是否对该应用在可见范围内");
		} else {
			throw new BusinessException("企业号发送消息失败:" + resultMap.get("errmsg"));
		}
		logger.info("返回结果json_result为【{}】", JsonUtil.object2Json(returnMessage));
		return returnMessage;

	}

	/**
	 * 企业号推送图文消息接口
	 * @param request 请求
	 * @param response 响应
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/news/send", method = RequestMethod.POST)
	@ResponseBody
	@CheckRequestParam(checkRequestType = CheckParamType.CHECK_DATA)
	@RequireMessage(msg = "企业号发送消息", className = "SendMessageController", callerTypeIsCustom = false)
	public ReturnMessage sendNews(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ReturnMessage returnMessage = new ReturnMessage();
		String data = request.getParameter("data");
		logger.info("参数和签名校验成功，data数据包内容为【{}】", data);
		// 获取AccessToken
		String accessTokenInfo = dataGetWayComponent.getDataService().getAccessToken();
		JSONObject json = JSONObject.parseObject(accessTokenInfo);
		String access_token = json.getString("access_token");
		logger.info("access_token【{}】", access_token);
		JSONObject dataMap = JSONObject.parseObject(data);
		// touser toparty totag 不能同时为空
		if ((!dataMap.containsKey("touser") || StringUtils.isBlank(dataMap.get("touser").toString()))
				&& (!dataMap.containsKey("toparty") || StringUtils.isBlank(dataMap.get("toparty").toString()))
				&& (!dataMap.containsKey("totag") || StringUtils.isBlank(dataMap.get("totag").toString()))) {
			throw new BusinessException("企业号发送消息失败:消息接收人为空");
		}
		// 消息类型不能为空
		if (!dataMap.containsKey("msgtype") || StringUtils.isBlank(dataMap.get("msgtype").toString())) {
			throw new BusinessException("企业号发送消息失败:消息类型为空");
		}

		// 获取消息类型
		String type = dataMap.get("msgtype").toString();
		if (!"news".equals(type)) {
			throw new BusinessException("企业号发送消息失败:消息类型错误");
		}
		// data数据包中news属性不能为空
		if (!dataMap.containsKey("news")|| StringUtils.isBlank(dataMap.get("news").toString())) {
			throw new BusinessException("企业号发送消息失败:news消息体为空！");
		}

		JSONObject newsMap = JSONObject.parseObject(dataMap.getString("news"));
		// 消息体articles不能为空
		if (!newsMap.containsKey("articles") || StringUtils.isBlank(newsMap.getString("articles"))) {
			throw new BusinessException("企业号发送消息失败:articles消息体为空！");
		}

		logger.info("news【{}】", dataMap.get("news"));
		// 腾讯企业号发送消息接口地址
		String url = QYWX_WX_SEND_MESSAGE_URL.replace("ACCESS_TOKEN", access_token);
		// 企业号发送消息返回结果
		String result = "";

		result = HttpUtil.post(url, data.toString());

		logger.info("企业号发送消息返回结果result【{}】", result);
		if (StringUtils.isBlank(result)) {
			throw new BusinessException("企业号发送消息失败:腾讯返回接口为空");
		}
		// 腾讯返回的结果转换为map对象
		Map<String, String> resultMap = (Map<String, String>) JsonUtil.json2Map(result);
		if (resultMap.get("errmsg").equals("ok") && !result.contains("invalid")) {
			logger.info("消息发送成功");
			returnMessage.setResult_code("suc");
			returnMessage.setResult_msg("消息发送成功");
		// 腾讯返回82001代表企业号不存在此业务员，返回60011代表此业务员不在应用可见范围内
		}else if ("82001".equals(resultMap.get("errcode"))||"60011".equals(resultMap.get("errcode"))) {
			throw new BusinessException("企业号发送消息失败:请查看对应管理组权限，收件人是否存在，是否对该应用在可见范围内");
		}else {
			throw new BusinessException("企业号发送消息失败:" + resultMap.get("errmsg"));
		}
		logger.info("返回结果json_result为【{}】", JsonUtil.object2Json(returnMessage));
		return returnMessage;
		
	}

}
