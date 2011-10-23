package com.soboleiv.flatsearch.shared;

import java.io.Serializable;
import java.util.Date;

public class SearchRequest implements Serializable {
	private Interval<Date> fetchTime;
	public Interval<Date> getFetchTime() {
		return fetchTime;
	}	
	public void setFetchTime(Interval<Date> fetchTime) {
		this.fetchTime = fetchTime;
	}
}
