/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Jun 3, 2019
 *******************************************************************************/
package org.gocom.coframe.sdk.filter;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gocom.coframe.sdk.CofConstants;
import org.gocom.coframe.sdk.CofContext;
import org.gocom.coframe.sdk.config.CofSDKConfiguration;
import org.gocom.coframe.sdk.exception.CofErrorCode;
import org.gocom.coframe.sdk.model.CofFunction;
import org.gocom.coframe.sdk.model.CofUser;
import org.gocom.coframe.sdk.service.CofSDK;
import org.gocom.coframe.sdk.service.CofTokenService;
import org.gocom.coframe.sdk.util.HttpUtil;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * 请求拦截器
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
public class CofTokenFilter extends OncePerRequestFilter {
	private CofSDKConfiguration configuration;
	private CofSDK sdk;

	public CofTokenFilter(CofSDKConfiguration configuration, CofSDK sdk) {
		this.configuration = configuration;
		this.sdk = sdk;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String uri = request.getRequestURI();
		// 不拦截功能扫描
		if ((configuration.uri(CofConstants.FUNCTION_SCAN_URI)).equals(uri)) {
			filterChain.doFilter(request, response);
			return;
		}

		CofTokenService tokenService = sdk.getTokenService();
		String token = HttpUtil.getToken(request.getHeader(CofConstants.HHN_AUTHORIZATION));
		if (token != null) {
			// 将 token 放入上下文中
			CofContext.getContext().put(CofConstants.CONTEXT_TOKEN, token);
			// 将用户信息放入上下文。但此时的用户只有最基本的 id,name 与 tenantId
			CofContext.getContext().put(CofConstants.CONTEXT_LOGINED_USER_FUNCTION_CODES, tokenService.analytic(tokenService.decode(token)));
		}

		if (configuration.isValidateToken() && configuration.shouldFilter(uri)) {
			if (token == null) {
				throw CofErrorCode.TOKEN_EMPTY.runtimeException();
			}
			DecodedJWT jwt = tokenService.decode(token);
			CofUser user = tokenService.retrieve(jwt);
			tokenService.validate(jwt, user);

			// token 校验成功，将用户与权限放入上下文中
			CofContext.getContext().put(CofConstants.CONTEXT_LOGINED_USER, user);
			// 用户功能权限也放入上下文中
			Set<CofFunction> functions = tokenService.retrieveFunctions(user);
			Set<String> functionCodes = functions.stream().map(x -> x.getCode()).collect(Collectors.toSet());
			CofContext.getContext().put(CofConstants.CONTEXT_LOGINED_USER_FUNCTION_CODES, functionCodes);
		}
		filterChain.doFilter(request, response);
	}
}
