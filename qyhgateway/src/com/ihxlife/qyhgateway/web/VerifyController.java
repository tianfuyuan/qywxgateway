package com.ihxlife.qyhgateway.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ihxlife.qyhgateway.support.annotation.RequireMessage;
import com.ihxlife.qyhgateway.support.common.LoggerStartAndEnd;
import com.ihxlife.qyhgateway.support.components.DataGetWayComponent;
import com.ihxlife.qyhgateway.support.constant.PropertiesConstants;
import com.ihxlife.qyhgateway.support.exception.BusinessException;
import com.ihxlife.qyhgateway.support.redis.RedisUtil;
import com.ihxlife.qyhgateway.support.util.ConvertUtil;
import com.ihxlife.qyhgateway.support.util.DateUtil;
import com.ihxlife.qyhgateway.support.util.EncoderHandler;
import com.ihxlife.qyhgateway.support.util.GenerateRandomUtils;
import com.ihxlife.qyhgateway.support.util.HttpUtil;
import com.ihxlife.qyhgateway.support.util.JsonUtil;
import com.ihxlife.qyhgateway.support.util.SMSUtil;

/**
 * 验证码Controller
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/api/v1")
public class VerifyController {

	// 日志打印
	private static final Logger logger = LoggerFactory.getLogger(VerifyController.class);

	// 企业号查询人员接口
	private static final String QYWX_URL_PERSON_GET = PropertiesConstants.QYWX_URL_PERSON_GET;

	// 二次验证关注企业号接口
	private static final String QYWX_URL_PERSON_FOLLOW = PropertiesConstants.QYWX_URL_PERSON_FOLLOW;

	// 发送验证码地址
	private static final String QYWX_SEND_VERIFY_URL = PropertiesConstants.QYWX_SEND_VERIFY_URL;

	// 验证验证码地址
	private static final String QYWX_CHECK_VERIFY_URL = PropertiesConstants.QYWX_CHECK_VERIFY_URL;

	// 消息id
	private static final String QYWX_MSG_ID = PropertiesConstants.QYWX_MSG_ID;

	// 验证码业务类型
	private static final String QYWX_VERIFY_BUSITYPE = PropertiesConstants.QYWX_VERIFY_BUSITYPE;

	// 验证码签名key
	private static final String QYWX_VERIFY_KEY = PropertiesConstants.QYWX_VERIFY_KEY;

	// 信鸽提供的uid
	private static final String QYWX_VERIFY_UID = PropertiesConstants.QYWX_VERIFY_UID;

	// 官微回调的企业号地址
	private static final String GW_REDIRECT_URL = PropertiesConstants.GW_REDIRECT_URL;
	
	// 官微交易来源
	private static final String GW_TRADESOURCE = PropertiesConstants.GW_TRADESOURCE;
	
	// 官微秘钥
	private static final String GW_KEY = PropertiesConstants.GW_KEY;
	
	// 官微获取openid接口
	private static final String GW_QUERY_OPENID = PropertiesConstants.GW_QUERY_OPENID;
	
	// 官微注册接口
	private static final String GW_REGISTER_URL = PropertiesConstants.GW_REGISTER_URL;
	
	// 天池交易来源
	private static final String TC_SYSTEM_SOURCE = PropertiesConstants.TC_SYSTEM_SOURCE;
	
	// 天池秘钥
	private static final String TC_KEY = PropertiesConstants.TC_KEY;
	
	// 天池获取用户信息接口
	private static final String TC_QUERY_USER = PropertiesConstants.TC_QUERY_USER;
		
	// 根据code获取userid接口 
	private static final String QYWX_URL_PERSON_GET_USERID= PropertiesConstants.QYWX_URL_PERSON_GET_USERID;	
	
	// 微课堂部门ID
	private static final String WKT_DEPARTMENT_ID = PropertiesConstants.WKT_DEPARTMENT_ID;
	
	// 微课堂通知地址
	private static final String WKT_NOTICE_URL = PropertiesConstants.WKT_NOTICE_URL;
	
	// 微课堂key
	private static final String WKT_KEY = PropertiesConstants.WKT_KEY;
	
	// 微课堂交易来源
	private static final String WKT_TRADESOURCE = PropertiesConstants.WKT_TRADESOURCE;
	

	@Autowired
	private DataGetWayComponent dataGetWayComponent;

	
	/**
	 * 腾讯回调页面进入登录页面
	 * @throws Exception 
	 */
	@RequestMapping("/verify/callback")
	@RequireMessage(msg = "", className = "VerifyController", callerTypeIsCustom = true,interfaceIsVerify = true)
	public ModelAndView loginView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mv = new ModelAndView();
		//内勤页面
		mv.setViewName("/verify/officeLogin");
		String userid = "";
		String code = request.getParameter("code");
		logger.info("从腾讯获取code的值【{}】", code);
		// 从腾讯获取的参数为空
		if (StringUtils.isBlank(code)) {
			logger.info("从腾讯获取的code值为空");
		}
		// 从redis中获取code
		String result = readUserIdFromRedis(code);
		logger.info("腾讯的返回值为：【{}】",result);
		if(result.contains("UserId")){
			JSONObject resultJson = JSONObject.parseObject(result);
			userid = resultJson.getString("UserId");
			logger.info("userid为【{}】",userid);
			mv.addObject("userid", userid);
			if(isNumeric(userid)){
				//外勤页面
				mv.setViewName("/verify/outsideLogin");
			}
		}
		return mv;
	}

	/**
	 * 发送验证码接口
	 * @param request 请求
	 * @param response 响应
	 * @throws Exception
	 */
	@RequestMapping("/sms/code/send")
	@ResponseBody
	@RequireMessage(msg = "", className = "VerifyController", callerTypeIsCustom = true,interfaceIsVerify = true)
	public JSONObject sendMsg(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("result_code", "fail");
		// 取得手机号和工号
		String mobile = request.getParameter("mobile");
		String userid = request.getParameter("userid");
		logger.info("发送验证码，mobile==【{}】userid==【{}】", mobile, userid);

		if (StringUtils.isBlank(mobile) || StringUtils.isBlank(userid)) {
			throw new BusinessException("发送验证码失败：缺失参数，mobile【" + mobile + "】userid【" + userid + "】");
		}

		String accessTokenInfo = dataGetWayComponent.getDataService().getAccessToken();
		JSONObject jsonAcc = JSONObject.parseObject(accessTokenInfo);
		// 获取accesstoken
		String access_token = jsonAcc.getString("access_token");
		logger.info("获取access_token的值为【{}】", access_token);

		if (StringUtils.isBlank(access_token)) {
			throw new BusinessException("关注企业号失败：access_token为【" + access_token + "】");
		}

		String url = QYWX_URL_PERSON_GET.replace("ACCESS_TOKEN", access_token) + "&userid=" + userid;
		logger.info("根据工号查询企业号中是否有该业务员地址==【{}】", url);
		String result = HttpUtil.get(url);

		if (StringUtils.isBlank(result)) {
			throw new BusinessException("请求腾讯查询业务员返回结果为空");
		}

		JSONObject json = JSONObject.parseObject(result);
		String errcode = json.getString("errcode");

		if (!"0".equals(errcode)) {
			throw new BusinessException("关注企业号失败：您所输入的OA账号/工号在企业号中不存在！");
		}

		// 1： 通过手机号进行短信的发送
		LoggerStartAndEnd.loggerStartOrFinish(logger, "VerifyController", "调用短信发送接口", "开始");
		// 调用信鸽平台短信发送请求数据
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("msg_id", QYWX_MSG_ID + GenerateRandomUtils.getNumr(10));
		paramMap.put("nation_code", "");
		paramMap.put("mobile", mobile);
		paramMap.put("busi_type", QYWX_VERIFY_BUSITYPE);
		Map<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("data", JsonUtil.object2Json(paramMap));
		// 1、组装调用短信发送接口的请求数据
		Map<String, String> requestPTMap = SMSUtil.sign(requestMap, QYWX_VERIFY_UID, QYWX_VERIFY_KEY);
		// 2、post请求信鸽平台
		logger.info("-------------验证码发送接口：信鸽平台验证码发送接口地址【{}】请求报文为【{}】", QYWX_SEND_VERIFY_URL, requestPTMap);
		String responseStr = HttpUtil.post(QYWX_SEND_VERIFY_URL, requestPTMap);
		logger.info("-------------验证码发送接口：信鸽平台验证码发送接口返回报文为【{}】", responseStr);

		// 3、解析信鸽平台返回报文

		JSONObject responseJson = JSONObject.parseObject(responseStr);

		String serviceResult = responseJson.getString("result");

		if (!"succ".equals(serviceResult)) {
			throw new BusinessException("关注企业号失败：信鸽平台发送消息接口失败");
		}

		String dataParam = responseJson.get("data") == null ? "" : responseJson.getString("data");
		JSONObject dataParamJson = JSONObject.parseObject(dataParam);
		String msg_id = dataParamJson.get("msg_id") == null ? "" : dataParamJson.getString("msg_id");
		logger.info("验证码发送成功");
		jsonObject.put("result_code", "suc");
		jsonObject.put("result_message", "发送验证码成功！");
		jsonObject.put("result_msg_id", msg_id);
		return jsonObject;
	}

	/**
	 * 校验登录信息
	 * @param request 请求
	 * @param response 响应
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/message/verify")
	@ResponseBody
	@RequireMessage(msg = "", className = "VerifyController", callerTypeIsCustom = true,interfaceIsVerify = true)
	public JSONObject checkLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String department = "";
		JSONObject jsonResult = new JSONObject();
		jsonResult.put("result_code", "fail");
		// 获取参数
		String userId = request.getParameter("userId");// 输入的工号
		String userName = request.getParameter("userName");// 输入的姓名
		String mobile = request.getParameter("mobile");// 输入的手机号
		String idCode = request.getParameter("idCode");// 输入的验证码
		String msg_id = request.getParameter("msg_id");// 验证码唯一标识
		logger.info("关注企业号：userId==【{}】userName==【{}】mobile==【{}】idCode==【{}】msg_id==【{}】", userId, userName, mobile,
				idCode, msg_id);
		// 非空校验
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(userName) || StringUtils.isBlank(mobile)
				|| StringUtils.isBlank(idCode) || StringUtils.isBlank(msg_id)) {
			throw new BusinessException("关注企业号失败：缺失参数！userId【" + userId + "】userName【" + userName + "】mobile【" + mobile
					+ "】idCode【" + idCode + "】msg_id==【" + msg_id + "】");
		}
		String accessTokenInfo = dataGetWayComponent.getDataService().getAccessToken();
		JSONObject jsonAcc = JSONObject.parseObject(accessTokenInfo);
		// 获取accesstoken
		String access_token = jsonAcc.getString("access_token");
		logger.info("获取access_token的值为【{}】", access_token);
		// 查询企业号中此人信息
		String url = QYWX_URL_PERSON_GET.replace("ACCESS_TOKEN", access_token) + "&userid=" + userId;
		;
		logger.info("根据工号查询企业号中是否有该业务员地址==【{}】", url);
		String result = HttpUtil.get(url);
		logger.info("查询人员接口返回result为【{}】", result);
		if (StringUtils.isBlank(result)) {
			throw new BusinessException("请求腾讯查询业务员返回结果为空");
		}
		JSONObject json = JSONObject.parseObject(result);
		String errcode = json.getString("errcode");
		if (!"0".equals(errcode)) {
			throw new BusinessException("关注企业号失败：您所输入的OA账号/工号在企业号中不存在！");
		}
		//获取业务员所在部门（腾讯的）编号
		JSONArray array = json.getJSONArray("department");
		if(array != null && array.size()>0){
			department = array.getString(0);
		}
		// 是否重复关注校验
		if (json.get("status") != null && "1".equals(json.getString("status"))) {
			throw new BusinessException("您已经关注该企业号，无需重复关注！");
		}
		// 匹配姓名(创建人员接口中姓名为必填参数所以不用考虑姓名为空状况)
		if (!json.getString("name").equals(userName)) {
			throw new BusinessException("关注企业号失败：您填写的姓名有误,请重新填写！");
		}
		if (json.get("mobile") == null) {
			throw new BusinessException("关注企业号失败：企业号中您的信息不存在手机号！");
		}
		logger.info(json.getString("mobile")+":"+mobile+"比较："+json.getString("mobile").equals(mobile));
		// 匹配手机号
		if (!(json.getString("mobile").equals(mobile))) {
			throw new BusinessException("关注企业号失败：您填写的手机号有误,请重新填写！");
		}

		// 验证码校验
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("msg_id", msg_id);
		paramMap.put("mobile", mobile);
		paramMap.put("chk_code", idCode);
		paramMap.put("busi_type", QYWX_VERIFY_BUSITYPE);
		Map<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("data", JsonUtil.object2Json(paramMap));
		// 1、 信鸽平台公共参数和短信发送接口的请求数据组装
		Map<String, String> requestPTMap = SMSUtil.sign(requestMap, QYWX_VERIFY_UID, QYWX_VERIFY_KEY);
		// 2、post请求信鸽平台
		logger.info("申请身份验证接口调用：调用信鸽平台URL【{}】参数requestMap【{}】", QYWX_CHECK_VERIFY_URL, requestPTMap);
		String responseStr = HttpUtil.post(QYWX_CHECK_VERIFY_URL, requestMap);
		logger.info("申请身份验证接口调用：调用信鸽平台返回报文【{}】", responseStr);
		// 3、解析信鸽平台返回报文
		JSONObject responseJson = JSONObject.parseObject(responseStr);
		String serviceResult = responseJson.getString("result");
		String serviceMessage = responseJson.getString("message");
		if (!"succ".equals(serviceResult)) {
			logger.info("关注企业号失败：您填写的验证码有误,请重新填写！serviceMessage【{}】", serviceMessage);
			throw new BusinessException("关注企业号失败：您填写的验证码有误,请重新填写！");
		}

		// 开始关注企业号
		url = QYWX_URL_PERSON_FOLLOW.replace("ACCESS_TOKEN", access_token) + "&userid=" + userId;
		logger.info("关注企业号地址==【{}】", url);
		result = HttpUtil.get(url);
		logger.info("关注企业号返回result==【{}】", result);
		if (StringUtils.isBlank(result)) {
			throw new BusinessException("请求腾讯业务员关注接口返回结果为空");
		}
		json = JSONObject.parseObject(result);
		if (!"0".equals(json.getString("errcode"))) {
			logger.info("关注企业号失败：关注接口返回result为【{}】", result);
			throw new BusinessException("关注企业号失败：请确认微信号不为空！微信号已经与手机号绑定！");
		}
		logger.info("关注企业号成功！");
		jsonResult.put("result_code", "suc");
		jsonResult.put("result_msg", "关注成功！");
		//关注人身份是否为微课堂人员
		if(WKT_DEPARTMENT_ID.equals(department)){
			logger.info("关注人为微课堂人员，开始通知微课堂");
			String timestamps = System.currentTimeMillis()+"";
			String nonce = UUID.randomUUID()+"";
			JSONObject objectjson = new JSONObject();
			objectjson.put("agentcode", userId);
			objectjson.put("mobile", mobile);
			String sign = EncoderHandler.Md5_32(WKT_KEY+timestamps+nonce+WKT_TRADESOURCE+objectjson).toUpperCase();
			Map<String,String> wktRequestMap = new HashMap<String, String>();
			wktRequestMap.put("signature", sign);
			wktRequestMap.put("timestamp", timestamps);
			wktRequestMap.put("nonce", nonce);
			wktRequestMap.put("trade_source", WKT_TRADESOURCE);
			wktRequestMap.put("data", objectjson.toString());
			logger.info("请求微课堂报文为：【{}】",wktRequestMap);
			String wktResult = HttpUtil.post(WKT_NOTICE_URL, wktRequestMap);
			logger.info("通知微课堂返回结果为【{}】",wktResult);
		}
		return jsonResult;
	}

	/**
	 * 请求官微获取openid接口
	 * @param request 请求
	 * @param response 响应
	 * @throws IOException
	 */
	@RequestMapping("/wechat/openid")
	@RequireMessage(className = "VerifyController", msg = "", callerTypeIsCustom = true,interfaceIsVerify = true)
	public void sendHualife2GetOpenId(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String userid = request.getParameter("userid");
		JSONObject obj = new JSONObject();
		obj.put("redirectUrl", GW_REDIRECT_URL);
		obj.put("attach", userid);
		long timestamp = System.currentTimeMillis();
		// 随机字符串
		String nonce = UUID.randomUUID().toString();
		logger.info("key:【{}】，timestamp:【{}】，nonce：【{}】，trade_source：【{}】，data:【{}】", GW_KEY, timestamp, nonce,
				GW_TRADESOURCE, obj);
		String sign = EncoderHandler.Md5_32(GW_KEY + timestamp + nonce + GW_TRADESOURCE + obj).toUpperCase();
		logger.info("生成的签名为【{}】", sign);
		String jsondata = URLEncoder.encode(obj.toString(), "UTF-8");
		logger.info("格式化后的data包【{}】", jsondata);
		String url = GW_QUERY_OPENID + "signature=" + sign + "&timestamp=" + timestamp + "&nonce=" + nonce
				+ "&trade_source=" + GW_TRADESOURCE + "&data=" + jsondata;
		logger.info("重定向的地址为：【{}】", url);
		response.sendRedirect(url);
	}

	/**
	 * 官微携带openid回调接口
	 * @param request
	 * @return
	 */
	@RequestMapping("/wechat/callback")
	@RequireMessage(className = "VerifyController", msg = "", callerTypeIsCustom = true,interfaceIsVerify = true)
	public ModelAndView getOpenId(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/verify/bindsuccess");
		String openid = request.getParameter("openid");
		String userid = request.getParameter("attach");
		logger.info("获取到的openid为【{}】,userid为【{}】", openid, userid);
		if (StringUtils.isBlank(openid) || StringUtils.isBlank(userid)) {
			logger.info("userid或openid为空！userid【{}】，openid【{}】", userid, openid);
			return mv;
		}
		if (userid.length() == 16) {
			logger.info("userid工号为16位");
			return mv;
		}
		logger.info("开始获取userid数据");

		// ---------------------------------------------调用天池获取数据--开始---------------------------------------------
		String result = "";
		// 拼接请求天池报文
		JSONObject reqsky = new JSONObject();
		JSONObject head = new JSONObject();
		JSONObject body = new JSONObject();
		body.put("agent_code", userid);
		head.put("source_system", TC_SYSTEM_SOURCE);
		head.put("invoke_time", DateUtil.currentDate("yyyy-MM-dd HH:mm:ss"));
		head.put("security_check", EncoderHandler.Md5_32(body.toString() + TC_KEY));
		head.put("bus_serial_number", GenerateRandomUtils.getNumr(20));
		reqsky.put("head", head);
		reqsky.put("body", body);
		logger.info("调用天池接口，入参为【{}】", reqsky.toString());
		
		// 请求天池获取用户信息
		result = HttpUtil.post(TC_QUERY_USER, reqsky.toString());
		logger.info("调用天池接口返回数据为【{}】", result);
		JSONObject resultJsons = JSONObject.parseObject(result);
		JSONArray array = resultJsons.getJSONObject("body").getJSONArray("agent_info");
		if (array.size() <= 0) {
			logger.info("天池未查到员工，工号为【{}】", userid);
			return mv;
		}
		JSONObject userJson = array.getJSONObject(0);
		String userName = userJson.getString("name");
		String idType = userJson.getString("idno_type");
		String idNo = userJson.getString("idno");
		String birthday = userJson.getString("birthday");
		String sex = userJson.getString("sex");
		String mobile = userJson.getString("mobile");
		logger.info("请求天池获取的用户信息为：姓名【{}】，证件类型【{}】，证件号【{}】，生日【{}】，性别【{}】，手机号【{}】", userName, idType, idNo, birthday, sex,
				mobile);
		//转换性别
		sex = ConvertUtil.sexConvert(sex);
		logger.info("性别转换后为【{}】", sex);
		//转换证件类型
		idType = ConvertUtil.idTypeConvert(idType);
		logger.info("证件类型转换后为【{}】", idType);

		// ---------------------------------------------调用天池获取数据--结束---------------------------------------------

		// -----------------------------------------------请求官微注册--开始-----------------------------------------------
		long timestamps = System.currentTimeMillis();
		// 随机字符串
		String nonce = UUID.randomUUID().toString();
		// 拼接请求官微报文
		JSONObject data = new JSONObject();
		data.put("register_channel", GW_TRADESOURCE);
		data.put("openid", openid);
		data.put("user_name", userName);
		data.put("id_type", idType);
		data.put("id_no", idNo);
		data.put("birthday", birthday);
		data.put("gender", sex);
		data.put("mobile", mobile);
		logger.info("key:【{}】，timestamp:【{}】，nonce：【{}】，trade_source：【{}】，data:【{}】", GW_KEY, timestamps, nonce,
				GW_TRADESOURCE, data);
		String sign = EncoderHandler.Md5_32(GW_KEY + timestamps + nonce + GW_TRADESOURCE + data).toUpperCase();
		logger.info("生成的签名为【{}】", sign);

		Map<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("signature", sign);
		requestMap.put("timestamp", timestamps + "");
		requestMap.put("nonce", nonce);
		requestMap.put("trade_source", GW_TRADESOURCE);
		requestMap.put("data", data.toString());
		logger.info("请求官微报文为：【{}】", requestMap);
		String gwResult = HttpUtil.post(GW_REGISTER_URL, requestMap);
		logger.info("官微返回报文为：【{}】", gwResult);
		JSONObject gwResultJson = JSONObject.parseObject(gwResult);

		String resultCode = gwResultJson.getString("result");
		String resultMessage = gwResultJson.getString("message");
		if ("succ".equals(resultCode)) {
			logger.info("官微注册成功！信息为【{}】", resultMessage);
		} else {
			logger.info("官微注册失败！信息为【{}】", resultMessage);
		}
		// -----------------------------------------------请求官微注册--结束-----------------------------------------------

		return mv;
	}

	/**
	 * 获取主键
	 * 
	 * @return
	 */
	public static String getSerialNo() {
		// 生成年月日时分秒14位数字字符串
		String tSerialNo = "";
		Date dt = new Date();
		SimpleDateFormat matter1 = new SimpleDateFormat("yyyyMMddHHmmss");
		String tCurrentDate = matter1.format(dt);
		// 生成6位随机数字
		int RandomNo = (int) ((Math.random() * 9 + 1) * 100000);
		tSerialNo = tCurrentDate + String.valueOf(RandomNo);
		return tSerialNo;
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
		String accessTokenInfo = dataGetWayComponent.getDataService().getAccessToken();
		JSONObject jsonAcc = JSONObject.parseObject(accessTokenInfo);
		// 获取accesstoken
		String access_token = jsonAcc.getString("access_token");
		logger.info("获取access_token的值为【{}】", access_token);

		if (StringUtils.isBlank(access_token)) {
			throw new BusinessException("关注企业号失败：access_token为【" + access_token + "】");
		}
		// 请求腾讯获取userId的接口地址
		String url = QYWX_URL_PERSON_GET_USERID.replace("ACCESS_TOKEN", access_token).replace("CODE", code);
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
	
	/**
	 * 判断字符串是否为数字
	 * @param str 字符串
	 * @return
	 */
	private boolean isNumeric(String str){
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	} 
	
	
}
