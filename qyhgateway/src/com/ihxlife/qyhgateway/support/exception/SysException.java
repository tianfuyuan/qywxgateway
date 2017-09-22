package com.ihxlife.qyhgateway.support.exception;

public class SysException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SysException(String message){
		super(message);
	}
	
	public SysException(String message , Throwable cause){
		super(message , cause);
	}

}
