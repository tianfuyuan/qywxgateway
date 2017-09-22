package com.ihxlife.qyhgateway.support.exception;

/**
 * 自定义业务异常类
 * @author Administrator
 *
 */
public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = 3497148482792520097L;

	public BusinessException(String message){
		super(message);
	}
	
	public BusinessException(String message , Throwable cause){
		super(message , cause);
	}
}
