package com.soboleiv.flatsearch.server.geo;

import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

public class BatchLocationRequest {
	private List<String> addresses;
	private String city;
	
	public BatchLocationRequest(List<String> addresses) {
		super();
		this.addresses = addresses;
	}

	public void setCity(String city) {
		this.city = city;
	}
	public String getCity() {
		return city;
	}	
	public List<String> getAddresses() {
		return Lists.transform(addresses, new Function<String, String>(){
			public String apply(String arg0) {
				return city += arg0;
			}
		});
	}
}
