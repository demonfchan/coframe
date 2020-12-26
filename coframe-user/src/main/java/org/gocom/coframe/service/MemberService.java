/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Mar 12, 2019
 *******************************************************************************/
package org.gocom.coframe.service;

import static org.gocom.coframe.CoframeConstants.AUTH_TYPE_ROLE;
import static org.gocom.coframe.CoframeConstants.PARTY_TYPE_USER;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.gocom.coframe.core.exception.CoframeErrorCode;
import org.gocom.coframe.model.Member;
import org.gocom.coframe.model.PartyAuth;
import org.gocom.coframe.model.User;
import org.gocom.coframe.repository.MemberRepository;
import org.gocom.coframe.repository.PartyAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@Service
public class MemberService {
	@Autowired
	private MemberRepository repository;

	@Autowired
	private PartyAuthRepository partyAuthRepository;

	/**
	 * 添加成员
	 * 
	 * @param member
	 * @return
	 */
	@Transactional(rollbackFor = Throwable.class)
	public Member[] addMember(Member... members) {
		Arrays.asList(members).stream().forEach(member -> {
			if (member.getUser() == null) {
				throw CoframeErrorCode.USER_IN_MEMBER_IS_NULL.runtimeException();
			}
			if (member.getUser().getId() == null) {
				throw CoframeErrorCode.USER_IN_MEMBER_ID_IS_NULL.runtimeException();
			}
			if (member.getRoles().size() == 0) {
				throw CoframeErrorCode.MEMBER_MUST_HAS_ONE_ROLE_AT_LEAST.runtimeException();
			}
			member.getRoles().forEach(role -> {
				if (role.getId() == null) {
					throw CoframeErrorCode.ROLE_IN_MEMBER_ID_IS_NULL.runtimeException();
				}
				PartyAuth.Criteria partyAuthCriteria = new PartyAuth.Criteria();
				partyAuthCriteria.setPartyType(PARTY_TYPE_USER);
				partyAuthCriteria.setPartyId(member.getUser().getId());
				partyAuthCriteria.setAuthType(AUTH_TYPE_ROLE);
				partyAuthCriteria.setAuthId(role.getId());
				if (partyAuthRepository.count(partyAuthRepository.toSpecification(partyAuthCriteria)) == 0) {
					partyAuthRepository.save(new PartyAuth(PARTY_TYPE_USER, member.getUser().getId(), AUTH_TYPE_ROLE, role.getId(), role.getOwnerType(), role.getOwnerId()));
				}
			});
		});
		return members;
	}
	
	/**
	 * 删除某领域下的所有的成员
	 * @param ownerType 
	 * @param ownerId
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void deleteMembersByOwner(String ownerType, String ownerId) {
		if (ownerType == null) {
			throw CoframeErrorCode.MEMBER_OWNER_TYPE_IS_NULL.runtimeException("ownerType");
		}
		if (ownerId == null) {
			throw CoframeErrorCode.MEMBER_OWNER_ID_IS_NULL.runtimeException("ownerId");
		}

		PartyAuth.Criteria criteria = new PartyAuth.Criteria();
		criteria.setAuthOwnerType(ownerType);
		criteria.setAuthOwnerId(ownerId);
		partyAuthRepository.delete(partyAuthRepository.toSpecification(criteria));
	}

	/**
	 * 删除成员
	 * 
	 * @param members 给定的成员，必须含有 ownerType, ownerId, userId
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void deleteMembers(Member... members) {
		if (members != null) {
			Arrays.asList(members).forEach(member -> {
				if (member.getOwnerType() == null) {
					throw CoframeErrorCode.MEMBER_OWNER_TYPE_IS_NULL.runtimeException("member.ownerType");
				}
				if (member.getOwnerId() == null) {
					throw CoframeErrorCode.MEMBER_OWNER_ID_IS_NULL.runtimeException("member.ownerId");
				}
				if (member.getUser() == null) {
					throw CoframeErrorCode.USER_IN_MEMBER_IS_NULL.runtimeException("member.user");
				}
				if (member.getUser().getId() == null) {
					throw CoframeErrorCode.USER_IN_MEMBER_ID_IS_NULL.runtimeException("member.user.id");
				}

				PartyAuth.Criteria criteria = new PartyAuth.Criteria();
				criteria.setPartyType(PARTY_TYPE_USER);
				criteria.setPartyId(member.getUser().getId());
				criteria.setAuthOwnerType(member.getOwnerType());
				criteria.setAuthOwnerId(member.getOwnerId());
				partyAuthRepository.delete(partyAuthRepository.toSpecification(criteria));
			});
		}
	}

	/**
	 * 用户是否是给定 owner 的成员
	 * 
	 * @param ownerType
	 * @param ownerId
	 * @param userId
	 * @return
	 */
	public boolean isMemsber(String ownerType, String ownerId, String userId) {
		PartyAuth.Criteria criteria = new PartyAuth.Criteria();
		criteria.setPartyType(PARTY_TYPE_USER);
		criteria.setPartyId(userId);
		criteria.setAuthOwnerType(ownerType);
		criteria.setAuthOwnerId(ownerId);
		return partyAuthRepository.count(partyAuthRepository.toSpecification(criteria)) > 0;
	}
	
	/**
	 * 给定条件，查询成员
	 * @param ownerType 
	 * @param userId
	 * @return 返回的成员信息中，用户只有 id，其它属性未加载
	 */
	public Set<Member> findMembers(String ownerType, String userId) {
		Set<Member> members = new HashSet<>();
		if (ownerType == null) {
			throw CoframeErrorCode.NULL_PARAM.runtimeException("ownerType");
		}
		if (userId == null) {
			throw CoframeErrorCode.NULL_PARAM.runtimeException("userId");
		}
		Set<Object> users = repository.findMembers(ownerType, userId);
		if (users != null) {
			users.forEach(row -> {
				Object[] rowArray = (Object[]) row;
				members.add(toMember((String)rowArray[0], ownerType, (String)rowArray[1]));
			});
		}
		return members;
	}

	/**
	 * 分页查询所有成员
	 * 
	 * @param criteria
	 * @param pageable
	 * @return
	 */
	public Page<Member> pagingMembers(String ownerType, String ownerId, String userName, Pageable pageable) {
		Page<User> users = null;
		if (userName == null) {
			users = repository.pagingMembers(ownerType, ownerId, pageable);
		} else {
			users = repository.pagingMembers(ownerType, ownerId, userName, pageable);
		}
		if (users == null || users.getContent() == null) {
			return Page.empty();
		}
		return new PageImpl<Member>(users.map(user -> toMember(user, ownerType, ownerId)).getContent());
	}
	
	/**
	 * 将用户转化为成员
	 * 
	 * @param user
	 * @return
	 */
	private Member toMember(String userId, String ownerType, String ownerId) {
		Member member = new Member();
		member.setOwnerType(ownerType);
		member.setOwnerId(ownerId);
		User user = new User();
		user.setId(userId);
		member.setUser(user);
		member.setRoles(repository.getMemberRoles(ownerType, ownerId, user.getId()));
		return member;
	}

	/**
	 * 将用户转化为成员
	 * 
	 * @param user
	 * @return
	 */
	private Member toMember(User user, String ownerType, String ownerId) {
		Member member = new Member();
		member.setOwnerType(ownerType);
		member.setOwnerId(ownerId);
		member.setUser(user);
		member.setRoles(repository.getMemberRoles(ownerType, ownerId, user.getId()));
		return member;
	}
}
