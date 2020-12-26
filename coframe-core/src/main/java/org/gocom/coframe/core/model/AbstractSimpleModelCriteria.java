/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Feb 27, 2019
 *******************************************************************************/
package org.gocom.coframe.core.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.gocom.coframe.CoframeConstants;
import org.gocom.coframe.sdk.CofContext;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@ApiModel
public abstract class AbstractSimpleModelCriteria {
	@ApiModelProperty("Id")
	private String id;
	
	@ApiModelProperty("名称")
	private String name;
	
	@ApiModelProperty("是否使用模糊查询")
	private boolean usingLikeQuery = true;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the usingLikeQuery
	 */
	public boolean isUsingLikeQuery() {
		return usingLikeQuery;
	}

	/**
	 * @param usingLikeQuery the usingLikeQuery to set
	 */
	public void setUsingLikeQuery(boolean usingLikeQuery) {
		this.usingLikeQuery = usingLikeQuery;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Predicate> buildPredicates(Root root, CriteriaBuilder builder) {
		List<Predicate> predicates = new ArrayList<Predicate>();
		Optional.ofNullable(id).ifPresent((value) -> predicates.add(builder.equal(root.get(AbstractSimpleModel_.id), value)));
		// 从 context 中取出租户id, 添加过滤条件
		Object tenantId = CofContext.getContext().get(CoframeConstants.CONTEXT_TENANT_ID);
		Optional.ofNullable(tenantId).ifPresent((value) -> predicates.add(builder.equal(root.get(AbstractSimpleModel_.tenantId), (String)value)));
		if (usingLikeQuery) {
			Optional.ofNullable(name).ifPresent((value) -> predicates.add(builder.like(root.get(AbstractSimpleModel_.name), "%" + value + "%")));
		} else {
			Optional.ofNullable(name).ifPresent((value) -> predicates.add(builder.equal(root.get(AbstractSimpleModel_.name), value)));
		}
		return predicates;
	}
}
