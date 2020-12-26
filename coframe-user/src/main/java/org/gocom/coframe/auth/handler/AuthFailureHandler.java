/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Jun 3, 2019
 *******************************************************************************/
package org.gocom.coframe.auth.handler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gocom.coframe.core.exception.CoframeRuntimeException;
import org.gocom.coframe.sdk.exception.CofErrorCode;
import org.gocom.coframe.sdk.exception.CofErrorObject;
import org.gocom.coframe.sdk.exception.CofRuntimeException;
import org.gocom.coframe.sdk.util.JsonUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * 认证失败时的处理
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
public class AuthFailureHandler implements AuthenticationFailureHandler {
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		response.setStatus(HttpStatus.UNAUTHORIZED.value());

		CofErrorObject error = new CofErrorObject();
		if (exception instanceof InternalAuthenticationServiceException) {
			InternalAuthenticationServiceException e = (InternalAuthenticationServiceException) exception;
			if (e.getCause() instanceof CoframeRuntimeException) {
				CoframeRuntimeException ce = (CoframeRuntimeException) e.getCause();
				error.setError(ce.getErrorCode());
				error.setMessage(ce.getMessage());
			}
			if (e.getCause() instanceof CofRuntimeException) {
				CofRuntimeException ce = (CofRuntimeException) e.getCause();
				error.setError(ce.getErrorCode());
				error.setMessage(ce.getMessage());
			}
		} else if (exception instanceof BadCredentialsException) {
			error.setError(CofErrorCode.USER_PASSWORD_ERROR.name());
			error.setMessage(CofErrorCode.USER_PASSWORD_ERROR.getMessage());
		}
		error.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.getWriter().write(JsonUtil.toJson(error));
		response.getWriter().flush();
	}
}
