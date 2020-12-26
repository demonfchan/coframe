/*******************************************************************************
 * Copyright (c) 2001-2018 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on 2018年07月06日 17:26:46
 ******************************************************************************/

package com.primeton.iam.authentication;

import java.io.Serializable;
import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author huzd@primeton.com
 */
public class RequestHeaderAuthenticationToken extends AbstractAuthenticationToken implements Serializable {
	private static final long serialVersionUID = -886521234613480464L;

	/**
	 * Creates a token with the supplied array of authorities.
	 *
	 * @param authorities the collection of <tt>GrantedAuthority</tt>s for the
	 *                    principal represented by this authentication object.
	 */
	public RequestHeaderAuthenticationToken(Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
	}

	@Override
	public Object getCredentials() {
		return "N/A";
	}

	@Override
	public Object getPrincipal() {
		return "N/A";
	}

}
