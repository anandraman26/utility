package com.nv.hclutility.thread;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.nv.hclutility.db.DBUtility;
import com.nv.hclutility.pojo.Destination;
import com.nv.hclutility.pojo.TraceResult;
import com.nv.hclutility.util.PdfReportBuilder;
import com.nv.hclutility.util.PropertyFileConstants;
import com.nv.hclutility.util.PropertyUtil;
import com.nv.hclutility.util.SendEmail;

/**
 * 
 * @author Novelvox
 *
 */

public class EmailThread implements Runnable {

	private static final Logger LOGGER = Logger.getLogger(EmailThread.class);
	private static volatile EmailThread INSTANCE;
	private static volatile String sleepTime;

	public static EmailThread getInstance() {
		if (INSTANCE == null)
			INSTANCE = new EmailThread();
		return INSTANCE;
	}

	public EmailThread() {
		
	}

	@Override
	public void run() {
		while(true) {
			try {
				sleepTime = PropertyUtil.getInstance().getValueForKey("email.thread.sleepTime");
				LOGGER.info(PropertyFileConstants.EMAIL_THREAD+"Current Time is :"+Calendar.getInstance().getTime()+" and EMAIL Thread start after "+sleepTime+" milli second ");
				Thread.sleep(Long.valueOf(sleepTime));
				startEmailThread();
			} catch (Exception e) {
				LOGGER.error(PropertyFileConstants.EMAIL_THREAD+"Exception while e-mail thread : ", e);
			}
		}
	}

	private void startEmailThread() throws InterruptedException {
		ArrayList<Destination> destinationList = DBUtility.getInstance().getAPIList();
		LOGGER.info(PropertyFileConstants.EMAIL_THREAD+"destinationList " + destinationList.size());
		if (null != destinationList && !destinationList.isEmpty()) {
			for (Destination destination : destinationList) {
				TraceResult rtoList = DBUtility.getInstance()
						.getRTOListByDestinationIp(destination.getDestinationIP());
				if (null != rtoList && (null!=rtoList.getRtoListForEmailList() && !rtoList.getRtoListForEmailList().isEmpty())
						&& (null!=rtoList.getRtoListForEmailPdf() && !rtoList.getRtoListForEmailPdf().isEmpty() 
						&& rtoList.getRtoListForEmailPdf().size()>1)) {
					sendEmail(rtoList.getRtoListForEmailList(),rtoList.getRtoListForEmailPdf());
				} else {
					LOGGER.info(PropertyFileConstants.EMAIL_THREAD+"No RTO details found for : "+destination.getApplicationName()+" So EMAIL Not sent : ");
				}
			}
		} else {
			LOGGER.error(PropertyFileConstants.EMAIL_THREAD+"NOT Receive Destination LIst fromm DB");
		}
	}

	private void sendEmail(ConcurrentHashMap<String, TraceResult> rtoList, List<LinkedHashMap<String, String>> rtoListPdf) { 
		List<TraceResult> traceResults =new ArrayList<TraceResult>();
		for (Entry<String, TraceResult> result : rtoList.entrySet()) {
			traceResults.add(result.getValue());
		}
		try {
			String fileName="";
			for (TraceResult traceResult : traceResults) {
				fileName=traceResult.getApplicationName()+"_"+Calendar.getInstance().getTime().getTime() + ".pdf";
				break;
			}
			convertDBDataIntoPdf(rtoListPdf,fileName);
			LOGGER.info(PropertyFileConstants.EMAIL_THREAD+"Sending Email.... ");
			SendEmail.getInstance().sendEmail(traceResults,fileName);
		} catch (Exception e) {
			LOGGER.error(PropertyFileConstants.EMAIL_THREAD+"Exception while converting DB Data to pdf file "); 
		}
	}

	private void convertDBDataIntoPdf(List<LinkedHashMap<String, String>> rtoListPdf,String fileName) {
		PdfReportBuilder.preparePdfReport(rtoListPdf,fileName);
		LOGGER.info(PropertyFileConstants.EMAIL_THREAD+"convertedDBDataIntoPdf ");
	}

}
