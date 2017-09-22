package com.ihxlife.qyhgateway.dto;

/**
 * 返回信息对象
 * @author Administrator
 *
 */
public class ReturnMessage extends BaseClass{
	
	/**
	 * 返回结果编码
	 */
	private String result_code;
	
	/**
	 * 返回结果信息
	 */
	private String result_msg;
	public String getResult_code() {
		return result_code;
	}
	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}
	public String getResult_msg() {
		return result_msg;
	}
	public void setResult_msg(String result_msg) {
		this.result_msg = result_msg;
	}
}
