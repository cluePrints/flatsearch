package com.soboleiv.flatsearch.server.geo;

import java.net.URLEncoder;

import com.google.gwt.user.client.Random;
import com.soboleiv.flatsearch.server.crawler.UrlReader;

public class LocationSvcReader {
	UrlReader urlReader = new UrlReader();
	
	public String getByAddress(String address) {
		try {
			String encodedAddress = URLEncoder.encode(address, "UTF-8");
			String query = "http://maps.googleapis.com/maps/api/geocode/xml?address=" + encodedAddress+"&sensor=false";
			Thread.sleep(new java.util.Random().nextInt(1000));
			return urlReader.readUrlContent(query);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public void setUrlReader(UrlReader urlReader) {
		this.urlReader = urlReader;
	}
}
