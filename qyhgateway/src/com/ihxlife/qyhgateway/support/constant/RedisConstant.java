package com.ihxlife.qyhgateway.support.constant;

/**
 * redis常量类
 * @author Administrator
 *
 */
public class RedisConstant {
	
	/**
	 * redis中获取交易信息来源的key
	 */
	public static final String TRADE_SOURCE_KEY = "trade_source_key";

	/**
	 * redis中获取接口配置信息的key
	 */
	public static final String INTERFACE_CONF_KEY = "interface_conf_key";

	/**
	 * redis中获取中介配置信息的key
	 */
	public static final String MEDIRY_CONFIG_KEY = "mediry_config_key";

	/**
	 * redis中获取access_token的key
	 */
	public static final String ACCESS_TOKEN_KEY = "qyh_access_token_key";

	/**
	 * redis中获取JSAPITICKET的key
	 */
	public static final String JSAPI_TICKET_KEY = "jsapi_ticket_key";

	/**
	 * redis中获取access_token更新时间的Key
	 */
	public static final String ACCESS_TOKEN_UPDATE_TIME_KEY = "access_token_update_time_key";

	/**
	 * redis中获取ticket更新时间的key
	 */
	public static final String JSAPITICKET_UPDATE_TIME_KEY = "jsapiticket_update_time_key";

}
