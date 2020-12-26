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
import org.gocom.coframe.model.Dimension;
import org.gocom.coframe.model.Dimension_;
import org.springframework.data.jpa.domain.Specification;

/**
 * 权限存储实体操作
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
public interface DimensionRepository extends CoframeJpaRepository<Dimension, String> {
	/**
	 * 构造权限查询条件
	 * 
	 * @param criteria
	 * @return
	 */
	public default Specification<Dimension> toSpecification(Dimension.Criteria criteria) {
		return (root, query, builder) -> {
			List<Predicate> predicates = criteria.buildPredicates(root, builder);
			Optional.ofNullable(criteria.getMain()).ifPresent((value) -> predicates.add(builder.equal(root.get(Dimension_.main), value)));
			return builder.and(predicates.toArray(new Predicate[predicates.size()]));
		};
	}
}
