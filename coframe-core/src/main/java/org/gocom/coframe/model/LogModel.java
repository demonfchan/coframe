/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Apr 19, 2019
 *******************************************************************************/
package org.gocom.coframe.model;

import java.io.Serializable;

/**
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
public class LogModel implements Serializable {
	private static final long serialVersionUID = -1741259329119628467L;

	private String modelId;
	private String modelClassName;
	private Object model;

	/**
	 * @param modelClassName
	 * @param model
	 */
	public LogModel(String modelId, String modelClassName, Object model) {
		this.modelId = modelId;
		this.modelClassName = modelClassName;
		this.model = model;
	}

	/**
	 * @return the modelId
	 */
	public String getModelId() {
		return modelId;
	}

	/**
	 * @param modelId the modelId to set
	 */
	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	/**
	 * @return the modelClassName
	 */
	public String getModelClassName() {
		return modelClassName;
	}

	/**
	 * @param modelClassName the modelClassName to set
	 */
	public void setModelClassName(String modelClassName) {
		this.modelClassName = modelClassName;
	}

	/**
	 * @return the model
	 */
	public Object getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(Object model) {
		this.model = model;
	}
}
