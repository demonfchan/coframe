/*
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on 5/31/19 3:28 PM
 */
package org.gocom.coframe.sdk.model;

import java.util.Set;

public class CofRole extends CofMainModel {
	private static final long serialVersionUID = 1040338293330947620L;

	private String ownerType;
	private String ownerId;
	private Set<CofFunction> functions; // 用户拥有的所有功能

	public CofRole() {
	}
	
	public CofRole(String id) {
		super.setId(id);
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

	/**
	 * @return the functions
	 */
	public Set<CofFunction> getFunctions() {
		return functions;
	}

	/**
	 * @param functions the functions to set
	 */
	public void setFunctions(Set<CofFunction> functions) {
		this.functions = functions;
	}
}
