package com.soboleiv.flatsearch.server.geo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

import com.google.common.io.CharStreams;
import com.soboleiv.flatsearch.server.crawler.Crawler;

public class LocationSvcReader {
	public String getByAddress(String address) {
		try {
			String encodedAddress = URLEncoder.encode(address, "UTF-8");
			String query = "http://maps.googleapis.com/maps/api/geocode/xml?address=" + encodedAddress+"&sensor=false";
			return Crawler.readUrlContent(query);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
