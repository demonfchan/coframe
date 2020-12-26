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

import org.gocom.coframe.CoframeConstants;
import org.gocom.coframe.core.model.AbstractTreeModel;
import org.gocom.coframe.core.model.AbstractTreeModelCriteria;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 工作组
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@Entity
@Table(name = TABLE_CORE_PREFIX + "WORKGROUP")
@ApiModel("工作组")
public class Workgroup extends AbstractTreeModel<Workgroup> {
	private static final long serialVersionUID = 1797666940193836633L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "org_id")
	@ApiModelProperty("所属机构")
	private Organization organization;

	@ApiModelProperty("机构类型")
	private String type;

	@ApiModelProperty("层级")
	private Integer level;

	@ApiModelProperty("工作组状态")
	private String status = CoframeConstants.STATUS_ENABLED;

	@ApiModelProperty("负责人Id")
	private String managerId;

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
		private String status;
		private String organizationId;

		/**
		 * @return the organizationId
		 */
		public String getOrganizationId() {
			return organizationId;
		}

		/**
		 * @param organizationId the organizationId to set
		 */
		public void setOrganizationId(String organizationId) {
			this.organizationId = organizationId;
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
	 * @return the managerId
	 */
	public String getManagerId() {
		return managerId;
	}

	/**
	 * @param managerId the managerId to set
	 */
	public void setManagerId(String managerId) {
		this.managerId = managerId;
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
