package com.soboleiv.flatsearch.server.crawler;

public class CrawledResult {
	private String address;
	private String url;
	public CrawledResult(String address, String url) {
		super();
		this.address = address;
		this.url = url;
	}
	@Override
	public String toString() {
		return "CrawledResult [address=" + address + ", url=" + url + "]";
	}		
}
