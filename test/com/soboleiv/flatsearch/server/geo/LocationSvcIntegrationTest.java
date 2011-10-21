package com.soboleiv.flatsearch.server.geo;

import org.junit.Assert;
import org.junit.Test;

import com.soboleiv.flatsearch.server.geo.LocationSvcReader;

public class LocationSvcIntegrationTest {
	@Test
	public void shouldReturnSomething()
	{
		LocationSvcReader svc = new LocationSvcReader();
		String res = svc.getByAddress("1600+Amphitheatre+Parkway,+Mountain+View,+CA");
		Assert.assertTrue(res.contains("status>OK</status"));
	}
}
