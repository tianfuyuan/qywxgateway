package com.ihxlife.qyhgateway.web;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ihxlife.qyhgateway.dto.ReturnMessage;
import com.ihxlife.qyhgateway.service.RegisterService;
import com.ihxlife.qyhgateway.support.annotation.CheckParamType;
import com.ihxlife.qyhgateway.support.annotation.CheckRequestParam;
import com.ihxlife.qyhgateway.support.annotation.RequireMessage;


@Controller
@RequestMapping("/api/v1")
public class RegisterController {
	private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);

	@Autowired
	private RegisterService registerService;
	
	@RequestMapping("/micrclass/register")
	@ResponseBody
	@CheckRequestParam(checkRequestType=CheckParamType.CHECK_DATA)
	@RequireMessage(className="RegisterController",msg="微课堂注册",callerTypeIsCustom=false)
	public ReturnMessage registerQYH(HttpServletRequest request) throws Exception {
		// 参数
		String data = request.getParameter("data");
		logger.info("获取的参数 data【{}】", data);
		// 企业号新增人员
		ReturnMessage returnMessage = registerService.registerUser(data);
		return returnMessage;
	}
}
