package com.soboleiv.flatsearch.server.crawler;

import java.util.Date;

public class CrawledResult {
	private String data;
	private String url;
	private Date dateRetrieved = new Date();
	public CrawledResult(String data, String url) {
		super();
		this.data = data;
		this.url = url;
	}
	public String getData() {
		return data;
	}
	public String getUrl() {
		return url;
	}
	public Date getDateRetrieved() {
		return dateRetrieved;
	}
}
