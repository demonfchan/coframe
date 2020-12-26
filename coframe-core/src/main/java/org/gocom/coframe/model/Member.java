/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Mar 12, 2019
 *******************************************************************************/
package org.gocom.coframe.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.gocom.coframe.core.support.CoframeValidationGroups;

/**
 * 
 * 成员
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
public class Member implements Serializable {
	private static final long serialVersionUID = 1650242521511733287L;

	/**
	 * 用户
	 */
	@NotNull(groups = {CoframeValidationGroups.Create.class})
	private User user;

	/**
	 * 角色集
	 */
	@NotNull(groups = {CoframeValidationGroups.Create.class})
	private List<Role> roles = new ArrayList<>();

	/**
	 * 所属对象类型
	 */
	@NotBlank(groups = {CoframeValidationGroups.Create.class})
	private String ownerType;

	/**
	 * 所属对象 id
	 */
	@NotBlank(groups = {CoframeValidationGroups.Create.class})
	private String ownerId;

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the roles
	 */
	public List<Role> getRoles() {
		return roles;
	}

	/**
	 * @param roles the roles to set
	 */
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	/**
	 * @return the ownerType
	 */
	public String getOwnerType() {
		return ownerType;
	}

	/**
	 * @param ownerType the ownerType to set
	 */
	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	/**
	 * @return the ownerId
	 */
	public String getOwnerId() {
		return ownerId;
	}

	/**
	 * @param ownerId the ownerId to set
	 */
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

}
