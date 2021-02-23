package com.nv.hclutility.context;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.nv.hclutility.HCLUtilityEmail;
import com.nv.hclutility.HCLUtilityPing;

public class HCLUtilityContextListener implements ServletContextListener {

	/**
	 * Initialize log4j when the application is being started
	 */

	private static Logger LOGGER = Logger.getLogger(HCLUtilityContextListener.class);

	public void contextInitialized(ServletContextEvent event) {
		LOGGER.info("HCLUtility: Thread Started");
		try {
			init();
			HCLUtilityPing.getInstance().startThread();
			HCLUtilityEmail.getInstance().startThread();
		} catch (Exception e) {
			LOGGER.error("Exception while starting thread ", e);
		}

	}

	public void init() {
		try {
			LOGGER.info("init for ping command");
			Runtime.getRuntime().exec("taskkill /f /im ping.exe");
		} catch (Exception e) {
			LOGGER.error("Exception while destroying the context thread ", e);
		}
		try {
			LOGGER.info("init for tracert command");
			Runtime.getRuntime().exec("taskkill /f /im tracert.exe");
		} catch (Exception e) {
			LOGGER.error("Exception while destroying the context thread ", e);
		}
	}

	public void contextDestroyed(ServletContextEvent event) {
		try {
			LOGGER.info("contextDestroyed for ping command");
			Runtime.getRuntime().exec("taskkill /f /im ping.exe");
		} catch (Exception e) {
			LOGGER.error("Exception while destroying the context thread ", e);
		}
		try {
			LOGGER.info("contextDestroyed for tracert command");
			Runtime.getRuntime().exec("taskkill /f /im tracert.exe");
		} catch (Exception e) {
			LOGGER.error("Exception while destroying the context thread ", e);
		}
	}

	// public static void main(String[] args) {
	// LOGGER.info("HCLUtility: Thread Started");
	// try {
	// init();
	// HCLUtilityPing.getInstance().startThread();
	// HCLUtilityEmail.getInstance().startThread();
	// } catch (Exception e) {
	// LOGGER.error("Exception while starting thread ", e);
	// }
	// }

}
