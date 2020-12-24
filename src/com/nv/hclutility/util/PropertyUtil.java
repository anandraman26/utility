package com.nv.hclutility.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.isuite.iagent.commons.servelet.ISuiteContextListener;

public class PropertyUtil {

	private static Logger log = Logger.getLogger(PropertyUtil.class);
	private static final String PROPERTY_FILE_NAME = "hclutility.properties";
	private static Properties propCommonUtility;
	private static PropertyUtil thisInstance;
	public static final int defaultValue = 5;
	public static final int defaultMaxResultInDBValue = 50;
	private static boolean isLoaded = false;

	public static void refresh() {
		try {
			//String fileName = "D:\\Anand Raman\\Novelvox WorkSpace\\HCL\\HCLUtility\\conf\\" + File.separator + PROPERTY_FILE_NAME;
			String fileName = "C:/NovelVox/nad-server/webapps/ROOT/WEB-INF/conf" + File.separator + PROPERTY_FILE_NAME;
			
			if (null == fileName || fileName.isEmpty()) {
				fileName = ISuiteContextListener.getConfFolderPath() + File.separator + PROPERTY_FILE_NAME;
			}
			File f = new File(fileName);
			if (!isLoaded) {
				if (f.exists()) {
					FileInputStream in = new FileInputStream(f);
					propCommonUtility = new Properties();
					propCommonUtility.load(in);
					isLoaded = true;
				} else {
					log.error("Not able  to load Properties file : " + propCommonUtility);
					log.error("property file not found");
				}
			}
		} catch (FileNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	public static PropertyUtil getInstance() {
		if (thisInstance == null) {
			refresh();
			thisInstance = new PropertyUtil();
		}
		return thisInstance;
	}

	private PropertyUtil() {

	}

	public String getValueForKey(String key) {
		String p = propCommonUtility.getProperty(key);
		return p;
	}

}
