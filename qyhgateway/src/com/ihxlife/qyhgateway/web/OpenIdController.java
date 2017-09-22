package com.ihxlife.qyhgateway.web;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.ihxlife.qyhgateway.support.annotation.CheckParamType;
import com.ihxlife.qyhgateway.support.annotation.CheckRequestParam;
import com.ihxlife.qyhgateway.support.annotation.RequireMessage;
import com.ihxlife.qyhgateway.support.components.DataGetWayComponent;
import com.ihxlife.qyhgateway.support.constant.PropertiesConstants;
import com.ihxlife.qyhgateway.support.util.HttpUtil;
import com.ihxlife.qyhgateway.support.util.JsonUtil;

@Controller
@RequestMapping("/api/v1/oauth2")
public class OpenIdController {
	private static final Logger logger = LoggerFactory.getLogger(OpenIdController.class);

	// userid转openid接口
	private static final String QYWX_URL_CONVERT_OPENID = PropertiesConstants.QYWX_URL_CONVERT_OPENID;

	// 腾讯回调接口
	private static final String QYWX_URL_PAY_REDIRECTURL = PropertiesConstants.QYWX_URL_PAY_REDIRECTURL;

	// 企业号回调支付平台接口地址
	private static final String QYH_REDIRECTURL = PropertiesConstants.QYH_REDIRECTURL;
	
	// 根据accesstoken和code获取Userid接口 
	private static final String QYWX_WX_USERINFO_URL = PropertiesConstants.QYWX_WX_USERINFO_URL;

	// 请求腾讯获取code接口
	private static final String QYWX_URL_PERSON_GET_CODE = PropertiesConstants.QYWX_URL_PERSON_GET_CODE;

	// 企业号的corpid
	private static final String ACCESSTOKEN_CORPID = PropertiesConstants.ACCESSTOKEN_CORPID;

	@Autowired
	private DataGetWayComponent dataGetWayComponent;
	
	

	@RequestMapping("/authorize")
	@CheckRequestParam(checkRequestType = CheckParamType.CHECK_DATA)
	@RequireMessage(className = "OpenIdController", msg = "获取openid", callerTypeIsCustom = false)
	public String queryOpenid(HttpServletRequest request) throws IOException {
		// 获取数据包
		String data = request.getParameter("data");

		// 附加参数
		String attach = "";
		Map<String, String> dataMap = (Map<String, String>) JsonUtil.json2Map(data);
		if (dataMap.containsKey("attach")) {
			attach = dataMap.get("attach");
		}
		logger.info("自定义参数为【{}】", attach);
		// 拼接腾讯回调地址
		String requestWxRediectUrl = QYWX_URL_PAY_REDIRECTURL.replace("ATTACH", attach);
		logger.info("拼接腾讯回调地址为：【{}】", requestWxRediectUrl);
		// 拼接请求腾讯获取code地址
		String url = QYWX_URL_PERSON_GET_CODE.replace("CORPID", ACCESSTOKEN_CORPID).replace("REDIRECT_URI",
				requestWxRediectUrl);
		logger.info("拼接请求腾讯获取code地址的url为：【{}】", url);
		logger.info("重定向腾讯获取code地址");
		return "redirect:" + url;
	}
	
	

	@RequestMapping("/callback")
	@RequireMessage(className = "OpenIdController", msg = "获取openid", callerTypeIsCustom = false)
	public ModelAndView getCode2GetOpenId(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/common/weChatMessage");
		String code = request.getParameter("code");
		// 获取重定向地址
		String attach = request.getParameter("attach");
		// 获取附加参数
		logger.info("腾讯回调本接口携带的参数：code=【{}】，attach=【{}】", code, attach);
		// 从腾讯获取的参数为空
		if (StringUtils.isBlank(code) || StringUtils.isBlank(attach)) {
			mv.addObject("message", "获取userId失败：从腾讯获取code或自定义参数失败");
			logger.info("从腾讯获取code或自定义参数失败");
			return mv;
		}
		String accessTokenInfo = dataGetWayComponent.getDataService().getAccessToken();
		JSONObject json = JSONObject.parseObject(accessTokenInfo);
		String accessToken = json.getString("access_token");
		
		logger.info("access_token【{}】", accessToken);
		// 请求腾讯获取userId的接口地址
		String url = QYWX_WX_USERINFO_URL.replace("ACCESS_TOKEN", accessToken).replace("CODE", code);
		logger.info("获取用户信息的地址为【{}】", url);
		// 调用腾讯接口返回的结果
		String result = HttpUtil.get(url);
		logger.info("获取用户信息返回【{}】", result);

		if (result.contains("UserId")) {
			JSONObject obj = JSONObject.parseObject(result);
			String userId = obj.getString("UserId");
			String openUrl = QYWX_URL_CONVERT_OPENID.replace("ACCESS_TOKEN", accessToken);
			JSONObject requestJson = new JSONObject();
			requestJson.put("userid", userId);
			logger.info("userid转为openid接口请求参数==【{}】请求地址==【{}】", requestJson, openUrl);
			result = HttpUtil.post(openUrl, requestJson.toString());
			logger.info("userid转为openid接口返回==【{}】", result);
			if (StringUtils.isBlank(result)) {
				logger.info("取得openid失败:userid转为openid接口请求超时");
				mv.addObject("message", "取得openid失败:userid转为openid接口请求超时");
				return mv;
			}
			obj = JSONObject.parseObject(result);
			// 如果调用不成功
			if (!"0".equals(obj.getString("errcode"))) {
				logger.info("取得openid失败:userid转为openid接口请求失败");
				mv.addObject("message", "取得openid失败:userid转为openid接口请求失败");
				return mv;
			}
			// 取得openid
			String openid = obj.getString("openid");

			// 重新拼接重定向地址
			String reRedirectUrl = QYH_REDIRECTURL + "?attach=" + attach + "&openId=" + openid;
			logger.info("拼接后重定向地址为：【{}】", reRedirectUrl);
			response.sendRedirect(reRedirectUrl);
			return null;
		}
		if (result.contains("OpenId")) {
			JSONObject obj = JSONObject.parseObject(result);
			String openid = obj.getString("OpenId");
			// 重新拼接重定向地址
			String reRedirectUrl = QYH_REDIRECTURL + "?attach=" + attach + "&openId=" + openid;
			logger.info("拼接后重定向地址为：【{}】", reRedirectUrl);
			response.sendRedirect(reRedirectUrl);
			return null;
		}
		// 微信服务器异常，code无效
		if (result.contains("40029")) {
			logger.info("获取userId失败，微信服务器异常，code无效，请重试，map_userId【{}】", result);
			mv.addObject("message", "获取userId失败，微信服务器异常，code无效，请重试");
			return mv;
		}
		logger.info("获取userId失败，map_userId【{}】", result);
		mv.addObject("message", "获取userId失败");
		return mv;
	}
}
