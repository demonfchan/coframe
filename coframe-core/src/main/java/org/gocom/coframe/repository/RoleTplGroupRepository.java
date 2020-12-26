/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Mar 4, 2019
 *******************************************************************************/
package org.gocom.coframe.repository;

import java.util.List;

import javax.persistence.criteria.Predicate;

import org.gocom.coframe.core.jpa.CoframeJpaRepository;
import org.gocom.coframe.model.RoleTplGroup;
import org.springframework.data.jpa.domain.Specification;

/**
 * 角色模板实体操作
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
public interface RoleTplGroupRepository extends CoframeJpaRepository<RoleTplGroup, String> {
	/**
	 * 构造角色模板查询条件
	 * 
	 * @param criteria
	 * @return
	 */
	public default Specification<RoleTplGroup> toSpecification(RoleTplGroup.Criteria criteria) {
		return (root, query, builder) -> {
			List<Predicate> predicates = criteria.buildPredicates(root, builder);
			return builder.and(predicates.toArray(new Predicate[predicates.size()]));
		};
	}
}
