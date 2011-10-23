package com.soboleiv.flatsearch.client;

import java.util.Collection;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.soboleiv.flatsearch.shared.Place;
import com.soboleiv.flatsearch.shared.SearchRequest;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface SearchService extends RemoteService {
	Collection<Place> greetServer(SearchRequest request) throws IllegalArgumentException;
}
