package com.ihxlife.qyhgateway.support.util;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ip地址输出
 * @author Administrator
 *
 */
public class IpCommon {
	
	private static final Logger logger = LoggerFactory.getLogger(IpCommon.class);

	/**
	 * 获取IP地址
	 * @param request
	 * @return
	 */
	public static void outPutRemortIP(HttpServletRequest request) {
		if (request.getHeader("x-forwarded-for") == null) {
			logger.info("调用方IP为【{}】", request.getRemoteAddr());
			return;
		}
		logger.info("调用方IP为【{}】", request.getHeader("x-forwarded-for"));
	}
}
