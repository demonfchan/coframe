/*******************************************************************************
 * Copyright (c) 2001-2018 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on 2018年07月13日 08:37:11
 ******************************************************************************/

package com.primeton.iam.client;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.resource.UserRedirectRequiredException;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.util.UriComponentsBuilder;

import com.primeton.iam.config.IAMSecurity;

/**
 * @author huzd@primeton.com
 */
public class IAMClientLogoutHandler implements LogoutHandler {

    private OAuth2RestOperations restTemplate;
    private CookieTokenService cookieTokenService;
    private IAMSecurity iamSecurity;
    private String defaultLoginUrl;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // 单点退出
        try {
            getRestTemplate().delete(iamSecurity.getSso().getLogoutUrl());
        } catch (UserRedirectRequiredException e) {
            // 单点退出失败,是由于 access token 到期导致,
            // 因此此处需要进行获得新的 token 后,重新调用单点退出接口
            // /* // 用户再调用单点退出失败以后,不需要再重新登陆调用
            String redirectUri = e.getRedirectUri();
            Map<String, String> requestParams = e.getRequestParams();
            String redirect_uri = requestParams.get("redirect_uri");
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(redirect_uri);
            redirect_uri = builder.replacePath(request.getContextPath() + iamSecurity.getLoginUrl())
                    .replaceQuery("")
                    .build()
                    .encode().toUriString();
            requestParams.put("redirect_uri", redirect_uri);

            UserRedirectRequiredException redirectException =
                    new UserRedirectRequiredException(redirectUri, requestParams);

            AccessTokenRequest tokenRequest = getRestTemplate().getOAuth2ClientContext().getAccessTokenRequest();
            redirectException.setStateKey(e.getStateKey());
            tokenRequest.setStateKey(e.getStateKey());
            redirectException.setStateToPreserve(redirect_uri);
            tokenRequest.setPreservedState(redirect_uri);
            getRestTemplate().getOAuth2ClientContext().setPreservedState(e.getStateKey(), redirect_uri);
            String queryString = request.getQueryString();
            String requestPath = queryString != null ? request.getRequestURI() + "?" + queryString : request.getRequestURI();
            cookieTokenService.writeSavedRequestURIToCookie(requestPath, request, response);
            throw redirectException;
            // */
        }
        getCookieTokenService().cleanTokenCookie(response);
        getCookieTokenService().cleanSavedRequestURICookie(response);
        response.setStatus(HttpStatus.OK.value());
    }

    public OAuth2RestOperations getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(OAuth2RestOperations restTemplate) {
        this.restTemplate = restTemplate;
    }

    public CookieTokenService getCookieTokenService() {
        return cookieTokenService;
    }

    public void setCookieTokenService(CookieTokenService cookieTokenService) {
        this.cookieTokenService = cookieTokenService;
    }

    public IAMSecurity getIamSecurity() {
        return iamSecurity;
    }

    public void setIamSecurity(IAMSecurity iamSecurity) {
        this.iamSecurity = iamSecurity;
    }

    public String getDefaultLoginUrl() {
        return defaultLoginUrl;
    }

    public void setDefaultLoginUrl(String defaultLoginUrl) {
        this.defaultLoginUrl = defaultLoginUrl;
    }
}
