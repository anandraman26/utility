package com.nv.hclutility.pojo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class TraceResult {

	private String id;
	private String sourceIpAddress;
	private String destIpAddress;
	private Timestamp rtoTime;
	private StringBuffer traceLog;
	private Destination destination;
	private String applicationName;

	private List<LinkedHashMap<String, String>> rtoListForEmailPdf = new ArrayList<LinkedHashMap<String, String>>();
	private ConcurrentHashMap<String, TraceResult> rtoListForEmailList = new ConcurrentHashMap<String, TraceResult>();

	public List<LinkedHashMap<String, String>> getRtoListForEmailPdf() {
		return rtoListForEmailPdf;
	}

	public void setRtoListForEmailPdf(List<LinkedHashMap<String, String>> rtoListForEmailPdf) {
		this.rtoListForEmailPdf = rtoListForEmailPdf;
	}

	public ConcurrentHashMap<String, TraceResult> getRtoListForEmailList() {
		return rtoListForEmailList;
	}

	public void setRtoListForEmailList(ConcurrentHashMap<String, TraceResult> rtoListForEmailList) {
		this.rtoListForEmailList = rtoListForEmailList;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public TraceResult() {

	}

	public String getSourceIpAddress() {
		return sourceIpAddress;
	}

	public void setSourceIpAddress(String sourceIpAddress) {
		this.sourceIpAddress = sourceIpAddress;
	}

	public String getDestIpAddress() {
		return destIpAddress;
	}

	public void setDestIpAddress(String destIpAddress) {
		this.destIpAddress = destIpAddress;
	}

	public Timestamp getRtoTime() {
		return rtoTime;
	}

	public void setRtoTime(Timestamp rtoTime) {
		this.rtoTime = rtoTime;
	}

	public StringBuffer getTraceLog() {
		return traceLog;
	}

	public void setTraceLog(StringBuffer traceLog) {
		this.traceLog = traceLog;
	}

	public TraceResult(String sourceIpAddress, String destIpAddress, Timestamp rtoTime, StringBuffer traceLog) {
		super();
		this.sourceIpAddress = sourceIpAddress;
		this.destIpAddress = destIpAddress;
		this.rtoTime = rtoTime;
		this.traceLog = traceLog;
	}

	public Destination getDestination() {
		return destination;
	}

	public void setDestination(Destination destination) {
		this.destination = destination;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "TraceResult [id=" + id + ", sourceIpAddress=" + sourceIpAddress + ", destIpAddress=" + destIpAddress
				+ ", rtoTime=" + rtoTime + ", traceLog=" + traceLog + ", destination=" + destination
				+ ", applicationName=" + applicationName + "]";
	}

}
