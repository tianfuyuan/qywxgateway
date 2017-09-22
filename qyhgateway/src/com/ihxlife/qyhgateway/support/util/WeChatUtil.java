package com.ihxlife.qyhgateway.support.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.ihxlife.qyhgateway.support.constant.PropertiesConstants;
import com.ihxlife.qyhgateway.support.variable.DataVariable;

/**
 * 发送警告消息工具类
 * @author Administrator
 *
 */
public class WeChatUtil {

	private final static Logger logger = LoggerFactory.getLogger(WeChatUtil.class);
	
	//请求腾讯获取access_token所需的corpId
	private static final String ACCESSTOKEN_CORPID = PropertiesConstants.ACCESSTOKEN_CORPID;

	//请求腾讯获取access_token所需的secret 
	private static final String ACCESSTOKEN_SECRET = PropertiesConstants.ACCESSTOKEN_SECRET;
	
	//企业号发送警告消息用户
	private static final String SEND_WARNING_USER = PropertiesConstants.SEND_WARNING_USER;

	//企业号发送警告消息ip 
	private static final String SEND_WARNING_IP = PropertiesConstants.SEND_WARNING_IP;
	
	// 腾讯获取access_token接口地址
	private static final String QYWX_URL_GET_ACCESS_TOKEN = PropertiesConstants.QYWX_URL_GET_ACCESS_TOKEN;
	
	// 微信发送消息URL
	private static final String QYWX_WX_SEND_MESSAGE_URL = PropertiesConstants.QYWX_WX_SEND_MESSAGE_URL;

	/**
	 * 获取企业号access_token
	 * @param CorpID
	 * @param Corpsecret
	 */
	public static String getAccessToken(String corpid, String secret) {
		// 请求获取access_token的链接
		String responseStr = "";
		try {
			String url = QYWX_URL_GET_ACCESS_TOKEN;
			url = url.replace("CorpID", corpid).replace("SECRET", secret);
			logger.info("access_token请求地址url【{}】", url);
			HttpURLConnection httpurlconnection = null;
			URL httpUrl = new URL(url);
			httpurlconnection = (HttpURLConnection) httpUrl.openConnection();
			httpurlconnection.setConnectTimeout(5000);
			httpurlconnection.setDoOutput(true);
			httpurlconnection.setRequestMethod("GET");
			httpurlconnection.setDoInput(true);
			httpurlconnection.setUseCaches(false);
			httpurlconnection.setInstanceFollowRedirects(true);
			httpurlconnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			httpurlconnection.connect();

			// 获取页面内容
			java.io.InputStream in = httpurlconnection.getInputStream();
			java.io.BufferedReader breader = new java.io.BufferedReader(new java.io.InputStreamReader(in, "UTF-8"));
			responseStr = breader.readLine();

			logger.info("access_token微信返回【{}】", responseStr);
			Map<String, String> access_tokenMap = (Map<String, String>) JsonUtil.json2Map(responseStr);
			if (!"".equals(access_tokenMap.get("access_token"))) {
				logger.info("access_token微信返回【{}】", responseStr);
			} else {
				logger.error("请求获取微信access_token失败");
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("定时更新微信access_token异常【{}】", e.getMessage());
		}
		return responseStr;
	}

	/**
	 * 发送警告消息
	 */
	public static boolean sendWaring(String content) {
		try {
			String errcode = sendMessage(content);
			if ("40001".equals(errcode) || "40014".equals(errcode) || "41001".equals(errcode)
					|| "42001".equals(errcode)) {
				logger.info("CORPID【{}】，Secret【{}】", ACCESSTOKEN_CORPID, ACCESSTOKEN_SECRET);
				String access_token = getAccessToken(ACCESSTOKEN_CORPID, ACCESSTOKEN_SECRET);
				Map<String, String> map = (Map<String, String>) JsonUtil.json2Map(access_token);

				if (map.containsKey("access_token")) {
					logger.info("获取成员的身份信息失败：从腾讯获取access_token为空。");
					return false;
				}
				DataVariable.access_token = map.get("access_token");
				DataVariable.access_token_expires_time = map.get("expires_in");
				errcode = sendMessage(content);
			} else if (!"0".equals(errcode)) {
				errcode = sendMessage(content);
			}
			if ("0".equals(errcode)) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("发送消息异常【{}】", e.getMessage());
			return false;
		}
	}

	/**
	 * 发送警告消息
	 */
	private static String sendMessage(String content) {
		JSONObject data = new JSONObject();
		String access_token = DataVariable.access_token;
		String url = QYWX_WX_SEND_MESSAGE_URL;
		url = url.replace("ACCESS_TOKEN", access_token);
		data.put("touser", SEND_WARNING_USER);
		data.put("msgtype", "text");
		data.put("agentid", "0");
		JSONObject textJson = new JSONObject();
		textJson.put("content", "警告：企业号生产跑批系统跑批【" + content + "】停止跑批！停止服务器："+SEND_WARNING_IP+"，停止时间：" + DateUtil.getCurrentDate() + " "
				+ DateUtil.getCurrentTime());
		data.put("text", textJson);
		String result;
		try {
			result = HttpUtil.post(url, data.toJSONString());
			logger.info("发送消息结果result==【{}】", result);
			JSONObject jsonObject = JSONObject.parseObject(result);
			String errcode = jsonObject.getString("errcode");
			return errcode;
		} catch (Exception e) {
			logger.info("发送警告异常【{}】", e.getMessage());
			e.printStackTrace();
			return "error";
		}
	}
}
