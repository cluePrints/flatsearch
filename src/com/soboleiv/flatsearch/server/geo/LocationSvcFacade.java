package com.soboleiv.flatsearch.server.geo;

import com.soboleiv.flatsearch.shared.Location;

public class LocationSvcFacade {
	LocationParser parser = new LocationParser();
	LocationSvcReader reader = new LocationSvcReader();
	
	public Location get(String address)
	{
		address="Киев "+address;
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
