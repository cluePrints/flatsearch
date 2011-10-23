package com.soboleiv.flatsearch.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.soboleiv.flatsearch.client.admin.AdminService;
import com.soboleiv.flatsearch.server.crawler.BaseUrlNormalizer;
import com.soboleiv.flatsearch.server.crawler.CheckDataSourceCommand;
import com.soboleiv.flatsearch.server.crawler.Crawler;
import com.soboleiv.flatsearch.server.crawler.UrlNormalizer;
import com.soboleiv.flatsearch.server.transorm.RDTransformer;
import com.soboleiv.flatsearch.server.transorm.Transformer;
import com.soboleiv.flatsearch.shared.AdminResponse;

public class AdminServiceImpl extends RemoteServiceServlet implements AdminService{
	private Logger log = LoggerFactory.getLogger(AdminServiceImpl.class);

	public static final String LINKS_REGEXP = "href=\"(.{0,50}ru/offer/ad/.{0,50}/kiev/page.{0,50})\">";
	public static final String START_PAGE = "http://www.svdevelopment.com/ru/offer/ad/10/kiev/page/11/";
	
	public AdminResponse checkDataSources() {
		/*Crawler crawler = new Crawler(LINKS_REGEXP, START_PAGE);
		crawler.setMaxHits(10);
		Transformer transformer = new SDTransformer();
		CheckDataSourceCommand svdCommand = new CheckDataSourceCommand(crawler, transformer);
		svdCommand.run();*/
		
		String startingPage = "http://www.realdruzi.com.ua/lookforapp/?list_count=50";
		String pageRegexp = "<a class=\"\\w+\" href=\"(/lookforapp/\\d+.?)\">";
		Crawler crawler2 = new Crawler(pageRegexp, startingPage);
		Transformer transformer2 = new RDTransformer();
		crawler2.setMaxHits(2);
		UrlNormalizer normalizer = new BaseUrlNormalizer("http://www.realdruzi.com.ua");
		crawler2.setNormalizer(normalizer);
		CheckDataSourceCommand rdCommand = new CheckDataSourceCommand(crawler2, transformer2);
		rdCommand.run();
		
		return new AdminResponse();
	}
}
