/*******************************************************************************
 * Copyright (c) 2001-2018 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on 2018年07月06日 16:18:41
 ******************************************************************************/

package com.primeton.iam.authentication;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 请求头认证过滤器
 *
 * @author huzd@primeton.com
 */
public class RequestHeaderTokenAuthenticationFilter extends GenericFilterBean implements ApplicationEventPublisherAware {

    private final String requestHeader;
    private AuthenticationManager authenticationManager;
    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();
    private ApplicationEventPublisher eventPublisher = null;
    private AuthenticationSuccessHandler authenticationSuccessHandler = null;
    private AuthenticationFailureHandler authenticationFailureHandler = null;

    /**
     * 请求头名称
     *
     * @param requestHeader
     */
    public RequestHeaderTokenAuthenticationFilter(String requestHeader) {
        this.requestHeader = requestHeader;
        // 预设一个AuthenticationManager,在将 Filter 添加到过滤器链中去的时候进行设置
        // @see IAMSecurityConfiguration#configureRequestHeaderTokenAuthenticationFilters:121
        setAuthenticationManager(new NoopAuthenticationManager());
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp,
                         FilterChain chain) throws ServletException, IOException {
        SecurityContext context = SecurityContextHolder.getContext();
        if (logger.isDebugEnabled()) {
            logger.debug("Checking secure context token: "
                    + context.getAuthentication());
        }

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        Authentication authentication = doAuthenticate(request, response);
        chain.doFilter(request, response);
        if (SecurityContextHolder.getContext().getAuthentication() == authentication) {
            // 如果是基于 Http header 的认证,则不需要进行 SecurityContext 的持久化
            SecurityContextHolder.getContext().setAuthentication(null);
        }
    }

    /**
     * Do the actual authentication for a pre-authenticated user.
     */
    private Authentication doAuthenticate(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Authentication authResult;

        Object principal = getPreAuthenticatedPrincipal(request);

        if (principal == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("No pre-authenticated principal found in request");
            }

            return null;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("preAuthenticatedPrincipal = " + principal
                    + ", trying to authenticate");
        }

        try {
            PreAuthenticatedAuthenticationToken authRequest = new PreAuthenticatedAuthenticationToken(
                    principal, "N/A");
            authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
            authResult = authenticationManager.authenticate(authRequest);
            successfulAuthentication(request, response, authResult);
            return authResult;
        } catch (AuthenticationException failed) {
            unsuccessfulAuthentication(request, response, failed);
        }
        return null;
    }

    /**
     * 返回 request header 的值和 token
     *
     * @param request
     * @return
     */
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        // 如果没有指定头的值,则返回 null
        if (request.getHeader(requestHeader) == null) {
            return null;
        }
        return new RequestHeaderToken(requestHeader, request.getHeader(requestHeader));
    }

    /**
     * Puts the <code>Authentication</code> instance returned by the authentication
     * manager into the secure context.
     */
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response, Authentication authResult) throws IOException, ServletException {
        if (logger.isDebugEnabled()) {
            logger.debug("Authentication success: " + authResult);
        }
        SecurityContextHolder.getContext().setAuthentication(authResult);
        // Fire event
        if (this.eventPublisher != null) {
            eventPublisher.publishEvent(new InteractiveAuthenticationSuccessEvent(
                    authResult, this.getClass()));
        }

        if (authenticationSuccessHandler != null) {
            authenticationSuccessHandler.onAuthenticationSuccess(request, response, authResult);
        }
    }

    /**
     * Ensures the authentication object in the secure context is set to null when
     * authentication fails.
     * <p>
     * Caches the failure exception as a request attribute
     */
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();

        if (logger.isDebugEnabled()) {
            logger.debug("Cleared security context due to exception", failed);
        }
        request.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, failed);

        if (authenticationFailureHandler != null) {
            authenticationFailureHandler.onAuthenticationFailure(request, response, failed);
        }
    }

    @Override
    public void afterPropertiesSet() {
        try {
            super.afterPropertiesSet();
        } catch (ServletException e) {
            // convert to RuntimeException for passivity on afterPropertiesSet signature
            throw new RuntimeException(e);
        }
        Assert.notNull(authenticationManager, "An AuthenticationManager must be set");
    }


    private static class NoopAuthenticationManager implements AuthenticationManager {

        @Override
        public Authentication authenticate(Authentication authentication)
                throws AuthenticationException {
            throw new UnsupportedOperationException("No authentication should be done with this AuthenticationManager");
        }

    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = applicationEventPublisher;
    }

    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public void setAuthenticationDetailsSource(AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource) {
        this.authenticationDetailsSource = authenticationDetailsSource;
    }

    public void setAuthenticationSuccessHandler(AuthenticationSuccessHandler authenticationSuccessHandler) {
        this.authenticationSuccessHandler = authenticationSuccessHandler;
    }

    public void setAuthenticationFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
        this.authenticationFailureHandler = authenticationFailureHandler;
    }
}
