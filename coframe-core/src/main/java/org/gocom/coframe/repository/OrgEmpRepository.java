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
import org.gocom.coframe.model.OrgEmp;
import org.gocom.coframe.model.OrgEmp_;
import org.springframework.data.jpa.domain.Specification;

/**
 * 机构与员工关联存储实体操作
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
public interface OrgEmpRepository extends CoframeJpaRepository<OrgEmp, String> {
	/**
	 * 构建查询条件
	 * 
	 * @param userId
	 * @param roleId
	 * @return
	 */
	public default Specification<OrgEmp> toSpecification(String orgId, String empId) {
		return (root, query, builder) -> {
			List<Predicate> predicates = new ArrayList<>();
			Optional.ofNullable(orgId).ifPresent((value) -> predicates.add(builder.equal(root.get(OrgEmp_.orgId), value)));
			Optional.ofNullable(empId).ifPresent((value) -> predicates.add(builder.equal(root.get(OrgEmp_.empId), value)));
			return builder.and(predicates.toArray(new Predicate[predicates.size()]));
		};
	}
}
