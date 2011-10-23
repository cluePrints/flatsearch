package com.soboleiv.flatsearch.shared;

import java.io.Serializable;

public class Interval<T extends Comparable<T>> implements Serializable {
	private T min;
	private T max;
	public Interval() {
	}
	public Interval(T min, T max) {
		super();
		this.min = min;
		this.max = max;
	}
	
	public static <T extends Comparable<T>> Interval<T> between(T min, T max) {
		return new Interval<T>(min, max);
	}
	
	public static <T extends Comparable<T>> Interval<T> after(T min) {
		return Interval.between(min, null);
	}
	
	public T getMin() {
		return min;
	}
	
	public T getMax() {
		return max;
	}
	
	public boolean minOpen() {
		return min == null;
	}
	
	public boolean maxOpen() {
		return max == null;
	}
}
