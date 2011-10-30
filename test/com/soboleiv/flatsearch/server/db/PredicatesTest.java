package com.soboleiv.flatsearch.server.db;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import com.soboleiv.flatsearch.shared.Interval;
import com.soboleiv.flatsearch.shared.Place;

public class PredicatesTest {
	@Test
	public void shouldEqPageItWasFetchedFrom() {
		Place place = new Place();
		place.setWasFoundAt("aaa");
		boolean res = Predicates.wasFoundAt("aaa").appliesTo(place);
		Assert.assertTrue(res);
	}
	
	@Test
	public void shouldNeqPageItWasFetchedFromIfItIsDifferent() {
		Place place = new Place();
		place.setWasFoundAt("aaa");
		boolean res = Predicates.wasFoundAt("aab").appliesTo(place);
		Assert.assertFalse(res);
	}
	
	@Test
	public void shouldNeqToEverythingIfIsNull() {
		Place place = new Place();
		place.setWasFoundAt("aaa");
		boolean res = Predicates.wasFoundAt(null).appliesTo(place);
		Assert.assertFalse(res);
		
		place.setWasFoundAt(null);
		res = Predicates.wasFoundAt(null).appliesTo(place);
		Assert.assertFalse(res);
	}
	
	
	@Test
	public void shouldSurviveNotInitedClassInSqlLikeBehaviour() {
		Place place = new Place();
		place.setWasFetchedAt(new Date());
		boolean res = Predicates.fetchTime(null).appliesTo(place);
		Assert.assertFalse(res);
	}
	
	@Test
	public void shouldTreatNullBoundariesAsNotExisting() {
		Place place = new Place();
		place.setWasFetchedAt(new Date());
		boolean res = Predicates.fetchTime(Interval.between((Date) null, null)).appliesTo(place);
		Assert.assertTrue(res);
	}
	
	@Test
	public void shouldMatchNullValueOnlyIfBothBoundariesAreOpen() {
		Place place = new Place();
		place.setWasFetchedAt(null);
		Date twoMinsAgo = new Date(System.currentTimeMillis() - 1000 * 60 *2);
		Date twoMinsFwd = new Date(System.currentTimeMillis() + 1000 * 60 *2);

		boolean res = Predicates.fetchTime(Interval.between(twoMinsAgo, twoMinsFwd)).appliesTo(place);
		Assert.assertFalse(res);
		
		res = Predicates.fetchTime(Interval.between(null, twoMinsFwd)).appliesTo(place);
		Assert.assertFalse(res);
		
		res = Predicates.fetchTime(Interval.between(twoMinsAgo, null)).appliesTo(place);
		Assert.assertFalse(res);

		res = Predicates.fetchTime(Interval.between((Date) null, null)).appliesTo(place);
		Assert.assertTrue(res);
	}
	
	@Test
	public void shouldMatchRightOpenConstraint() {
		Place place = new Place();
		place.setWasFetchedAt(new Date());
		Date twoMinsAgo = new Date(System.currentTimeMillis() - 1000 * 60 *2);
		boolean res = Predicates.fetchTime(Interval.after(twoMinsAgo)).appliesTo(place);
		Assert.assertTrue(res);
	}
	
	@Test
	public void shouldMatchClosedConstraints() {
		Place place = new Place();
		Date now = new Date();
		place.setWasFetchedAt(now);
		Date twoMinsAgo = new Date(System.currentTimeMillis() - 1000 * 60 *2);
		Date twoMinsFwd = new Date(System.currentTimeMillis() + 1000 * 60 *2);

		boolean res = Predicates.fetchTime(Interval.between(twoMinsAgo, twoMinsFwd)).appliesTo(place);
		Assert.assertTrue(res);
		
		place.setWasFetchedAt(twoMinsFwd);
		res = Predicates.fetchTime(Interval.between(twoMinsAgo, now)).appliesTo(place);
		Assert.assertFalse(res);
	}
	
	@Test
	public void shouldMatchLeftOpenConstraint() {
		Place place = new Place();
		Date now = new Date();
		place.setWasFetchedAt(now);
		Date twoMinsFwd = new Date(System.currentTimeMillis() + 1000 * 60 *2);

		boolean res = Predicates.fetchTime(Interval.before(twoMinsFwd)).appliesTo(place);
		Assert.assertTrue(res);

		place.setWasFetchedAt(twoMinsFwd);
		res = Predicates.fetchTime(Interval.before(now)).appliesTo(place);
		Assert.assertFalse(res);
	}

	
}
