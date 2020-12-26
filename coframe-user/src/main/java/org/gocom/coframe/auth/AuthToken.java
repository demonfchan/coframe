/**
 *
 */
package org.gocom.coframe.auth;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.gocom.coframe.sdk.model.CofUser;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author qinscx
 *
 */
public class AuthToken extends AbstractAuthenticationToken {
	private static final long serialVersionUID = -4070922420100279970L;

	private UserDetails principal;
	private String credentials;
	private String token;
	private CofUser loginedUser;
	private Map<String, Object> infos = new HashMap<>(); // 供扩展

	public AuthToken(String token) {
		super(Collections.emptyList());
		this.token = token;
	}

	public AuthToken(CofUser loginedUser, UserDetails principal, String token, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.loginedUser = loginedUser;
		this.principal = principal;
		this.token = token;
	}

	@Override
	public void setDetails(Object details) {
		super.setDetails(details);
		this.setAuthenticated(true);
	}

	@Override
	public Object getCredentials() {
		return credentials;
	}

	@Override
	public Object getPrincipal() {
		return principal;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * @return the loginedUser
	 */
	public CofUser getLoginedUser() {
		return loginedUser;
	}

	/**
	 * @param loginedUser the loginedUser to set
	 */
	public void setLoginedUser(CofUser loginedUser) {
		this.loginedUser = loginedUser;
	}

	/**
	 * @param principal the principal to set
	 */
	public void setPrincipal(UserDetails principal) {
		this.principal = principal;
	}

	/**
	 * @param credentials the credentials to set
	 */
	public void setCredentials(String credentials) {
		this.credentials = credentials;
	}

	public Map<String, Object> getInfos() {
		return infos;
	}

	public void setInfos(Map<String, Object> infos) {
		this.infos = infos;
	}
}