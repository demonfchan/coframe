/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Feb 28, 2019
 *******************************************************************************/
package org.gocom.coframe.sdk.model;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

/**
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
public class CofLoginUser implements Serializable {
	private static final long serialVersionUID = 7290337869591585918L;

	@NotBlank
	private String username;
	@NotBlank
	private String password;

	private String tenantId; // 可选的租户Id，如果带了，将去指定的租户中查询用户

	public CofLoginUser() {
	}

	/**
	 * @param username
	 * @param password
	 */
	public CofLoginUser(String username, String password) {
		this.username = username;
		this.password = password;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the tenantId
	 */
	public String getTenantId() {
		return tenantId;
	}

	/**
	 * @param tenantId the tenantId to set
	 */
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
}