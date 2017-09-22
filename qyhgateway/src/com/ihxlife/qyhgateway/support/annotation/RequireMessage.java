package com.ihxlife.qyhgateway.support.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 需要的参数信息
 * @author Administrator
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireMessage {
	
	/**
	 * 给调用方返回提示信息关键词
	 * @return 关键词
	 */
	public String msg();
	
	/**
	 * 当前类名（日志打印使用）
	 * @return 当前类名
	 */
	public String className();
	
	/**
	 * 调用方是否为客户
	 * @return ture 为客户   false 不为客户
	 */
	public boolean callerTypeIsCustom();
	
	/**
	 * 接口是否为二次验证
	 * @return true为是 false为否  默认否false
	 */
	public boolean interfaceIsVerify() default false;
}
