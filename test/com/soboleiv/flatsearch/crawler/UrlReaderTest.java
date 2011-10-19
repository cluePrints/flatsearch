package com.soboleiv.flatsearch.crawler;

import org.easymock.EasyMock;
import org.junit.Test;

import com.soboleiv.flatsearch.server.crawler.CachedUrlReader;
import com.soboleiv.flatsearch.server.crawler.CachedUrlReader.Interaction;
import com.soboleiv.flatsearch.server.crawler.UrlReader;
import com.soboleiv.flatsearch.server.db.DataStore;

public class UrlReaderTest {
	@Test
	public void shouldConsiderExpiryTime() {
		UrlReader urlReader = EasyMock.createMock(UrlReader.class);
		EasyMock.expect(urlReader.readUrlContent(EasyMock.isA(String.class))).andReturn("").times(2);
		EasyMock.replay(urlReader);
		CachedUrlReader reader = new CachedUrlReader(urlReader);
		mockDataStore(reader);
		reader.setExpiryDays(0);
		
		reader.readUrlContent("");
		reader.readUrlContent("");
		
		EasyMock.verify(urlReader);
	}
	
	@Test
	public void shouldConsiderExpiryNotHappenedToServeStuffFromCache() {
		UrlReader urlReader = EasyMock.createMock(UrlReader.class);
		EasyMock.expect(urlReader.readUrlContent(EasyMock.isA(String.class))).andReturn("").times(1);
		EasyMock.replay(urlReader);
		CachedUrlReader reader = new CachedUrlReader(urlReader);
		mockDataStore(reader);
		reader.setExpiryDays(1);
		
		reader.readUrlContent("");
		reader.readUrlContent("");
		
		EasyMock.verify(urlReader);
	}

	private void mockDataStore(CachedUrlReader reader) {
		DataStore<Interaction> store = DataStore.forTests();
		reader.setStore(store);
	}
}
