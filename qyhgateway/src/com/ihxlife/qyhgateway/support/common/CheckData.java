package com.ihxlife.qyhgateway.support.common;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ihxlife.qyhgateway.dto.ReturnMessage;
import com.ihxlife.qyhgateway.support.components.DataGetWayComponent;
import com.ihxlife.qyhgateway.support.util.EncoderHandler;


/**
 * 数据校验类
 * 
 * @author Administrator
 *
 */
public class CheckData {
	
	private static final Logger logger = LoggerFactory.getLogger(CheckData.class);


	/**
	 * 验证请求所携带参数和签名校验(不带data数据包)
	 * @param request 请求的request
	 * @param dataJson 需要返回的json数据
	 * @param msg 打印日志和返回的提示信息关键词
	 * @return 需要返回的json数据（如果校验通过，ReturnMessage对象所携带的result_code的值为“suc”）
	 * @throws Exception
	 */
	public static ReturnMessage checkUrlParamSignature(DataGetWayComponent dataGetWayComponent,HttpServletRequest request, String msg) {
		ReturnMessage returnMessage = new ReturnMessage();
		returnMessage.setResult_code("fail");
		// 时间戳
		String timestamp = request.getParameter("timestamp");
		// 随机数
		String nonce = request.getParameter("nonce");
		// 交易来源
		String trade_source = request.getParameter("trade_source");
		// 签名
		String signature = request.getParameter("signature");
		logger.info("获取的参数timestamp【{}】，nonce【{}】, trade_source【{}】,signature【{}】", timestamp, nonce, trade_source,
				signature);
		
		// 所需参数为空标识
		if (StringUtils.isBlank(trade_source) || StringUtils.isBlank(timestamp) || StringUtils.isBlank(nonce)
				|| StringUtils.isBlank(signature)) {
			logger.info("获取{}信息失败:缺少参数", msg);
			returnMessage.setResult_msg("获取" + msg + "信息失败:缺少参数");
			return returnMessage;
		}
		
		// 从交易来源配置文件中获取key
		String checkKey = dataGetWayComponent.getDataService().getTradeSource(trade_source);
		if (checkKey == null || "".equals(checkKey)) {
			logger.info("获取{}信息失败:该来源为非法来源", msg);
			returnMessage.setResult_msg("获取" + msg + "信息失败:该来源为非法来源");
			return returnMessage;
		}
		
		// 签名校验
		String sign = EncoderHandler.Md5_32(checkKey + timestamp + nonce + trade_source).toUpperCase();
		logger.info("生成的sign==【{}】", sign);
		
		if (!sign.equals(signature)) {
			logger.info("获取{}信息失败:签名校验失败！", msg);
			returnMessage.setResult_msg("获取" + msg + "信息失败:签名校验失败！");
			return returnMessage;
		}
		
		returnMessage.setResult_code("suc");
		return returnMessage;
	}

	/**
	 * 验证请求所携带参数和签名校验(携带data数据包)
	 * @param request 请求的request
	 * @param dataJson 需要返回的json数据
	 * @param msg 打印日志和返回的提示信息关键词
	 * @param logger 打印日志对象
	 * @return 需要返回的json数据（如果校验通过，ReturnMessage对象所携带的result_code的值为“suc”,
	 *         result_msg的值为请求传递过来的data数据包）
	 */
	public static ReturnMessage checkUrlParamSignatureData(DataGetWayComponent dataGetWayComponent,HttpServletRequest request, String msg) {
		ReturnMessage returnMessage = new ReturnMessage();
		returnMessage.setResult_code("fail");
		// 时间戳
		String timestamp = request.getParameter("timestamp");
		// 随机数
		String nonce = request.getParameter("nonce");
		// 交易来源
		String trade_source = request.getParameter("trade_source");
		// 签名
		String signature = request.getParameter("signature");
		// 数据包
		String data = request.getParameter("data");
		logger.info("获取的参数timestamp【{}】，nonce【{}】, trade_source【{}】,signature【{}】,data【{}】", timestamp, nonce,
				trade_source, signature, data);
		
		// 所需参数为空标识
		if (StringUtils.isBlank(trade_source) || StringUtils.isBlank(timestamp) || StringUtils.isBlank(nonce)
				|| StringUtils.isBlank(signature) || StringUtils.isBlank(data)) {
			logger.info("{}失败:缺少参数", msg);
			returnMessage.setResult_msg(msg + "信息失败:缺少参数");
			return returnMessage;
		}
		
		// 从交易来源配置文件中获取key
		String checkKey = dataGetWayComponent.getDataService().getTradeSource(trade_source);
		if (checkKey == null || "".equals(checkKey)) {
			logger.info("{}失败:该来源为非法来源", msg);
			returnMessage.setResult_msg(msg + "失败:该来源为非法来源");
			return returnMessage;
		}
		
		logger.info("checkKey为:【{}】，timestamp【{}】，nonce【{}】, trade_source【{}】，data【{}】", checkKey, timestamp, nonce,
				trade_source, data);
		// 签名校验
		String sign = EncoderHandler.Md5_32(checkKey + timestamp + nonce + trade_source + data).toUpperCase();
		logger.info("生成的sign==【{}】", sign);
		
		if (!sign.equals(signature)) {
			logger.info("{}失败:签名校验失败！", msg);
			returnMessage.setResult_msg(msg + "失败:签名校验失败！");
			return returnMessage;
		}
		
		returnMessage.setResult_code("suc");
		returnMessage.setResult_msg(data);
		return returnMessage;
	}

}
