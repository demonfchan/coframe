/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Mar 27, 2019
 *******************************************************************************/
package org.gocom.coframe.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.Predicate;

import org.gocom.coframe.core.jpa.CoframeJpaRepository;
import org.gocom.coframe.model.AuthRes;
import org.gocom.coframe.model.AuthRes_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
public interface AuthResRepository extends CoframeJpaRepository<AuthRes, String> {
	/**
	 * 构建查询条件
	 * 
	 * @param criteria
	 * @return
	 */
	public default Specification<AuthRes> toSpecification(AuthRes.Criteria criteria) {
		return (root, query, builder) -> {
			List<Predicate> predicates = new ArrayList<>();
			Optional.ofNullable(criteria.getType()).ifPresent((value) -> predicates.add(builder.equal(root.get(AuthRes_.type), value)));
			Optional.ofNullable(criteria.getAuthType()).ifPresent((value) -> predicates.add(builder.equal(root.get(AuthRes_.authType), value)));
			Optional.ofNullable(criteria.getAuthId()).ifPresent((value) -> predicates.add(builder.equal(root.get(AuthRes_.authId), value)));
			Optional.ofNullable(criteria.getResType()).ifPresent((value) -> predicates.add(builder.equal(root.get(AuthRes_.resType), value)));
			Optional.ofNullable(criteria.getResId()).ifPresent((value) -> predicates.add(builder.equal(root.get(AuthRes_.resId), value)));
			return builder.and(predicates.toArray(new Predicate[predicates.size()]));
		};
	}

	@Query("select ar.resId from AuthRes ar where ar.authType =:authType and ar.authId = :authId and ar.resType = :resType")
	public List<String> listResIds(@Param("authType") String authType, @Param("authId") String authId, @Param("resType") String resType);
	
	@Query("select ar.authId from AuthRes ar where ar.authType =:authType and ar.resId = :resId and ar.resType = :resType")
	public List<String> listAuthIds(@Param("authType") String authType, @Param("resId") String resId, @Param("resType") String resType);
}