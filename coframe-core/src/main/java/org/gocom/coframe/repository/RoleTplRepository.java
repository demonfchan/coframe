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
import org.gocom.coframe.model.AuthTpl_;
import org.gocom.coframe.model.RoleTpl;
import org.gocom.coframe.model.RoleTplGroup_;
import org.gocom.coframe.model.RoleTpl_;
import org.springframework.data.jpa.domain.Specification;

/**
 * 角色模板实体操作
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
public interface RoleTplRepository extends CoframeJpaRepository<RoleTpl, String> {
	/**
	 * 构造角色模板查询条件
	 * 
	 * @param criteria
	 * @return
	 */
	public default Specification<RoleTpl> toSpecification(RoleTpl.Criteria criteria) {
		return (root, query, builder) -> {
			List<Predicate> predicates = criteria.buildPredicates(root, builder);
			Optional.ofNullable(criteria.getAuthTplId()).ifPresent((value) -> predicates.add(builder.equal(root.get(RoleTpl_.authTpl).get(AuthTpl_.id), value)));
			Optional.ofNullable(criteria.getGroupId()).ifPresent((value) -> predicates.add(builder.equal(root.get(RoleTpl_.tplGroup).get(RoleTplGroup_.id), value)));
			return builder.and(predicates.toArray(new Predicate[predicates.size()]));
		};
	}
}
