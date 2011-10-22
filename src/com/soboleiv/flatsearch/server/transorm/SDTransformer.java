package com.soboleiv.flatsearch.server.transorm;

import java.util.List;

import com.google.common.collect.Lists;
import com.soboleiv.flatsearch.server.crawler.ToArrayRegexpMapper;
import com.soboleiv.flatsearch.shared.Place;

public class SDTransformer implements Transformer{
	public static final String ROOMS_REGEXP = "<td class=\"\\w+\"\\s+nowrap>(\\d+)</td>";
	public static final String COST_REGEXP = "<td class=\"acenter\"\\s+nowrap>([0-9\\s]{3,})\\s+/\\s+";
	public static final String STREET_REGEXP = "кий</td>\\W+<td   nowrap>(.{0,400})</td>";
	public static final String URL_REGEXP = "<a href=\"(.{0,100})\" target=\"_blank\">подробнее</a>";	
	public static final String DATE_POSTED_REGEXP = "<td>([0-9.]+)</td>\\s+</tr>\\s+<tr id=\"item";
	
	public static final String DATA_REGEXP = STREET_REGEXP + skip(500)
											+ ROOMS_REGEXP+ skip(800)
											+ COST_REGEXP +skip(500)
											+ URL_REGEXP + skip(100)
											+ DATE_POSTED_REGEXP;

	private ToArrayRegexpMapper dataMapper = new ToArrayRegexpMapper(DATA_REGEXP);
	
	private int ADDRESS = 0;
	private int ROOMS = 1;
	private int COST = 2;
	private int URL = 3;
	private int DATE = 4;
	
	public List<Place> parse(String html) {
		List<String[]> datas = dataMapper.parseData(html);
		
		List<Place> results = Lists.newLinkedList();
		for (String[] data : datas) {
			String address = data[ADDRESS];
			String url = data[URL];
			String date = data[DATE];
			String roomsStr = data[ROOMS];
			String costStr = data[COST];
			
			int rooms = intFromStr(roomsStr);
			int cost = intFromStr(costStr);
			
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

	private int intFromStr(String roomsStr) {
		try {
			roomsStr = roomsStr.replaceAll("\\s+", "");
			return Integer.parseInt(roomsStr);
		} catch (NumberFormatException ex) {
			return -1;
		}
	}
	
	private static String skip(int maxSkip) {
		return ".{0,"+maxSkip+"}";
	}
}
