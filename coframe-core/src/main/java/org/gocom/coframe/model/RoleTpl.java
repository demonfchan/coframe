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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import org.gocom.coframe.core.model.AbstractSimpleModel;
import org.gocom.coframe.core.model.AbstractSimpleModelCriteria;
import org.gocom.coframe.core.support.CoframeValidationGroups;

/**
 * 角色模板
 *
 * @author qinsc (mailto:qinsc@primeton.com)
 */
@Entity
@Table(name = TABLE_CORE_PREFIX + "ROLE_TEMPLATE")
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", resolver = CoframeObjectIdResolver.class,scope = RoleTpl.class)
public class RoleTpl extends AbstractSimpleModel {
	private static final long serialVersionUID = -2638611024374228910L;

	@ManyToOne
	@JoinColumn(name = "auth_tpl_id")
	@NotNull(groups = { CoframeValidationGroups.Create.class })
	@Null(groups = { CoframeValidationGroups.Update.class })
	private AuthTpl authTpl;

	@ManyToOne
	@JoinColumn(name = "groupId")
	private RoleTplGroup tplGroup;

	/**
	 * 查询条件
	 * 
	 * @author qinsc (mailto:qinsc@primeton.com)
	 *
	 */
	public static class Criteria extends AbstractSimpleModelCriteria {
		private String authTplId;
		private String groupId;

		/**
		 * @return the groupId
		 */
		public String getGroupId() {
			return groupId;
		}

		/**
		 * @param groupId the groupId to set
		 */
		public void setGroupId(String groupId) {
			this.groupId = groupId;
		}

		/**
		 * @return the authTplId
		 */
		public String getAuthTplId() {
			return authTplId;
		}

		/**
		 * @param authTplId the authTplId to set
		 */
		public void setAuthTplId(String authTplId) {
			this.authTplId = authTplId;
		}
	}

	/**
	 * @return the authTpl
	 */
	public AuthTpl getAuthTpl() {
		return authTpl;
	}

	/**
	 * @param authTpl the authTpl to set
	 */
	public void setAuthTpl(AuthTpl authTpl) {
		this.authTpl = authTpl;
	}

	/**
	 * @return the tplGroup
	 */
	public RoleTplGroup getTplGroup() {
		return tplGroup;
	}

	/**
	 * @param tplGroup the tplGroup to set
	 */
	public void setTplGroup(RoleTplGroup tplGroup) {
		this.tplGroup = tplGroup;
	}
}
