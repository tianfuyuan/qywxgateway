package com.ihxlife.qyhgateway.support.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发送短信工具类
 * @author Administrator
 */
public class SMSUtil {

	private static final String SIGN = "signature";

	/**
	 * 访问通知服务平台参数组装
	 * @param paraMap 接口请求数据
	 * @param uid 客户端id(通知服务平台分配给客户端的)
	 * @param key 客户端key(通知服务平台分配给客户端的)
	 * @return
	 */
	public static Map<String, String> sign(Map<String, String> paraMap, String uid, String key) {
		// 1、时间戳
		String timestamp = String.valueOf(System.currentTimeMillis());
		// 2、 20位随机字符串n
		String nonce = GenerateRandomUtils.generateRandomCharAndNumber(20);
		paraMap.put("uid", uid);
		paraMap.put("timestamp", timestamp);
		paraMap.put("nonce", nonce);

		// 3、生成待签名字符串（除signature外的公共参数和data进行去空值，然后排序，并以&字符串连接）
		String params = getLinkString(paraMap);
		System.out.println("访问通知平台生成签名参数为【{" + params + "}】");

		// 4、生成签名串（待签名字符串+密钥，然后md5)
		String signature = EncoderHandler.sign(params, key, "UTF-8");
		System.out.println("访问通知平台的签名为【{" + signature + "}】");

		// 5、将签名串公共请求参数放入map
		paraMap.put("signature", signature);

		return paraMap;
	}

	public static String getLinkString(Map<String, String> sArray) {
		// 除去数组中的空值和签名参数
		Map<String, String> sPara = paraFilter(sArray);
		// 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
		String prestr = createLinkString(sPara);
		return prestr;
	}

	/**
	 * 除去数组中的空值和签名参数
	 * @param sArray 签名参数组
	 * @return 去掉空值与签名参数后的新签名参数组
	 */
	public static Map<String, String> paraFilter(Map<String, String> sArray) {

		Map<String, String> result = new HashMap<String, String>();

		if (sArray == null || sArray.size() <= 0) {
			return result;
		}
		for (String key : sArray.keySet()) {
			String value = sArray.get(key);
			if (value == null || value.equals("") || SIGN.equalsIgnoreCase(key)) {
				continue;
			}
			result.put(key, value);
		}

		return result;
	}

	/**
	 * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
	 * @param params 需要排序并参与字符拼接的参数组
	 * @return 拼接后字符串
	 */
	public static String createLinkString(Map<String, String> params) {

		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);

		String prestr = "";

		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);

			if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
				prestr = prestr + key + "=" + value;
			} else {
				prestr = prestr + key + "=" + value + "&";
			}
		}

		return prestr;
	}

}
