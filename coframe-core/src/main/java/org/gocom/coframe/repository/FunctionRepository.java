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
import org.gocom.coframe.model.Function;
import org.gocom.coframe.model.Function_;
import org.springframework.data.jpa.domain.Specification;

/**
 * 权限存储实体操作
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
public interface FunctionRepository extends CoframeJpaRepository<Function, String> {
	/**
	 * 构造权限查询条件
	 * 
	 * @param criteria
	 * @return
	 */
	public default Specification<Function> toSpecification(Function.Criteria criteria) {
		return (root, query, builder) -> {
			// 添加通用查询条件
			List<Predicate> predicates = criteria.buildPredicates(root, builder);
			// 添加特定查询条件
			Optional.ofNullable(criteria.getType()).ifPresent((value) -> predicates.add(builder.equal(root.get(Function_.type), value)));
			Optional.ofNullable(criteria.getGroupId()).ifPresent((value) -> predicates.add(builder.equal(root.get(Function_.groupId), value)));
			return builder.and(predicates.toArray(new Predicate[predicates.size()]));
		};
	}
}
