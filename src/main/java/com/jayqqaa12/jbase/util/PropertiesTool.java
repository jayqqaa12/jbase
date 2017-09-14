package com.jayqqaa12.jbase.util;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PropertiesTool {
    private static final Pattern PATTERN = Pattern
			.compile("\\$\\{([^\\}]+)\\}");

	public static String get(Properties properties, String key) {
		String value = properties.getProperty(key);
		Matcher matcher = PATTERN.matcher(value);
		StringBuffer buffer = new StringBuffer();
		while (matcher.find()) {
			String matcherKey = matcher.group(1);
			String matchervalue = properties.getProperty(matcherKey);
			if (matchervalue != null) {
				matcher.appendReplacement(buffer, matchervalue);
			}
		}
		matcher.appendTail(buffer);
		return buffer.toString();
	}

}