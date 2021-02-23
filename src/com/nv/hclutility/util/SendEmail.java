package com.nv.hclutility.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import com.nv.hclutility.pojo.TraceResult;

public class SendEmail extends Authenticator{

	private static final Logger LOGGER = Logger.getLogger(SendEmail.class);
	private static volatile SendEmail INSTANCE;

	public static SendEmail getInstance() {
		if (INSTANCE == null)
			INSTANCE = new SendEmail();
		return INSTANCE;
	}

	private SendEmail() {
		    super();
	}

	public void sendEmail(List<TraceResult> traceResults,String fileName) {
		
		try {
			String FILEPATH = PropertyUtil.getInstance().getValueForKey("FilePath");
			String[] aesName = PropertyUtil.getInstance().getValueForKey("email.aes.name").split(",");
			String applicationName = "";
			String destinationIp = "";
			String sourceIp = "";

			for (TraceResult traceResult : traceResults) {
				applicationName = traceResult.getApplicationName();
				destinationIp = traceResult.getDestIpAddress();
				sourceIp = traceResult.getSourceIpAddress();
				break;
			}
			// 1) get the session object
			Properties props = new Properties();
			props.put("mail.smtp.auth", PropertyUtil.getInstance().getValueForKey("email.smtp.auth"));
			props.put("mail.smtp.starttls.enable", PropertyUtil.getInstance().getValueForKey("email.smtp.starttls.enable"));
			props.put("mail.smtp.host", PropertyUtil.getInstance().getValueForKey("email.smtp.host"));
			props.put("mail.smtp.port", PropertyUtil.getInstance().getValueForKey("email.smtp.port"));

			Session session = Session.getInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(PropertyUtil.getInstance().getValueForKey("email.user"),
							PropertyUtil.getInstance().getValueForKey("email.pwd"));
				}
			});

			// 2) compose message
			try {
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(PropertyUtil.getInstance().getValueForKey("email.from")));
				message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(PropertyUtil.getInstance().getValueForKey("email.to")));

				if (Arrays.asList(aesName).contains(applicationName)) {
					message.setSubject(PropertyUtil.getInstance().getValueForKey("email.subject.AES").replace("#sourceIp#", sourceIp).replace("#applicationName#", applicationName));
				} else {
					message.setSubject(PropertyUtil.getInstance().getValueForKey("email.subject.SNOW").replace("#sourceIp#", sourceIp).replace("#applicationName#", applicationName));
				}
				message.setText("Yo it has been sent");
				String msg = PropertyUtil.getInstance().getValueForKey("email.message").replace("#sourceIp#", sourceIp).replace("#applicationName#", applicationName).replace("#destinationIp#", destinationIp);

				// 4) create new MimeBodyPart object and set DataHandler object to this object
				Multipart multipart = new MimeMultipart();
				MimeBodyPart messageBodyPart = new MimeBodyPart(); 
				LOGGER.info(fileName);
				String file=FILEPATH+fileName;
				LOGGER.info(file);
				DataSource source = new FileDataSource(fileName);
				messageBodyPart.setDataHandler(new DataHandler(source));
				messageBodyPart.setFileName(fileName);
				multipart.addBodyPart(messageBodyPart);
				
				// 6) set the multiplart object to the message object
				message.setContent(multipart);
				
				LOGGER.debug(PropertyFileConstants.EMAIL_THREAD+"Message Body:" + msg);
				message.setContent(msg, "text/html");
				
				Transport.send(message);
				//delete(new File(file));
				try {
					//DBUtility.getInstance().updateEmailStatus(traceResults,PropertyUtil.getInstance().getValueForKey("email.to"),"Yes");
				} catch (Exception e) {
					LOGGER.error(PropertyFileConstants.EMAIL_THREAD+"Exception while updateing the email status ", e);
				}
			} catch (MessagingException ex) {
				LOGGER.error(PropertyFileConstants.EMAIL_THREAD+"Exception in sending email ", ex);
				//DBUtility.getInstance().updateEmailStatus(traceResults,PropertyUtil.getInstance().getValueForKey("email.to"),"No");
			}
		} catch (Exception e) {
			LOGGER.error(PropertyFileConstants.EMAIL_THREAD+"Exception in sending email ", e);
		}

	}
	
	private static void delete(File file) {
		LOGGER.info(PropertyFileConstants.EMAIL_THREAD+"Deleting file ............"+file); 
		if (file.exists()) {
			file.delete();
			LOGGER.info(PropertyFileConstants.EMAIL_THREAD+"Deleted file ............"+file); 
		} else {
			LOGGER.info(PropertyFileConstants.EMAIL_THREAD+"existing file on server does not exist\t"+file);
		}

	}
	
	public static void main(String[] args) {
		DOMConfigurator.configure("D:\\Anand Raman\\Novelvox WorkSpace\\HCL\\HCLUtility\\conf\\log4j.xml");
		TraceResult traceResults=new TraceResult();
		traceResults.setApplicationName("Noida-AES");
		traceResults.setDestIpAddress("ascensionprod.service-now.com");
		traceResults.setSourceIpAddress("10.10.11.111");
		List<TraceResult> traceResults2=new ArrayList<>();
		traceResults2.add(traceResults);
		SendEmail.getInstance().sendEmail(traceResults2,"D:/Generating_a_client_from_WSDL.pdf");
	}
}
