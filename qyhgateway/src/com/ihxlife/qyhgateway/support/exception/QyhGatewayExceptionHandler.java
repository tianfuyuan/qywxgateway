package com.ihxlife.qyhgateway.support.exception;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import com.ihxlife.qyhgateway.dto.ReturnMessage;
import com.ihxlife.qyhgateway.support.annotation.RequireMessage;
import com.ihxlife.qyhgateway.support.util.JsonUtil;


/**
 * 异常处理
 * @author Administrator
 *
 */
public class QyhGatewayExceptionHandler extends ExceptionHandlerExceptionResolver {

	private static final Logger logger = LoggerFactory.getLogger(QyhGatewayExceptionHandler.class);
	
	public String defaultErrorView;
	
	
	public String getDefaultErrorView() {
		return defaultErrorView;
	}


	public void setDefaultErrorView(String defaultErrorView) {
		this.defaultErrorView = defaultErrorView;
	}


	/**
	 * 异常处理方法
	 */
	protected ModelAndView doResolveHandlerMethodException(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod, Exception exception) {
		
		logger.info("*****************进入统一异常处理*****************");
		
		ModelAndView mv = null;
		
		if(handlerMethod == null || handlerMethod.getMethod() == null){
			return mv;
		}
		//获取当前抛出异常的方法
		Method method = handlerMethod.getMethod();
		//声明返回值
		ReturnMessage returnMessage = null;
		//声明PrintWriter
		PrintWriter out = null;
		//声明异常信息
		String message = "";
		//设置响应编码格式
		response.setContentType("application/json;charset=UTF-8");
		
		//从抛出异常方法中获取ResponseBody注解
		ResponseBody responseAnno = method.getAnnotation(ResponseBody.class);
		//从抛出异常方法中获取RequireMessage注解
		RequireMessage requireMsgAnno = method.getAnnotation(RequireMessage.class);
		logger.info("发生异常的方法为【{}】",method.getName());
		
		if(exception == null){
			return mv;
		}
		
		//如果抛出异常方法没有使用ResponseBody注解，则代表不需要返回json
		if(responseAnno==null){
			//携带异常信息返回页面
			mv = new ModelAndView();
			message = getErrorMessage(exception, requireMsgAnno);
			mv.setViewName(defaultErrorView);
			mv.addObject("message", message);
			logger.info("返回的message信息为【{}】",message);
			return mv;
		}
		try {
			//获取Writer对象
			out = response.getWriter();
			returnMessage = new ReturnMessage();
			returnMessage.setResult_code("fail");
			//获取异常信息
			message = getErrorMessage(exception, requireMsgAnno);
			returnMessage.setResult_msg(message);
		} catch (IOException e) {
			logger.info("response获取Writer对象异常【{}】",e);
			return mv;
		} finally {
			logger.info("返回的结果为：【{}】",JsonUtil.object2Json(returnMessage));
			out.print(JsonUtil.object2Json(returnMessage));
			if (out != null) {
				out.flush();
				out.close();
			}
			logger.info("*****************统一异常处理结束*****************");
		}
		return mv;
	}
	
	/**
	 * 获取异常信息
	 * @param exception 异常对象
	 * @param requireMsgAnno 参数信息注解
	 * @return
	 */
	private String getErrorMessage(Exception exception,RequireMessage requireMsgAnno){
		String message = "";
		if(exception instanceof BusinessException){
			logger.info("业务异常信息为：【{"+exception+"}】");
			message = exception.getMessage();
			return message;
		}else if(exception instanceof SysException){
			logger.info("系统异常信息为：【{}】",exception);
		}else{
			logger.info("系统异常信息为：【{}】",exception);
			return "系统异常,请联系保险公司！";
		}
		//如果调用方不是客户,返回错误信息
		if(!requireMsgAnno.callerTypeIsCustom()&&!requireMsgAnno.interfaceIsVerify()){
			message = exception.getMessage();
			return message;
		}
		message = "系统异常,请联系保险公司！";
		return message;
	}
}
