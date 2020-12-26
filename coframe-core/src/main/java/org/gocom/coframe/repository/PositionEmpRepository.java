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
import org.gocom.coframe.model.PositionEmp;
import org.gocom.coframe.model.PositionEmp_;
import org.springframework.data.jpa.domain.Specification;

/**
 * 岗位与员工关联存储实体操作
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
public interface PositionEmpRepository extends CoframeJpaRepository<PositionEmp, String> {
	/**
	 * 构建查询条件
	 * 
	 * @param positionId
	 * @param empId
	 * @return
	 */
	public default Specification<PositionEmp> toSpecification(String positionId, String empId) {
		return (root, query, builder) -> {
			List<Predicate> predicates = new ArrayList<>();
			Optional.ofNullable(positionId).ifPresent((value) -> predicates.add(builder.equal(root.get(PositionEmp_.positionId), value)));
			Optional.ofNullable(empId).ifPresent((value) -> predicates.add(builder.equal(root.get(PositionEmp_.empId), value)));
			return builder.and(predicates.toArray(new Predicate[predicates.size()]));
		};
	}
}
