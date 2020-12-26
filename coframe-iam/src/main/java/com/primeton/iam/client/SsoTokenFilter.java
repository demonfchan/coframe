package com.primeton.iam.client;

import static org.gocom.coframe.sdk.CofConstants.SSO_TOKEN;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.gocom.coframe.CoframeConstants;
import org.gocom.coframe.sdk.CofContext;
import org.gocom.coframe.sdk.exception.CofErrorCode;
import org.gocom.coframe.sdk.exception.CofRuntimeException;
import org.gocom.coframe.sdk.model.CofRole;
import org.gocom.coframe.sdk.model.CofUser;
import org.gocom.coframe.service.IUserService;
//import org.gocom.coframe.CoframeConstants;
//import org.gocom.coframe.sdk.CofContext;
//import org.gocom.coframe.sdk.exception.CofRuntimeException;
//import org.gocom.coframe.sdk.model.CofUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

public class SsoTokenFilter extends OncePerRequestFilter {
	@Autowired
	public IUserService userService;
	@Autowired
	public SSoTokenService sSoTokenService;

	private List<RequestMatcher> permissiveRequestMatchers;
	IAMAuthenticationFailureHandler failureHandler = new IAMAuthenticationFailureHandler();

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		AuthenticationException failed = null;
		String ssoToken = null;
		String accessToken = null;
		String authToken = request.getHeader("Authorization");
		if(request.getRequestURI().endsWith("/auth-token")) {
			if (request.getCookies() != null) {
				ssoToken = Arrays.asList(request.getCookies()).stream()
						.filter(cookie -> cookie.getName().equals(SSO_TOKEN)).map(cookie -> cookie.getValue())
						.collect(Collectors.joining(""));
			}
			if(StringUtils.isBlank(ssoToken)) {
				return;
			} else {
				response.setHeader("X-Auth-Token-0", ssoToken);
			}
		}
		if (StringUtils.isNotBlank(authToken)) {
			ssoToken = StringUtils.removeStart(authToken, "Bearer ");
		} else {
			if (request.getCookies() != null) {
				ssoToken = Arrays.asList(request.getCookies()).stream()
						.filter(cookie -> cookie.getName().equals(SSO_TOKEN)).map(cookie -> cookie.getValue())
						.collect(Collectors.joining(""));
			}
		}
		if (StringUtils.isBlank(ssoToken)) {
			throw CofErrorCode.TOKEN_EMPTY.runtimeException();
		}
		if (ssoToken.split("\\|").length <= 1) {
			throw CofErrorCode.TOKEN_INVALID.runtimeException();
		}
		accessToken = ssoToken.split("\\|")[1];
		CofUser tokenUser = sSoTokenService.analytic(accessToken);
		if (tokenUser == null) {
			throw CofErrorCode.TOKEN_INVALID.runtimeException();
		}
		CofContext.getContext().put(CoframeConstants.CONTEXT_TENANT_ID, tokenUser.getTenantId());
		CofUser user = userService.getUserByName(tokenUser.getName());
		if (user == null) {
			throw CofErrorCode.USER_NAME_NOT_FOUND.runtimeException();
		}
		BeanUtils.copyProperties(user, tokenUser);
		tokenUser.setToken(accessToken);
		try {
			sSoTokenService.validate(accessToken);
		} catch (CofRuntimeException e) {
			failed = new InternalAuthenticationServiceException("", e);
		} catch (InternalAuthenticationServiceException e) {
			logger.error("An internal error occurred while trying to authenticate the user.", failed);
			failed = e;
		} catch (AuthenticationException e) {
			failed = e;
		}
		if (failed == null) {
			iamSuccessfulAuthentication(response, tokenUser);
			filterChain.doFilter(request, response);
		} else if (!permissiveRequest(request)) {
			if(StringUtils.isNotBlank(accessToken)) {
				iamSuccessfulAuthentication(response, tokenUser);
				filterChain.doFilter(request, response);
				return ;
			}
			unsuccessfulAuthentication(request, response, failed);
			return;
		}
	}

	private void iamSuccessfulAuthentication(HttpServletResponse response, CofUser tokenUser) {
		// 上下文中缓存用户
		CofContext.getContext().put(CoframeConstants.CONTEXT_LOGINED_USER, tokenUser);
		// 上下文中缓存tenantId
		CofContext.getContext().put(CoframeConstants.CONTEXT_TENANT_ID, tokenUser.getTenantId());
		// 上下文中缓存用户所有的权限码
			tokenUser.setRoles(userService.getUserRoles(tokenUser.getId()));
			Set<String> functionCodes = new HashSet<>();
			if (tokenUser.getRoles() != null) {
				tokenUser.getRoles().forEach(r -> {
					CofRole role = userService.getRole(r.getId());
					if (role != null && role.getFunctions() != null) {
						role.getFunctions().forEach(function -> {
							functionCodes.add(function.getCode());
						});
					}
				});
			}
			CofContext.getContext().put(CoframeConstants.CONTEXT_LOGINED_USER_FUNCTION_CODES, functionCodes);
	}

	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
		// token 校验失败
		failureHandler.onAuthenticationFailure(request, response, failed);
	}

	protected boolean permissiveRequest(HttpServletRequest request) {
		if (permissiveRequestMatchers == null) {
			return false;
		}
		for (RequestMatcher permissiveMatcher : permissiveRequestMatchers) {
			if (permissiveMatcher.matches(request)) {
				return true;
			}
		}
		return false;
	}
}
