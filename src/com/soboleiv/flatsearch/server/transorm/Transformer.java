package com.soboleiv.flatsearch.server.transorm;

import java.util.List;

import com.soboleiv.flatsearch.shared.Place;

public interface Transformer {
	List<Place> parse(String html); 
}
