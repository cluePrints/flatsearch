package com.soboleiv.flatsearch.server;

import java.util.Date;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.db4o.query.Predicate;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.soboleiv.flatsearch.client.SearchService;
import com.soboleiv.flatsearch.server.db.DataStore;
import com.soboleiv.flatsearch.server.db.Predicates;
import com.soboleiv.flatsearch.shared.Interval;
import com.soboleiv.flatsearch.shared.Place;
import com.soboleiv.flatsearch.shared.SearchRequest;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class SearchServiceImpl extends RemoteServiceServlet implements
		SearchService {
	private Logger log = LoggerFactory.getLogger(SearchServiceImpl.class);	
	private DataStore<Place> store = DataStore.persistent();
	
	public Collection<Place> greetServer(final SearchRequest input) throws IllegalArgumentException {
		/*String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");

		// Escape data from the client to avoid cross-site script vulnerabilities.
		input = escapeHtml(input);
		userAgent = escapeHtml(userAgent);*/
		log.debug("Called with {}", input);
		Collection<Place> results = store.getBy(Predicates.fetchTime(input.getFetchTime()));
		log.debug("Returning {} results", results.size());
		return results;
	}
	
	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}
}
