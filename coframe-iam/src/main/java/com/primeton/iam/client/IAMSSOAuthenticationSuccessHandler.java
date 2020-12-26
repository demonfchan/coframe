/*******************************************************************************
 * Copyright (c) 2001-2018 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on 2018年06月28日 16:36:04
 ******************************************************************************/

package com.primeton.iam.client;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 */
public class IAMSSOAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	private OAuth2RestOperations restTemplate;
	private CookieTokenService cookieTokenService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
		writeTokenToCookie(authentication, response);
		getCookieTokenService().cleanSavedRequestURICookie(response);
		response.flushBuffer();
		super.onAuthenticationSuccess(request, response, authentication);
	}

	/**
	 * 处理之前请求的地址
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@Override
	protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
		String savedRequestURI = getCookieTokenService().extractSavedRequestURI(request);
		return calculateCurrentUri(request, savedRequestURI);
	}

	private String calculateCurrentUri(HttpServletRequest request, String path) {
		ServletUriComponentsBuilder sBuilder = ServletUriComponentsBuilder.fromRequest(request);
		String query = "";
		if (path != null) {
			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(path);
			UriComponents build = builder.build();
			path = build.getPath();
			query = build.getQuery();
		}
		UriComponents uri = null;
		try {
			uri = sBuilder.replacePath(path != null ? path : "/").replaceQuery(query).build(true);
		} catch (IllegalArgumentException ex) {
			return null;
		}
		return uri.toString();
	}

	/**
	 * token 写 cookie
	 *
	 * @param authentication
	 * @param response
	 */
	private void writeTokenToCookie(Authentication authentication, HttpServletResponse response) {
		if (authentication instanceof OAuth2Authentication) {
			OAuth2AccessToken accessToken = getRestTemplate().getAccessToken();
			if (accessToken != null) {
				String at = accessToken.getValue();
				String rt = null;
				OAuth2RefreshToken refreshToken = accessToken.getRefreshToken();
				if (refreshToken != null) {
					rt = refreshToken.getValue();
				}
				getCookieTokenService().saveTokenToCookie(rt, at, response);
			}
		}
	}

	public CookieTokenService getCookieTokenService() {
		return cookieTokenService;
	}

	public void setCookieTokenService(CookieTokenService cookieTokenService) {
		this.cookieTokenService = cookieTokenService;
	}

	public OAuth2RestOperations getRestTemplate() {
		return restTemplate;
	}

	public void setRestTemplate(OAuth2RestOperations restTemplate) {
		this.restTemplate = restTemplate;
	}
}
