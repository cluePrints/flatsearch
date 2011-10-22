package com.soboleiv.flatsearch.server.transorm;

import java.util.List;

import com.google.common.collect.Lists;
import com.soboleiv.flatsearch.server.crawler.ToArrayRegexpMapper;
import com.soboleiv.flatsearch.shared.Place;

public class SDTransformer implements Transformer{
	public static final String STREET_REGEXP = "кий</td>\\W+<td   nowrap>(.{0,400})</td>";
	public static final String URL_REGEXP = "<a href=\"(.{0,100})\" target=\"_blank\">подробнее</a>";	
	public static final String DATE_POSTED_REGEXP = "<td>([0-9.]+)</td>\\s+</tr>\\s+<tr id=\"item";
	
	public static final String DATA_REGEXP = STREET_REGEXP + ".{0,800}" + URL_REGEXP + ".{0,100}" + DATE_POSTED_REGEXP;

	private ToArrayRegexpMapper dataMapper = new ToArrayRegexpMapper(DATA_REGEXP);
	
	private int ADDRESS = 0;
	private int URL = 1;
	private int DATE = 2;
	
	public List<Place> parse(String html) {
		List<String[]> datas = dataMapper.parseData(html);
		
		List<Place> results = Lists.newLinkedList();
		for (String[] data : datas) {
			String address = data[ADDRESS];
			String url = data[URL];
			String date = data[DATE];
			
			Place result = new Place();
			result.setOriginalUrl(url);
			result.setAddress(address);
			result.setPostingDate(date);
			results.add(result);
		}
		return results;
	}
}
