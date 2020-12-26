/*******************************************************************************
 * Copyright (c) 2001-2018 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on 2018年07月12日 22:26:46
 ******************************************************************************/

package com.primeton.iam.client;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.DelegatingAuthenticationEntryPoint;

/**
 * @author huzd@primeton.com
 */
public class IAMAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private CookieTokenService cookieTokenService;
    private DelegatingAuthenticationEntryPoint entryPoint;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // 如果 token 过去,则清除客户端 cookie 中的 token
        cookieTokenService.cleanTokenCookie(response);
        entryPoint.commence(request, response, authException);
    }

    public CookieTokenService getCookieTokenService() {
        return cookieTokenService;
    }

    public void setCookieTokenService(CookieTokenService cookieTokenService) {
        this.cookieTokenService = cookieTokenService;
    }

    public DelegatingAuthenticationEntryPoint getEntryPoint() {
        return entryPoint;
    }

    public void setEntryPoint(DelegatingAuthenticationEntryPoint entryPoint) {
        this.entryPoint = entryPoint;
    }
}
