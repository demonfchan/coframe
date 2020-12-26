/*******************************************************************************
 * Copyright (c) 2001-2018 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on 2018年07月12日 16:26:08
 ******************************************************************************/

package com.primeton.iam.client;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.resource.UserRedirectRequiredException;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.ExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import com.primeton.iam.config.IAMSecurity;

/**
 * @author huzd@primeton.com
 */
public class CookieTokenExtractor implements TokenExtractor {

    private IAMSecurity iamSecurity;
    private TokenStore tokenStore;
    private CookieTokenService cookieTokenService;
    private OAuth2RestOperations restTemplate;

    @Override
    public Authentication extract(HttpServletRequest request) {
        TokenTuple tokenTuple = cookieTokenService.extractToken(request);
        String accessTokenValue = tokenTuple.getAccessToken();
        String refreshTokenValue = tokenTuple.getRefreshToken();
        if (accessTokenValue == null || refreshTokenValue == null) {
            return null;
        }

        request.setAttribute(OAuth2ClientContextFilter.CURRENT_URI, calculateCurrentUri(request));

        OAuth2AccessToken accessToken = tokenStore.readAccessToken(accessTokenValue);
        OAuth2RefreshToken refreshToken = tokenStore.readRefreshToken(refreshTokenValue);

        if (refreshToken instanceof ExpiringOAuth2RefreshToken) {
            Date expiration = ((ExpiringOAuth2RefreshToken) refreshToken).getExpiration();
            if (expiration.getTime() < System.currentTimeMillis()) {
                return null;
            }
        }
        DefaultOAuth2AccessToken newAccessToken = new DefaultOAuth2AccessToken(accessToken);
        newAccessToken.setRefreshToken(refreshToken);
        // 设置当前 token;
        restTemplate.getOAuth2ClientContext().setAccessToken(newAccessToken);
        // 检查 当前 token 的有效性,如果已经过去,则通过RefreshToken 刷新 token
        try {
            accessToken = restTemplate.getAccessToken();
        } catch (UserRedirectRequiredException e) {
            throw new InvalidTokenException(e.getMessage());
        }
        return new PreAuthenticatedAuthenticationToken(accessToken.getValue(), "");
    }

    /**
     * Calculate the current URI given the request.
     *
     * @param request The request.
     * @return The current uri.
     */
    protected String calculateCurrentUri(HttpServletRequest request) {
        ServletUriComponentsBuilder builder = ServletUriComponentsBuilder
                .fromRequest(request);
        // Now work around SPR-10172...
        String queryString = request.getQueryString();
        boolean legalSpaces = queryString != null && queryString.contains("+");
        if (legalSpaces) {
            builder.replaceQuery(queryString.replace("+", "%20"));
        }
        UriComponents uri = null;
        try {
            uri = builder
                    .replacePath(request.getContextPath() + iamSecurity.getLoginUrl())
                    .replaceQueryParam("code").build(true);
        } catch (IllegalArgumentException ex) {
            // ignore failures to parse the url (including query string). does't
            // make sense for redirection purposes anyway.
            return null;
        }
        String query = uri.getQuery();
        if (legalSpaces) {
            query = query.replace("%20", "+");
        }
        return ServletUriComponentsBuilder.fromUri(uri.toUri())
                .replaceQuery(query).build().toString();
    }

    public void setIamSecurity(IAMSecurity iamSecurity) {
        this.iamSecurity = iamSecurity;
    }

    public TokenStore getTokenStore() {
        return tokenStore;
    }

    public void setTokenStore(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
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
