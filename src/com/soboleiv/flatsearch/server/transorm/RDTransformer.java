package com.soboleiv.flatsearch.server.transorm;

import static com.soboleiv.flatsearch.server.transorm.Field.ADDRESS;
import static com.soboleiv.flatsearch.server.transorm.Field.COST;
import static com.soboleiv.flatsearch.server.transorm.Field.DATE;
import static com.soboleiv.flatsearch.server.transorm.Field.ROOMS;
import static com.soboleiv.flatsearch.server.transorm.Field.URL;

import com.soboleiv.flatsearch.server.crawler.BaseUrlNormalizer;

public class RDTransformer extends RowBasedTransformer{
	public static final String ROOMS_REGEXP = "Количество комнат:</td><td class=\"ivnFullDataValue_td\">(\\d+)</td></tr>";
	public static final String COST_REGEXP = "Цена</span>:</td><td class=\"\\w+\"><span style=\".{0,100}\">(.{0,8})у.е.</span></td></tr>";
	public static final String STREET_REGEXP = "<td class=\"ivnFullDataText_td\">Улица:</td><td class=\"ivnFullDataValue_td\">(.{0,50})</td>";
	public static final String URL_REGEXP = "<a href=\"(.{0,100})\">подробнее</a>";	
	//public static final String DATE_POSTED_REGEXP = "<td>([0-9.]+)</td>\\s+</tr>\\s+<tr id=\"item";
	
	public static final String DATA_REGEXP = STREET_REGEXP + skip(500)
											+ ROOMS_REGEXP+ skip(800)
											+ COST_REGEXP +skip(500)
											+ URL_REGEXP /*+ skip(100)
											+ DATE_POSTED_REGEXP*/;
	public RDTransformer() {
		super(DATA_REGEXP, ADDRESS,ROOMS,COST,URL,DATE);
		setNormalizer(new BaseUrlNormalizer("http://www.realdruzi.com.ua"));
	}
	
	private static String skip(int maxSkip) {
		return ".{0,"+maxSkip+"}";
	}
}
