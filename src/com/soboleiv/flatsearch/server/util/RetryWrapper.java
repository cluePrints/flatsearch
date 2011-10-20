package com.soboleiv.flatsearch.server.util;

import java.util.concurrent.Callable;

public class RetryWrapper {
	private int retriesAllowed = 1;
	
	public <V> V perform(Callable<V> call) {
		Exception latest = null;
		for (int i=0; i<retriesAllowed; i++) {
			try {
				return call.call();	
			} catch (Exception ex) {
				latest = ex;
			} 
		}
		throw new RuntimeException(latest);
	}
	
	public void setRetriesAllowed(int retriesAllowed) {
		this.retriesAllowed = retriesAllowed;
	}
}
