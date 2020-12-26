/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Mar 4, 2019
 *******************************************************************************/
package org.gocom.coframe.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.Predicate;

import org.gocom.coframe.core.jpa.CoframeJpaRepository;
import org.gocom.coframe.model.AuthTplResGroup;
import org.gocom.coframe.model.AuthTplResGroup_;
import org.springframework.data.jpa.domain.Specification;

/**
 * 参与者与角色关联存储实体操作
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
public interface AuthTplResGroupRepository extends CoframeJpaRepository<AuthTplResGroup, String> {
	/**
	 * 构建查询条件
	 * 
	 * @param userId
	 * @param roleBind
	 * @return
	 */
	public default Specification<AuthTplResGroup> toSpecification(String authTplId, String resGroupId) {
		return (root, query, builder) -> {
			List<Predicate> predicates = new ArrayList<>();
			Optional.ofNullable(authTplId).ifPresent((value) -> predicates.add(builder.equal(root.get(AuthTplResGroup_.authTplId), value)));
			Optional.ofNullable(resGroupId).ifPresent((value) -> predicates.add(builder.equal(root.get(AuthTplResGroup_.resGroupId), value)));
			return builder.and(predicates.toArray(new Predicate[predicates.size()]));
		};
	}
}
