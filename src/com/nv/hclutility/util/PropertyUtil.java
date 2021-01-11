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
	private static long lastModifiedTime;
	public static String fileName = "D:\\Anand Raman\\Novelvox WorkSpace\\HCL\\HCLUtility\\conf" + File.separator
			+ PROPERTY_FILE_NAME;

	public static void refresh() {
		try {
			if (null == fileName || fileName.isEmpty()) {
				fileName = ISuiteContextListener.getConfFolderPath() + File.separator + PROPERTY_FILE_NAME;
			}
			File f = new File(fileName);
			if (f.exists()) {
				FileInputStream in = new FileInputStream(f);
				propCommonUtility = new Properties();
				propCommonUtility.load(in);
				lastModifiedTime = f.lastModified();
				loadUpdatedFile();
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

	private static boolean loadUpdatedFile() {
		boolean fileUpdate = false;
		File tmpFile = new File(fileName);
		if (tmpFile.lastModified() > lastModifiedTime && lastModifiedTime > 0) {
			FileInputStream inStream2 = null;
			try {
				inStream2 = new FileInputStream(tmpFile);
				propCommonUtility.load(inStream2);
				log.info("Propeties file loaded successfully");
				fileUpdate = true;
			} catch (Exception e) {
				fileUpdate = false;
				log.error("Error loading properties file: " + e);
			}
			lastModifiedTime = tmpFile.lastModified();
			log.info("Updated Properties file key:value is " + propCommonUtility.toString());
		}
		return fileUpdate;
	}

	public static PropertyUtil getInstance() {
		if (thisInstance == null) {
			thisInstance = new PropertyUtil();
		}
		return thisInstance;
	}

	private PropertyUtil() {

	}

	public String getValueForKey(String key) {
		if (loadUpdatedFile()) {
			refresh();
		}
		String p = propCommonUtility.getProperty(key);
		return p;
	}

}
