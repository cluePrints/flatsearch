package com.soboleiv.flatsearch.server.util;

import java.util.concurrent.Callable;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.soboleiv.flatsearch.server.util.RetryWrapper;

public class RetryWrapperTest {
	private RetryWrapper unit;

	@Test
	public void shouldRetryANumberOfTimes() {
		Callable<String> call = throwTimes(4);
		unit.setRetriesAllowed(5);

		String result = unit.perform(call);

		Assert.assertEquals(result, "result");
	}

	@Test(expected = RuntimeException.class)
	public void shouldThrowExceptionWhenRetriesExceeded() {
		Callable<String> call = throwTimes(5);
		unit.setRetriesAllowed(5);

		unit.perform(call);
	}

	@Test(expected = RuntimeException.class)
	public void shouldNotRetryByDefault() {
		Callable<String> call = throwTimes(1);

		unit.perform(call);
	}
	
	@Test
	public void shouldWorkOkIfNoExceptionsThrown() {
		Callable<String> call = throwTimes(0);

		unit.perform(call);
	}


	@Before
	public void before() {
		unit = new RetryWrapper();
	}

	private Callable<String> throwTimes(final int throwTimes) {
		Callable<String> call = new Callable<String>() {
			int i;

			public String call() throws Exception {
				if (i++ < throwTimes) {
					throw new RuntimeException();
				}
				return "result";
			}
		};
		return call;
	}
}