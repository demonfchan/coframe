/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Feb 27, 2019
 *******************************************************************************/
package org.gocom.coframe.model;

import static org.gocom.coframe.CoframeConstants.TABLE_CORE_PREFIX;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.groups.ConvertGroup;

import org.gocom.coframe.core.model.AbstractTreeModel;
import org.gocom.coframe.core.model.AbstractTreeModelCriteria;
import org.gocom.coframe.core.support.CoframeValidationGroups;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@Entity
@Table(name = TABLE_CORE_PREFIX + "POSITION")
@ApiModel("岗位")
public class Position extends AbstractTreeModel<Position> {
	private static final long serialVersionUID = -6067591311645564292L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "org_id")
	@Valid
	@NotNull(groups = CoframeValidationGroups.Create.class)
	@ConvertGroup(from = CoframeValidationGroups.Create.class, to = CoframeValidationGroups.Association.class)
	@ConvertGroup(from = CoframeValidationGroups.Update.class, to = CoframeValidationGroups.Association.class)
	@ApiModelProperty("所属机构")
	private Organization organization;

	@ApiModelProperty("岗位类型")
	private String type;

	@ApiModelProperty("岗位状态")
	private String status;

	@ApiModelProperty("岗位层级")
	private Integer level;

	@ApiModelProperty("同级序号")
	private Integer sortNo;

	/**
	 * 查询条件
	 * 
	 * @author qinsc (mailto:qinsc@primeton.com)
	 *
	 */
	public static class Criteria extends AbstractTreeModelCriteria {
		private String type;
		private String orgId;
		private String orgName;
		private String orgCode;

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
		 * @return the orgName
		 */
		public String getOrgName() {
			return orgName;
		}

		/**
		 * @param orgName the orgName to set
		 */
		public void setOrgName(String orgName) {
			this.orgName = orgName;
		}

		/**
		 * @return the orgCode
		 */
		public String getOrgCode() {
			return orgCode;
		}

		/**
		 * @param orgCode the orgCode to set
		 */
		public void setOrgCode(String orgCode) {
			this.orgCode = orgCode;
		}
	}

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
	 * @return the organization
	 */
	public Organization getOrganization() {
		return organization;
	}

	/**
	 * @param organization the organization to set
	 */
	public void setOrganization(Organization organization) {
		this.organization = organization;
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
	 * @return the level
	 */
	public Integer getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(Integer level) {
		this.level = level;
	}

	/**
	 * @return the sortNo
	 */
	public Integer getSortNo() {
		return sortNo;
	}

	/**
	 * @param sortNo the sortNo to set
	 */
	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}
}
