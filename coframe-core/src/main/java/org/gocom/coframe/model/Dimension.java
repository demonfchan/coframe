/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Mar 26, 2019
 *******************************************************************************/
package org.gocom.coframe.model;

import static org.gocom.coframe.CoframeConstants.TABLE_CORE_PREFIX;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.gocom.coframe.core.model.AbstractMainModel;
import org.gocom.coframe.core.model.AbstractMainModelCriteria;

import io.swagger.annotations.ApiModel;

/**
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@Entity
@Table(name = TABLE_CORE_PREFIX + "DIMENSION")
@ApiModel("维度")
public class Dimension extends AbstractMainModel {
	private static final long serialVersionUID = 2559519327633355551L;

	@Column(name = "`IS_MAIN`")
	private boolean main;

	/**
	 * 查询条件
	 * 
	 * @author qinsc (mailto:qinsc@primeton.com)
	 *
	 */
	public static class Criteria extends AbstractMainModelCriteria {
		private Boolean main;

		/**
		 * @return the main
		 */
		public Boolean getMain() {
			return main;
		}

		/**
		 * @param main the main to set
		 */
		public void setMain(Boolean main) {
			this.main = main;
		}
	}

	/**
	 * @return the main
	 */
	public boolean isMain() {
		return main;
	}

	/**
	 * @param main the main to set
	 */
	public void setMain(boolean main) {
		this.main = main;
	}
}
