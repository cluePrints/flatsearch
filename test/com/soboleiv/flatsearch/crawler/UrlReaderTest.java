package com.soboleiv.flatsearch.crawler;

import org.easymock.EasyMock;
import org.junit.Test;

import com.soboleiv.flatsearch.server.crawler.CachedUrlReader;
import com.soboleiv.flatsearch.server.crawler.UrlReader;

public class UrlReaderTest {
	@Test
	public void shouldConsiderExpiryTime()
	{
		UrlReader urlReader = EasyMock.createMock(UrlReader.class);
		EasyMock.expect(urlReader.readUrlContent(EasyMock.isA(String.class))).andReturn("").times(2);
		CachedUrlReader reader = new CachedUrlReader(urlReader);
		reader.setExpiryDays(0);
		
		reader.readUrlContent("");
		
		EasyMock.verify(urlReader);
	}
}
