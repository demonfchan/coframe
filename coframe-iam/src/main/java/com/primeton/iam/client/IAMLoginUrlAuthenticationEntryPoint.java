/*******************************************************************************
 * Copyright (c) 2001-2018 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on 2018年07月11日 11:00:50
 ******************************************************************************/

package com.primeton.iam.client;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

/**
 * @author huzd@primeton.com
 */
public class IAMLoginUrlAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {


    private CookieTokenService cookieTokenService;

    /**
     * @param loginFormUrl URL where the login page can be found. Should either be
     *                     relative to the web-app context path (include a leading {@code /}) or an absolute
     *                     URL.
     */
    public IAMLoginUrlAuthenticationEntryPoint(String loginFormUrl) {
        super(loginFormUrl);
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String requestURI = request.getRequestURI();
        String queryString = request.getQueryString();
        if (requestURI != null &&requestURI.startsWith("/iam")&&
                !getLoginFormUrl().equals(request.getServletPath())) {// 跳过登陆地址的保存,防止死循环和默认调到首页
            String requestPath = queryString != null ? requestURI + "?" + queryString : requestURI;
            getCookieTokenService().writeSavedRequestURIToCookie(requestPath, request, response);
        }
        super.commence(request, response, authException);
    }

    public CookieTokenService getCookieTokenService() {
        return cookieTokenService;
    }

    public void setCookieTokenService(CookieTokenService cookieTokenService) {
        this.cookieTokenService = cookieTokenService;
    }
}
