package com.soboleiv.flatsearch.server.geo;


public class CachedLocationSvcReader {
	public LocationSvcReader reader = new LocationSvcReader();
	
	public String getByAddress(String address) {
		return reader.getByAddress(address);
	}
}
