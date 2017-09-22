package com.ihxlife.qyhgateway.support.constant;

import java.util.Properties;

/**
 * 配置文件常量
 * 
 * @author admin
 *
 */
public class PropertiesConstants {

	private static final Properties URL_PROPERTIES = PropertiesInit.getUrlProperties();
	
	private static final Properties PARAM_PROPERTIES = PropertiesInit.getParamProperties();

	/******************************************* SEND_WARNING_PROPERTIES *********************************************/
	/***** 企业号发送警告消息ip *****/
	public static final String SEND_WARNING_IP = PARAM_PROPERTIES.getProperty("send.warning.ip");
	/***** 企业号发送警告消息用户 *****/
	public static final String SEND_WARNING_USER = PARAM_PROPERTIES.getProperty("send.warning.user");
	/******************************************* SEND_WARNING_PROPERTIES *********************************************/

	
	
	/******************************************* ACCESSTOKEN_PROPERTIES *********************************************/

	/***** 请求腾讯获取access_token所需的corpId *****/
	public static final String ACCESSTOKEN_CORPID = PARAM_PROPERTIES.getProperty("accesstoken.corpid");
	/***** 请求腾讯获取access_token所需的secret *****/
	public static final String ACCESSTOKEN_SECRET = PARAM_PROPERTIES.getProperty("accesstoken.secret");

	/******************************************* ACCESSTOKEN_PROPERTIES *********************************************/

	
	
	/******************************************* URL_PROPERTIES *********************************************/

	/***** 秘钥*****/
	public static final String QYWX_KEY = URL_PROPERTIES.getProperty("qyh.key");
	/***** 交易来源 *****/
	public static final String QYWX_TRADE_SOURCE = URL_PROPERTIES.getProperty("qyh.tradesource");
	/***** 微信发送消息url *****/
	public static final String QYWX_WX_SEND_MESSAGE_URL = URL_PROPERTIES.getProperty("wx.sendmessage.url");
	/***** 根据accesstoken和code获取Userid接口 *****/
	public static final String QYWX_WX_USERINFO_URL = URL_PROPERTIES.getProperty("wx.userinfo.url");
	/***** 根据accesstoken和userid获取用户信息接口 *****/
	public static final String QYWX_WX_GET_USER_URL = URL_PROPERTIES.getProperty("wx.get.user.url");
	/***** 发送验证码地址 *****/
	public static final String QYWX_SEND_VERIFY_URL = URL_PROPERTIES.getProperty("verify.send.url");
	/***** 验证验证码地址 *****/
	public static final String QYWX_CHECK_VERIFY_URL = URL_PROPERTIES.getProperty("verify.check.url");
	/***** 消息id *****/
	public static final String QYWX_MSG_ID = URL_PROPERTIES.getProperty("verify.msg.id");
	/***** 验证码业务类型 *****/
	public static final String QYWX_VERIFY_BUSITYPE = URL_PROPERTIES.getProperty("verify.busitype");
	/***** 验证码签名key *****/
	public static final String QYWX_VERIFY_KEY = URL_PROPERTIES.getProperty("verify.key");
	/***** 信鸽提供的uid *****/
	public static final String QYWX_VERIFY_UID = URL_PROPERTIES.getProperty("verify.uid");

	/***** 查询人员接口 *****/
	public static final String QYWX_URL_PERSON_GET = URL_PROPERTIES.getProperty("wx.person.get");
	/***** 创建人员接口 *****/
	public static final String QYWX_URL_PERSON_CREATE = URL_PROPERTIES.getProperty("wx.person.create");
	/***** 根据code获取userid接口 *****/
	public static final String QYWX_URL_PERSON_GET_USERID = URL_PROPERTIES.getProperty("wx.person.getuserid");
	/***** 二次验证关注企业号接口 *****/
	public static final String QYWX_URL_PERSON_FOLLOW = URL_PROPERTIES.getProperty("wx.person.follow");
	/***** 获取JSAPITICKE接口 *****/
	public static final String QYWX_URL_GET_JSAPITICKET = URL_PROPERTIES.getProperty("wx.get.jsapiticket");
	/***** 获取ACCESS_TOKEN接口地址 *****/
	public static final String QYWX_URL_GET_ACCESS_TOKEN = URL_PROPERTIES.getProperty("wx.get.accesstoken");
	/***** 请求腾讯获取code接口 *****/
	public static final String QYWX_URL_PERSON_GET_CODE = URL_PROPERTIES.getProperty("wx.person.getcode");
	/***** 请求腾讯获取code接口需要让腾讯回调的接口地址 *****/
	public static final String QYWX_URL_WX_REDIRECT_URL = URL_PROPERTIES.getProperty("wx.rediect.url");
	/***** 请求腾讯获取code接口需要让腾讯回调的接口地址(内部) *****/
	public static final String QYWX_URL_WX_REDIRECT_URL_OFFICE = URL_PROPERTIES.getProperty("wx.rediect.officeurl");
	/***** 官微回调的企业号地址 *****/
	public static final String GW_REDIRECT_URL = URL_PROPERTIES.getProperty("gw.redirecturl");
	/***** 官微交易来源 *****/
	public static final String GW_TRADESOURCE = URL_PROPERTIES.getProperty("gw.tradeSource");
	/***** 官微秘钥 *****/
	public static final String GW_KEY = URL_PROPERTIES.getProperty("gw.key");
	/***** 官微获取openid接口 *****/
	public static final String GW_QUERY_OPENID = URL_PROPERTIES.getProperty("gw.queryopenid");
	/***** 官微注册接口 *****/
	public static final String GW_REGISTER_URL = URL_PROPERTIES.getProperty("gw.registerurl");
	/***** 天池交易来源 *****/
	public static final String TC_SYSTEM_SOURCE = URL_PROPERTIES.getProperty("tc.sourcesystem");
	/***** 天池秘钥 *****/
	public static final String TC_KEY = URL_PROPERTIES.getProperty("tc.key");
	/***** 天池获取用户信息接口 *****/
	public static final String TC_QUERY_USER = URL_PROPERTIES.getProperty("tc.queryuser");
	/***** 企业号二次验证需要让腾讯回调的地址 *****/
	public static final String VERIFY_LOGIN_REDIRECT_URL = URL_PROPERTIES.getProperty("verify.loginredirect");
	/***** userid转openid接口 *****/
	public static final String QYWX_URL_CONVERT_OPENID = URL_PROPERTIES.getProperty("wx.convert.openid");
	/***** 腾讯回调接口 *****/
	public static final String QYWX_URL_PAY_REDIRECTURL = URL_PROPERTIES.getProperty("wx.pay.redirecturl");
	/***** 企业号回调支付平台接口地址 *****/
	public static final String QYH_REDIRECTURL = URL_PROPERTIES.getProperty("qyh.redirecturl");
	/***** 微课堂部门id *****/
	public static final String WKT_DEPARTMENT_ID = URL_PROPERTIES.getProperty("wkt.departmentid");
	/***** 微课堂通知地址 *****/
	public static final String WKT_NOTICE_URL = URL_PROPERTIES.getProperty("wkt.noticeurl");
	/***** 微课堂key *****/
	public static final String WKT_KEY = URL_PROPERTIES.getProperty("wkt.key");
	/***** 微课堂交易来源 *****/
	public static final String WKT_TRADESOURCE = URL_PROPERTIES.getProperty("wkt.tradesourc");

	/******************************************* URL_PROPERTIES *********************************************/

}
