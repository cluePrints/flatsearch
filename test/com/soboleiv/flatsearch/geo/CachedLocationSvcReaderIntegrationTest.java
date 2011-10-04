package com.soboleiv.flatsearch.geo;

import org.junit.Assert;
import org.junit.Test;

import com.soboleiv.flatsearch.server.db.DataStore;
import com.soboleiv.flatsearch.server.geo.CachedLocationSvcReader;
import com.soboleiv.flatsearch.server.geo.LocationSvcReader;

public class CachedLocationSvcReaderIntegrationTest {
	@Test
	public void shouldNotBotherServiceTwice()
	{
		DataStore.inMemory = true;
		
		CachedLocationSvcReader reader = new CachedLocationSvcReader();
		reader.reader = new LocationSvcReader(){
			private int attempt = 0;
			public String getByAddress(String address) {
				if (attempt++>1)
					Assert.fail();
				return address;				
			};
		};
		
		String result1 = reader.getByAddress("Address");
		String result2 = reader.getByAddress("Address");
		
		Assert.assertEquals(result1, result2);
	}
}
