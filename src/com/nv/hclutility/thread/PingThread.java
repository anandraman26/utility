package com.nv.hclutility.thread;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.nv.hclutility.util.PropertyFileConstants;
import com.nv.hclutility.util.PropertyUtil;

public class PingThread implements Runnable {
	
	private static final Logger LOGGER = Logger.getLogger(PingThread.class);
	private static volatile PingThread INSTANCE;

	public static PingThread getInstance() {
		if (INSTANCE == null)
			INSTANCE = new PingThread();
		return INSTANCE;
	}
	
	private PingThread() {
	}

	private String ipAddress;

	public PingThread(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	@Override
	public void run() {
		ArrayList<String> pingCommandList = new ArrayList<String>();
		pingCommandList.add("ping");
		pingCommandList.add("-t");
		// can be replaced by IP
		pingCommandList.add(this.ipAddress);
		LOGGER.info(PropertyFileConstants.PING_THREAD+"Ping Thread Started :... ");
		try {
			PingThread.pingCommands(pingCommandList, ipAddress);
		} catch (Exception e) {
			LOGGER.error(PropertyFileConstants.PING_THREAD+"Exception while Ping command : "+pingCommandList.toString());
			LOGGER.error(PropertyFileConstants.PING_THREAD+"Exception ",e); 
		}
	}
	
	private static void pingCommands(ArrayList<String> commandList, String destIpAddress) throws Exception {
		// creating the sub process, execute system command
		ProcessBuilder build = new ProcessBuilder(commandList);
		Process process = build.start();

		// to read the output
		BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
		BufferedReader Error = new BufferedReader(new InputStreamReader(process.getErrorStream()));
		String s = null;
		boolean rtoFlag = true;
		LOGGER.info(PropertyFileConstants.PING_THREAD+"Ping Command for the destination Ip is : " + destIpAddress);
		while ((s = input.readLine()) != null) {
			if (s.equalsIgnoreCase("Request timed out.")&& rtoFlag) {
				rtoFlag=false;
				Timestamp rtoTime = new Timestamp(System.currentTimeMillis());
				//System.out.println("Ping thread +" + rtoTime + ": Request timed out. for the destination  "+ destIpAddress);
				LOGGER.warn(PropertyFileConstants.PING_THREAD+"\tRequest timed out. at time " + rtoTime + ":  for the destination ip "+ destIpAddress);
				String sourceIpAddress=PropertyUtil.getInstance().getValueForKey("server.ip");
				startTraceThread(sourceIpAddress,destIpAddress, rtoTime);
			}else {
				rtoFlag=true;
			}
		}
		//LOGGER.info(PropertyFileConstants.PING_THREAD+"error (if any): Stoping Ping Thread By Task Manager/ Service stop ");
		while ((s = Error.readLine()) != null) {
			LOGGER.error(PropertyFileConstants.PING_THREAD+"error while ping " + s);
		}
	}

	public static void startTraceThread(String sourceIpAddress,String destIpAddress, Timestamp rtoTime) {
		try {
			new Thread(new TraceThread(sourceIpAddress,destIpAddress, rtoTime)).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}