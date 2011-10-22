package com.soboleiv.flatsearch.server.crawler;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class Crawler {
	private RegexpDataMapper<String> linksToFollowRegexp;

	private List<String> pagesToVisit = Lists.newLinkedList();
	private Set<String> visited = Sets.newHashSet();
	private List<CrawledResult> data = Lists.newLinkedList();
	
	private UrlReader urlReader = new UrlReader();
	
	private int maxHits = Integer.MAX_VALUE;

	public Crawler(String linksToFollowRegexp,
			String startingPage) {
		super();
		this.linksToFollowRegexp = new ToStringRegexpMapper(linksToFollowRegexp);
		this.pagesToVisit.add(startingPage);
	}

	public void start() {
		do {
			processPage(pagesToVisit.get(0));
		} while (!pagesToVisit.isEmpty() && visited.size() < maxHits);
		logDataCaptured();
	}

	public void logDataCaptured() {
		for (CrawledResult dataPoint : data) {
			System.out.println(dataPoint);
		}
	}
	
	public void setMaxHits(int maxVisits) {
		this.maxHits = maxVisits;
	}
	
	public Set<String> getVisited() {
		return visited;
	}
	
	public List<CrawledResult> getData() {
		return data;
	}

	private void processPage(String url) {
		if (!visited.contains(url)) {
			System.out.println("Processing: " + url);
			String content = urlReader.readUrlContent(url);

			List<String> linksToFollow = linksToFollowRegexp.parseData(content);
			pagesToVisit.addAll(linksToFollow);
			System.out.println("Added " + linksToFollow.size() + " links to follow.");

			data.add(new CrawledResult(content, url));
			visited.add(url);

			System.out.println("Progress: " + visited.size() + " done, "
					+ pagesToVisit.size() + " left. ");
		}

		pagesToVisit.remove(url);
	}
}
