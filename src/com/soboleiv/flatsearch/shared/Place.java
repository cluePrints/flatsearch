package com.soboleiv.flatsearch.shared;

import java.io.Serializable;

public class Place implements Serializable{
	public static final Place INVALID = new Place();
	
	private double lat;
	private double lon;
	private String url;
	private String address;
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Override
	public String toString() {
		return "Place [lat=" + lat + ", lon=" + lon + ", url=" + url
				+ ", address=" + address + "]";
	}
	
}
