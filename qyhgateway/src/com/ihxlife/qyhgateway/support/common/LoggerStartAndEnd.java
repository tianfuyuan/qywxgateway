package com.ihxlife.qyhgateway.support.common;

import org.slf4j.Logger;

/**
 * 日志打印（方法的开始或者结束工具类）
 * 
 * @author Administrator
 *
 */
public class LoggerStartAndEnd {
	
	/**
	 * 接口开始与结束日志输出方法
	 * @param methodName 方法名
	 * @param startOrFinish方法是开始还是结束
	 */
	public static void loggerStartOrFinish(Logger logger, String clazz, String methodName, String startOrFinish) {
		logger.info("----------------{}------{}---------{}-------", clazz, methodName, startOrFinish);
	}
	
}
