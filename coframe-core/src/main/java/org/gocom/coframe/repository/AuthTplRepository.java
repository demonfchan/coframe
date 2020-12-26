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
import org.gocom.coframe.model.AuthTpl;
import org.springframework.data.jpa.domain.Specification;

/**
 * 认证模板存储实体操作
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
public interface AuthTplRepository extends CoframeJpaRepository<AuthTpl, String>{
	/**
	 * 构造权限模板查询条件
	 * 
	 * @param criteria
	 * @return
	 */
	public default Specification<AuthTpl> toSpecification(AuthTpl.Criteria criteria) {
		return (root, query, builder) -> {
			// 添加通用查询条件
			List<Predicate> predicates = criteria.buildPredicates(root, builder);
			return builder.and(predicates.toArray(new Predicate[predicates.size()]));
		};
	}
}
