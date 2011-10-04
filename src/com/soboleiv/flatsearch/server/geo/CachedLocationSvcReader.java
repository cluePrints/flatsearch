package com.soboleiv.flatsearch.server.geo;

import java.util.Date;

import com.soboleiv.flatsearch.server.db.DataStore;

public class CachedLocationSvcReader {
	public DataStore<CachedLocationResponse> store = new DataStore<CachedLocationResponse>();
	public LocationSvcReader reader = new LocationSvcReader();
	
	public String getByAddress(String address) {
		CachedLocationResponse ex = new CachedLocationResponse(address, null);
		CachedLocationResponse result = store.getByExample(ex);
		if (result == null) {
			result = new CachedLocationResponse(address, reader.getByAddress(address));
			store.save(result);
		}
		return result.getResponse();
	}
}

class CachedLocationResponse {
	private String address;
	private String response;
	public CachedLocationResponse(String address, String response) {
		super();
		this.address = address;
		this.response = response;
	}
	
	public String getAddress() {
		return address;
	}
	
	public String getResponse() {
		return response;
	}	
}
