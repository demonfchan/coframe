/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Mar 27, 2019
 *******************************************************************************/
package org.gocom.coframe.model;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 资源
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@ApiModel("带操作的资源")
public class OperatableResource implements Serializable {
	private static final long serialVersionUID = -695093860771545956L;

	@ApiModelProperty("资源类型，当前类型有： MENU, FUNCTION")
	@NotBlank
	private String type;

	@ApiModelProperty("资源 id")
	@NotBlank
	private String id;

	@ApiModelProperty("需要进行的操作，如 ADD, REMOVE")
	@NotBlank
	private String operation;

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

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
	 * @return the operation
	 */
	public String getOperation() {
		return operation;
	}

	/**
	 * @param operation the operation to set
	 */
	public void setOperation(String operation) {
		this.operation = operation;
	}
}
