package com.soboleiv.flatsearch.server.db;

import java.util.Date;

import com.db4o.query.Predicate;
import com.google.common.base.Function;
import com.soboleiv.flatsearch.shared.Interval;
import com.soboleiv.flatsearch.shared.Place;
/**
 * TODO: I bet existence of this class is a good reason to use some scala on the serverside.
 */
public class Predicates {
	public static Predicate<Place> ALL = new Predicate<Place>() {
		public boolean match(Place arg0) {
			return true;
		}
	};
	
	public static Predicate<Place> NEVER = new Predicate<Place>() {
		public boolean match(Place arg0) {
			return false;
		}
	};


	public static Predicate<Place> fetchTime(Interval<Date> interval) {
		return bounded(GET_FETCH_TIME, interval);
	}
	
	public static Predicate<Place> price(Interval<Integer> interval) {
		return bounded(GET_PRICE, interval);
	}
	
	public static Predicate<Place> wasFoundAt(String url) {
		return eq(GET_FOUND_AT, url);
	}
	
	public static <T> Predicate<Place> eq(final Function<Place, T> field, final T exactValue) {
		if (exactValue == null)
			return NEVER;

		return new Predicate<Place>() {
			@Override
			public boolean match(Place place) {
				return exactValue.equals(field.apply(place));
			}
		};
	}
	
	public static <T extends Comparable<T>> Predicate<Place> bounded(final Function<Place, T> field, final Interval<T> interval) {
		if (interval == null)
			return NEVER;
		
		return new Predicate<Place>() {
			@Override
			public boolean match(Place arg0) {
				T val = field.apply(arg0);
				if (val == null) {
					if (interval.minOpen() && interval.maxOpen()) {
						return true;
					} else {
						return false;
					}
				}
					
				if (!interval.minOpen()) {
					if (compare(interval.getMin(), val) == 1) {
						return false;
					}
				}
				if (!interval.maxOpen()) {
					if (compare(interval.getMax(), val) == -1) {
						return false;
					}
				}
				return true;
			}
		};

	}

	private static final Function<Place, Date> GET_FETCH_TIME = new Function<Place, Date>() {
		public Date apply(Place arg0) {
			return arg0.getWasFetchedAt();
		}
	};
	
	private static final Function<Place, Integer> GET_PRICE = new Function<Place, Integer>() {
		public Integer apply(Place arg0) {
			return arg0.getPriceUsd();
		}
	};
	
	private static final Function<Place, String> GET_FOUND_AT = new Function<Place, String>() {
		public String apply(Place arg0) {
			return arg0.getWasFoundAt();
		}
	};
	
	
	private static <T extends Comparable<T>> int compare(T a, T b) {
		if (a == null || b == null)
			return 999;
		
		return Integer.signum(a.compareTo(b));
	}
}
