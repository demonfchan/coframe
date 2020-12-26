/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Feb 26, 2019
 *******************************************************************************/
package org.gocom.coframe.core.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * 抽像持久实体模型，有 id 属性
 *
 * @author wangwb (mailto:wangwb@primeton.com)
 */
@MappedSuperclass
public class AbstractFixedPersistentModel extends AbstractPersistentModel {
	private static final long serialVersionUID = -2869114447286616207L;
	
	/**
	 * 是否固定，为 true 的，不能修改，也不能删除
	 */
	@Column(name = "IS_FIXED")
	private boolean fixed;

	/**
	 * @return the fixed
	 */
	public boolean isFixed() {
		return fixed;
	}

	/**
	 * @param fixed the fixed to set
	 */
	public void setFixed(boolean fixed) {
		this.fixed = fixed;
	}
}
