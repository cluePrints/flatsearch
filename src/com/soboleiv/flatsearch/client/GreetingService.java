package com.soboleiv.flatsearch.client;

import java.util.Collection;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.soboleiv.flatsearch.shared.Place;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
	Collection<Place> greetServer(String name) throws IllegalArgumentException;
}
