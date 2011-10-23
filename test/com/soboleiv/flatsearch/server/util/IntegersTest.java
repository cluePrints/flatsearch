package com.soboleiv.flatsearch.server.util;

import org.junit.Assert;
import org.junit.Test;

public class IntegersTest {
	@Test
	public void shouldReturnMinusOneIfNoDigitsAtAll() {
		Assert.assertEquals(-1, Integers.fromString("a#E#"));
		Assert.assertEquals(-1, Integers.fromString(""));
		Assert.assertEquals(-1, Integers.fromString(null));
	}
	
	@Test
	public void shouldParseNormalInt() {
		Assert.assertEquals(1390, Integers.fromString("1390"));
	}
	
	@Test
	public void shouldStripNonDigitChars() {
		Assert.assertEquals(982, Integers.fromString("0a9 .82"));
	}
}
