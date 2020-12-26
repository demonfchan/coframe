/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Jun 25, 2019
 *******************************************************************************/
package org.gocom.coframe.sdk.model;

import java.io.Serializable;

/**
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
public class CofModel implements Serializable {
	private static final long serialVersionUID = 3990822412873340712L;

	private String id;
	private String name;
	private String tenantId;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
