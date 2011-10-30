package com.soboleiv.flatsearch.server.crawler;

import static org.easymock.EasyMock.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

@SuppressWarnings("unchecked")
public class CrawlerTest {
	Crawler crawler;
	
	@Before
	public void before() {
		crawler = new Crawler(null, null);
	}
	
	@Test
	public void shouldNotResetDataAsItCouldBeUsedAsAWayToPassDataOut() throws Exception
	{
		Object data = crawler.data;
		
		crawler.reset();
		
		Assert.assertSame(data, crawler.data);
	}
	
	@Test
	public void shouldResetVisitedPages() throws Exception
	{
		crawler.visited = Sets.newHashSet("aa");
		
		crawler.reset();
		
		Assert.assertSame(0, crawler.visited.size());
	}
	
	@Test
	public void shouldResetPagesToVisitQUeueToStartingOne() throws Exception
	{
		crawler.pagesToVisit = Lists.newArrayList("bb");
		crawler.startingPage = "ztart";
		
		crawler.reset();
		
		Assert.assertSame(1, crawler.pagesToVisit.size());
		Assert.assertEquals("ztart", crawler.pagesToVisit.iterator().next());
	}
	
	@Test
	public void shouldStopPageCrawlingOnExternalCondition() throws Exception
	{
		preparePartialCrawlerWithExpectedHitsOf(1);
		crawler.stopCondition = Predicates.alwaysTrue();
		
		crawler.start();
	}
	

	@Test
	public void shouldStopPageCrawlingOnEmptyVisitQueue() throws Exception
	{
		preparePartialCrawlerWithExpectedHitsOf(1);
		crawler.stopCondition = Predicates.alwaysFalse();
		
		crawler.start();
	}
	
	@Test
	public void shouldNotCrawlPageIfVisitedAlready()
	{
		crawler.visited = Sets.newHashSet("http://ya.ru");		
		crawler.urlReader = createMock(UrlReader.class);
		replay(crawler.urlReader);
		
		crawler.crawlPage("http://ya.ru");
		
		verify(crawler.urlReader);
	}
	
	@Test
	public void shouldCrawlPageAndAddToVisitedListIfNotVisitedAlready() throws Exception
	{
		crawler = new Crawler(null, null) {
			@Override
			void extractLinksToFollow(String url, String content) {
			}
		};
		
		crawler.pagesToVisit = Lists.newArrayList("http://not-visited.ru");
		crawler.visited = Sets.newHashSet();		
		UrlReader reader = createMock(UrlReader.class);
		expect(reader.readUrlContent("http://not-visited.ru")).andReturn("");
		replay(reader);
		
		crawler.urlReader = reader;
		crawler.crawlPage("http://not-visited.ru");
		
		verify(crawler.urlReader);
		Assert.assertEquals(0, crawler.visited.size());
		Assert.assertEquals(1, crawler.pagesToVisit.size());
	}
	
	@Test
	public void shouldNormalizeExtractedUrls() throws Exception {
		UrlNormalizer mockNormalizer = createMock(UrlNormalizer.class);
		expect(mockNormalizer.normalize("urlBefore")).andReturn("urlAfter");
		replay(mockNormalizer);
				
		RegexpDataMapper<String> linksToFollowRegexp = createMock(RegexpDataMapper.class);
		expect(linksToFollowRegexp.parseData("content")).andReturn(Lists.newArrayList("urlBefore"));
		replay(linksToFollowRegexp);
		
		crawler.setNormalizer(mockNormalizer);
		crawler.linksToFollowRegexp = linksToFollowRegexp;
		crawler.pagesToVisit = Lists.newLinkedList();
		crawler.stopCondition = Predicates.alwaysFalse();
		
		crawler.extractLinksToFollow("urlBefore", "content");
		
		Assert.assertEquals(1, crawler.pagesToVisit.size());
		Assert.assertEquals("urlAfter", crawler.pagesToVisit.get(0));
	}
	
	@Test
	public void shouldNotExtractLinksToFollowIfCrawlingConditionSignalsNotAllowed() {
		RegexpDataMapper<String> linksToFollowRegexp = createMock(RegexpDataMapper.class);
		expect(linksToFollowRegexp.parseData(isA(String.class))).andThrow(new AssertionError(""));
		replay(linksToFollowRegexp);
		
		crawler.linksToFollowRegexp = linksToFollowRegexp;
		crawler.stopCondition = Predicates.alwaysTrue();
		
		crawler.extractLinksToFollow("url", "content");		
	}
	
	@Test
	public void shouldRemoveCrawledPagesFromTheQueue() {
		crawler = new Crawler(null, null){
			@Override
			void crawlPage(String url) {
				// do nothing - mocked;
			}
		};
		
		crawler.visited = Sets.newHashSet("1", "2", "3");
		crawler.pagesToVisit = Lists.newArrayList("4", "5", "6");
		
		crawler.crawlPageAndMarkVisited();
		
		Assert.assertEquals(4, crawler.visited.size());
		Assert.assertEquals(2, crawler.pagesToVisit.size());
	}

	@Test
	public void shouldCrawlMultipleTimesIfNewPagesFound() {
		preparePartialCrawlerWithExpectedHitsAndPagesFound(5);
	
		crawler.setStopCondition(StopConditions.limitByMaxHits(crawler, 2));
		crawler.start();
	}
	
	@Test(expected=AssertionError.class)
	public void shouldCrawlMultipleTimesIfNewPagesFoundAndFailAsMockCrawlerWillThrowAnError() {
		preparePartialCrawlerWithExpectedHitsAndPagesFound(1);
	
		crawler.setStopCondition(StopConditions.limitByMaxHits(crawler, 50));
		crawler.start();
	}
	
	private void preparePartialCrawlerWithExpectedHitsOf(final int allowedCount) {
		preparePartialCrawlerWithExpectedHitsOf(allowedCount, false);
	}
	
	private void preparePartialCrawlerWithExpectedHitsAndPagesFound(final int allowedCount) {
		preparePartialCrawlerWithExpectedHitsOf(allowedCount, true);
	}
	
	private void preparePartialCrawlerWithExpectedHitsOf(final int allowedCount, final boolean simulatePagesFound) {
		crawler = new Crawler(null, null){
			int count;
			@Override
			String crawlPageAndMarkVisited() {
				Assert.assertTrue(allowedCount>=count++);							
				if (simulatePagesFound) {
					pagesToVisit.add(String.valueOf(count));
					String page = pagesToVisit.get(0);
					visited.add(page);
				}
				pagesToVisit.remove(0);
				return "";
			}
		};
	}
}
