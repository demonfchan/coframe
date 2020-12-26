/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Mar 27, 2019
 *******************************************************************************/
package org.gocom.coframe.model;

import static org.gocom.coframe.CoframeConstants.TABLE_CORE_PREFIX;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.gocom.coframe.core.model.AbstractFixedPersistentModel;

import io.swagger.annotations.ApiModel;

/**
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@Entity
@Table(name = TABLE_CORE_PREFIX + "AUTH_TPL_RES_GROUP")
@ApiModel("权限模板与资源级的关联")
public class AuthTplResGroup extends AbstractFixedPersistentModel {
	private static final long serialVersionUID = 6590890116902438250L;

	@Column(name = "`AUTH_TPL_ID`")
	private String authTplId;

	@Column(name = "`RES_GROUP_ID`")
	private String resGroupId;
	
	/**
	 * @return the authTplId
	 */
	public String getAuthTplId() {
		return authTplId;
	}

	/**
	 * @param authTplId the authTplId to set
	 */
	public void setAuthTplId(String authTplId) {
		this.authTplId = authTplId;
	}

	/**
	 * @return the resGroupId
	 */
	public String getResGroupId() {
		return resGroupId;
	}

	/**
	 * @param resGroupId the resGroupId to set
	 */
	public void setResGroupId(String resGroupId) {
		this.resGroupId = resGroupId;
	}
}
