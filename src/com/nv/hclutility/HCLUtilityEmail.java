package com.nv.hclutility;

import org.apache.log4j.Logger;

import com.nv.hclutility.thread.EmailThread;
import com.nv.hclutility.util.PropertyUtil;

/**
 * 
 * 
 */
public class HCLUtilityEmail {

	private static final Logger LOGGER = Logger.getLogger(HCLUtilityEmail.class);
	private static volatile HCLUtilityEmail INSTANCE;

	public static HCLUtilityEmail getInstance() {
		if (INSTANCE == null)
			INSTANCE = new HCLUtilityEmail();
		return INSTANCE;
	}

	private HCLUtilityEmail() {
	}

	public void startThread() {
		String flag = PropertyUtil.getInstance().getValueForKey("hclutility.thread.flag");
		if (flag.equals("true")) {
			try {
				new Thread(EmailThread.getInstance()).start();
			} catch (Exception e) {
				LOGGER.info("Exception in Email Thread ", e);
			}
		} else {
			LOGGER.info("We are not starting HCL Utility thread and since its flag is : " + flag);
		}
	}
}
