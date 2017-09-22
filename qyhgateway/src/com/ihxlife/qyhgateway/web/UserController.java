package com.ihxlife.qyhgateway.web;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.ihxlife.qyhgateway.dto.ReturnUserMessage;
import com.ihxlife.qyhgateway.support.annotation.CheckParamType;
import com.ihxlife.qyhgateway.support.annotation.CheckRequestParam;
import com.ihxlife.qyhgateway.support.annotation.RequireMessage;
import com.ihxlife.qyhgateway.support.components.DataGetWayComponent;
import com.ihxlife.qyhgateway.support.constant.PropertiesConstants;
import com.ihxlife.qyhgateway.support.exception.BusinessException;
import com.ihxlife.qyhgateway.support.redis.RedisUtil;
import com.ihxlife.qyhgateway.support.util.EncoderHandler;
import com.ihxlife.qyhgateway.support.util.HttpUtil;
import com.ihxlife.qyhgateway.support.util.JsonUtil;


/**
 * 用户Controller
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/api/v1")
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private DataGetWayComponent dataGetWayComponent;
    
	//请求腾讯获取code接口需要让腾讯回调的接口地址(内部) 
	private static final String QYWX_URL_WX_REDIRECT_URL_OFFICE = PropertiesConstants.QYWX_URL_WX_REDIRECT_URL_OFFICE;

	//交易来源
	private static final String QYWX_TRADE_SOURCE = PropertiesConstants.QYWX_TRADE_SOURCE;

	//秘钥
	private static final String QYWX_KEY = PropertiesConstants.QYWX_KEY;

	//企业号应用的CropId
	private static final String ACCESSTOKEN_CORPID = PropertiesConstants.ACCESSTOKEN_CORPID;

	//根据AccessToken和Code获取UserId接口 
	private static final String QYWX_WX_USERINFO_URL = PropertiesConstants.QYWX_WX_USERINFO_URL;

	//根据AccessToken和UserId获取用户信息接口
	private static final String QYWX_WX_GET_USER_URL = PropertiesConstants.QYWX_WX_GET_USER_URL;

	//请求腾讯获取Code接口
	private static final String QYWX_URL_PERSON_GET_CODE = PropertiesConstants.QYWX_URL_PERSON_GET_CODE;

	//请求腾讯获取Code接口需要让腾讯回调的接口地址 
	private static final String QYWX_URL_WX_REDIRECT_URL = PropertiesConstants.QYWX_URL_WX_REDIRECT_URL;
	
	// 企业号二次验证需要让腾讯回调的地址
	private static final String VERIFY_LOGIN_REDIRECT_URL = PropertiesConstants.VERIFY_LOGIN_REDIRECT_URL;


	/**
	 * 菜单配置统一入口auth验证
	 * @param request 请求
	 * @return
	 */
	@RequestMapping("/oauth2/index")
	@RequireMessage(msg="",className="UserController",callerTypeIsCustom=false)
	public String index(HttpServletRequest request) {
		String url = "";
		// 获取state参数，本参数值为接口配置信息id值
		String state = request.getParameter("state");
		String authType = request.getParameter("authType");
		if("verify".equals(authType)){
			url = QYWX_URL_PERSON_GET_CODE.replace("CORPID", ACCESSTOKEN_CORPID)
					.replace("REDIRECT_URI", VERIFY_LOGIN_REDIRECT_URL);
			logger.info("请求腾讯获取code的地址为：【{}】", url);
			return "redirect:" + url;
		}
		logger.info("需要携带的state为:【{}】", state);
		url = QYWX_URL_PERSON_GET_CODE.replace("CORPID", ACCESSTOKEN_CORPID)
				.replace("REDIRECT_URI", QYWX_URL_WX_REDIRECT_URL_OFFICE).replace("STATE", state);
		logger.info("请求腾讯获取code的地址为：【{}】", url);
		return "redirect:" + url;
	}

	/**
	 * 腾讯回调接口，通过传递过来的code获取userId
	 * @param request 请求
	 * @param response 响应
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/user/oauth2/callback")
	@RequireMessage(msg="",className="UserController",callerTypeIsCustom=false)
	public ModelAndView getCode2GetUserId(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/common/weChatMessage");
		String code = request.getParameter("code");
		// 对应接口配置文件的主键
		String id = request.getParameter("state");
		logger.info("从腾讯获取code的值【{}】id值【{}】", code, id);
		// 从腾讯获取的参数为空
		if (StringUtils.isBlank(code) || StringUtils.isBlank(id)) {
			throw new BusinessException("获取成员的身份信息失败：从腾讯获取code或state失败");
		}
		
		// 由于腾讯返回的参数后面加了其他字符，需要手动截取前十位
		id = id.substring(0, 10);
		logger.info("截取后id值【{}】", id);
		// 从接口信息配置文件中获取重定向接口地址
		String redirectUrl = dataGetWayComponent.getDataService().getInterfaceConfig(id);
		// 接口配置信息文件中不存在此信息
		if (StringUtils.isBlank(redirectUrl)) {
			throw new BusinessException("获取成员的身份信息失败：接口配置文件中没有对应的数据");
		}
		
		// 从redis中获取code
		String result = readUserIdFromRedis(code);
		if (result.contains("UserId")) {
			// 时间戳
			String timestamp = String.valueOf(System.currentTimeMillis());
			// 随机字符串
			String nonce = UUID.randomUUID().toString();
			// 数据包
			JSONObject resultJson = JSONObject.parseObject(result);
			String userid =  resultJson.getString("UserId");
			// 生成秘钥
			String signature = EncoderHandler.encodeByMD5(QYWX_KEY + timestamp + nonce + QYWX_TRADE_SOURCE)
					.toUpperCase();
			// 拼装请求第三方地址URL
			StringBuffer buffer = new StringBuffer();
			buffer.append(redirectUrl);
			buffer.append("timestamp=");
			buffer.append(timestamp);
			buffer.append("&nonce=");
			buffer.append(nonce);
			buffer.append("&trade_source=");
			buffer.append(QYWX_TRADE_SOURCE);
			buffer.append("&signature=");
			buffer.append(signature);
			// 以参数形式传递数据
			buffer.append("&userid=");
			buffer.append(userid);
			// 重新拼装后重定向地址
			String newRedirectUrl = buffer.toString();
			logger.info("重定向redirectUrl值【{}】", newRedirectUrl);
			response.sendRedirect(newRedirectUrl);
			return null;
		}
		// 如果未关注企业号获取的则是openid
		if (result.contains("OpenId")) {
			throw new BusinessException("获取成员的身份信息失败，请查看成员是否关注该企业号，用户是否为该企业号成员");
		}
		
		// 微信服务器异常，code无效
		if (result.contains("40029")) {
			throw new BusinessException("获取成员的身份信息失败，微信服务器异常，code无效，请重试");
		}
		throw new BusinessException("请求腾讯获取成员的身份信息失败");
	}

	/**
	 * 获取用户信息接口
	 * @param request 请求
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/user/get")
	@ResponseBody
	@CheckRequestParam(checkRequestType = CheckParamType.CHECK_DATA)
	@RequireMessage(msg="获取用户信息",className="UserController",callerTypeIsCustom=false)
	public ReturnUserMessage getUserInfo(HttpServletRequest request) throws Exception {
		ReturnUserMessage returnUserMessage = new ReturnUserMessage();
		String data = request.getParameter("data");
		logger.info("参数和签名校验成功，data数据包内容为【{}】", data);
		Map<String, String> dataMap = (Map<String, String>) JsonUtil.json2Map(data);
		
		if (!dataMap.containsKey("userid") || StringUtils.isBlank(dataMap.get("userid"))) {
			throw new BusinessException("获取用户信息失败：缺失工号");
		}
		
		String userId = dataMap.get("userid");
		String accessTokenInfo = dataGetWayComponent.getDataService().getAccessToken();
		JSONObject json = JSONObject.parseObject(accessTokenInfo);
		// 获取accesstoken
		String access_token = json.getString("access_token");
		logger.info("获取access_token的值为【{}】", access_token);
		// data数据包json
		JSONObject dataJson = new JSONObject();
		String url = QYWX_WX_GET_USER_URL.replace("ACCESS_TOKEN", access_token).replace("USERID", userId);
		logger.info("请求用户信息的url【{}】", url);
		String result = HttpUtil.get(url);
		logger.info("腾讯返回的result【{}】", result);
		
		if (StringUtils.isBlank(result)) {
			throw new BusinessException("请求腾讯查询业务员返回结果为空");
		}
		
		// 腾讯返回的json数据
		JSONObject txResultJson = JSONObject.parseObject(result);
		String errcode = txResultJson.getString("errcode");
		
		if("60111".equals(errcode)){
			throw new BusinessException("获取用户信息失败：请确认企业号中是否存在此业务员");
		}
		if (!errcode.equals("0")) {
			throw new BusinessException("获取用户信息失败:腾讯返回错误信息");
		}
		
		dataJson.put("userid", txResultJson.get("userid"));
		dataJson.put("name", txResultJson.get("name"));
		dataJson.put("mobile", txResultJson.get("mobile"));
		dataJson.put("weixinid", txResultJson.get("weixinid"));
		dataJson.put("avatar", txResultJson.get("avatar"));
		dataJson.put("status", txResultJson.get("status"));
		
		returnUserMessage.setResult_code("suc");
		returnUserMessage.setResult_msg("获取企业号用户信息成功");
		returnUserMessage.setData((dataJson.toString()).replace("\\", ""));
		logger.info("返回的数据dateJson【{}】", JsonUtil.object2Json(returnUserMessage));
		return returnUserMessage;
	}
	
	/**
	 * 获取userId接口(供第三方调用)
	 * @param request 请求
	 * @param response 响应
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/other/query/authorize")
	@CheckRequestParam(checkRequestType = CheckParamType.CHECK_DATA)
	@RequireMessage(msg="获取userId信息",className="UserController",callerTypeIsCustom=false)
	public ModelAndView getUserId(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/common/weChatMessage");
		// 重定向地址
		String redirectUrl = "";
		// 附加参数
		String attach = "";
		
		String data = request.getParameter("data");
		Map<String, String> dataMap = (Map<String, String>) JsonUtil.json2Map(data);
		
		// 重定向地址为空标识
		if (!dataMap.containsKey("redirectURL") || "".equals(dataMap.get("redirectURL"))) {
			throw new BusinessException("获取userId信息失败：缺少回调地址");
		}
		
		redirectUrl = dataMap.get("redirectURL");
		logger.info("回调地址为：【{}】", redirectUrl);
		
		if (dataMap.containsKey("attach")) {
			attach = dataMap.get("attach");
		}
		// 对请求携带的重定向地址做utf-8转码操作
		redirectUrl = URLEncoder.encode(redirectUrl, "UTF-8");
		logger.info("UTF-8转码后回调地址为：【{}】", redirectUrl);
		// 拼接腾讯回调地址
		String requestWxRediectUrl = QYWX_URL_WX_REDIRECT_URL.replace("REDIRECT_URL", redirectUrl);
		logger.info("拼接腾讯回调地址为：【{}】", requestWxRediectUrl);
		// 拼接请求腾讯获取code地址
		String url = QYWX_URL_PERSON_GET_CODE.replace("CORPID", ACCESSTOKEN_CORPID)
				.replace("REDIRECT_URI", requestWxRediectUrl).replace("STATE", null == attach ? "" : attach);
		logger.info("拼接请求腾讯获取code地址的url为：【{}】", url);
		logger.info("重定向腾讯获取code地址");
		response.sendRedirect(url);
		return null;
	}

	/**
	 * 腾讯回调接口
	 * @param request 请求
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/other/query/callback")
	@ResponseBody
	@RequireMessage(msg="",className="UserController",callerTypeIsCustom=false)
	public ModelAndView getUserIdByCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/common/weChatMessage");
		String code = request.getParameter("code");
		// 获取重定向地址
		String redirecetUrl = request.getParameter("redirectURL");
		// 获取附加参数
		String attach = request.getParameter("state");
		logger.info("腾讯回调本接口携带的参数：code=【{}】，redirecetUrl=【{}】，attach=【{}】", code, redirecetUrl, attach);
		
		// 从腾讯获取的参数为空
		if (StringUtils.isBlank(code) || StringUtils.isBlank(redirecetUrl)) {
			throw new BusinessException("获取userId失败：从腾讯获取code或重定向地址失败");
		}
		String accessTokenInfo = dataGetWayComponent.getDataService().getAccessToken();
		JSONObject json = JSONObject.parseObject(accessTokenInfo);
		// 获取accesstoken
		String access_token = json.getString("access_token");
		logger.info("access_token【{}】", access_token);
		
		// 从redis中获取code
		String result = readUserIdFromRedis(code);
		if (result.contains("UserId")) {
			JSONObject resultJson = JSONObject.parseObject(result);
			// 从腾讯返回值中取出userid
			String userid = resultJson.getString("UserId");
			// 重新拼接重定向地址
			String reRedirectUrl = redirecetUrl + "?attach=" + attach + "&userid=" + userid;
			logger.info("拼接后重定向地址为：【{}】", reRedirectUrl);
			response.sendRedirect(reRedirectUrl);
			return null;
		}
		
		if (result.contains("OpenId")) {
			throw new BusinessException("获取userId失败:请查看成员是否关注该企业号，用户是否为该企业号成员");
		}
		
		// 微信服务器异常，code无效
		if (result.contains("40029")) {
			throw new BusinessException("获取userId失败，微信服务器异常，code无效，请重试");
		}
		
		throw new BusinessException("获取userId失败");
	}

	/**
	 * 根据code从redis中获取userId
	 * @param code 腾讯回调所携带的code
	 * @return
	 * @throws Exception
	 */
	private String readUserIdFromRedis(String code) throws Exception {
		String userId = "";
		
		if (RedisUtil.isExists(code)) {
			userId = RedisUtil.getResisInfo(code);
			logger.info("redis中存在userId，code为【{}】,userId为【{}】", code, userId);
			return userId;
		}
		
		logger.info("redis中不存在userId，需要从腾讯重新获取userId");
		// 调用数据获取方式组件来获取AccessToken
		String accessTokenInfo = dataGetWayComponent.getDataService().getAccessToken();
		JSONObject json = JSONObject.parseObject(accessTokenInfo);
		// 获取accesstoken
		String access_token = json.getString("access_token");
		// 请求腾讯获取userId的接口地址
		String url = QYWX_WX_USERINFO_URL.replace("ACCESS_TOKEN", access_token).replace("CODE", code);
		logger.info("获取用户信息的地址为【{}】", url);
		// 调用腾讯接口返回的结果
		userId = HttpUtil.get(url);
		logger.info("获取用户信息返回【{}】", userId);
		
		// 如果返回正确userid则放入redis中
		if (StringUtils.isNotBlank(userId) && userId.contains("UserId")) {
			// 将code userid绑定放入redis中
			RedisUtil.insertResisInfo(code, userId, 60 * 10);
		}
		
		return userId;

	}

}
