/*******************************************************************************
 *
 * Copyright (c) 2001-2018 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on  
 *******************************************************************************/

package com.primeton.iam.client;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;


public class IAMClientLoginFailureHandler implements AuthenticationFailureHandler {
	private static Logger log = LoggerFactory.getLogger(IAMClientLoginFailureHandler.class);

    /**
     * Called when an authentication attempt fails.
     *
     * @param request   the request during which the authentication attempt occurred.
     * @param response  the response.
     * @param exception the exception which was thrown to reject the authentication
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        ObjectMapper objectMapper = new ObjectMapper();
        String responseMsg;
        if (exception instanceof UsernameNotFoundException || exception instanceof BadCredentialsException) {

            log.error("username or password error.", exception);
            responseMsg = "username or password error.";
        } else if (exception instanceof DisabledException) {

            log.error("Account has been disabled.", exception);
            responseMsg = "Account has been disabled.";
        } else if (exception instanceof LockedException) {
            log.error("Account has been locked.", exception);
            responseMsg = "Account has been locked.";
        }
        else if (exception instanceof org.springframework.security.authentication.CredentialsExpiredException) {
            log.error("Account password have expired.", exception);
            responseMsg = "Account password have expired.";
        } else if (exception instanceof AccountExpiredException) {
            log.error("Account expired.", exception);
            responseMsg = "Account expired.";
        } else {
            log.error("login error.", exception);
            log.error("exception = {}", exception.getClass().getCanonicalName());
            responseMsg = "login error." + exception.getMessage();
        }

        Map<String, String> responseMsgMap = new HashMap<>();
        responseMsgMap.put("message", responseMsg);
        try {
            String json = objectMapper.writeValueAsString(responseMsgMap);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
            response.getWriter().write(json);
        } catch (Exception e1) {
            e1.printStackTrace();
            log.error("log in error" + e1.getMessage(), e1);
        }

    }

}
