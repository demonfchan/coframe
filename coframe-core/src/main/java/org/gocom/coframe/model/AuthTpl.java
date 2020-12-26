/*******************************************************************************
 * 
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on November 20, 2010
 *******************************************************************************/
package org.gocom.coframe.model;

import static org.gocom.coframe.CoframeConstants.TABLE_CORE_PREFIX;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Null;

import org.gocom.coframe.core.model.AbstractSimpleModel;
import org.gocom.coframe.core.model.AbstractSimpleModelCriteria;
import org.gocom.coframe.core.support.CoframeValidationGroups;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;

/**
 * 权限模板
 *
 * @author qinsc (mailto:qinsc@primeton.com)
 */
@Entity
@Table(name = TABLE_CORE_PREFIX + "AUTH_TEMPLATE")
public class AuthTpl extends AbstractSimpleModel {
	private static final long serialVersionUID = 7307105948374242291L;

	@OneToMany(mappedBy = "authTpl", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<RoleTpl> roleTpls;

	@Transient
	@ApiModelProperty("权限模板包含的根资源组集") // 创建时可以带过来进行关联，更新时不允许，只能通过 service 中的增减根资源组方法更新关联
	@Null(groups = { CoframeValidationGroups.Update.class })
	private List<ResGroup> rootResGroups;

	/**
	 * 查询条件
	 * 
	 * @author qinsc (mailto:qinsc@primeton.com)
	 *
	 */
	public static class Criteria extends AbstractSimpleModelCriteria {
	}

	/**
	 * @return the roleTpls
	 */
	public List<RoleTpl> getRoleTpls() {
		return roleTpls;
	}

	/**
	 * @param roleTpls the roleTpls to set
	 */
	public void setRoleTpls(List<RoleTpl> roleTpls) {
		this.roleTpls = roleTpls;
	}

	/**
	 * @return the rootResGroups
	 */
	public List<ResGroup> getRootResGroups() {
		return rootResGroups;
	}

	/**
	 * @param rootResGroups the rootResGroups to set
	 */
	public void setRootResGroups(List<ResGroup> rootResGroups) {
		this.rootResGroups = rootResGroups;
	}

}
