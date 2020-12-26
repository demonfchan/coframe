/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Feb 28, 2019
 *******************************************************************************/
package org.gocom.coframe.model;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

/**
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
public class UserPassword implements Serializable {
	private static final long serialVersionUID = 2201130263272319520L;
	@NotBlank
	private String oldPassword;
	@NotBlank
	private String newPassword;


	/**
	 * @return the oldPassword
	 */
	public String getOldPassword() {
		return oldPassword;
	}

	/**
	 * @param oldPassword the oldPassword to set
	 */
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	/**
	 * @return the newPassword
	 */
	public String getNewPassword() {
		return newPassword;
	}

	/**
	 * @param newPassword the newPassword to set
	 */
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
}