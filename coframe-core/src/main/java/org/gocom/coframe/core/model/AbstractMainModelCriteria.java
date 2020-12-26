/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Feb 27, 2019
 *******************************************************************************/
package org.gocom.coframe.core.model;

import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
public abstract class AbstractMainModelCriteria extends AbstractSimpleModelCriteria {
	@ApiModelProperty("编码")
	private String code;

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Predicate> buildPredicates(Root root, CriteriaBuilder builder) {
		List<Predicate> predicates = super.buildPredicates(root, builder);
		if (isUsingLikeQuery()) {
			Optional.ofNullable(code).ifPresent((value) -> predicates.add(builder.like(root.get(AbstractMainModel_.code), "%" + value + "%")));
		} else {
			Optional.ofNullable(code).ifPresent((value) -> predicates.add(builder.equal(root.get(AbstractMainModel_.code), value)));
		}
		return predicates;
	}
}
