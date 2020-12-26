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
@Table(name = TABLE_CORE_PREFIX + "POSITION_EMP_MAPPING")
public class PositionEmp extends AbstractFixedPersistentModel {
	private static final long serialVersionUID = 578866196696504309L;

	@Column(name = "`POSITION_ID`")
	private String positionId;

	@Column(name = "`EMP_ID`")
	private String empId;

	/**
	 * 
	 */
	public PositionEmp() {
	}

	/**
	 * @param positionId
	 * @param empId
	 */
	public PositionEmp(String positionId, String empId) {
		this.positionId = positionId;
		this.empId = empId;
	}

	/**
	 * @return the positionId
	 */
	public String getPositionId() {
		return positionId;
	}

	/**
	 * @param positionId the positionId to set
	 */
	public void setPositionId(String positionId) {
		this.positionId = positionId;
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
