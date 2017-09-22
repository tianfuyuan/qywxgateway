package com.ihxlife.qyhgateway.support.util;

/**
 * 转换工具类
 * @author Administrator
 *
 */
public class ConvertUtil {
	/**
	 * 性别转换
	 * @param sex 性别
	 * @return
	 */
	public static String sexConvert(String sex) {
		if (sex == null) {
			sex = "UN";
		} else if ("1".equals(sex)) {
			sex = "F";
		} else if ("0".equals(sex)) {
			sex = "M";
		} else {
			sex = "UN";
		}
		return sex;
	}

	/**
	 * 证件类型转换
	 * @param idType 证件类型
	 * @return
	 */
	public static String idTypeConvert(String idType) {
		if ("0".equals(idType)) {
			idType = "01";
		} else if ("1".equals(idType)) {
			idType = "02";
		} else if ("2".equals(idType)) {
			idType = "03";
		} else if ("3".equals(idType)) {
			idType = "04";
		} else if ("4".equals(idType)) {
			idType = "05";
		} else if ("6".equals(idType)) {
			idType = "06";
		} else if ("7".equals(idType)) {
			idType = "07";
		} else if ("A".equals(idType)) {
			idType = "08";
		}
		return idType;
	}
}
