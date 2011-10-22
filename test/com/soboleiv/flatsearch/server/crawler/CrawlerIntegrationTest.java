package com.soboleiv.flatsearch.server.crawler;

import java.net.URLDecoder;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.soboleiv.flatsearch.server.GreetingServiceImpl;
import com.soboleiv.flatsearch.server.crawler.Crawler;

public class CrawlerIntegrationTest {
	private static Logger log = LoggerFactory.getLogger(Crawler.class);
	
	@Test
	public void test() throws Exception
	{
		log.info(URLDecoder.decode("addSearch%5Bdt_id%5D=10&addSearch%5Bd_id%5D=0&addSearch%5Bt_id%5D=0&addSearch%5Bprice_min%5D=&addSearch%5Bprice_max%5D=&addSearch%5Bcurrency%5D=usd&addSearch%5BpriceFor%5D=0&addSearch%5Bfields%5D%5B384%5D%5Bmin%5D=0&addSearch%5Bfields%5D%5B384%5D%5Bmax%5D=0&addSearch%5Bfields%5D%5B388%5D%5Bmin%5D=&addSearch%5Bfields%5D%5B388%5D%5Bmax%5D=&addSearch%5Bfields%5D%5B390%5D%5Bmin%5D=&addSearch%5Bfields%5D%5B390%5D%5Bmax%5D=&addSearch%5Bphone%5D=&addSearch%5Bdays%5D=&fsbmtfReg=%CF%EE%E8%F1%EA", "UTF-8"));
		Crawler crawler = new Crawler(GreetingServiceImpl.LINKS_REGEXP, GreetingServiceImpl.START_PAGE);
		crawler.setMaxHits(3);
		crawler.start();		
		Assert.assertEquals(3,crawler.getVisited().size());
		Assert.assertEquals(3, crawler.getData().size());
	}
}


