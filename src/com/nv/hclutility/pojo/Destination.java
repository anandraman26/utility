package com.nv.hclutility.pojo;

public class Destination {

	private String destinationIP;
	private String applicationName;

	public String getDestinationIP() {
		return destinationIP;
	}

	public void setDestinationIP(String destinationIP) {
		this.destinationIP = destinationIP;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	@Override
	public String toString() {
		return "Destination [destinationIP=" + destinationIP + ", applicationName=" + applicationName + "]";
	}

}
