package com.ihxlife.qyhgateway.support.spring;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringBeanRegisterUtil {

	private static ApplicationContext context = new ClassPathXmlApplicationContext("classpath:/applicationContext-*.xml");
	private static ConfigurableApplicationContext configurableContext = (ConfigurableApplicationContext) context;
	private static BeanDefinitionRegistry beanDefinitionRegistry = (DefaultListableBeanFactory) configurableContext
			.getBeanFactory();

	/**
	 * 注册bean
	 * @param beanId beanid
	 * @param className 类名
	 */
	public static void registerBean(String beanId, String className) {
		// get the BeanDefinitionBuilder
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(className);
		// get the BeanDefinition
		BeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
		// register the bean
		beanDefinitionRegistry.registerBeanDefinition(beanId, beanDefinition);
	}
	
	/**
	 * 移除bean
	 * @param beanName beanid
	 */
	public static void unregisterBean(String beanName) {
		beanDefinitionRegistry.removeBeanDefinition(beanName);
	}
	
	/**
	 * 根据beanid检查是否已注册bean
	 * @param beanName beanid
	 * @return
	 */
	public static boolean isBeanNameUse(String beanName){
		return beanDefinitionRegistry.isBeanNameInUse(beanName);
	}
	
	/**
	 * 获取bean
	 * @param clazz 类
	 * @return
	 */
	public static <T> T getBean(Class<T> clazz) {
        return (T)context.getBean(clazz);
    }
}
