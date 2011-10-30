package com.soboleiv.flatsearch.server.crawler;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.soboleiv.flatsearch.server.util.InputsSanitizer;

abstract class RegexpDataMapper<T> {
	protected String dataRegexp;	

	public RegexpDataMapper() {
		// TODO: check if this thing is still required to satisfy compiler on windows
	}
	
	public List<T> parseData(String content) {
		Pattern pattern = Pattern.compile(dataRegexp, Pattern.CASE_INSENSITIVE
				| Pattern.DOTALL);
		Matcher matcher = pattern.matcher(content);
		if (matcher.groupCount() == 0)
			throw new IllegalStateException(
					"Group count should be more then 0 - they point to data to be captured.");

		List<T> results = new LinkedList<T>();
		SanitizingMatcher secure = new SanitizingMatcher(matcher);
		while (matcher.find()) {
			T result = mapMatch(secure);
			results.add(result);
		}
		return results;
	}
	
	protected abstract T mapMatch(SanitizingMatcher matcher);
}

class SanitizingMatcher {
	private InputsSanitizer checker = new InputsSanitizer();
	private Matcher matcher;
	
	public SanitizingMatcher(Matcher matcher) {
		super();
		this.matcher = matcher;
	}

	public String group(int num) {
		String val = matcher.group(num);		
		return checker.sanitize(val);
	}
	
	public int groupCount() {
		return matcher.groupCount();
	}
	
	public boolean find() {
		return matcher.find();
	}
}
