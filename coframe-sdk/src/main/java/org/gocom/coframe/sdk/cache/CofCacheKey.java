/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Jun 3, 2019
 *******************************************************************************/
package org.gocom.coframe.sdk.cache;

import static org.gocom.coframe.sdk.util.PatternUtil.fmtReg;
import static org.gocom.coframe.sdk.util.PatternUtil.fmt;

import static org.gocom.coframe.sdk.CofConstants.COF;

import static org.gocom.coframe.sdk.CofConstants.ROLE_ID;
import static org.gocom.coframe.sdk.CofConstants.TO_ROLE;
import static org.gocom.coframe.sdk.CofConstants.TO_USER;
import static org.gocom.coframe.sdk.CofConstants.USER_NAME;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@Component
public class CofCacheKey {
	@Value("${coframe.cache.key-namespace:cof-standalone}")
	protected String keyNameSpace;

	/**
	 * @return the keyNameSpace
	 */
	public String getKeyNameSpace() {
		return keyNameSpace;
	}

	//
	// key
	//
	
	/**
	 * userName -> user
	 *
	 * @param userName
	 * @return
	 */
	public String userName2User(String userName) {
		return COF + keyNameSpace + USER_NAME + userName + TO_USER;
	}

	/**
	 * roleId - role
	 * 
	 * @param roleId
	 * @return
	 */
	public String roleId2Role(String roleId) {
		return COF + keyNameSpace + ROLE_ID + roleId + TO_ROLE;
	}

	//
	// redis key pattern
	//
	public String userName2UserPattern() {
		return fmt(COF + keyNameSpace + USER_NAME) + "*" + fmt(TO_USER);
	}

	//
	// reg pattern
	//

	/**
	 * 校验是否是 userName2User key 的正则表达式
	 * 
	 * @return
	 */
	public String userName2UserRegExp() {
		return "^" + fmtReg(COF + keyNameSpace + USER_NAME) + ".*?" + fmtReg(TO_USER) + "$";
	}

//	
//	public static void main(String[] args) {
//		CofCacheKey kk = new CofCacheKey();
//		kk.keyNameSpace = "cof-qinsc";
//		String k = kk.userName2User("qinscxxx");
//		System.out.println(k.matches(kk.userName2UserRegExp()));
//	}
}
