/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Mar 5, 2019
 *******************************************************************************/
package org.gocom.coframe.model;

import static org.gocom.coframe.CoframeConstants.TABLE_CORE_PREFIX;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.gocom.coframe.core.model.AbstractFixedPersistentModel;

/**
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@Entity
@Table(name = TABLE_CORE_PREFIX + "ORG_EMP_MAPPING")
public class OrgEmp extends AbstractFixedPersistentModel {
	private static final long serialVersionUID = -1611907670572661656L;

	@Column(name = "`ORG_ID`")
	private String orgId;

	@Column(name = "`EMP_ID`")
	private String empId;

	/**
	 * 
	 */
	public OrgEmp() {
	}

	/**
	 * @param orgId
	 * @param empId
	 */
	public OrgEmp(String orgId, String empId) {
		this.orgId = orgId;
		this.empId = empId;
	}

	/**
	 * @return the orgId
	 */
	public String getOrgId() {
		return orgId;
	}

	/**
	 * @param orgId the orgId to set
	 */
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	/**
	 * @return the empId
	 */
	public String getEmpId() {
		return empId;
	}

	/**
	 * @param empId the empId to set
	 */
	public void setEmpId(String empId) {
		this.empId = empId;
	}

}
