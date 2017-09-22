package com.ihxlife.qyhgateway.support.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 请求参数校验注解
 * @author Administrator
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckRequestParam {
	/**
	 * 请求参数校验类型、默认校验请求参数包含数据包
	 * @return
	 */
	public CheckParamType checkRequestType() default CheckParamType.CHECK_DATA;
}
