package com.soboleiv.flatsearch.client;

import com.soboleiv.flatsearch.shared.Place;

public class PlaceFormatter {
	public String format(Place place) {
		String result = "<table>";
		result = addRow(result, "Адресс", place.getAddress());
		result = addRow(result, "Цена, $", place.getPriceUsd());
		result = addRow(result, "Комнат", place.getRoomsNumber());
		result = addRow(result, "Давность", place.getPostingDate());
		result = addRow(result, "Подробности", asHtmlReference(place.getOriginalUrl()));
		result = addRow(result, "Найдено", asHtmlReference(place.getWasFoundAt()));
		result += "</table>";
		return result;
	}

	private String asHtmlReference(String url) {
		return "<a href=\""+url+"\" target=\"_blank\">здесь</a>";
	}
	
	private String addRow(String result, String caption, Object val) {
		return result + "<tr><td><b>"+caption+":</b></td><td>"+val+"</td></tr>";
	}
}
