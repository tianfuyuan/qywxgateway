package com.ihxlife.qyhgateway.support.interceptor;

import java.io.PrintWriter;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.ihxlife.qyhgateway.dto.ReturnMessage;
import com.ihxlife.qyhgateway.support.annotation.CheckParamType;
import com.ihxlife.qyhgateway.support.annotation.CheckRequestParam;
import com.ihxlife.qyhgateway.support.annotation.RequireMessage;
import com.ihxlife.qyhgateway.support.common.CheckData;
import com.ihxlife.qyhgateway.support.common.LoggerStartAndEnd;
import com.ihxlife.qyhgateway.support.components.DataGetWayComponent;
import com.ihxlife.qyhgateway.support.util.GenerateRandomUtils;
import com.ihxlife.qyhgateway.support.util.IpCommon;
import com.ihxlife.qyhgateway.support.util.JsonUtil;

/**
 * 企业号网关拦截器
 * @author Administrator
 *
 */
public class QyhGatewayInterceptor implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(QyhGatewayInterceptor.class);
		
	@Autowired
	private DataGetWayComponent dataGetWayComponent;
	
	/**
	 * 接口执行完成后调用（前提是preHandle（）方法返回值为true）
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		
		//获取方法
		Method method = handlerMethod.getMethod();
		
		//获取当前接口的RequireMessage注解（所有接口必须添加此注解，必须添加msg属性、和clazzName属性）
		RequireMessage msgAnnotation = method.getAnnotation(RequireMessage.class);
		//类名
		String className = "";
		//方法名
		String methodName = "";
		//获取类名
		className = msgAnnotation.className();
		//获取方法名
		methodName = method.getName();
		Long startTime = (Long) request.getAttribute("startTime");
		Long endTime = System.currentTimeMillis();
		//接口总耗时
		Long useTime = endTime-startTime;
		logger.info("接口执行总耗时【{}】",useTime);
		LoggerStartAndEnd.loggerStartOrFinish(logger, className, methodName, "结束");
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
			throws Exception {
		
	}

	/**
	 * 进入接口前进行拦截
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		// 生成随机数
		String randomNum = GenerateRandomUtils.getCharAndNumr(20);
		Thread.currentThread().setName(randomNum);
		//获取系统当前时间
		long startTime = System.currentTimeMillis();
		request.setAttribute("startTime", startTime);
		//需要返回的json数据
		ReturnMessage returnMessage = new ReturnMessage();
		// 实现PrintWriter
		PrintWriter out = null;
		//类名
		String className = "";
		//方法名
		String methodName = "";
		
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		
		//获取方法
		Method method = handlerMethod.getMethod();
		
		//获取当前接口的RequireMessage注解（所有接口必须添加此注解，必须添加msg属性、和clazzName属性）
		RequireMessage msgAnnotation = method.getAnnotation(RequireMessage.class);
		//获取当前接口的ResponseBody注解
		ResponseBody responseAnno = method.getAnnotation(ResponseBody.class);
		//获取类名
		className = msgAnnotation.className();
		//获取方法名
		methodName = method.getName();

		LoggerStartAndEnd.loggerStartOrFinish(logger, className, methodName, "开始");
		String url = request.getRequestURI();
		
		logger.info("请求路径为【{}】",url);
		// 调用ip地址输出信息方法
		IpCommon.outPutRemortIP(request);
		response.setContentType("application/json;charset=UTF-8");
		
		//获取当前接口的校验请求参数注解
		CheckRequestParam checkAnnotation = method.getAnnotation(CheckRequestParam.class);
		
		//判断接口是否使用了CheckRequestParam注解
		//对于未使用CheckRequestParam注解的接口，则代表不需要进行数据校验
		if(checkAnnotation == null){
			return true;
		}
		//从注解中获取参数校验类型
		CheckParamType checkRequestType = checkAnnotation.checkRequestType();
		//获取注解中属性值
		String msg = msgAnnotation.msg();
		//参数校验类型为验证包含data数据包
		if(CheckParamType.CHECK_DATA.equals(checkRequestType)){
			//调用方法验证请求所携带参数和签名校验(携带data数据包)
			returnMessage = CheckData.checkUrlParamSignatureData(dataGetWayComponent, request, msg);
		}
		//参数校验类型为验证不包含data数据包
		if(CheckParamType.CHECK_NO_DATA.equals(checkRequestType)){
			//调用方法验证请求所携带参数和签名校验(不带data数据包)
			returnMessage = CheckData.checkUrlParamSignature(dataGetWayComponent, request, msg);
		}
		
		//参数校验通过result_code返回值为suc
		if(!"suc".equals(returnMessage.getResult_code())){
			//如果没有使用ResponseBody注解
			if(responseAnno == null){
				String message = returnMessage.getResult_msg();
				request.setAttribute("message", message);
				//跳转错误页面
				request.getRequestDispatcher("/WEB-INF/views/common/weChatMessage.jsp").forward(request, response);
				Long endTime = System.currentTimeMillis();
				Long useTime = endTime-startTime;
				logger.info("接口执行总耗时【{}】",useTime);
				LoggerStartAndEnd.loggerStartOrFinish(logger, className, methodName, "结束");
				return false;
			}
			out = response.getWriter();
			// 输出PringtWriter
			out.print(JsonUtil.object2Json(returnMessage)); 
			out.flush();
			out.close();
			Long endTime = System.currentTimeMillis();
			Long useTime = endTime-startTime;
			logger.info("接口执行总耗时【{}】",useTime);
			LoggerStartAndEnd.loggerStartOrFinish(logger, className, methodName, "结束");
			return false;
		}
		
		return true;
	}
}
