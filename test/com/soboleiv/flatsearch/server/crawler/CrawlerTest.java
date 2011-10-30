package com.soboleiv.flatsearch.server.crawler;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

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
		crawler.urlReader = EasyMock.createMock(UrlReader.class);
		EasyMock.replay(crawler.urlReader);
		
		crawler.crawlPage("http://ya.ru");
		
		EasyMock.verify(crawler.urlReader);
	}
	
	@Test
	public void shouldCrawlPageAndAddToVisitedListIfNotVisitedAlready() throws Exception
	{
		crawler = new Crawler(null, null) {
			@Override
			void extractLinksToFollow(String content) {
			}
		};
		
		crawler.pagesToVisit = Lists.newArrayList("http://not-visited.ru");
		crawler.visited = Sets.newHashSet();		
		UrlReader reader = EasyMock.createMock(UrlReader.class);
		EasyMock.expect(reader.readUrlContent("http://not-visited.ru")).andReturn("");
		EasyMock.replay(reader);
		
		crawler.urlReader = reader;
		crawler.crawlPage("http://not-visited.ru");
		
		EasyMock.verify(crawler.urlReader);
		Assert.assertEquals(0, crawler.visited.size());
		Assert.assertEquals(1, crawler.pagesToVisit.size());
	}
	
	@Test
	public void shouldNormalizeExtractedUrls() throws Exception {
		UrlNormalizer mockNormalizer = EasyMock.createMock(UrlNormalizer.class);
		EasyMock.expect(mockNormalizer.normalize("urlBefore")).andReturn("urlAfter");
		EasyMock.replay(mockNormalizer);
		
		RegexpDataMapper<String> linksToFollowRegexp = EasyMock.createMock(RegexpDataMapper.class);
		EasyMock.expect(linksToFollowRegexp.parseData("content")).andReturn(Lists.newArrayList("urlBefore"));
		EasyMock.replay(linksToFollowRegexp);
		
		crawler.setNormalizer(mockNormalizer);
		crawler.linksToFollowRegexp = linksToFollowRegexp;
		crawler.pagesToVisit = Lists.newLinkedList();
		
		crawler.extractLinksToFollow("content");
		
		Assert.assertEquals(1, crawler.pagesToVisit.size());
		Assert.assertEquals("urlAfter", crawler.pagesToVisit.get(0));
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
