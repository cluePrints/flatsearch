package com.soboleiv.flatsearch.server.transorm;

import static com.soboleiv.flatsearch.server.transorm.Field.ADDRESS;
import static com.soboleiv.flatsearch.server.transorm.Field.COST;
import static com.soboleiv.flatsearch.server.transorm.Field.DATE;
import static com.soboleiv.flatsearch.server.transorm.Field.ROOMS;
import static com.soboleiv.flatsearch.server.transorm.Field.URL;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;
import com.soboleiv.flatsearch.server.crawler.ToArrayRegexpMapper;
import com.soboleiv.flatsearch.server.crawler.UrlNormalizer;
import com.soboleiv.flatsearch.server.util.Integers;
import com.soboleiv.flatsearch.shared.Place;

public class RowBasedTransformer implements Transformer{	
	protected List<Field> order;
	private ToArrayRegexpMapper dataMapper;
	private UrlNormalizer normalizer = UrlNormalizer.NONE;
	
	public RowBasedTransformer(String regexp, Field... order) {
		this.dataMapper = new ToArrayRegexpMapper(regexp);
		this.order = Arrays.asList(order);
	}
	
	public List<Place> parse(String html) {
		List<String[]> datas = dataMapper.parseData(html);
		
		List<Place> results = Lists.newLinkedList();
		for (String[] data : datas) {
			String address = extract(data, ADDRESS);
			String url = normalizer.normalize(extract(data, URL));
			String date = extract(data, DATE);
			String roomsStr = extract(data, ROOMS);
			String costStr = extract(data, COST);
			
			int rooms = Integers.fromString(roomsStr);
			int cost = Integers.fromString(costStr);
			
			Place result = new Place();
			result.setOriginalUrl(url);
			result.setAddress(address);
			result.setPostingDate(date);
			result.setRoomsNumber(rooms);
			result.setPriceUsd(cost);
			results.add(result);
		}
		return results;
	}

	private String extract(String[] data, Field field) {
		int index = order.indexOf(field);
		if (index < 0 || index > data.length -1)
			return null;
		
		return data[index];
	}
	
	public void setNormalizer(UrlNormalizer normalizer) {
		this.normalizer = normalizer;
	}
}
