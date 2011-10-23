package com.soboleiv.flatsearch.server.transformer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import com.google.common.io.Files;
import com.google.gwt.thirdparty.guava.common.base.Charsets;
import com.soboleiv.flatsearch.server.transorm.RDTransformer;
import com.soboleiv.flatsearch.shared.Place;

public class RDTransformerTest {
	@Test
	public void shouldParseFirstRowProperly() throws Exception {
		String content = loadData();		
		RDTransformer unit = new RDTransformer();
		List<Place> result = unit.parse(content);
		
		Assert.assertTrue(result.size()>0);
		
		Map<String, Place> resultsByAddress = new HashMap<String, Place>();
		for (Place place : result) {
			resultsByAddress.put(place.getAddress(), place);
		}
		Place first = resultsByAddress.get("Жилянская");
		Assert.assertEquals("Жилянская", first.getAddress());
		Assert.assertEquals(1, first.getRoomsNumber());
		Assert.assertEquals(600, first.getPriceUsd());
		Assert.assertEquals("http://www.realdruzi.com.ua/app/109091/", first.getOriginalUrl());
		Assert.assertNotNull("22.10.2011", first.getPostingDate());
	}

	private String loadData() throws IOException {
		URL url = this.getClass().getResource("RDExample.html");
		String name = url.getFile();
		String content = Files.toString(new File(name), Charsets.UTF_8);
		return content;
	}
}
