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
import org.gocom.coframe.model.PartyAuth;
import org.gocom.coframe.model.PartyAuth_;
import org.springframework.data.jpa.domain.Specification;

/**
 * 参与者与角色关联存储实体操作
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
public interface PartyAuthRepository extends CoframeJpaRepository<PartyAuth, String> {
	/**
	 * 构建查询条件
	 * 
	 * @param criteria
	 * @return
	 */
	public default Specification<PartyAuth> toSpecification(PartyAuth.Criteria criteria) {
		return (root, query, builder) -> {
			List<Predicate> predicates = new ArrayList<>();
			Optional.ofNullable(criteria.getPartyType()).ifPresent((value) -> predicates.add(builder.equal(root.get(PartyAuth_.partyType), value)));
			Optional.ofNullable(criteria.getPartyId()).ifPresent((value) -> predicates.add(builder.equal(root.get(PartyAuth_.partyId), value)));
			Optional.ofNullable(criteria.getAuthType()).ifPresent((value) -> predicates.add(builder.equal(root.get(PartyAuth_.authType), value)));
			Optional.ofNullable(criteria.getAuthId()).ifPresent((value) -> predicates.add(builder.equal(root.get(PartyAuth_.authId), value)));
			Optional.ofNullable(criteria.getAuthOwnerType()).ifPresent((value) -> predicates.add(builder.equal(root.get(PartyAuth_.authOwnerType), value)));
			Optional.ofNullable(criteria.getAuthOwnerId()).ifPresent((value) -> predicates.add(builder.equal(root.get(PartyAuth_.authOwnerId), value)));
			return builder.and(predicates.toArray(new Predicate[predicates.size()]));
		};
	}
}
