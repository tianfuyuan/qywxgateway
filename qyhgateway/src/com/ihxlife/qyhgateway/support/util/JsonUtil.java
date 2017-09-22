package com.ihxlife.qyhgateway.support.util;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

/**
 * json 简单操作的工具类
 * @author Administrator
 */
public class JsonUtil {

	/**
	 * 把JSON文本parse为JSONObject或者JSONArray
	 * @param text
	 * @return
	 */
	public static final Object parse(String text) {
		return JSON.parse(text);
	}

	/**
	 * 把JSON文本parse成JSONObject
	 * @param text
	 * @return
	 */
	public static final JSONObject parseObject(String text) {
		return JSON.parseObject(text);
	}

	/**
	 * 把JSON文本parse为JavaBean
	 * @param text
	 * @param clazz
	 * @return
	 */
	public static final <T> T json2Object(String text, Class<T> clazz) {
		return JSON.parseObject(text, clazz);
	}

	/**
	 * 把JSON文本parse为map<String,String>
	 * @param text
	 * @param clazz
	 * @return
	 */
	public static final Map<String, String> json2Map(String jsonStr) {
		Map<String, String> map = JSON.parseObject(jsonStr, new TypeReference<Map<String, String>>() {
		});
		return map;
	}

	/**
	 * 把JSON文本parse为map<String,Object>
	 * @param text
	 * @param clazz
	 * @return
	 */
	public static final Map<String, Object> json2MapObj(String jsonStr) {
		Map<String, Object> map = JSON.parseObject(jsonStr, new TypeReference<Map<String, Object>>() {
		});
		return map;
	}

	/**
	 * 把JSON文本parse成JSONArray
	 * @param text
	 * @return
	 */
	public static final JSONArray json2Array(String text) {
		return JSON.parseArray(text);
	}

	/**
	 * 把JSON文本parse成JavaBean集合
	 * @param text
	 * @param clazz
	 * @return
	 */
	public static final <T> List<T> json2List(String text, Class<T> clazz) {
		return JSON.parseArray(text, clazz);
	}

	/**
	 * 将JavaBean序列化为JSON文本
	 * @param object
	 * @return
	 */
	public static final String object2Json(Object object) {
		return JSON.toJSONString(object);
	}

	/**
	 * 将JavaBean序列化为带格式的JSON文本
	 * @param object
	 * 
	 * @param prettyFormat true为带格式的文本
	 *            
	 * @return
	 */
	public static final String toJSONString(Object object, boolean prettyFormat) {
		return JSON.toJSONString(object, prettyFormat);
	}

	/**
	 * 将JavaBean转换为JSONObject或者JSONArray
	 * @param javaObject
	 * @return
	 */
	public static final Object object2JSONBean(Object javaObject) {
		return JSON.toJSON(javaObject);
	}

}