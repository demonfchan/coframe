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
import org.gocom.coframe.model.WorkgroupEmp;
import org.gocom.coframe.model.WorkgroupEmp_;
import org.springframework.data.jpa.domain.Specification;

/**
 * 工作组与员工关联存储实体操作
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
public interface WorkgroupEmpRepository extends CoframeJpaRepository<WorkgroupEmp, String> {
	/**
	 * 构建查询条件
	 * 
	 * @param workgroupId
	 * @param empId
	 * @return
	 */
	public default Specification<WorkgroupEmp> toSpecification(String workgroupId, String empId) {
		return (root, query, builder) -> {
			List<Predicate> predicates = new ArrayList<>();
			Optional.ofNullable(workgroupId).ifPresent((value) -> predicates.add(builder.equal(root.get(WorkgroupEmp_.workgroupId), value)));
			Optional.ofNullable(empId).ifPresent((value) -> predicates.add(builder.equal(root.get(WorkgroupEmp_.empId), value)));
			return builder.and(predicates.toArray(new Predicate[predicates.size()]));
		};
	}
}
