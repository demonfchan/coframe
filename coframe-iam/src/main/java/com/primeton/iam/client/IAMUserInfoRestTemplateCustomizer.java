/*******************************************************************************
 * Copyright (c) 2001-2018 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on 2018年07月20日 17:53:04
 ******************************************************************************/

package com.primeton.iam.client;

import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateCustomizer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.RequestEnhancer;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeAccessTokenProvider;
import org.springframework.util.MultiValueMap;

import java.util.Collections;

/**
 * @author huzd@primeton.com
 */
public class IAMUserInfoRestTemplateCustomizer implements UserInfoRestTemplateCustomizer {
    @Override
    public void customize(OAuth2RestTemplate template) {
        AuthorizationCodeAccessTokenProvider accessTokenProvider = new AuthorizationCodeAccessTokenProvider();
        accessTokenProvider.setTokenRequestEnhancer(new AcceptJsonRequestEnhancer());
        accessTokenProvider.setStateMandatory(false);
        // AccessTokenProviderChain 支持
        template.setAccessTokenProvider(new IAMAccessTokenProviderChain(Collections.singletonList(accessTokenProvider)));
    }

    static class AcceptJsonRequestEnhancer implements RequestEnhancer {

        @Override
        public void enhance(AccessTokenRequest request,
                            OAuth2ProtectedResourceDetails resource,
                            MultiValueMap<String, String> form, HttpHeaders headers) {
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        }
    }
}
