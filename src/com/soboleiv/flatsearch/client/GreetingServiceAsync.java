package com.soboleiv.flatsearch.client;

import java.util.Collection;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.soboleiv.flatsearch.shared.Place;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	void greetServer(String input, AsyncCallback<Collection<Place>> callback)
			throws IllegalArgumentException;
}
