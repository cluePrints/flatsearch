package com.soboleiv.flatsearch.crawler;

import org.easymock.EasyMock;
import org.junit.Test;

import com.soboleiv.flatsearch.server.crawler.CachedUrlReader;
import com.soboleiv.flatsearch.server.crawler.UrlReader;
import com.soboleiv.flatsearch.server.db.DataStore;

public class UrlReaderTest {
	@Test
	public void shouldConsiderExpiryTime() {
		DataStore.inMemory = true;
		
		UrlReader urlReader = EasyMock.createMock(UrlReader.class);
		EasyMock.expect(urlReader.readUrlContent(EasyMock.isA(String.class))).andReturn("").times(2);
		EasyMock.replay(urlReader);
		CachedUrlReader reader = new CachedUrlReader(urlReader);
		reader.setExpiryDays(0);
		
		reader.readUrlContent("");
		reader.readUrlContent("");
		
		EasyMock.verify(urlReader);
	}
	
	@Test
	public void shouldConsiderExpiryNotHappenedToServeStuffFromCache() {
		DataStore.inMemory = true;
		
		UrlReader urlReader = EasyMock.createMock(UrlReader.class);
		EasyMock.expect(urlReader.readUrlContent(EasyMock.isA(String.class))).andReturn("").times(1);
		EasyMock.replay(urlReader);
		CachedUrlReader reader = new CachedUrlReader(urlReader);
		reader.setExpiryDays(1);
		
		reader.readUrlContent("");
		reader.readUrlContent("");
		
		EasyMock.verify(urlReader);
	}
}
