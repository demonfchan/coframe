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
 * 工作组与员工关联
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@Entity
@Table(name = TABLE_CORE_PREFIX + "WORKGROUP_EMP_MAPPING")
public class WorkgroupEmp extends AbstractFixedPersistentModel {
	private static final long serialVersionUID = 2898976963349061071L;

	@Column(name = "`WORKGROUP_ID`")
	private String workgroupId;

	@Column(name = "`EMP_ID`")
	private String empId;

	/**
	 * 
	 */
	public WorkgroupEmp() {
	}

	/**
	 * @param workgroupId
	 * @param empId
	 */
	public WorkgroupEmp(String workgroupId, String empId) {
		this.workgroupId = workgroupId;
		this.empId = empId;
	}

	/**
	 * @return the workgroupId
	 */
	public String getWorkgroupId() {
		return workgroupId;
	}

	/**
	 * @param workgroupId the workgroupId to set
	 */
	public void setWorkgroupId(String workgroupId) {
		this.workgroupId = workgroupId;
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
