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
import java.util.Set;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.gocom.coframe.CoframeConstants;
import org.gocom.coframe.core.jpa.CoframeJpaRepository;
import org.gocom.coframe.model.Function;
import org.gocom.coframe.model.Menu;
import org.gocom.coframe.model.PartyAuth;
import org.gocom.coframe.model.PartyAuth_;
import org.gocom.coframe.model.Role;
import org.gocom.coframe.model.User;
import org.gocom.coframe.model.User_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 用户存储实体操作
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
public interface UserRepository extends CoframeJpaRepository<User, String> {
	/**
	 * 构造用户查询条件
	 * 
	 * @param criteria
	 * @return
	 */
	public default Specification<User> toSpecification(User.Criteria criteria) {
		return (root, query, builder) -> {
			List<Predicate> predicates = criteria.buildPredicates(root, builder);
			Optional.ofNullable(criteria.getStatus()).ifPresent((value) -> predicates.add(builder.equal(root.get(User_.status), value)));
			Optional.ofNullable(criteria.getEmployeeId()).ifPresent((value) -> predicates.add(builder.equal(root.get(User_.EMPLOYEE_ID), value)));
			// 根据绑定状态过滤
			Optional.ofNullable(criteria.getBindState()).ifPresent((value) -> {
				// 只查询已绑定人员的用户
				if (CoframeConstants.BINDED_ONLY.equals(value)) {
					predicates.add(builder.isNotNull(root.get(User_.employeeId)));
				} else
				// 只查询未绑定人员的用户
				if (CoframeConstants.UNBINDED_ONLY.equals(value)) {
					predicates.add(builder.isNull(root.get(User_.employeeId)));
				}
			});
			// 成员过滤
			Optional.ofNullable(criteria.getMemberState()).ifPresent((value) -> {
				// 只有 memberState, ownerType, ownerId 同时设置，过滤才生效
				if (criteria.getOwnerType() != null && criteria.getOwnerId() != null) {
					Subquery<String> subQuery = query.subquery(String.class);
					Root<PartyAuth> partyAuthRoot = subQuery.from(PartyAuth.class);
					subQuery.select(partyAuthRoot.get(PartyAuth_.PARTY_ID)).where(builder.and( //
							builder.equal(partyAuthRoot.get(PartyAuth_.PARTY_TYPE), CoframeConstants.PARTY_TYPE_USER), //
							builder.equal(partyAuthRoot.get(PartyAuth_.AUTH_OWNER_TYPE), criteria.getOwnerType()), //
							builder.equal(partyAuthRoot.get(PartyAuth_.AUTH_OWNER_ID), criteria.getOwnerId()) //
					));
					// 查询为某领域成员的用户
					if (CoframeConstants.IS_MEMBER.equals(value)) {
						predicates.add(builder.in(root.get(User_.id)).value(subQuery));
					} else 
					// 查询不为某领域成员的用户	
					if (CoframeConstants.NOT_MEMBER.equals(value)) {
						predicates.add(builder.not(builder.in(root.get(User_.id)).value(subQuery)));

					}
				}
			});
			return builder.and(predicates.toArray(new Predicate[predicates.size()]));
		};
	}
	
	@Query("SELECT m from Menu m \n" + //
			"LEFT JOIN AuthRes authRes ON m.id = authRes.resId \n" + //
			"LEFT JOIN PartyAuth partyAuth ON authRes.authId = partyAuth.authId \n" + //
			"WHERE authRes.authType='ROLE' and partyAuth.authType='ROLE' and authRes.resType = 'MENU' AND partyAuth.partyType = 'USER' AND partyAuth.partyId = :userId")
	public Set<Menu> findUserMenus(@Param(value = "userId") String userId);

	@Query("SELECT m from Menu m \n" + //
			"LEFT JOIN AuthRes authRes ON m.id = authRes.resId \n" + //
			"LEFT JOIN PartyAuth partyAuth ON authRes.authId = partyAuth.authId \n" + //
			"WHERE authRes.authType='ROLE' and partyAuth.authType='ROLE' and partyAuth.authOwnerType= :ownerType and partyAuth.authOwnerId=:ownerId and authRes.resType = 'MENU' AND partyAuth.partyType = 'USER' AND partyAuth.partyId = :userId")
	public Set<Menu> findUserMenusByOwner(@Param(value = "userId") String userId, @Param(value = "ownerType") String ownerType, @Param(value = "ownerId") String ownerId);

	@Query("SELECT f from Function f \n" + //
			"LEFT JOIN AuthRes authRes ON f.id = authRes.resId \n" + //
			"LEFT JOIN PartyAuth partyAuth ON authRes.authId = partyAuth.authId \n" + //
			"WHERE authRes.authType='ROLE' and partyAuth.authType='ROLE' and authRes.resType = 'FUNCTION' AND partyAuth.partyType = 'USER' AND partyAuth.partyId = :userId")
	public Set<Function> findUserFunctions(@Param(value = "userId") String userId);
	
	@Query("SELECT f from Function f \n" + //
			"LEFT JOIN AuthRes authRes ON f.id = authRes.resId \n" + //
			"LEFT JOIN PartyAuth partyAuth ON authRes.authId = partyAuth.authId \n" + //
			"WHERE authRes.authType='ROLE' and partyAuth.authType='ROLE' and partyAuth.authOwnerType= :ownerType and partyAuth.authOwnerId=:ownerId and authRes.resType = 'FUNCTION' AND partyAuth.partyType = 'USER' AND partyAuth.partyId = :userId")
	public Set<Function> findUserFunctionsByOwner(@Param(value = "userId") String userId, @Param(value = "ownerType") String ownerType, @Param(value = "ownerId") String ownerId);

	@Query("SELECT role from Role role \n" + //
			"LEFT JOIN PartyAuth partyAuth ON partyAuth.authId = role.id \n " + //
			"WHERE partyAuth.authType = 'ROLE' and partyAuth.partyType = 'USER' AND partyAuth.partyId = :userId")
	public Set<Role> listRoles(@Param(value = "userId") String userId);
}
