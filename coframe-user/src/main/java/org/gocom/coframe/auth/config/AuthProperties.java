/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Feb 28, 2019
 *******************************************************************************/
package org.gocom.coframe.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 认证属性
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@Configuration
public class AuthProperties {
	/**
	 * token超时时间, 默认一小时。超时后，token 失效，用户需要重新登陆以获取新的 token
	 */
	@Value("${coframe.auth.token-expired:3600}")
	private int tokenExpired; 
	
	/**
	 * token刷新时间, 默认20分钟，代表 token 有效期内的最后20分钟，是 token 的可刷新时间
	 */
	@Value("${coframe.auth.token-refresh-interval:1200}")
	private int tokenRefreshInterval; 
	
	
	
	/**
	 * @return the tokenExpired
	 */
	public int getTokenExpired() {
		return tokenExpired;
	}

	/**
	 * @param tokenExpired the tokenExpired to set
	 */
	public void setTokenExpired(int tokenExpired) {
		this.tokenExpired = tokenExpired;
	}

	/**
	 * @return the tokenRefreshInterval
	 */
	public int getTokenRefreshInterval() {
		return tokenRefreshInterval;
	}

	/**
	 * @param tokenRefreshInterval the tokenRefreshInterval to set
	 */
	public void setTokenRefreshInterval(int tokenRefreshInterval) {
		this.tokenRefreshInterval = tokenRefreshInterval;
	}

}
