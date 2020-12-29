package com.nv.hclutility.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

public class PropertiesFileMonitor {
	private static Logger log = Logger.getLogger(PropertiesFileMonitor.class);

	private static Properties properties = new Properties();

	private static long lastTime;
	private static long modifiedFileTime;

	// private static final String FILE_NAME =
	// "C:/IST/iagent-apache-tomcat-6.0.16/webapps/iagent/WEB-INF/conf/BAJServices.properties";

	private static final String FILE_NAME = "C:/NovelVox/nad-server/webapps/ROOT/WEB-INF/conf/hclutility.properties";

	private static final int FILE_MONITOR_INTERVAL = 1 * 1000;

	public static void getPropertiesFileMonitor(ArrayList<String> fileNameWithPath) {
		// InputStream in =
		// PropertyUtil.class.getClass().getResourceAsStream(FILE_NAME);
		try {
			new Thread(new Runnable() {
				public void run() {
					//while (true) {
						for (String fileName : fileNameWithPath) {
							FileInputStream inStream = null;
							File f = new File(PATH + fileName);
							try {
								inStream = new FileInputStream(f);
							} catch (FileNotFoundException e2) {
								// TODO Auto-generated catch block
								e2.printStackTrace();
							}
							try {
								properties.load(inStream);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							System.out.println(fileName + "\nproperties file is " + properties);
							lastTime = f.lastModified();

							File tmpFile = new File(FILE_NAME);
							modifiedFileTime = tmpFile.lastModified();
							if (modifiedFileTime > lastTime && lastTime > 0) {
								//System.out.println("Last motified file is " + tmpFile.lastModified());
								FileInputStream inStream2 = null;
								// InputStream in1 =
								// PropertyUtil.class.getClass().getResourceAsStream(FILE_NAME);
								try {
									inStream2 = new FileInputStream(tmpFile);
									properties.load(inStream2);
									log.info("Propeties file loaded successfully");
									// System.out.println("Database information is :
									// "+dbUrl+"\n"+dbUsername+"\n"+dbpAssword+"\n");
								} catch (Exception e) {
									System.err.println(
											"Unable to load properties file during change (" + tmpFile + ") : " + e);
									log.error("Error loading properties file: " + e);
								}
								lastTime = tmpFile.lastModified();
							}
							try {
								Thread.sleep(FILE_MONITOR_INTERVAL);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
							concurrentHashMap.put(fileName, properties);
						}
					}
				//}
			}).start();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Unable to load properties file (" + ""+ ")" + e);
			e.printStackTrace();
			log.error("Error loading properties file: " + e);
		}
	}

	public static String getProperty(String key) {
		return properties.getProperty(key);
	}

	private static final String PATH = "C:/NovelVox/nad-server/webapps/ROOT/WEB-INF/conf/";

	public static void main(String[] args) throws IOException {
		final File folder = new File(PATH);
		listFilesForFolder(folder);
		//System.out.println(propertiesName);
		getPropertiesFileMonitor(propertiesName);
		System.out.println(concurrentHashMap);
	}

	static ConcurrentHashMap<String, Properties> concurrentHashMap = new ConcurrentHashMap<String, Properties>();
	static ArrayList<String> propertiesName = new ArrayList<>();

	public static void listFilesForFolder(final File folder) {
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry);
			} else {
				if (fileEntry.getName().endsWith(".properties")) {
					propertiesName.add(fileEntry.getName());
				}
			}
		}
	}

	public static String getProperty(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}
}
