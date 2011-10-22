package com.soboleiv.flatsearch.server;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.soboleiv.flatsearch.client.GreetingService;
import com.soboleiv.flatsearch.server.crawler.CachedUrlReader;
import com.soboleiv.flatsearch.server.crawler.CachedUrlReader.Interaction;
import com.soboleiv.flatsearch.server.crawler.CrawledResult;
import com.soboleiv.flatsearch.server.crawler.Crawler;
import com.soboleiv.flatsearch.server.crawler.UrlReader;
import com.soboleiv.flatsearch.server.db.DataStore;
import com.soboleiv.flatsearch.server.geo.LocationSvcFacade;
import com.soboleiv.flatsearch.server.geo.LocationSvcReader;
import com.soboleiv.flatsearch.server.transorm.SDTransformer;
import com.soboleiv.flatsearch.shared.FieldVerifier;
import com.soboleiv.flatsearch.shared.Location;
import com.soboleiv.flatsearch.shared.Place;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
		GreetingService {

	public static final String LINKS_REGEXP = "href=\"(.{0,50}ru/offer/ad/.{0,50}/kiev/page.{0,50})\">";
	public static final String START_PAGE = "http://www.svdevelopment.com/ru/offer/ad/10/kiev/page/11/";
	
	private LocationSvcFacade locSvc = createLocSvc();
	private SDTransformer transformer = new SDTransformer();
	
	private Logger log = LoggerFactory.getLogger(GreetingServiceImpl.class);
	
	public Collection<Place> greetServer(String input) throws IllegalArgumentException {
		// Verify that the input is valid. 
		if (!FieldVerifier.isValidName(input)) {
			// If the input is not valid, throw an IllegalArgumentException back to
			// the client.
			throw new IllegalArgumentException(
					"Name must be at least 4 characters long");
		}

		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");

		// Escape data from the client to avoid cross-site script vulnerabilities.
		input = escapeHtml(input);
		userAgent = escapeHtml(userAgent);
		
		Crawler c = new Crawler(LINKS_REGEXP, START_PAGE);
		c.setMaxHits(2);
		c.start();
		
		List<CrawledResult> data = c.getData();
		List<Place> places = lookup(data);

		return places;
	}

	private LocationSvcFacade createLocSvc() {
		LocationSvcFacade locSvc = new LocationSvcFacade();
		CachedUrlReader urlReader = new CachedUrlReader(new UrlReader());
		DataStore<Interaction> db = DataStore.persistent();
		urlReader.setStore(db);
		LocationSvcReader reader = new LocationSvcReader();
		reader.setUrlReader(urlReader);
		locSvc.setReader(reader);
		return locSvc;
	}

	public List<Place> lookup(List<CrawledResult> data) {
		List<Place> places = Lists.newLinkedList();
		for (CrawledResult item : data){
			int idx = data.indexOf(item);
			log.info("{}",idx);
			
			convertAndAddIfValid(item, places);
		}
		return places;
	}

	private void convertAndAddIfValid(CrawledResult item, List<Place> places) {
		try {
			String html = item.getData();
			List<Place> results = transformer.parse(html);
			for (Place place : results) {
				String address = place.getAddress();
				Location location = locSvc.get(address);
				
				if (location == Location.INVALID)
					return;
				
				place.setCoordinates(location);
				places.add(place);
			}
		} catch (Exception ex) {
			log.error("We've got a problem", ex);
		}
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
