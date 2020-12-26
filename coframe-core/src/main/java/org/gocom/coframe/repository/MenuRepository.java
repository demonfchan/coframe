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
import org.gocom.coframe.model.Menu;
import org.gocom.coframe.model.Menu_;
import org.springframework.data.jpa.domain.Specification;

/**
 * 权限存储实体操作
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
public interface MenuRepository extends CoframeJpaRepository<Menu, String> {
	/**
	 * 构造权限查询条件
	 * 
	 * @param criteria
	 * @return
	 */
	public default Specification<Menu> toSpecification(Menu.Criteria criteria) {
		return (root, query, builder) -> {
			// 添加通用查询条件
			List<Predicate> predicates = criteria.buildPredicates(root, builder);
			// 添加特定查询条件
			Optional.ofNullable(criteria.getGroupId()).ifPresent(
					value -> predicates.add(builder.equal(root.get(Menu_.groupId), value))
				);
			return builder.and(predicates.toArray(new Predicate[predicates.size()]));
		};
	}
}
