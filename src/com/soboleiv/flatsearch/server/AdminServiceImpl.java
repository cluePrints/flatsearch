package com.soboleiv.flatsearch.server;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.soboleiv.flatsearch.client.SearchService;
import com.soboleiv.flatsearch.client.admin.AdminService;
import com.soboleiv.flatsearch.server.crawler.CachedUrlReader;
import com.soboleiv.flatsearch.server.crawler.CrawledResult;
import com.soboleiv.flatsearch.server.crawler.Crawler;
import com.soboleiv.flatsearch.server.crawler.UrlReader;
import com.soboleiv.flatsearch.server.crawler.CachedUrlReader.Interaction;
import com.soboleiv.flatsearch.server.db.DataStore;
import com.soboleiv.flatsearch.server.geo.LocationSvcFacade;
import com.soboleiv.flatsearch.server.geo.LocationSvcReader;
import com.soboleiv.flatsearch.server.transorm.SDTransformer;
import com.soboleiv.flatsearch.shared.AdminResponse;
import com.soboleiv.flatsearch.shared.Location;
import com.soboleiv.flatsearch.shared.Place;

public class AdminServiceImpl extends RemoteServiceServlet implements AdminService{
	private LocationSvcFacade locSvc = createLocSvc();
	private SDTransformer transformer = new SDTransformer();
	
	private Logger log = LoggerFactory.getLogger(AdminServiceImpl.class);
	
	private DataStore<Place> store = DataStore.persistent();

	public static final String LINKS_REGEXP = "href=\"(.{0,50}ru/offer/ad/.{0,50}/kiev/page.{0,50})\">";
	public static final String START_PAGE = "http://www.svdevelopment.com/ru/offer/ad/10/kiev/page/11/";

	
	public AdminResponse checkDataSources() {
		Crawler c = new Crawler(LINKS_REGEXP, START_PAGE);
		c.setMaxHits(2);
		c.start();
		
		List<CrawledResult> data = c.getData();
		List<Place> places = lookup(data);
		
		persist(places);

		return new AdminResponse();
	}

	private void persist(List<Place> places) {
		log.debug("Saving {} elements", places.size());
		for (Place place : places) {
			store.save(place);
		}
		log.debug("Saving done", places.size());
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

}
