package com.soboleiv.flatsearch.server.crawler;

public interface UrlNormalizer {
	public String normalize(String url);
	
	public static UrlNormalizer NONE = new UrlNormalizer() {		
		public String normalize(String url) {
			return url;
		}
	}; 
}