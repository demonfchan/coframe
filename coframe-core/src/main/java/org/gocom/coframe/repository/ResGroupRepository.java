/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Mar 4, 2019
 *******************************************************************************/
package org.gocom.coframe.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.Predicate;

import org.gocom.coframe.core.jpa.CoframeJpaRepository;
import org.gocom.coframe.model.ResGroup;
import org.gocom.coframe.model.ResGroup_;
import org.springframework.data.jpa.domain.Specification;

/**
 * 资源组存储实体操作
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
public interface ResGroupRepository extends CoframeJpaRepository<ResGroup, String> {
	/**
	 * 构造查询条件
	 * 
	 * @param criteria
	 * @return
	 */
	public default Specification<ResGroup> toSpecification(ResGroup.Criteria criteria) {
		return (root, query, builder) -> {
			List<Predicate> predicates = criteria.buildPredicates(root, builder);
			Optional.ofNullable(criteria.getType()).ifPresent((value) -> predicates.add(builder.equal(root.get(ResGroup_.type), value)));
			return builder.and(predicates.toArray(new Predicate[predicates.size()]));
		};
	}
}
