package com.soboleiv.flatsearch.server.geo;

import java.net.URLEncoder;

import com.soboleiv.flatsearch.server.crawler.UrlReader;

public class LocationSvcReader {
	UrlReader urlReader = new UrlReader();
	
	public String getByAddress(String address) {
		try {
			String encodedAddress = URLEncoder.encode(address, "UTF-8");
			String query = "http://maps.googleapis.com/maps/api/geocode/xml?address=" + encodedAddress+"&sensor=false";
			return urlReader.readUrlContent(query);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public void setUrlReader(UrlReader urlReader) {
		this.urlReader = urlReader;
	}
}
