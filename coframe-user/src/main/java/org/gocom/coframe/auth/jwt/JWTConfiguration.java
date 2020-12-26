/**
 * 
 */
package org.gocom.coframe.auth.jwt;

import org.gocom.coframe.sdk.config.CofSDKConfiguration;
import org.gocom.coframe.sdk.service.CofTokenService;
import org.gocom.coframe.service.IUserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;

/**
 * @author qinscx
 *
 */
public class JWTConfiguration<T extends JWTConfiguration<T, B>, B extends HttpSecurityBuilder<B>> extends AbstractHttpConfigurer<T, B> {

	private JWTFilter authFilter;

	public JWTConfiguration(IUserService userService, CofTokenService tokenService, CofSDKConfiguration sdkConfiguration) {
		this.authFilter = new JWTFilter(userService, tokenService, sdkConfiguration);
	}

	@Override
	public void configure(B http) throws Exception {
		authFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
		JWTFilter filter = postProcess(authFilter);
		http.addFilterBefore(filter, LogoutFilter.class);
	}

	/**
	 * 设置许可的url。默认允许所有
	 * 
	 * @param urls
	 * @return
	 */
	public JWTConfiguration<T, B> permissiveRequestUrls(String... urls) {
		authFilter.setPermissiveUrl(urls);
		return this;
	}

	public JWTConfiguration<T, B> successHandler(AuthenticationSuccessHandler successHandler) {
		authFilter.setAuthenticationSuccessHandler(successHandler);
		return this;
	}

	public JWTConfiguration<T, B> failureHandler(AuthenticationFailureHandler failureHandler) {
		authFilter.setAuthenticationFailureHandler(failureHandler);
		return this;
	}
}
