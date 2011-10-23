package com.soboleiv.flatsearch.server.crawler;

public class BaseUrlNormalizer implements UrlNormalizer {
	private String basePath;

	public BaseUrlNormalizer(String basePath) {
		super();
		this.basePath = basePath;
	}
	
	/* (non-Javadoc)
	 * @see com.soboleiv.flatsearch.server.crawler.UrlNormalizer#normalize(java.lang.String)
	 */
	public String normalize(String url) {
		if (url.startsWith("/"))
			return basePath + url;
		return url;
	}
}
