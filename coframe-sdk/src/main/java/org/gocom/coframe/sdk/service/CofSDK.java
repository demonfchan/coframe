/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Jun 28, 2019
 *******************************************************************************/
package org.gocom.coframe.sdk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@Component
public class CofSDK {
	@Autowired
	private CofTokenService tokenService;

	@Autowired
	private CofAuthService authService;

	@Autowired
	private CofDictService dictService;

	/**
	 * 获取 Token 服务
	 * 
	 * @return the tokenService
	 */
	public CofTokenService getTokenService() {
		return tokenService;
	}

	/**
	 * 获取认证服务
	 * 
	 * @return the authService
	 */
	public CofAuthService getAuthService() {
		return authService;
	}

	/**
	 * 获取数据字典服务
	 * 
	 * @return the dictService
	 */
	public CofDictService getDictService() {
		return dictService;
	}
}
