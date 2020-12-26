/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Mar 4, 2019
 *******************************************************************************/
package org.gocom.coframe.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.gocom.coframe.CoframeConstants;
import org.gocom.coframe.core.exception.CoframeErrorCode;
import org.gocom.coframe.core.jpa.CoframeJpaRepository;
import org.gocom.coframe.core.service.AbstractPersistentModelService;
import org.gocom.coframe.core.support.CoframeIdUtil;
import org.gocom.coframe.model.AuthRes;
import org.gocom.coframe.model.OperatableResource;
import org.gocom.coframe.model.ResGroup;
import org.gocom.coframe.model.Role;
import org.gocom.coframe.model.RoleTpl;
import org.gocom.coframe.repository.AuthResRepository;
import org.gocom.coframe.repository.RoleRepository;
import org.gocom.coframe.repository.RoleTplRepository;
import org.gocom.coframe.sdk.util.CofBeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@Service
public class RoleTplService extends AbstractPersistentModelService<RoleTpl> {
	@Autowired
	private RoleTplRepository repository;

	@Autowired
	private AuthResRepository authResRepository;

	@Autowired
	private AuthTplService authTplService;

	@Autowired
	private RoleService roleService;
	
	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private RoleTplService roleTplService;

	@Override
	protected CoframeJpaRepository<RoleTpl, String> getRepository() {
		return repository;
	}

	/**
	 * 按条件查询
	 * 
	 * @param criteria
	 * @return
	 */
	public List<RoleTpl> findByCriteria(RoleTpl.Criteria criteria) {
		return repository.findAll(repository.toSpecification(criteria));
	}

	/**
	 * 按条件查询计数
	 * 
	 * @param criteria
	 * @return
	 */
	public long countByCriteria(RoleTpl.Criteria criteria) {
		return repository.count(repository.toSpecification(criteria));
	}
	
	@Override
	public void preDelete(String id) {
		// 如果模块下还有角色，模块不允许删除
		Role.Criteria criteria = new Role.Criteria();
		criteria.setRoleTplId(id);
		if (roleRepository.count(roleRepository.toSpecification(criteria)) > 0) {
			throw CoframeErrorCode.ROLE_TPL_DELETE_NOT_ALLOWED_BY_HAS_CHILDREN.runtimeException();
		}
	}

	/**
	 * 获取角色模板包含的权限树
	 * 
	 * @param roleTemplateId
	 * @return
	 */
	public List<ResGroup> loadWholeResources(String roleTemplateId) {
		List<ResGroup> resGroups = new ArrayList<>();
		repository.findById(roleTemplateId).ifPresent(roleTemplate -> {
			List<String> menuIds = authResRepository.listResIds(CoframeConstants.AUTH_TYPE_ROLE_TEMPLATE, roleTemplateId, CoframeConstants.RESOURCE_TYPE_MENU);
			List<String> functionIds = authResRepository.listResIds(CoframeConstants.AUTH_TYPE_ROLE_TEMPLATE, roleTemplateId, CoframeConstants.RESOURCE_TYPE_FUNCTION);
			List<ResGroup> groups = authTplService.loadWholeResources(roleTemplate.getAuthTpl().getId(), menuIds, functionIds);
			resGroups.addAll(groups);
		});
		return resGroups;
	}

	/**
	 * 分页查询使用某角色模板创建的角色
	 * 
	 * @param roleTplId
	 * @param criteria
	 * @param pageable
	 * @return
	 */
	public Page<Role> pageRolesByRoleTplId(String roleTplId, String roleName, Pageable pageable) {
		Role.Criteria criteria = new Role.Criteria();
		criteria.setRoleTplId(roleTplId);
		criteria.setName(roleName);
		return roleService.pagingByCriteria(criteria, pageable);
	}

	/**
	 * 操作模板资源
	 * 
	 * @param roleTplId
	 * @param operatableResource
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void handleRoleTplResource(String roleTplId, OperatableResource[] operatableResources) {
		Arrays.asList(operatableResources).stream().forEach(operatableResource -> {
			// 添加
			if (CoframeConstants.OPERATE_TYPE_ADD.equals(operatableResource.getOperation())) {
				AuthRes authRes = new AuthRes(CoframeConstants.AUTH_TYPE_ROLE_TEMPLATE, roleTplId, operatableResource.getType(), operatableResource.getId());
				authResRepository.save(authRes);
			} else
			// 移除
			if (CoframeConstants.OPERATE_TYPE_REMOVE.equals(operatableResource.getOperation())) {
				AuthRes.Criteria criteria = new AuthRes.Criteria();
				criteria.setAuthType(CoframeConstants.AUTH_TYPE_ROLE_TEMPLATE);
				criteria.setAuthId(roleTplId);
				criteria.setResType(operatableResource.getType());
				criteria.setResId(operatableResource.getId());
				authResRepository.delete(authResRepository.toSpecification(criteria));
			}
		});
	}

	/**
	 * 根据角色模板，实例化一个角色出来
	 * 
	 * @param roleTpleId
	 * @param ownerType
	 * @param ownerId
	 * @return
	 */
	@Transactional(rollbackFor = Throwable.class)
	public Role newRoleInstance(String roleTplId, String ownerType, String ownerId) {
		RoleTpl tpl = findById(roleTplId);
		Role role = new Role();
		RoleTpl roleTpl = roleTplService.findById(roleTplId);
		// 属性复制
		CofBeanUtils.copyNotNullProperties(tpl, role);
		// 属性覆盖
		role.setId(CoframeIdUtil.generateId(Role.class));
		role.setRoleTpl(roleTpl);
		role.setCode(role.getId()); // 自动生成的角色，code 与 id 保持一致
		role.setOwnerType(ownerType);
		role.setOwnerId(ownerId);
		role.setFixed(false);
		return roleService.create(role);
	}
}
