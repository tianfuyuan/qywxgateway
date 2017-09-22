package com.ihxlife.qyhgateway.support.components;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ihxlife.qyhgateway.service.DataService;
import com.ihxlife.qyhgateway.service.impl.DataService4PropertiesImpl;
import com.ihxlife.qyhgateway.support.spring.SpringBeanRegisterUtil;

/**
 * 数据获取方式核心控制类
 * 
 * @author Administrator
 *
 */
public class DataGetWayComponent {
	private static final Logger logger = LoggerFactory.getLogger(DataGetWayComponent.class);
	
	private static DataService dataService ;


	public DataService getDataService() {
		return DataGetWayComponent.dataService;
	}


	public void setDataService(DataService dataService) {
		DataGetWayComponent.dataService = dataService;
	}
	
	
	public void initDataService(){
		logger.info("初始化dataService");
		SpringBeanRegisterUtil.registerBean("dataService4PropertiesImpl", DataService4PropertiesImpl.class.getName());
		DataGetWayComponent.dataService = SpringBeanRegisterUtil.getBean(DataService.class);
	}
	
	public static void updateDataService(DataService service){
		DataGetWayComponent.dataService = service;
	}

}
