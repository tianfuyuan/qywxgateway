package com.ihxlife.qyhgateway.dto;

import java.lang.reflect.Field;

/**
 * javaBean基类
 * @author Administrator
 *
 */
public class BaseClass {
	
	

	@Override
	@SuppressWarnings("rawtypes")
	public String toString() {
		Class clazz =this.getClass();
		StringBuffer buffer = new StringBuffer();
		buffer.append("{");
		do {
			Field[] fields = clazz.getDeclaredFields();
			for (Field f : fields) {
				f.setAccessible(true);
				String variableName = f.getName();
				try {
					Object value = f.get(this);
					buffer.append("【"+variableName+"="+value+"】");
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			clazz = clazz.getSuperclass();
		} while (clazz != Object.class);
		buffer.append("}");
		return buffer.toString();
	}
	
}
