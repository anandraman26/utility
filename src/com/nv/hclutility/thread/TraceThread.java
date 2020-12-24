package com.nv.hclutility.thread;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.nv.hclutility.db.DBUtility;
import com.nv.hclutility.pojo.TraceResult;
import com.nv.hclutility.util.PropertyFileConstants;

public class TraceThread  implements Runnable{//implements Callable<TraceResult> {

	private static final Logger LOGGER = Logger.getLogger(TraceThread.class);
	private static volatile TraceThread INSTANCE;

	private String destIpAddress;
	private String sourceIpAddress;
	private Timestamp rtoTime;

	public static TraceThread getInstance() {
		if (INSTANCE == null)
			INSTANCE = new TraceThread();
		return INSTANCE;
	}

	private TraceThread() {

	}

	public TraceThread(String sourceIpAddress, String destIpAddress, Timestamp rtoTime) {
		this.destIpAddress = destIpAddress;
		this.rtoTime = rtoTime;
		this.sourceIpAddress = sourceIpAddress;
	}

	@Override
	public void run() {
		ArrayList<String> traceCommandList = new ArrayList<String>();
		traceCommandList.add("tracert");
		traceCommandList.add(destIpAddress);
		try {
			TraceThread.traceCommand(traceCommandList, sourceIpAddress, destIpAddress, rtoTime);
		} catch (Exception e) {
			LOGGER.error(PropertyFileConstants.TRACE_THREAD+"Exception Trace thread Started  " + e);
		}
	}

	private static void traceCommand(ArrayList<String> traceCommandList, String sourceIpAddress, String destIpAddress,
			Timestamp rtoTime) {
		try {
			
			ProcessBuilder build = new ProcessBuilder(traceCommandList);
			Process process = build.start();
			BufferedReader InputStream = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String s = "";
			StringBuffer traceResult = new StringBuffer();
			while ((s = InputStream.readLine()) != null) {
				// traceResult.add(s + "\n");
				traceResult.append(s + "\n");
				if (s.contains("Trace complete.")) {
					try {
						destroyedRuningTrace();
					} catch (Exception e) {
						LOGGER.error(PropertyFileConstants.TRACE_THREAD+"Exception while stoping the tracert command", e);
					}
					TraceResult finalTraceResult = new TraceResult(sourceIpAddress, destIpAddress, rtoTime,
							traceResult);
					LOGGER.warn(PropertyFileConstants.TRACE_THREAD+"Trace complete. Request timed out. at time " + rtoTime + ":  for the destination ip " + destIpAddress);
					DBUtility.getInstance().setTraceDetails(finalTraceResult);
				}
			}

		} catch (Exception e) {
			LOGGER.info(PropertyFileConstants.TRACE_THREAD+"Exception Trace command  " + e);
		}
	}

	public static void destroyedRuningTrace() {
		try {
			LOGGER.info(PropertyFileConstants.TRACE_THREAD+"Destroyed Trace Command");
			Runtime.getRuntime().exec("taskkill /f /im tracert.exe");
		} catch (Exception e) {
			LOGGER.error(PropertyFileConstants.TRACE_THREAD+"Exception while destroying the context thread ", e);
		}
	}

}
