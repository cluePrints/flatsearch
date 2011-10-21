package com.soboleiv.flatsearch.server.geo;

import java.util.Collections;
import java.util.Map;

public class LocationSvcFacade {
	LocationParser parser = new LocationParser();
	LocationSvcReader reader = new LocationSvcReader();
	
	public Map<String, Location> getLocationsForAddresses(BatchLocationRequest loc) {
		String addr = loc.getAddresses().get(0);
		Location res = getSingle(addr);
		return Collections.singletonMap(addr, res);
	}
	
	private Location getSingle(String address)
	{
		String content = reader.getByAddress(address);
		return parser.parse(content);
	}
	
	public void setReader(LocationSvcReader reader) {
		this.reader = reader;
	}
	
	public LocationSvcReader getReader() {
		return reader;
	}
}
