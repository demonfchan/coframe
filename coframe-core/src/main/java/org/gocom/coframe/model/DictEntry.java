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
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.gocom.coframe.CoframeConstants;
import org.gocom.coframe.core.model.AbstractTreeModel;
import org.gocom.coframe.core.model.AbstractTreeModelCriteria;
import org.gocom.coframe.core.support.CoframeValidationGroups;

import io.swagger.annotations.ApiModelProperty;

/**
 * 字典项
 *
 * @author qinsc (mailto:qinsc@primeton.com)
 */
@Entity
@Table(name = TABLE_CORE_PREFIX + "DICT_ENTRY")
public class DictEntry extends AbstractTreeModel<DictEntry> {
	private static final long serialVersionUID = 8068524419079007298L;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "dict_type_id")
	private DictType dictType;

	private String status = CoframeConstants.STATUS_ENABLED;

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
		private String dictTypeName; // 字典类型名称
		private String dictTypeCode;// 字典类型编码
		private String dictTypeId; // 字典类型 Id
		private String status;
		private String locale;

		/**
		 * @return the dictTypeId
		 */
		public String getDictTypeId() {
			return dictTypeId;
		}

		/**
		 * @param dictTypeId the dictTypeId to set
		 */
		public void setDictTypeId(String dictTypeId) {
			this.dictTypeId = dictTypeId;
		}

		/**
		 * @return the dictTypeName
		 */
		public String getDictTypeName() {
			return dictTypeName;
		}

		/**
		 * @param dictTypeName the dictTypeName to set
		 */
		public void setDictTypeName(String dictTypeName) {
			this.dictTypeName = dictTypeName;
		}

		/**
		 * @return the dictTypeCode
		 */
		public String getDictTypeCode() {
			return dictTypeCode;
		}

		/**
		 * @param dictTypeCode the dictTypeCode to set
		 */
		public void setDictTypeCode(String dictTypeCode) {
			this.dictTypeCode = dictTypeCode;
		}

		/**
		 * @return the status
		 */
		public String getStatus() {
			return status;
		}

		/**
		 * @param status the status to set
		 */
		public void setStatus(String status) {
			this.status = status;
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

	/**
	 * @return the dictType
	 */
	public DictType getDictType() {
		return dictType;
	}

	/**
	 * @param dictType the dictType to set
	 */
	public void setDictType(DictType dictType) {
		this.dictType = dictType;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
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
