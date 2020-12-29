package com.nv.hclutility.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PropertyUtilMonitor {
	private static Logger log = Logger.getLogger(PropertyUtilMonitor.class);

	private static Properties properties = new Properties();

	private static long lastTime;

	// private static final String FILE_NAME =
	// "C:/IST/iagent-apache-tomcat-6.0.16/webapps/iagent/WEB-INF/conf/BAJServices.properties";

	private static final String FILE_NAME = "C:/NovelVox/iagent-server/webapps/ROOT/WEB-INF/conf/BAJServices.properties";

	private static final int FILE_MONITOR_INTERVAL = 30 * 1000;

	static {
		FileInputStream inStream = null;
		File f = null;
		try {
			f = new File(FILE_NAME);
			inStream = new FileInputStream(f);
			properties.load(inStream);
			lastTime = f.lastModified();
			new Thread(new Runnable() {
				public void run() {
					try {
						Thread.sleep(FILE_MONITOR_INTERVAL);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					File tmpFile = new File(FILE_NAME);
					if (tmpFile.lastModified() > lastTime && lastTime > 0) {
						FileInputStream inStream2 = null;
						try {
							inStream2 = new FileInputStream(tmpFile);
							properties.load(inStream2);
							log.info("Propeties file loaded successfully");
						} catch (Exception e) {
							System.err.println("Unable to load properties file during change (" + tmpFile + ") : " + e);
							log.error("Error loading properties file: " + e);
						}
						lastTime = tmpFile.lastModified();
					}
				}
			}).start();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Unable to load properties file (" + f + ")" + e);
			e.printStackTrace();
			log.error("Error loading properties file: " + e);
		}
	}

	public static String getProperty(String key) {
		return properties.getProperty(key);
	}

	public static String getProperty(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}
}
