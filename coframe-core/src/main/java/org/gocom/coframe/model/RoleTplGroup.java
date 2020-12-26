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

import org.gocom.coframe.core.model.AbstractSimpleModel;
import org.gocom.coframe.core.model.AbstractSimpleModelCriteria;

/**
 * 角色模板组
 *
 * @author qinsc (mailto:qinsc@primeton.com)
 */
@Entity
@Table(name = TABLE_CORE_PREFIX + "ROLE_TEMPLATE_GROUP")
public class RoleTplGroup extends AbstractSimpleModel {
	private static final long serialVersionUID = 7514327115119923923L;

	/**
	 * 查询条件
	 * 
	 * @author qinsc (mailto:qinsc@primeton.com)
	 *
	 */
	public static class Criteria extends AbstractSimpleModelCriteria {
	}
}
