/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Mar 26, 2019
 *******************************************************************************/
package org.gocom.coframe.repository;

import java.util.List;
import java.util.Set;

import org.gocom.coframe.core.jpa.CoframeJpaRepository;
import org.gocom.coframe.core.model.AbstractPersistentModel;
import org.gocom.coframe.model.Role;
import org.gocom.coframe.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
public interface MemberRepository extends CoframeJpaRepository<AbstractPersistentModel, String> {
	/**
	 * 分页获取成员
	 * 
	 * @param ownerType
	 * @param ownerId
	 * @param pageable
	 * @return
	 */
	@Query(value = "select DISTINCT user from User user " + //
			"left join PartyAuth partyAuth on partyAuth.partyId = user.id " + //
			"where partyAuth.authType = 'ROLE' and partyAuth.partyType = 'USER' " + //
			"and partyAuth.authOwnerType =:ownerType and partyAuth.authOwnerId =:ownerId") //
	public Page<User> pagingMembers(@Param("ownerType") String ownerType, @Param("ownerId") String ownerId, Pageable pageable);
	
	/**
	 * 根据条件查询成员
	 * @param ownerType
	 * @param userId
	 * @param pageable
	 * @return
	 */
	@Query(value = "select DISTINCT user.id, partyAuth.authOwnerId from User user " + //
			"left join PartyAuth partyAuth on partyAuth.partyId = user.id " + //
			"where partyAuth.authType = 'ROLE' and partyAuth.partyType = 'USER' " + //
			"and partyAuth.authOwnerType =:ownerType and partyAuth.partyId =:userId") //
	public Set<Object> findMembers(@Param("ownerType") String ownerType, @Param("userId") String userId);

	/**
	 * 分页获取成员
	 * 
	 * @param ownerType
	 * @param ownerId
	 * @param pageable
	 * @return
	 */
	@Query("select DISTINCT user from User user " + //
			"left join PartyAuth partyAuth on partyAuth.partyId = user.id " + //
			"where partyAuth.authType = 'ROLE' and partyAuth.partyType = 'USER' " + //
			"and partyAuth.authOwnerType =:ownerType and partyAuth.authOwnerId =:ownerId and user.name like concat('%', :userName, '%')")
	public Page<User> pagingMembers(@Param("ownerType") String ownerType, @Param("ownerId") String ownerId, @Param("userName") String userName, Pageable pageable);

	/**
	 * 获取成员的角色
	 * 
	 * @param ownerType
	 * @param ownerId
	 * @param userId
	 * @return
	 */
	@Query("select role from Role role " + //
			"left join PartyAuth partyAuth on role.id = partyAuth.authId " + //
			"where partyAuth.authType = 'ROLE' and partyAuth.partyType = 'USER' " + //
			"and partyAuth.partyId = :userId and role.ownerType = :ownerType and role.ownerId = :ownerId")
	public List<Role> getMemberRoles(@Param("ownerType") String ownerType, @Param("ownerId") String ownerId, @Param("userId") String userId);
}
