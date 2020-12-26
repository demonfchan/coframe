/*******************************************************************************
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on November 20, 2010
 *******************************************************************************/
package org.gocom.coframe.model;

import static org.gocom.coframe.CoframeConstants.TABLE_CORE_PREFIX;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import org.gocom.coframe.core.model.AbstractMainModel;
import org.gocom.coframe.core.model.AbstractMainModelCriteria;
import org.gocom.coframe.core.support.CoframeValidationGroups;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 用户
 *
 * @author qinsc (mailto:qinsc@primeton.com)
 */
@Entity
@Table(name = TABLE_CORE_PREFIX + "ROLE")
@ApiModel("角色")
public class Role extends AbstractMainModel {
	private static final long serialVersionUID = 7222377699951829783L;

	@ApiModelProperty("角色所属对象类型")
	private String ownerType;

	@ApiModelProperty("角色所属对象Id")
	private String ownerId;

	@ApiModelProperty("角色模板 id")
	@ManyToOne
	@JoinColumn(name = "role_tpl_id")
	@NotNull(groups = CoframeValidationGroups.Create.class)
	@Null(groups = { CoframeValidationGroups.Update.class })
	private RoleTpl roleTpl;

	/**
	 * @return the ownerType
	 */
	public String getOwnerType() {
		return ownerType;
	}

	/**
	 * @param ownerType the ownerType to set
	 */
	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	/**
	 * @return the ownerId
	 */
	public String getOwnerId() {
		return ownerId;
	}

	/**
	 * @param ownerId the ownerId to set
	 */
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	/**
	 * @return the roleTpl
	 */
	public RoleTpl getRoleTpl() {
		return roleTpl;
	}

	/**
	 * @param roleTpl the roleTpl to set
	 */
	public void setRoleTpl(RoleTpl roleTpl) {
		this.roleTpl = roleTpl;
	}

	/**
	 * 查询条件
	 * 
	 * @author qinsc (mailto:qinsc@primeton.com)
	 *
	 */
	public static class Criteria extends AbstractMainModelCriteria {
		private String ownerType;
		private String ownerId;
		private String roleTplId;
		private Boolean listPlatformRoles = false;

		/**
		 * @return the ownerType
		 */
		public String getOwnerType() {
			return ownerType;
		}

		/**
		 * @param ownerType the ownerType to set
		 */
		public void setOwnerType(String ownerType) {
			this.ownerType = ownerType;
		}

		/**
		 * @return the ownerId
		 */
		public String getOwnerId() {
			return ownerId;
		}

		/**
		 * @param ownerId the ownerId to set
		 */
		public void setOwnerId(String ownerId) {
			this.ownerId = ownerId;
		}

		/**
		 * @return the roleTplId
		 */
		public String getRoleTplId() {
			return roleTplId;
		}

		/**
		 * @param roleTplId the roleTplId to set
		 */
		public void setRoleTplId(String roleTplId) {
			this.roleTplId = roleTplId;
		}

		/**
		 * @return the listPlatformRoles
		 */
		public Boolean getListPlatformRoles() {
			return listPlatformRoles;
		}

		/**
		 * @param listPlatformRoles the listPlatformRoles to set
		 */
		public void setListPlatformRoles(Boolean listPlatformRoles) {
			this.listPlatformRoles = listPlatformRoles;
		}
	}
}
