package com.ihxlife.qyhgateway.dto;

/**
 * 返回用户信息对象
 * @author Administrator
 *
 */
public class ReturnUserMessage extends ReturnMessage {
	
	/**
	 * data数据包
	 */
	private String data;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	
}
