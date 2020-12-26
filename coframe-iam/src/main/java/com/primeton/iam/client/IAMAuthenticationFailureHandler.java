/*******************************************************************************
 * Copyright (c) 2001-2018 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on 2018年07月13日 14:57:34
 ******************************************************************************/

package com.primeton.iam.client;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * @author huzd@primeton.com
 */
public class IAMAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        Throwable throwable = exception;

        StringBuilder message = new StringBuilder("{");
        message.append('"' + "code" + '"')
                .append(':')
                .append('"' + 403 + '"')
                .append(',')
                .append('"' + "message" + '"')
                .append(':')
                .append('"');
        while (throwable != null) {
            message.append(String.format("| %s: %s",
                    throwable.getClass().getName(),
                    throwable.getMessage()));
            throwable = throwable.getCause();
        }
        message.append('"');
        message.append("}");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(message.toString());
    }
}
