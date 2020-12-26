package org.gocom.coframe.auth.up;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;

/**
 * 用户名与密码校验配置(UP = UsernamePassword)
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 * @param <T>
 * @param <B>
 */
public class UPConfiguration<T extends UPConfiguration<T, B>, B extends HttpSecurityBuilder<B>> extends AbstractHttpConfigurer<T, B> {
	private UPFilter authFilter;

	public UPConfiguration() {
		this.authFilter = new UPFilter();
	}

	@Override
	public void configure(B http) throws Exception {
		authFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
		authFilter.setSessionAuthenticationStrategy(new NullAuthenticatedSessionStrategy());

		UPFilter filter = postProcess(authFilter);
		http.addFilterAfter(filter, LogoutFilter.class);
	}

	public UPConfiguration<T, B> successHandler(AuthenticationSuccessHandler authSuccessHandler) {
		authFilter.setAuthenticationSuccessHandler(authSuccessHandler);
		return this;
	}
	
	public UPConfiguration<T, B> failureHandler(AuthenticationFailureHandler authFailureHandler) {
		authFilter.setAuthenticationFailureHandler(authFailureHandler);
		return this;
	}
}