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

import org.gocom.coframe.CoframeConstants;
import org.gocom.coframe.core.jpa.CoframeJpaRepository;
import org.gocom.coframe.model.Function;
import org.gocom.coframe.model.Role;
import org.gocom.coframe.model.RoleTpl_;
import org.gocom.coframe.model.Role_;
import org.gocom.coframe.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 角色存储实体操作
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
public interface RoleRepository extends CoframeJpaRepository<Role, String> {
	/**
	 * 构造角色查询条件
	 * 
	 * @param criteria
	 * @return
	 */
	public default Specification<Role> toSpecification(Role.Criteria criteria) {
		return (root, query, builder) -> {
			List<Predicate> predicates = criteria.buildPredicates(root, builder);
			Optional.ofNullable(criteria.getRoleTplId()).ifPresent(value -> predicates.add(builder.equal(root.get(Role_.roleTpl).get(RoleTpl_.id), value)));
			Optional.ofNullable(criteria.getListPlatformRoles()).ifPresent(listPlatformRoles -> {
				if (listPlatformRoles) {	// 列出平台角色
					predicates.add(builder.equal(root.get(Role_.OWNER_TYPE), CoframeConstants.OWNER_TYPE_PLATFORM));
					predicates.add(builder.equal(root.get(Role_.OWNER_ID), CoframeConstants.OWNER_ID_PLATFORM));
				} else {
					Optional.ofNullable(criteria.getOwnerType()).ifPresent(value -> predicates.add(builder.equal(root.get(Role_.OWNER_TYPE), value)));
					Optional.ofNullable(criteria.getOwnerId()).ifPresent(value -> predicates.add(builder.equal(root.get(Role_.OWNER_ID), value)));
				}
			});
			return builder.and(predicates.toArray(new Predicate[predicates.size()]));
		};
	}

	@Query(value = "select user from User user " + //
			"left join PartyAuth partyAuth on user.id=partyAuth.partyId " + //
			"where partyAuth.partyType='USER' and partyAuth.authType='ROLE' and partyAuth.authId=:roleId")
	public Page<User> getUsersByRoleId(@Param("roleId") String roleId, Pageable pageable);

	@Query(value = "select user from User user " + //
			"left join PartyAuth partyAuth on user.id=partyAuth.partyId " + //
			"where user.name like concat('%', :userName, '%') and partyAuth.partyType='USER' and partyAuth.authType='ROLE' and partyAuth.authId=:roleId")
	public Page<User> getUsersByRoleId(@Param("roleId") String roleId, @Param("userName") String userName, Pageable pageable);
	
	@Query(value = "select function from Function function " + //
			"left join AuthRes authRes on function.id=authRes.resId " + //
			"where authRes.authType='ROLE' and authRes.resType='FUNCTION' and authRes.type='MAPPING' and authRes.authId=:roleId")
	public List<Function> getRoleFunctions(@Param("roleId") String roleId);
}
