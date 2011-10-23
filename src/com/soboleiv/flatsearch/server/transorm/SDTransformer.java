package com.soboleiv.flatsearch.server.transorm;

import static com.soboleiv.flatsearch.server.transorm.Field.ADDRESS;
import static com.soboleiv.flatsearch.server.transorm.Field.COST;
import static com.soboleiv.flatsearch.server.transorm.Field.DATE;
import static com.soboleiv.flatsearch.server.transorm.Field.ROOMS;
import static com.soboleiv.flatsearch.server.transorm.Field.URL;

public class SDTransformer extends RowBasedTransformer{
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
	public SDTransformer() {
		super(DATA_REGEXP, ADDRESS,ROOMS,COST,URL,DATE);
	}
	
	private static String skip(int maxSkip) {
		return ".{0,"+maxSkip+"}";
	}
}
