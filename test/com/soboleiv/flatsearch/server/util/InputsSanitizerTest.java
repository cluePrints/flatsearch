package com.soboleiv.flatsearch.server.util;

import org.junit.Assert;
import org.junit.Test;

public class InputsSanitizerTest {
	@Test
	public void shouldCleanHtmlTags()
	{
		String result = new InputsSanitizer().sanitize("<html>");
		Assert.assertFalse(result.contains("<html>"));
	}
}
