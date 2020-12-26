/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Mar 4, 2019
 *******************************************************************************/
package org.gocom.coframe.service;

import static org.gocom.coframe.core.support.CoframePersistentModelEvent.Type.POST_CREATE;
import static org.gocom.coframe.core.support.CoframePersistentModelEvent.Type.POST_DELETE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.gocom.coframe.CoframeConstants;
import org.gocom.coframe.core.exception.CoframeErrorCode;
import org.gocom.coframe.core.jpa.CoframeJpaRepository;
import org.gocom.coframe.core.service.AbstractMainModelService;
import org.gocom.coframe.core.support.CoframePersistentModelEvent;
import org.gocom.coframe.model.AuthRes;
import org.gocom.coframe.model.OperatableResource;
import org.gocom.coframe.model.PartyAuth;
import org.gocom.coframe.model.ResGroup;
import org.gocom.coframe.model.Role;
import org.gocom.coframe.model.User;
import org.gocom.coframe.repository.AuthResRepository;
import org.gocom.coframe.repository.EmployeeRepository;
import org.gocom.coframe.repository.PartyAuthRepository;
import org.gocom.coframe.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 角色相关操作
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@Service
public class RoleService extends AbstractMainModelService<Role> {
	@Autowired
	private RoleRepository repository;

	@Autowired
	private EmployeeRepository employeeRepo;
	
	@Autowired
	private PartyAuthRepository partyAuthRepository;

	@Autowired
	private AuthResRepository authResRepository;

	@Autowired
	private AuthTplService authTplService;

	@Override
	protected CoframeJpaRepository<Role, String> getRepository() {
		return repository;
	}
	
	@Override
	public void preCreate(Role model) {
		if (model.getOwnerType() == null || model.getOwnerId() == null) {
			model.setOwnerType(CoframeConstants.OWNER_TYPE_PLATFORM);
			model.setOwnerId(CoframeConstants.OWNER_ID_PLATFORM);
		}
	}

	@Override
	public void afterCreate(Role model, Role savedModel) {
		if (model.getRoleTpl() != null) {
			// 从角色模板中，复制与资源的关联
			AuthRes.Criteria criteria = new AuthRes.Criteria();
			criteria.setAuthType(CoframeConstants.AUTH_TYPE_ROLE_TEMPLATE);
			criteria.setAuthId(model.getRoleTpl().getId());
			List<AuthRes> authResList = authResRepository.findAll(authResRepository.toSpecification(criteria));
			if (authResList != null) {
				authResList.forEach(roleTplAuthRes -> {
					AuthRes roleAuthRes = new AuthRes();
					roleAuthRes.setAuthType(CoframeConstants.AUTH_TYPE_ROLE);
					roleAuthRes.setAuthId(savedModel.getId());
					roleAuthRes.setResType(roleTplAuthRes.getResType());
					roleAuthRes.setResId(roleTplAuthRes.getResId());
					roleAuthRes.setType(roleTplAuthRes.getType());
					authResRepository.save(roleAuthRes);
				});
			}
		}
	}

	/**
	 * 按条件分页查询角色
	 * 
	 * @param criteria
	 * @param pageable
	 * @return
	 */
	public Page<Role> pagingByCriteria(Role.Criteria criteria, Pageable pageable) {
		return repository.findAll(repository.toSpecification(criteria), pageable);
	}

	/**
	 * 分页获取已分配某角色的用户
	 * 
	 * @param roleId
	 * @param pageable
	 * @return
	 */
	public Page<User> getUsersByRoleId(String roleId, String userName, Pageable pageable) {
		Page<User> users = null;
		if (userName == null) {
			users = repository.getUsersByRoleId(roleId, pageable);
		} else {
			users = repository.getUsersByRoleId(roleId, userName, pageable);
		}
		users.getContent().stream().forEach(user -> {
			if(user.getEmployeeId() != null) {
				user.setEmployee(employeeRepo.findById(user.getEmployeeId()).get());
			}
		});
		return users;
	}

	@Override
	public void preDelete(String id) {
		// 删除用户等与角色的关联
		repository.findById(id).ifPresent(role -> {
			PartyAuth.Criteria criteria = new PartyAuth.Criteria();
			criteria.setAuthType(CoframeConstants.AUTH_TYPE_ROLE);
			criteria.setAuthId(id);
			partyAuthRepository.delete(partyAuthRepository.toSpecification(criteria));
		});
		// 删除角色与资源的关联
		{
			AuthRes.Criteria criteria = new AuthRes.Criteria();
			criteria.setAuthType(CoframeConstants.AUTH_TYPE_ROLE);
			criteria.setAuthId(id);
			authResRepository.delete(authResRepository.toSpecification(criteria));
		}
	}
	
