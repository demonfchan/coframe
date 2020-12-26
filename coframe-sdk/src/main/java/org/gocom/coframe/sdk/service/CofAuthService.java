/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Jun 27, 2019
 *******************************************************************************/
package org.gocom.coframe.sdk.service;

import static org.gocom.coframe.sdk.util.HttpUtil.headers;

import org.gocom.coframe.sdk.CofConstants;
import org.gocom.coframe.sdk.config.CofSDKConfiguration;
import org.gocom.coframe.sdk.model.CofLoginUser;
import org.gocom.coframe.sdk.model.CofUser;
import org.gocom.coframe.sdk.util.HttpUtil.UrlBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@Component
public class CofAuthService {
	@Autowired
	private CofSDKConfiguration configuration;
	

	@Autowired
	@Qualifier(CofConstants.COF_REST_TEMPLATE)
	private RestTemplate restTemplate;

	/**
	 * 登陆
	 * @param user
	 * @return
	 */
	public CofUser login(CofLoginUser user) {
		UrlBuilder builder = new UrlBuilder(configuration.getCoframeUriPrefix()).path("/users/login");
		ParameterizedTypeReference<CofUser> responseType = new ParameterizedTypeReference<CofUser>() {};
		ResponseEntity<CofUser> result = restTemplate.exchange(builder.build(), HttpMethod.POST, new HttpEntity<CofLoginUser>(user, headers()), responseType);
		return result.getBody();
	}

	/**
	 * 登出
	 */
	public void logout() {
		UrlBuilder builder = new UrlBuilder(configuration.getCoframeUriPrefix()).path("/users/logout");
		ParameterizedTypeReference<Void> responseType = new ParameterizedTypeReference<Void>() {};
		restTemplate.exchange(builder.build(), HttpMethod.POST, new HttpEntity<CofLoginUser>(headers()), responseType);
	}
}
