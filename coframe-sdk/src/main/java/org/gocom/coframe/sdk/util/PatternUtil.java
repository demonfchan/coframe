/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on May 14, 2019
 *******************************************************************************/
package org.gocom.coframe.sdk.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
public class PatternUtil {
	private static Pattern parttern = Pattern.compile("\\{.*?\\}");
	
	/**
	 * 将字符串转化为 redis 可以接受的 pattern
	 * 
	 * @param str
	 * @return
	 */
	public static String fmt(String pattern) {
		String patternString = pattern.replaceAll("\\[", "\\\\[");
		patternString = patternString.replaceAll("\\]", "\\\\]");
		return patternString;
	}

	/**
	 * 将字符串转化为正则可以接受的 pattern
	 * @param regExp
	 * @return
	 */
	public static String fmtReg(String regExp) {
		String regString = regExp.replaceAll("\\[", "\\\\[");
		regString = regString.replaceAll("\\]", "\\\\]");
		regString = regString.replaceAll("\\.", "\\\\.");
		return regString;
	}
	
	/**
	 * 将 uri 转化为正则表达式。主要是对 pathParam 的处理
	 * @param uri
	 * @return
	 */
	public static String uri2RegExp(String uri) {
		Matcher m = parttern.matcher(uri);
		String reg = m.replaceAll(".*?");
		reg = reg.startsWith(".*?") ? reg : "^" + reg;
		reg = reg.endsWith(".*?") ? reg : reg + "$";
		return reg;
	}
}
