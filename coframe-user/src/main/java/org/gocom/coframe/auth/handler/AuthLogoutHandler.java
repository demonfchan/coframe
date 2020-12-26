/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Jun 3, 2019
 *******************************************************************************/
package org.gocom.coframe.auth.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gocom.coframe.CoframeConstants;
import org.gocom.coframe.auth.CoframeUserDetailsService;
import org.gocom.coframe.sdk.CofContext;
import org.gocom.coframe.sdk.model.CofUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.LogoutHandler;

/**
 * 登出时的处理
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
public class AuthLogoutHandler implements LogoutHandler {
	private CoframeUserDetailsService userDetailsService;

	public AuthLogoutHandler(CoframeUserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		String userName = null;
		if (authentication == null) {
			CofUser user = CofContext.getContext().get(CoframeConstants.CONTEXT_LOGINED_USER, CofUser.class);
			if (user != null) {
				userName = user.getName();
			}
		} else {
			UserDetails user = (UserDetails) authentication.getPrincipal();
			if (user != null) {
				userName = user.getUsername();
			}
		}
		if (userName != null) {
			userDetailsService.deleteUserLoginInfo(userName);
		}
	}
}
