package com.primeton.iam.client;


import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * @author huzd@primeton.com
 */
public class IAMCookieTokenWriteFilter extends OncePerRequestFilter {
    private final CookieTokenService cookieTokenService;
    private final OAuth2RestOperations restTemplate;
    private static Logger log = LoggerFactory.getLogger(IAMCookieTokenWriteFilter.class);

    public IAMCookieTokenWriteFilter(CookieTokenService cookieTokenService, OAuth2RestOperations restTemplate) {
        Assert.notNull(restTemplate, "must not be null");
        this.restTemplate = restTemplate;
        Assert.notNull(cookieTokenService, "must not be null");
        this.cookieTokenService = cookieTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        OAuth2AccessToken accessToken = null;
        try {
            accessToken = restTemplate.getAccessToken();
            if (accessToken != null) {
                String at = accessToken.getValue();
                String rt = accessToken.getRefreshToken().getValue();
                cookieTokenService.saveTokenToCookie(rt, at, response);
            }
        } catch (RuntimeException e) {
            /*ignore*/
            log.debug(e.getMessage(), e);
        }

        filterChain.doFilter(request, response);
    }


}
