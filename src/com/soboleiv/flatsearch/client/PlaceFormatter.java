package com.soboleiv.flatsearch.client;

import com.soboleiv.flatsearch.shared.Place;

public class PlaceFormatter {
	public String format(Place place) {
		String result = "<table>";
		result = addRow(result, "Address", place.getAddress());
		result = addRow(result, "Price", place.getPrice());
		result = addRow(result, "Rooms", place.getRoomsNumber());
		result = addRow(result, "Posted on", place.getPostingDate());
		result = addRow(result, "Original", getUrl(place));
		result += "</table>";
		return result;
	}

	private String getUrl(Place place) {
		return "<a href=\""+place.getOriginalUrl()+"\" target=\"_blank\">more</a>";
	}
	
	private String addRow(String result, String caption, Object val) {
		return result + "<tr><td><b>"+caption+":</b></td><td>"+val+"</td></tr>";
	}
}
