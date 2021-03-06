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
import java.util.Optional;

import org.gocom.coframe.CoframeConstants;
import org.gocom.coframe.core.exception.CoframeErrorCode;
import org.gocom.coframe.core.jpa.CoframeJpaRepository;
import org.gocom.coframe.core.service.AbstractPersistentModelService;
import org.gocom.coframe.model.AuthTpl;
import org.gocom.coframe.model.AuthTplResGroup;
import org.gocom.coframe.model.ResGroup;
import org.gocom.coframe.model.RoleTpl;
import org.gocom.coframe.repository.AuthTplRepository;
import org.gocom.coframe.repository.AuthTplResGroupRepository;
import org.gocom.coframe.repository.RoleTplRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@Service
public class AuthTplService extends AbstractPersistentModelService<AuthTpl> {
	@Autowired
	private AuthTplRepository repository;

	@Autowired
	private ResGroupService resGroupService;

	@Autowired
	private AuthTplResGroupRepository authTplResGroupRepository;

	@Autowired
	private RoleTplService roleTplService;
	
	@Autowired
	private RoleTplRepository roleTplRepository;

	@Override
	protected CoframeJpaRepository<AuthTpl, String> getRepository() {
		return repository;
	}

	@Override
	public void preCreate(AuthTpl template) {
		if (template.getRootResGroups() != null) {
			template.getRootResGroups().forEach(resGroup -> {
				checkRootResGroup(resGroup.getId());
			});
		}
	}

	@Override
	public void afterCreate(AuthTpl template, AuthTpl savedTemplate) {
		if (template.getRootResGroups() != null) {
			template.getRootResGroups().forEach(resGroup -> {
				AuthTplResGroup authTplResGroup = new AuthTplResGroup();
				authTplResGroup.setAuthTplId(savedTemplate.getId());
				authTplResGroup.setResGroupId(resGroup.getId());
				authTplResGroupRepository.save(authTplResGroup);
			});
		}
	}
	
	@Override
	public void preDelete(String id) {
		// ????????????????????????????????????????????????????????????
		RoleTpl.Criteria criteria = new RoleTpl.Criteria();
		criteria.setAuthTplId(id);
		if (roleTplRepository.count(roleTplRepository.toSpecification(criteria)) > 0) {
			throw CoframeErrorCode.AUTH_TPL_DELETE_NOT_ALLOWED_BY_HAS_CHILDREN.runtimeException();
		}
	}

	/**
	 * ??????????????????????????????
	 * 
	 * @param criteria
	 * @return
	 */
	public List<AuthTpl> findByCriteria(AuthTpl.Criteria criteria) {
		return repository.findAll(repository.toSpecification(criteria));
	}

	/**
	 * ???????????????????????????????????????????????????
	 * 
	 * @param authTemplateId
	 * @param roleTemplateName
	 * @return
	 */
	public List<RoleTpl> querySubRoleTemplates(String authTemplateId, String roleTemplateName) {
		RoleTpl.Criteria criteria = new RoleTpl.Criteria();
		criteria.setAuthTplId(authTemplateId);
		criteria.setName(roleTemplateName);
		return roleTplService.findByCriteria(criteria);
	}

	/**
	 * ?????????????????????????????????
	 * 
	 * @param authTemplateId
	 * @param resGroupId
	 */
	public void addRootResourceGroups(String authTemplateId, String resGroupId) {
		// ???????????????????????????????????????????????????
		checkRootResGroup(resGroupId);
		if (authTplResGroupRepository.count(authTplResGroupRepository.toSpecification(authTemplateId, resGroupId)) == 0) {
			AuthTplResGroup authTplResGroup = new AuthTplResGroup();
			authTplResGroup.setAuthTplId(authTemplateId);
			authTplResGroup.setResGroupId(resGroupId);
			authTplResGroupRepository.save(authTplResGroup);
		}
	}

	/**
	 * ????????????????????????????????????
	 * 
	 * @param authTemplateId
	 * @param resGroupId
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void removeRootResourceGroups(String authTemplateId, String resGroupId) {
		authTplResGroupRepository.delete(authTplResGroupRepository.toSpecification(authTemplateId, resGroupId));
	}

	/**
	 * ????????????????????????????????????????????????
	 * 
	 * @return
	 */
	public List<ResGroup> loadWholeResources(String authTemplateId, List<String> menuIds, List<String> functionIds) {
		List<ResGroup> groups = new ArrayList<>();
		Optional.ofNullable(authTplResGroupRepository.findAll(authTplResGroupRepository.toSpecification(authTemplateId, null))).ifPresent(authTplResGroups -> {
			authTplResGroups.forEach(authTplResGroup -> {
				ResGroup group = resGroupService.loadById(authTplResGroup.getResGroupId());
				if (group != null) {
					groups.add(resGroupService.loadWholeResources(group, menuIds, functionIds));
				}
			});
		});
		return groups;
	}

	/**
	 * ?????? id ???????????????????????????????????????ROOT.?????? ROOT ????????????????????????
	 * 
	 * @param resGroupId
	 */
	private void checkRootResGroup(String resGroupId) {
		ResGroup group = resGroupService.findById(resGroupId);
		if (!CoframeConstants.RES_GROUP_TYPE_ROOT.equals(group.getType())) {
			throw CoframeErrorCode.AUTH_TPL_CAN_ONLY_ADD_ROOT_RES_GROUP.runtimeException();
		}
	}

	public List<AuthTpl> queryALLAuthTpls(AuthTpl.Criteria criteria) {
		return repository.findAll(repository.toSpecification(criteria));
	}

	public List<ResGroup> queryResGroups(String authTplId, boolean showAllRootResGroups) {
		ResGroup.Criteria criteria = new ResGroup.Criteria();
		criteria.setType(CoframeConstants.RES_GROUP_TYPE_ROOT);
		List<ResGroup> allRootResGroups = resGroupService.findByCriteria(criteria);

		List<AuthTplResGroup> authTplResGroups = authTplResGroupRepository.findAll(authTplResGroupRepository.toSpecification(authTplId, null));
		if (showAllRootResGroups) {
			if (authTplResGroups != null) {
				OUTER: for (ResGroup resGroup : allRootResGroups) {
					for (AuthTplResGroup tplResGroup : authTplResGroups) {
						if (tplResGroup.getResGroupId().equals(resGroup.getId())) {
							resGroup.setSeleted(true);
							continue OUTER;
						}
					}
				}
			}
			return allRootResGroups;
		} else {
			List<ResGroup> resGroups = new ArrayList<>();
			if (authTplResGroups != null) {
				for (AuthTplResGroup tplResGroup : authTplResGroups) {
					resGroups.add(resGroupService.findById(tplResGroup.getResGroupId()));
				}
			}
			return resGroups;
		}
	}
}
