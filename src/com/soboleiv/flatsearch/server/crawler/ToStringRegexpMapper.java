package com.soboleiv.flatsearch.server.crawler;


public class ToStringRegexpMapper extends RegexpDataMapper<String>{
	public ToStringRegexpMapper(String dataRegexp) {
		this.dataRegexp = dataRegexp;
	}
	
	protected String mapMatch(SanitizingMatcher matcher) {
		return matcher.group(1);
	}
}
