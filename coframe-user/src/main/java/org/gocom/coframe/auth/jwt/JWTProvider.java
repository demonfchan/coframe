/**
 * 
 */
package org.gocom.coframe.auth.jwt;

import org.gocom.coframe.CoframeConstants;
import org.gocom.coframe.auth.AuthToken;
import org.gocom.coframe.auth.CoframeUserDetailsService;
import org.gocom.coframe.sdk.CofContext;
import org.gocom.coframe.sdk.exception.CofErrorCode;
import org.gocom.coframe.sdk.model.CofUser;
import org.gocom.coframe.sdk.service.CofTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * @author qinscx
 *
 */
public class JWTProvider implements AuthenticationProvider {
	private CoframeUserDetailsService userDetailsService;
	private CofTokenService tokenService;

	public JWTProvider(CoframeUserDetailsService userDetailsService, CofTokenService tokenService) {
		this.userDetailsService = userDetailsService;
		this.tokenService = tokenService;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		// 清空上下文, 防止因为线程复用导致的脏数据
		CofContext.getContext().clear();
		
		// 取token
		String token = ((AuthToken) authentication).getToken();

		DecodedJWT jwt = tokenService.decode(token);
		CofUser tokenUser = tokenService.analytic(jwt);

		// 取用户
		CofUser user = userDetailsService.getLoginedUser(tokenUser.getName());
		if (user == null) {
			throw CofErrorCode.USER_NAME_NOT_FOUND.responseCode(HttpStatus.UNAUTHORIZED.value()).runtimeException();
		}
		if (user.getSalt() == null) {
			throw CofErrorCode.USER_NOT_LOGIN.responseCode(HttpStatus.UNAUTHORIZED.value()).runtimeException();
		}
		tokenService.validate(jwt, user);

		// token 验证成功，将 tenantId 存入 Context，以供后续使用。这是实现真正多租户的关键点
		CofContext.getContext().put(CoframeConstants.CONTEXT_TENANT_ID, tokenUser.getTenantId());
		UserDetails userDetail = userDetailsService.loadUserByUsername(tokenUser.getName());
		return new AuthToken(user, userDetail, token, userDetail.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.isAssignableFrom(AuthToken.class);
	}
}