	/**
	 * 删除某领域下的所有的角色
	 * @param ownerType
	 * @param ownerId
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void deleteByOwner(String ownerType, String ownerId) {
		if (ownerType == null) {
			throw CoframeErrorCode.NULL_PARAM.runtimeException("ownerType");
		}
		if (ownerId == null) {
			throw CoframeErrorCode.NULL_PARAM.runtimeException("ownerId");
		}
		
		 Role.Criteria criteria = new Role.Criteria();
		 criteria.setOwnerType(ownerType);
		 criteria.setOwnerId(ownerId);
		 repository.delete(repository.toSpecification(criteria));
	}

	/**
	 * 将角色绑定至用户
	 * 
	 * @param roleId
	 * @param userIds
	 */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional(rollbackFor = Throwable.class)
	public void bindRoleToUsers(String roleId, String... userIds) {
		repository.findById(roleId).ifPresent(role -> {
			PartyAuth.Criteria criteria = new PartyAuth.Criteria();
			criteria.setAuthType(CoframeConstants.AUTH_TYPE_ROLE);
			criteria.setAuthId(role.getId());
			criteria.setPartyType(CoframeConstants.PARTY_TYPE_USER);
			Arrays.asList(userIds).forEach(userId -> {
				criteria.setPartyId(userId);
				if (partyAuthRepository.count(partyAuthRepository.toSpecification(criteria)) == 0) {
                    PartyAuth partyAuth = partyAuthRepository.save(new PartyAuth(CoframeConstants.PARTY_TYPE_USER, userId, CoframeConstants.AUTH_TYPE_ROLE, role.getId(), role.getOwnerType(), role.getOwnerId()));
                    // 发布事件
                    eventPublisher.publishEvent(new CoframePersistentModelEvent(POST_CREATE, partyAuth));
				}
			});
		});
	}

	/**
	 * 将角色与用户解绑
	 * 
	 * @param roleId
	 * @param userIds
	 */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Transactional(rollbackFor = Throwable.class)
	public void unbindRoleToUsers(String roleId, String... userIds) {
		repository.findById(roleId).ifPresent(role -> {
			PartyAuth.Criteria criteria = new PartyAuth.Criteria();
			criteria.setAuthType(CoframeConstants.AUTH_TYPE_ROLE);
			criteria.setAuthId(role.getId());
			criteria.setPartyType(CoframeConstants.PARTY_TYPE_USER);
			Arrays.asList(userIds).forEach(userId -> {
				criteria.setPartyId(userId);
				partyAuthRepository.delete(partyAuthRepository.toSpecification(criteria));
				// 发布事件
                PartyAuth partyAuth = new PartyAuth(CoframeConstants.PARTY_TYPE_USER, userId, CoframeConstants.AUTH_TYPE_ROLE, role.getId());
                eventPublisher.publishEvent(new CoframePersistentModelEvent(POST_DELETE, partyAuth));
			});
		});
	}

	/**
	 * 加载角色的所有资源
	 * @param roleId
	 * @return
	 */
	public List<ResGroup> loadWholeResources(String roleId) {
		List<ResGroup> resGroups = new ArrayList<>();
		repository.findById(roleId).ifPresent(role -> {
			List<String> menuIds = authResRepository.listResIds(CoframeConstants.AUTH_TYPE_ROLE, roleId, CoframeConstants.RESOURCE_TYPE_MENU);
			List<String> functionIds = authResRepository.listResIds(CoframeConstants.AUTH_TYPE_ROLE, roleId, CoframeConstants.RESOURCE_TYPE_FUNCTION);
			List<ResGroup> groups = authTplService.loadWholeResources(role.getRoleTpl().getAuthTpl().getId(), menuIds, functionIds);
			resGroups.addAll(groups);
		});
		return resGroups;
	}

	/**
	 * 处理角色资源加减
	 * @param roleId
	 * @param operatableResources
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional(rollbackFor = Throwable.class)
	public void handleRoleTplResource(String roleId, OperatableResource[] operatableResources) {
		Arrays.asList(operatableResources).stream().forEach(operatableResource -> {
			// 添加
			if (CoframeConstants.OPERATE_TYPE_ADD.equals(operatableResource.getOperation())) {
				AuthRes authRes = new AuthRes(CoframeConstants.AUTH_TYPE_ROLE, roleId, operatableResource.getType(), operatableResource.getId());
				authResRepository.save(authRes);
                // 发布事件
                eventPublisher.publishEvent(new CoframePersistentModelEvent(POST_CREATE, authRes));
			} else
			// 移除
			if (CoframeConstants.OPERATE_TYPE_REMOVE.equals(operatableResource.getOperation())) {
				AuthRes.Criteria criteria = new AuthRes.Criteria();
				criteria.setAuthType(CoframeConstants.AUTH_TYPE_ROLE);
				criteria.setAuthId(roleId);
				criteria.setResType(operatableResource.getType());
				criteria.setResId(operatableResource.getId());
				authResRepository.delete(authResRepository.toSpecification(criteria));
                // 发布事件
                AuthRes authRes = new AuthRes(CoframeConstants.AUTH_TYPE_ROLE, roleId, operatableResource.getType(), operatableResource.getId());
                eventPublisher.publishEvent(new CoframePersistentModelEvent(POST_DELETE, authRes));
			}
		});
	}
}
