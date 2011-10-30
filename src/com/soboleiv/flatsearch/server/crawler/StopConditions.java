package com.soboleiv.flatsearch.server.crawler;

import java.util.Collection;

import com.google.common.base.Predicate;
import com.soboleiv.flatsearch.server.db.DataStore;
import com.soboleiv.flatsearch.server.db.Predicates;
import com.soboleiv.flatsearch.shared.Place;

public class StopConditions {
	public static Predicate<String> limitByMaxHits(final Crawler crawler, final int maxHits) {
		return new Predicate<String>() {
			public boolean apply(String justVisitedPage) {
				return crawler.visited.size() >= maxHits;
			}
		};
	}
	
	public static Predicate<String> pageWasVisited() {
		return new PageVisitedCondition();
	}
}

class PageVisitedCondition implements Predicate<String> {
	DataStore<Place> db;
	public boolean apply(String url) {
		int results = db.count(Predicates.wasFoundAt(url));
		return false;
	}
}
