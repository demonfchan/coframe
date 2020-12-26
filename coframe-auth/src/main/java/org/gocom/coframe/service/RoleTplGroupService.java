/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Mar 4, 2019
 *******************************************************************************/
package org.gocom.coframe.service;

import java.util.ArrayList;
import java.util.List;

import org.gocom.coframe.core.exception.CoframeErrorCode;
import org.gocom.coframe.core.jpa.CoframeJpaRepository;
import org.gocom.coframe.core.service.AbstractPersistentModelService;
import org.gocom.coframe.model.Role;
import org.gocom.coframe.model.RoleTpl;
import org.gocom.coframe.model.RoleTplGroup;
import org.gocom.coframe.repository.RoleTplGroupRepository;
import org.gocom.coframe.repository.RoleTplRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@Service
public class RoleTplGroupService extends AbstractPersistentModelService<RoleTplGroup> {
	@Autowired
	private RoleTplGroupRepository repository;

	@Autowired
	private RoleTplRepository roleTplRepository;
	
	@Autowired
	private RoleTplService roleTplService;

	@Override
	protected CoframeJpaRepository<RoleTplGroup, String> getRepository() {
		return repository;
	}

	/**
	 * 按条件查询
	 * 
	 * @param criteria
	 * @return
	 */
	public List<RoleTplGroup> findByCriteria(RoleTplGroup.Criteria criteria) {
		return repository.findAll(repository.toSpecification(criteria));
	}

	@Override
	public void preDelete(String id) {
		// 如果组下还有子的角色模板，组不允许删除
		RoleTpl.Criteria criteria = new RoleTpl.Criteria();
		criteria.setGroupId(id);
		if (roleTplRepository.count(roleTplRepository.toSpecification(criteria)) > 0) {
			throw CoframeErrorCode.ROLE_TPL_GRP_DELETE_NOT_ALLOWED_BY_HAS_CHILDREN.runtimeException();
		}
	}

	/**
	 * 根据角色模板组 Id, 为其下的每个角色模板实例一个角色出来
	 * 
	 * @param roleTplGroupId
	 * @return
	 */
	@Transactional(rollbackFor = Throwable.class)
	public List<Role> newRoleInstances(String roleTplGroupId, String ownerType, String ownerId) {
		List<Role> newRoles = new ArrayList<>();
		// 检查一下这个角色模板组是否存在
		findById(roleTplGroupId);
		// 查出模板组下的所有角色模板
		RoleTpl.Criteria criteria = new RoleTpl.Criteria();
		criteria.setGroupId(roleTplGroupId);
		List<RoleTpl> roleTpls = roleTplRepository.findAll(roleTplRepository.toSpecification(criteria));
		// 每个角色模板新建一个角色，并设置归属(owerType + ownerId)
		if (roleTpls != null) {
			roleTpls.forEach(roleTpl -> {
				newRoles.add(roleTplService.newRoleInstance(roleTpl.getId(), ownerType, ownerId));
			});
		}
		return newRoles;
	}
}
