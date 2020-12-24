package com.nv.hclutility;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

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

	public static void main(String[] args) {
		DOMConfigurator.configure("D:\\Anand Raman\\Novelvox WorkSpace\\HCL\\HCLUtility\\conf\\log4j.xml");
		HCLUtilityEmail.getInstance().startThread();
	}

	public void startThread() {
		String flag = PropertyUtil.getInstance().getValueForKey("hclutility.thread.flag");
		if (flag.equals("true")) {
			try {
				new Thread(new EmailThread()).start();
			} catch (Exception e) {
				LOGGER.info("Exception in Email Thread ", e);
			}
		} else {
			LOGGER.info("We are not starting HCL Utility thread and since its flag is : " + flag);
		}
	}
}
