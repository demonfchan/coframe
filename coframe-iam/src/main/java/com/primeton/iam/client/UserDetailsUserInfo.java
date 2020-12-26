/*******************************************************************************
 * Copyright (c) 2001-2018 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on 2018年07月12日 18:24:50
 ******************************************************************************/

package com.primeton.iam.client;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.util.StringUtils;

/**
 * // TODO 需要对用户的权限信息等进行解析
 *
 * @author huzd@primeton.com
 */
public class UserDetailsUserInfo extends UserInfo implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String userid;

	private String tenantid;

	final String AUTHORITIES = AccessTokenConverter.AUTHORITIES;
	@SuppressWarnings("unchecked")
	private Collection<? extends GrantedAuthority> defaultAuthorities = Collections.EMPTY_LIST;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return getAuthorities(this.getAttributes());
	}

	@Override
	public String getPassword() {
		return "";
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getTenantid() {
		return tenantid;
	}

	public void setTenantid(String tenantid) {
		this.tenantid = tenantid;
	}

	private Collection<? extends GrantedAuthority> getAuthorities(Map<String, ?> map) {
		if (!map.containsKey(AUTHORITIES)) {
			return defaultAuthorities;
		}
		Object authorities = map.get(AUTHORITIES);
		if (authorities instanceof String) {
			return AuthorityUtils.commaSeparatedStringToAuthorityList((String) authorities);
		}
		if (authorities instanceof Collection) {
			return AuthorityUtils.commaSeparatedStringToAuthorityList(StringUtils.collectionToCommaDelimitedString((Collection<?>) authorities));
		}
		throw new IllegalArgumentException("Authorities must be either a String or a Collection");
	}

	public Collection<? extends GrantedAuthority> getDefaultAuthorities() {
		return defaultAuthorities;
	}

	public void setDefaultAuthorities(Collection<? extends GrantedAuthority> defaultAuthorities) {
		this.defaultAuthorities = defaultAuthorities;
	}

}
