/*******************************************************************************
 * Copyright (c) 2001-2018 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on 2018年07月20日 18:23:19
 ******************************************************************************/

package com.primeton.iam.client;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.resource.UserRedirectRequiredException;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.client.token.AccessTokenProviderChain;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.ClientTokenServices;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;

import java.util.List;

/**
 * @author huzd@primeton.com
 */
public class IAMAccessTokenProviderChain extends AccessTokenProviderChain {

    private ClientTokenServices clientTokenServices;

    public IAMAccessTokenProviderChain(List<? extends AccessTokenProvider> chain) {
        super(chain);
    }

    /**
     * 覆盖了父类的方法,在用户未认证的情况下,通过 refresh_token 去服务器端刷新用户accessToken,
     * 这可能存在安全隐患,任何人拿到用户的 refresh_token 和 accessToken, 有可能无休止的操作下去
     * <p>
     * 但是用户的认证又基于 accessToken,
     *
     * @param resource 受保护资源信息
     * @param request  AccessTokenRequest
     * @return OAuth2AccessToken
     * @throws UserRedirectRequiredException 获得 token 出现错误的时候,重定向用户
     * @throws AccessDeniedException         服务器端拒绝
     */
    @Override
    public OAuth2AccessToken obtainAccessToken(OAuth2ProtectedResourceDetails resource, AccessTokenRequest request) throws UserRedirectRequiredException, AccessDeniedException {
        OAuth2AccessToken accessToken = null;
        OAuth2AccessToken existingToken = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth instanceof AnonymousAuthenticationToken) {
            if (!resource.isClientOnly()) {
                throw new InsufficientAuthenticationException(
                        "Authentication is required to obtain an access token (anonymous not allowed)");
            }
        }

        existingToken = request.getExistingToken();
        if (existingToken == null && clientTokenServices != null && (auth != null && auth.isAuthenticated())) {
            existingToken = clientTokenServices.getAccessToken(resource, auth);
        }

        if (existingToken != null) {
            if (existingToken.isExpired()) {
                if (clientTokenServices != null) {
                    clientTokenServices.removeAccessToken(resource, auth);
                }
                OAuth2RefreshToken refreshToken = existingToken.getRefreshToken();
                if (refreshToken != null && !resource.isClientOnly()) {
                    accessToken = refreshAccessToken(resource, refreshToken, request);
                }
            } else {
                accessToken = existingToken;
            }
        }

        // Give unauthenticated users a chance to get a token and be redirected

        if (accessToken == null) {
            // looks like we need to try to obtain a new token.
            accessToken = obtainNewAccessTokenInternal(resource, request);

            if (accessToken == null) {
                throw new IllegalStateException(
                        "An OAuth 2 access token must be obtained or an exception thrown.");
            }
        }

        if (clientTokenServices != null
                && (resource.isClientOnly() || auth != null && auth.isAuthenticated())) {
            clientTokenServices.saveAccessToken(resource, auth, accessToken);
        }

        return accessToken;
    }

    @Override
    public void setClientTokenServices(ClientTokenServices clientTokenServices) {
        this.clientTokenServices = clientTokenServices;
        super.setClientTokenServices(clientTokenServices);
    }
}
