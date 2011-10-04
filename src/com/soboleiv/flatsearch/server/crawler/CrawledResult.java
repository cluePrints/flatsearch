package com.soboleiv.flatsearch.server.crawler;

import java.util.Date;

public class CrawledResult {
	private String address;
	private String url;
	private Date dateRetrieved = new Date();
	public CrawledResult(String address, String url) {
		super();
		this.address = address;
		this.url = url;
	}
	@Override
	public String toString() {
		return "CrawledResult [address=" + address + ", url=" + url
				+ ", dateRetrieved=" + dateRetrieved + "]";
	}
	
	public String getAddress() {
		return address;
	}
	
	public Date getDateRetrieved() {
		return dateRetrieved;
	}
	
	public String getUrl() {
		return url;
	}
}
