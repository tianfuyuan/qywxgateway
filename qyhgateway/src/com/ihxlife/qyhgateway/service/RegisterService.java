package com.ihxlife.qyhgateway.service;

import com.ihxlife.qyhgateway.dto.ReturnMessage;

public interface RegisterService {
	public ReturnMessage registerUser(String data) throws Exception;
}
