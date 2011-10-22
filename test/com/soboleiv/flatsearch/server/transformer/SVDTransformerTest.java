package com.soboleiv.flatsearch.server.transformer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import com.google.common.io.Files;
import com.soboleiv.flatsearch.server.transorm.SDTransformer;
import com.soboleiv.flatsearch.shared.Place;

public class SVDTransformerTest {
	@Test
	public void shouldParseFirstRowProperly() throws Exception {
		String content = loadData();		
		SDTransformer unit = new SDTransformer();
		System.out.println(content);
		List<Place> result = unit.parse(content);		
		Map<String, Place> resultsByAddress = new HashMap<String, Place>();
		for (Place place : result) {
			resultsByAddress.put(place.getAddress(), place);
		}
		Place first = resultsByAddress.get("Комарова пр. , 9-А");
		Assert.assertEquals(1, first.getRoomsNumber());
		Assert.assertEquals("22.10.2011", result.get(0).getPostingDate());
		Assert.assertEquals("http://www.svdevelopment.com/ru/offer/ad/10/kiev/3248039/", result.get(0).getOriginalUrl());
		Assert.assertEquals("Комарова пр. , 9-А", result.get(0).getAddress());
		Assert.assertEquals(375, result.get(0).getPriceUsd());
	}

	private String loadData() throws IOException {
		URL url = this.getClass().getResource("SVDExample.html");
		String name = url.getFile();
		String content = Files.toString(new File(name), Charset.forName("Windows-1251"));
		return content;
	}
}
