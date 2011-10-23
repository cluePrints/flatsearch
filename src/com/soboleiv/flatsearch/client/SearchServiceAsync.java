package com.soboleiv.flatsearch.client;

import java.util.Collection;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.soboleiv.flatsearch.shared.Place;
import com.soboleiv.flatsearch.shared.SearchRequest;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface SearchServiceAsync {
	void greetServer(SearchRequest input, AsyncCallback<Collection<Place>> callback)
			throws IllegalArgumentException;
}
