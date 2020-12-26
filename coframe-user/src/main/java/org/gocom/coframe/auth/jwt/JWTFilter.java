/**
 * 
 */
package org.gocom.coframe.auth.jwt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gocom.coframe.CoframeConstants;
import org.gocom.coframe.auth.AuthToken;
import org.gocom.coframe.auth.handler.AuthFailureHandler;
import org.gocom.coframe.sdk.CofConstants;
import org.gocom.coframe.sdk.CofContext;
import org.gocom.coframe.sdk.config.CofSDKConfiguration;
import org.gocom.coframe.sdk.exception.CofRuntimeException;
import org.gocom.coframe.sdk.model.CofRole;
import org.gocom.coframe.sdk.model.CofUser;
import org.gocom.coframe.sdk.service.CofTokenService;
import org.gocom.coframe.sdk.util.HttpUtil;
import org.gocom.coframe.service.IUserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * @author qinscx
 *
 */
public class JWTFilter extends OncePerRequestFilter {
	private RequestMatcher requiresAuthenticationRequestMatcher;
	private List<RequestMatcher> permissiveRequestMatchers;
	private AuthenticationManager authenticationManager;

	private AuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
	private AuthenticationFailureHandler failureHandler = new AuthFailureHandler();
	private IUserService userService;
	private CofTokenService tokenService;
	private CofSDKConfiguration sdkConfiguration;

	public JWTFilter(IUserService userService, CofTokenService tokenService, CofSDKConfiguration sdkConfiguration) {
		this.requiresAuthenticationRequestMatcher = new RequestHeaderRequestMatcher("Authorization");
		this.userService = userService;
		this.tokenService = tokenService;
		;
		this.sdkConfiguration = sdkConfiguration;
	}

	@Override
	public void afterPropertiesSet() {
		Assert.notNull(authenticationManager, "authenticationManager must be specified");
		Assert.notNull(successHandler, "AuthenticationSuccessHandler must be specified");
		Assert.notNull(failureHandler, "AuthenticationFailureHandler must be specified");
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		if (!requiresAuthentication(request, response)) {
			filterChain.doFilter(request, response);
			return;
		}

		String uri = request.getRequestURI();
		// 不拦截登陆
		if ((sdkConfiguration.uri(CofConstants.COF_LOGIN_URI)).equals(uri)) {
			filterChain.doFilter(request, response);
			return;
		}

		// 不拦截功能扫描
		if ((sdkConfiguration.uri(CofConstants.FUNCTION_SCAN_URI)).equals(uri)) {
			filterChain.doFilter(request, response);
			return;
		}

		// coframe
		String token = HttpUtil.getToken(request.getHeader(CofConstants.HHN_AUTHORIZATION));
		// 登出只检验 token 的合法性
		if ((sdkConfiguration.uri(CofConstants.COF_LOGOUT_URI)).equals(uri)) {
			try {
				CofUser user = tokenService.analytic(token);
				// 上下文中缓存用户
				CofContext.getContext().put(CoframeConstants.CONTEXT_LOGINED_USER, user);
			} catch (CofRuntimeException e) {
				unsuccessfulAuthentication(request, response, new InternalAuthenticationServiceException("", e));
			}
			filterChain.doFilter(request, response);
			return;
		}
		
		Authentication authResult = null;
		AuthenticationException failed = null;
		try {
			authResult = getAuthenticationManager().authenticate(new AuthToken(token));
		} catch (CofRuntimeException e) {
			failed = new InternalAuthenticationServiceException("", e);
		} catch (InternalAuthenticationServiceException e) {
			logger.error("An internal error occurred while trying to authenticate the user.", failed);
			failed = e;
		} catch (AuthenticationException e) {
			failed = e;
		}
		if (authResult != null) {
			successfulAuthentication(request, response, filterChain, authResult);
		} else if (!permissiveRequest(request)) {
			unsuccessfulAuthentication(request, response, failed);
			return;
		}
		filterChain.doFilter(request, response);
	}


	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
		// token 校验失败
		failureHandler.onAuthenticationFailure(request, response, failed);
	}

	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
		// token 校验成功，将当前用户信息放入上下文
		SecurityContextHolder.getContext().setAuthentication(authResult); // 认证结果必须放入 SecurityContextHolder 的上下文，否则会报 403，拒构访问
		CofUser user = ((AuthToken) authResult).getLoginedUser();

		// 上下文中缓存用户
		CofContext.getContext().put(CoframeConstants.CONTEXT_LOGINED_USER, user);
		// 上下文中缓存tenantId
		CofContext.getContext().put(CoframeConstants.CONTEXT_TENANT_ID, user.getTenantId());
		// 上下文中缓存用户所有的权限码
		Set<String> codes = new HashSet<>();
		if (user.getRoles() != null) {
			user.getRoles().forEach(r -> {
				CofRole role = userService.getRole(r.getId());
				if (role != null && role.getFunctions() != null) {
					role.getFunctions().forEach(function -> {
						codes.add(function.getCode());
					});
				}
			});
		}
		CofContext.getContext().put(CoframeConstants.CONTEXT_LOGINED_USER_FUNCTION_CODES, codes);
		successHandler.onAuthenticationSuccess(request, response, authResult);
	}

	protected AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}

	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
		return requiresAuthenticationRequestMatcher.matches(request);
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

	public void setPermissiveUrl(String... urls) {
		if (permissiveRequestMatchers == null) {
			permissiveRequestMatchers = new ArrayList<>();
		}
		for (String url : urls) {
			permissiveRequestMatchers.add(new AntPathRequestMatcher(url));
		}
	}

	public void setAuthenticationSuccessHandler(AuthenticationSuccessHandler successHandler) {
		Assert.notNull(successHandler, "successHandler cannot be null");
		this.successHandler = successHandler;
	}

	public void setAuthenticationFailureHandler(AuthenticationFailureHandler failureHandler) {
		Assert.notNull(failureHandler, "failureHandler cannot be null");
		this.failureHandler = failureHandler;
	}

	protected AuthenticationSuccessHandler getSuccessHandler() {
		return successHandler;
	}

	protected AuthenticationFailureHandler getFailureHandler() {
		return failureHandler;
	}

}
