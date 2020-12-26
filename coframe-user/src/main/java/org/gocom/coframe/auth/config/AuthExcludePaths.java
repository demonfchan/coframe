/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Jun 4, 2019
 *******************************************************************************/
package org.gocom.coframe.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取自户自定义的不需要进行认证的路径集合，支持以 * 号通配。配置支持数据，
 * 如下例所示： coframe: auth: exclude-paths: /js/**, /images/**
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@Component
@ConfigurationProperties(prefix = "coframe.auth")
public class AuthExcludePaths {
	private String[] excludePaths = new String[0];

	/**
	 * @return the excludePaths
	 */
	public String[] getExcludePaths() {
		return excludePaths;
	}

	/**
	 * @param excludePaths the excludePaths to set
	 */
	public void setExcludePaths(String[] excludePaths) {
		this.excludePaths = excludePaths;
	}
}
