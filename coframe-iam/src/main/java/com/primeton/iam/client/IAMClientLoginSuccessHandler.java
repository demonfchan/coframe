/*******************************************************************************
 *
 * Copyright (c) 2001-2018 Primeton Technologies, Ltd.
 * All rights reserved.
 * 
 * Created on Jul 16, 2018 4:47:24 PM
 *******************************************************************************/

package com.primeton.iam.client;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class IAMClientLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		if (getMatcher().matches(request)) {
			response.setStatus(HttpStatus.OK.value());
			return;
		}
		super.onAuthenticationSuccess(request, response, authentication);
	}

	public RequestMatcher getMatcher() {
		ContentNegotiationStrategy contentNegotiationStrategy = new HeaderContentNegotiationStrategy();
		MediaTypeRequestMatcher jsonXmlMatcher = new MediaTypeRequestMatcher(contentNegotiationStrategy,
				MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_XML);
		RequestHeaderRequestMatcher ajaxMatcher = new RequestHeaderRequestMatcher("X-Requested-With", "XMLHttpRequest");
		return new OrRequestMatcher(ajaxMatcher, jsonXmlMatcher);
	}
}
