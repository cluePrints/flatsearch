package com.soboleiv.flatsearch.server.crawler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.soboleiv.flatsearch.server.crawler.CachedUrlReader.Interaction;
import com.soboleiv.flatsearch.server.db.DataStore;
import com.soboleiv.flatsearch.server.geo.LocationSvcFacade;
import com.soboleiv.flatsearch.server.geo.LocationSvcReader;
import com.soboleiv.flatsearch.server.transorm.Transformer;
import com.soboleiv.flatsearch.shared.Location;
import com.soboleiv.flatsearch.shared.Place;

public class CheckDataSourceCommand {
	private Logger log = LoggerFactory.getLogger(CheckDataSourceCommand.class);
	private LocationSvcFacade locSvc = createLocSvc();
	private DataStore<Place> store = DataStore.persistent();
	
	private Crawler crawler;
	private Transformer transformer;
	
	public CheckDataSourceCommand(Crawler crawler, Transformer transformer) {
		super();
		this.crawler = crawler;
		this.transformer = transformer;
	}
	
	public void run() {
		crawler.start();
		List<CrawledResult> data = crawler.getData();
		List<Place> places = lookup(data);
		
		persist(places);
	}
	
	private List<Place> lookup(List<CrawledResult> data) {
		List<Place> places = Lists.newLinkedList();
		for (CrawledResult item : data){
			int idx = data.indexOf(item);
			log.info("{}",idx);
			
			convertAndAddIfValid(item, places);
		}
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

	private void persist(List<Place> places) {
		log.debug("Saving {} elements", places.size());
		for (Place place : places) {
			store.save(place);
		}
		log.debug("Saving done", places.size());
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


}
