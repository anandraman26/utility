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
	private static long lastTime;

	public static void refresh() {
		try {
			String fileName = "C:/NovelVox/nad-server/webapps/ROOT/WEB-INF/conf" + File.separator + PROPERTY_FILE_NAME;

			if (null == fileName || fileName.isEmpty()) {
				fileName = ISuiteContextListener.getConfFolderPath() + File.separator + PROPERTY_FILE_NAME;
			}
			File f = new File(fileName);
			if (f.exists()) {
				FileInputStream in = new FileInputStream(f);
				propCommonUtility = new Properties();
				propCommonUtility.load(in);
				lastTime = f.lastModified();
				File tmpFile = new File(fileName);
				if (tmpFile.lastModified() > lastTime && lastTime > 0) {
					FileInputStream inStream2 = null;
					try {
						inStream2 = new FileInputStream(tmpFile);
						propCommonUtility.load(inStream2);
						log.info("Propeties file loaded successfully");
					} catch (Exception e) {
						System.err.println("Unable to load properties file during change (" + tmpFile + ") : " + e);
						log.error("Error loading properties file: " + e);
					}
					lastTime = tmpFile.lastModified();
				}
			} else {
				log.error("Not able  to load Properties file : " + propCommonUtility);
				log.error("property file not found");
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
		refresh();
		String p = propCommonUtility.getProperty(key);
		return p;
	}

}
