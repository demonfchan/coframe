/*******************************************************************************
 * 
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on November 20, 2010
 *******************************************************************************/
package org.gocom.coframe.model;

import static org.gocom.coframe.CoframeConstants.TABLE_CORE_PREFIX;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.gocom.coframe.core.model.AbstractTreeModel;
import org.gocom.coframe.core.model.AbstractTreeModelCriteria;
import org.gocom.coframe.core.support.CoframeValidationGroups;

import io.swagger.annotations.ApiModelProperty;

/**
 * 字典类型
 *
 * @author qinsc (mailto:qinsc@primeton.com)
 */
@Entity
@Table(name = TABLE_CORE_PREFIX + "DICT_TYPE")
public class DictType extends AbstractTreeModel<DictType> {
	private static final long serialVersionUID = -7082342647125661992L;

	@ApiModelProperty("默认国际化区域，如果要求的区域即为此区域，直接返回 name。否则，需要去国际化表中查找对应语言的 name 值")
	@NotBlank(groups = { CoframeValidationGroups.Create.class })
	private String locale;

	/**
	 * 查询条件
	 * 
	 * @author qinsc (mailto:qinsc@primeton.com)
	 *
	 */
	public static class Criteria extends AbstractTreeModelCriteria {
		private String locale;

		/**
		 * @return the locale
		 */
		public String getLocale() {
			return locale;
		}

		/**
		 * @param locale the locale to set
		 */
		public void setLocale(String locale) {
			this.locale = locale;
		}
	}

	/**
	 * @return the locale
	 */
	public String getLocale() {
		return locale;
	}

	/**
	 * @param locale the locale to set
	 */
	public void setLocale(String locale) {
		this.locale = locale;
	}
}
