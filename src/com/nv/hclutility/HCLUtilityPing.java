package com.nv.hclutility;

import java.util.HashSet;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import com.nv.hclutility.thread.PingThread;
import com.nv.hclutility.util.PropertyUtil;

/**
 * 
 * 
 */
public class HCLUtilityPing {

	private static final Logger LOGGER = Logger.getLogger(HCLUtilityPing.class);
	private static volatile HCLUtilityPing INSTANCE;

	public static HCLUtilityPing getInstance() {
		if (INSTANCE == null)
			INSTANCE = new HCLUtilityPing();
		return INSTANCE;
	}

	String sleepTime = null;

	private HCLUtilityPing() {
		sleepTime = PropertyUtil.getInstance().getValueForKey("ping.thread.sleepTime");
	}

	public static void main(String[] args) {
		DOMConfigurator.configure("D:\\Anand Raman\\Novelvox WorkSpace\\HCL\\HCLUtility\\conf\\log4j.xml");
		HCLUtilityPing.getInstance().startThread();
	}

	public void startThread() {
		try {
			LOGGER.info("sleepTime "+sleepTime);
			Thread.sleep(Long.valueOf(sleepTime));
		} catch (InterruptedException e1) {
			LOGGER.error("Exception ", e1);
		}
		String flag = PropertyUtil.getInstance().getValueForKey("hclutility.thread.flag");
		if (flag.equals("true")) {
			LOGGER.info("Loading all the endpoints from properties file "+snowAPI);
			getValuesFromPropertiesFile();
			try {
				startPingThread();
			} catch (Exception e) {
				LOGGER.info("Exception in Ping thread ", e);
			}

		} else {
			LOGGER.info("We are not starting HCL Utility thread and since its flag is : " + flag);
		}
	}

	private void startPingThread() {
		try {
			for (String ip : snowAPI) {
				  new Thread(new PingThread(ip)).start();  
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static HashSet<String> snowAPI = new HashSet<>();

	private static void getValuesFromPropertiesFile() {
		try {
			String[] snowApi = PropertyUtil.getInstance().getValueForKey("snow.api").split(",");
			for (String string : snowApi) {
				snowAPI.add(string);
			}
			LOGGER.info("Properties file & DB  loaded " + snowAPI.toString());
		} catch (Exception e) {
			LOGGER.error("Exception while loading SNOW API from properties file ", e);
		}
	}

}
