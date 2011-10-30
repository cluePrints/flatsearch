package com.soboleiv.flatsearch.server.crawler;

import com.google.common.base.Predicate;

public class StopConditions {
	public static Predicate<String> limitByMaxHits(final Crawler crawler, final int maxHits) {
		return new Predicate<String>() {
			public boolean apply(String justVisitedPage) {
				return crawler.visited.size() >= maxHits;
			}
		};
	}
}
