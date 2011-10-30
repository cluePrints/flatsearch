package com.soboleiv.flatsearch.server.crawler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToArrayRegexpMapper extends RegexpDataMapper<String[]>{
	private Logger log = LoggerFactory.getLogger(ToArrayRegexpMapper.class);
	
	public ToArrayRegexpMapper(String dataRegexp) {
		this.dataRegexp = dataRegexp;
		log.debug("Created with regexp: "+dataRegexp);
	}
	
	protected String[] mapMatch(InputProcessor matcher) {
		String[] result = new String[matcher.groupCount()];
		for (int i = 0; i < matcher.groupCount() ; i++) {
			result[i] = matcher.group(i + 1);
		}
		return result;
	}
}
