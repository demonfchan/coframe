/*******************************************************************************
 * Copyright (c) 2001-2018 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on 2018年07月10日 16:55:43
 ******************************************************************************/

package com.primeton.iam.client;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;

import java.util.Map;

/**
 * @author huzd@primeton.com
 */
public class IAMClientUserAuthenticationConverter extends DefaultUserAuthenticationConverter {

	private static final String NICKNAME = "nickname";
	private static final String REG_DATE = "regdate";
	private static final String EMAIL = "email";
	private static final String USERID = "user_id";
	private static final String TENANTID = "tenant_id";

	public Authentication extractAuthentication(Map<String, ?> map) {
		if (map.containsKey(USERNAME)) {
//			for (String key : map.keySet()) {
//				System.out.println("key=" + key);
//				Object obj = map.get(key);
//				System.out.println(obj);
//				System.out.println();
//			}
			String username = String.valueOf(map.get(USERNAME));
			String email = String.valueOf(map.get(EMAIL));
			String nickname = String.valueOf(map.get(NICKNAME));
			String regdate = String.valueOf(map.get(REG_DATE));
			String userid = String.valueOf(map.get(USERID));
			String tenantid = String.valueOf(map.get(TENANTID));
			UserDetailsUserInfo userInfo = new UserDetailsUserInfo();
			userInfo.setUsername(username);
			userInfo.setNickname(nickname);
			userInfo.setEmail(email);
			userInfo.setAttribute(REG_DATE, regdate);
			userInfo.setUserid(userid);
			userInfo.setTenantid(tenantid);
			// TODO 是否处理授权信息????
			return new UsernamePasswordAuthenticationToken(userInfo, "N/A", userInfo.getAuthorities());
		}
		return null;
	}

}
