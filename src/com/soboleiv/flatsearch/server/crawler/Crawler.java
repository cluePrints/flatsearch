package com.soboleiv.flatsearch.server.crawler;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * As crawler is parsing 1 site at a time, it's single-threaded due to perverted sense of nettiquete.  
 */
public class Crawler {
	private static Logger log = LoggerFactory.getLogger(Crawler.class);
	
	RegexpDataMapper<String> linksToFollowRegexp;

	List<String> pagesToVisit;
	Set<String> visited;
	List<CrawledResult> data = Lists.newLinkedList();
	
	UrlReader urlReader = new UrlReader();
	UrlNormalizer normalizer = UrlNormalizer.NONE;
	
	private int maxHits = Integer.MAX_VALUE;
	String startingPage;
	
	Predicate<String> stopCondition = new Predicate<String>() {
		public boolean apply(String justVisitedPage) {
			return visited.size() >= maxHits;
		}
	};

	public Crawler(String linksToFollowRegexp,
			String startingPage) {
		super();
		this.linksToFollowRegexp = new ToStringRegexpMapper(linksToFollowRegexp);
		this.startingPage = startingPage;
	}

	public void start() {
		reset();		
		boolean timeToStop;
		do {
			/* TODO: start from the newest if possible and move to latest
			 * spotting already visited DATA(not page) could be good signal to stop
			 * and we don't know about data at this stage, but stop condition could :)
			 **/
			String visited = crawlPageAndMarkVisited();
			timeToStop = stopCondition.apply(visited);
		} while (!pagesToVisit.isEmpty() && !timeToStop);
	}

	String crawlPageAndMarkVisited() {
		String page = pagesToVisit.get(0);
		
		crawlPage(page);
		
		pagesToVisit.remove(0);
		visited.add(page);
		
		return page;
	}

	void reset() {
		pagesToVisit = Lists.newLinkedList();
		pagesToVisit.add(startingPage);
		visited = Sets.newHashSet();
	}

	public void setStopCondition(Predicate<String> stopCondition) {
		this.stopCondition = stopCondition;
	}
	
	public List<CrawledResult> getData() {
		return data;
	}

	void crawlPage(String url) {
		if (!visited.contains(url)) {
			log.debug("Processing: {}", url);
			String content = urlReader.readUrlContent(url);

			extractLinksToFollow(content);

			data.add(new CrawledResult(content, url));

			log.debug("Progress: {} done, {} left. ", visited.size(), pagesToVisit.size());
		}
	}

	void extractLinksToFollow(String content) {
		List<String> linksToFollow = linksToFollowRegexp.parseData(content);
		for (String link : linksToFollow) {
			String normalizedUrl = normalizer.normalize(link);
			pagesToVisit.add(normalizedUrl);
		}
		log.debug("Added {} links to follow.", linksToFollow.size());
	}
	
	public void setNormalizer(UrlNormalizer normalizer) {
		this.normalizer = normalizer;
	}
}
