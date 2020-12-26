/*******************************************************************************
 * Copyright (c) 2001-2018 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on 2018年07月12日 21:57:41
 ******************************************************************************/

package com.primeton.iam.client;

import java.util.Collections;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;

import com.primeton.iam.client.cipher.PlainTokenCipher;
import com.primeton.iam.client.cipher.TokenCipher;

/**
 * @author huzd@primeton.com
 */
public class CookieTokenService {
	private static final Logger log = LoggerFactory.getLogger(CookieTokenService.class);
	private String tokenCookieName = "X-Auth-Token";
	private String savedRequestURICookieName = "X-SavedRequestURI";
	private TokenCipher tokenCipher = new PlainTokenCipher();// 默认不进行加密处理
	private String cookieNameSuffix = "";

	/**
	 * @param request
	 * @return
	 */
	public TokenTuple extractToken(HttpServletRequest request) {
		String rt = null, at = null;
		String cookieValue = null;
		cookieValue = extractCookieValue(getTokenCookieName(), request);
		String decrypt = tokenCipher.decrypt(cookieValue);
		if (decrypt != null) {
			String[] tokens = decrypt.split("\\|");
			rt = tokens[0];
			at = tokens[1];
			if ("null".equals(rt)) {
				rt = null;
			}
			if ("null".equals(at)) {
				at = null;
			}
		}
		TokenTuple tokenTuple = new TokenTuple();
		tokenTuple.setAccessToken(at);
		tokenTuple.setRefreshToken(rt);
		return tokenTuple;
	}

	/**
	 * @param request
	 * @return
	 */
	public String extractSavedRequestURI(HttpServletRequest request) {
		return extractCookieValue(getSavedRequestURICookieName(), request);
	}

	/**
	 * @param cookieName
	 * @param request
	 * @return
	 */
	public String extractCookieValue(String cookieName, HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return null;
		}
		for (Cookie cookie : cookies) {
			if (cookieName.equals(cookie.getName())) {
				return cookie.getValue();
			}
		}
		return null;
	}

	public void writeSavedRequestURIToCookie(String requestURI, HttpServletRequest request, HttpServletResponse response) {
		// FIXED: 如果请求是 ajax 请求,或者是 json 请求,则不进行回调地址的保存
		if (getSavedRequestURIMatcher().matches(request)) {
			return;
		}
		writeTokenToCookie(getSavedRequestURICookieName(), requestURI, response);
	}

	private RequestMatcher getSavedRequestURIMatcher() {
		return new OrRequestMatcher(ajaxRequestMatcher(), jsonXmlRequestMatcher());
	}

	private RequestMatcher ajaxRequestMatcher() {
		return new RequestHeaderRequestMatcher("X-Requested-With", "XMLHttpRequest");
	}

	private RequestMatcher jsonXmlRequestMatcher() {
		MediaTypeRequestMatcher jsonXmlMatcher = new MediaTypeRequestMatcher(new HeaderContentNegotiationStrategy(), MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_XML);
		jsonXmlMatcher.setIgnoredMediaTypes(Collections.singleton(MediaType.ALL));
		jsonXmlMatcher.setUseEquals(true);
		return jsonXmlMatcher;
	}

	/**
	 * refreshToken 和 accessToken 使用 (refreshToken|accessToken)这样的方式和成一个 token,
	 * 然匠再进行加密处理
	 *
	 * @param refreshToken 刷新 token
	 * @param accessToken  访问 token
	 * @param response     http response
	 */
	public void saveTokenToCookie(String refreshToken, String accessToken, HttpServletResponse response) {
		String token = String.format("%s|%s", refreshToken, accessToken);
		// 对 token 进行加密处理
		token = tokenCipher.encrypt(token);
		String tokenName = getTokenCookieName();
		writeTokenToCookie(tokenName, token, response);
		response.setHeader(tokenName, token);
	}

	/**
	 * @param cookieName cookie 名称
	 * @param token      token
	 * @param response   response
	 */
	private void writeTokenToCookie(String cookieName, String token, HttpServletResponse response) {
		Cookie cookie = new Cookie(cookieName, token);
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		// cookie.setSecure(true); // 只在https才发生
		response.addCookie(cookie);
		log.debug(String.format("Write Token to Cookie: [%s=%s]", cookieName, token));
	}

	/**
	 * @param response
	 */
	public void cleanTokenCookie(HttpServletResponse response) {
		cleanCookie(getTokenCookieName(), response);
	}

	/**
	 * 清除savedRequestURICookie
	 *
	 * @param response
	 */
	protected void cleanSavedRequestURICookie(HttpServletResponse response) {
		cleanCookie(getSavedRequestURICookieName(), response);
	}

	protected void cleanCookie(String cookieName, HttpServletResponse response) {
		Cookie cookie = new Cookie(cookieName, null);
		cookie.setMaxAge(0);
		cookie.setPath("/");
		response.addCookie(cookie);
	}

	public String getSavedRequestURICookieName() {
		if (StringUtils.hasText(getCookieNameSuffix())) {
			return savedRequestURICookieName + "-" + getCookieNameSuffix();
		}
		return savedRequestURICookieName;
	}

	public void setSavedRequestURICookieName(String savedRequestURICookieName) {
		this.savedRequestURICookieName = savedRequestURICookieName;
	}

	public String getTokenCookieName() {
		if (StringUtils.hasText(getCookieNameSuffix())) {
			return tokenCookieName + "-" + getCookieNameSuffix();
		}
		return tokenCookieName;
	}

	public void setTokenCookieName(String tokenCookieName) {
		this.tokenCookieName = tokenCookieName;
	}

	public TokenCipher getTokenCipher() {
		return tokenCipher;
	}

	public void setTokenCipher(TokenCipher tokenCipher) {
		this.tokenCipher = tokenCipher;
	}

	public String getCookieNameSuffix() {
		return cookieNameSuffix;
	}

	public void setCookieNameSuffix(String cookieNameSuffix) {
		this.cookieNameSuffix = cookieNameSuffix;
	}
}
