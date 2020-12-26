/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Jun 3, 2019
 *******************************************************************************/
package org.gocom.coframe.auth.up;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gocom.coframe.auth.CoframeUserDetailsService;
import org.gocom.coframe.sdk.CofConstants;
import org.gocom.coframe.sdk.model.CofUser;
import org.gocom.coframe.sdk.util.JsonUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * 用户名与密码校验成功处理类
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
public class UPSuccessHandler implements AuthenticationSuccessHandler {
	private CoframeUserDetailsService userDetailsService;

	public UPSuccessHandler(CoframeUserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		CofUser cofUser = userDetailsService.saveUserLoginInfo((UserDetails) authentication.getPrincipal());
		
		// 触发一次数据缓存
		userDetailsService.getLoginedUser(cofUser.getName());
		// 将 token 放到返回的 http header 中
		response.setHeader(CofConstants.HHN_AUTHORIZATION, cofUser.getToken());
		// 返回用户基本信息
		CofUser user = new CofUser();
		user.setId(cofUser.getId());
		user.setName(cofUser.getName());
		user.setTenantId(cofUser.getTenantId());
		user.setToken(cofUser.getToken());
		response.getWriter().write(JsonUtil.toJson(user));
		response.getWriter().flush();
	}
}
