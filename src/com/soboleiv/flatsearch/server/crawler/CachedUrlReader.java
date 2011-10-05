package com.soboleiv.flatsearch.server.crawler;

import java.util.Date;

import org.joda.time.DateTime;

import com.soboleiv.flatsearch.server.db.DataStore;

public class CachedUrlReader extends UrlReader {
	private UrlReader urlReader;
	private DataStore<Interaction> store = new DataStore<Interaction>();
	private int expiryDays = Integer.MAX_VALUE;
	
	public CachedUrlReader(UrlReader urlReader) {
		super();
		this.urlReader = urlReader;
	}

	public String readUrlContent(String address) {
		Interaction result = getFromCache(address);
		if (result == null) {
			String content = urlReader.readUrlContent(address);
			result = Interaction.stamped()
						.setAddress(address)
						.setResponse(content);
			store.save(result);
		}
		return result.getResponse();
	}
	
	public void setExpiryDays(int expiryDays) {
		this.expiryDays = expiryDays;
	}

	private Interaction getFromCache(String address) {
		Interaction ex = Interaction.example().setAddress(address);
		Interaction result = store.getByExample(ex);
		if (result != null) {
			if (isExpired(result)) {;
				result = null;
			}
		}
		return result;
	}

	private boolean isExpired(Interaction result) {
		if (result.getDate() == null)
			return true;
		return result.getDate().plusDays(expiryDays).isBeforeNow();
	}
}

class Interaction {
	private String address;
	private String response;
	private Date date;

	public Interaction setAddress(String address) {
		this.address = address;
		return this;
	}
	
	public Interaction setResponse(String response) {
		this.response = response;
		return this;
	}

	public static Interaction example() {
		return new Interaction();
	}

	public static Interaction stamped() {
		Interaction res = new Interaction();
		res.date = new Date();
		return res;
	}

	public String getAddress() {
		return address;
	}

	public String getResponse() {
		return response;
	}
	
	public DateTime getDate() {
		if (date == null)
			return null;
		return new DateTime(date.getTime());
	}
}