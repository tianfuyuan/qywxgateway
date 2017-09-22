package com.ihxlife.qyhgateway.support.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName: PropertiesUtil
 * @Description: 配置文件类
 * @author chang
 */
public class PropertiesUtil {

	private static final Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

	public PropertiesUtil() {

	}
	
	/**
	 * 获取配置文件
	 * @param fileName
	 * @return
	 */
	public Properties getProperties(String fileName) {
		Properties prop = new Properties();
		FileInputStream fileInputStream = null;
		try {
			prop.clear();
			String path = this.getClass().getResource("").getPath().replace("classes/com/ihxlife/qyhgateway/support/util/",
					"config/") + fileName;
			path = path.replace("%20", " ");
			logger.info("加载对应的properties路径path【{}】", path);
			File tmp = new File(path);
			if (!tmp.exists() || !tmp.isFile())
				return null;
			fileInputStream = new FileInputStream(path);
			prop.load(fileInputStream);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return prop;
	}

}
