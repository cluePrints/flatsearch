package com.soboleiv.flatsearch.shared;

import java.io.Serializable;

public class Location implements Serializable {
	private double latitude;
	private double longitude;
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public static final Location INVALID = new Location(){
		public double getLatitude() {
			throw new RuntimeException("");
		};
		
		@Override
		public double getLongitude() {
			throw new RuntimeException("");
		}
		
	};
	@Override
	public String toString() {
		return "Location [latitude=" + latitude + ", longitude=" + longitude
				+ "]";
	}
}
