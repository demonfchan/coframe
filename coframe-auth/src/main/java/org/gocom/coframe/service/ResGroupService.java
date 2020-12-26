/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Mar 4, 2019
 *******************************************************************************/
package org.gocom.coframe.service;

import java.util.List;
import java.util.Optional;

import org.gocom.coframe.CoframeConstants;
import org.gocom.coframe.core.exception.CoframeErrorCode;
import org.gocom.coframe.core.jpa.CoframeJpaRepository;
import org.gocom.coframe.core.service.AbstractTreeModelService;
import org.gocom.coframe.model.Function;
import org.gocom.coframe.model.Menu;
import org.gocom.coframe.model.ResGroup;
import org.gocom.coframe.repository.ResGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@Service
public class ResGroupService extends AbstractTreeModelService<ResGroup, ResGroup.Criteria> {
	@Autowired
	private ResGroupRepository repository;

	@Autowired
	private MenuService menuService;

	@Autowired
	private FunctionService functionService;

	@Override
	protected CoframeJpaRepository<ResGroup, String> getRepository() {
		return repository;
	}

	/**
	 * 按条件查询权限组
	 * 
	 * @param criteria
	 * @return
	 */
	public List<ResGroup> findByCriteria(ResGroup.Criteria criteria) {
		List<ResGroup> resGroups = repository.findAll(repository.toSpecification(criteria));
		if (criteria.isLoadChildren() && resGroups != null) {
			resGroups.forEach(resGroup -> {
				loadTreeModel(resGroup, criteria.getLoadChildrenLevel(), criteria.isLoadOthers());
			});
		}
		if (resGroups != null) {
			resGroups.stream().forEach(resGroup -> {
				if (resGroup != null) {
					if (resGroup.getChildren() != null) {
						resGroup.getChildren().stream().forEach(res -> {
							if (res != null) {
								if (res.getChildren() != null) {
									res.getChildren().sort((r1, r2) -> {
										if (Optional.ofNullable(r1.getSortNo()).isPresent() && 
												r1.getSortNo().compareTo(r2.getSortNo()) == 0
												&& Optional.ofNullable(r1.getCreateTime()).isPresent()
												&& Optional.ofNullable(r2.getCreateTime()).isPresent()) {
											return -r1.getCreateTime().compareTo(r2.getCreateTime());
										} else {
											return Integer.valueOf(r1.getSortNo()).compareTo(r2.getSortNo());
										}
									});
								}
							}
						});
					}
				}
			});
		}
		return resGroups;
	}

	@Override
	public void loadTreeModelOthers(ResGroup model) {
		if (CoframeConstants.RES_GROUP_TYPE_MODULE.equals(model.getType())) {
			if (CoframeConstants.RESOURCE_TYPE_MENU.equals(model.getResType())) {
				model.getMenus().addAll(menuService.findByResGroupId(model.getId()));
			} else if (CoframeConstants.RESOURCE_TYPE_FUNCTION.equals(model.getResType())) {
				model.getFunctions().addAll(functionService.findByResGroupId(model.getId()));
			}
		}
	}

	/**
	 * 分页查询
	 * 
	 * @param criteria
	 * @param pageable
	 * @return
	 */
	@Override
	public Page<ResGroup> pagingByCriteria(ResGroup.Criteria criteria, Pageable pageable) {
		Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), new Sort(Direction.ASC, "sortNo"));
		Page<ResGroup> page = repository.findAll(repository.toSpecification(criteria), pageRequest);
		if (page != null) {
			List<ResGroup> resGroups = page.getContent();
			if (criteria.isLoadChildren() && resGroups != null) {
				resGroups.forEach(resGroup -> {
					loadTreeModel(resGroup, criteria.getLoadChildrenLevel(), criteria.isLoadOthers());
				});
			}
		}
		return page;
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public ResGroup update(ResGroup model) {
		return update(model, true);
	}

	/**
	 * 查询某权限组下的菜单
	 * 
	 * @param groupId
	 * @param criteria
	 * @return
	 */
	public List<Menu> findSubMenuByCriteria(String groupId, Menu.Criteria criteria) {
		criteria.setGroupId(groupId);
		return menuService.findByCriteria(criteria);
	}

	/**
	 * 查询某权限组下的功能
	 * 
	 * @param groupId
	 * @param criteria
	 * @return
	 */
	public List<Function> findSubFunctionByCriteria(String groupId, Function.Criteria criteria) {
		criteria.setGroupId(groupId);
		return functionService.findByCriteria(criteria);
	}

	/**
	 * 加载资源组之下的资源
	 * 
	 * @param group
	 * @return
	 */
	public ResGroup loadWholeResources(ResGroup group, List<String> menuIds, List<String> functionIds) {
		if (group.isLeaf()) {
			// 加载菜单
			if (CoframeConstants.RES_GROUP_TYPE_MODULE.equals(group.getType())) {
				if (CoframeConstants.RESOURCE_TYPE_MENU.equals(group.getResType())) {
					Optional.ofNullable(menuService.findByResGroupId(group.getId())).ifPresent(menus -> {
						if (menuIds != null) {
							menus.forEach(menu -> {
								if (menuIds.contains(menu.getId())) {
									menu.setSelected(true);
								}
							});
						}
						group.getMenus().addAll(menus);
					});
				} else if (CoframeConstants.RESOURCE_TYPE_FUNCTION.equals(group.getResType())) {
					Optional.ofNullable(functionService.findByResGroupId(group.getId())).ifPresent(functions -> {
						if (functionIds != null) {
							functions.forEach(function -> {
								if (functionIds.contains(function.getId())) {
									function.setSelected(true);
								}
							});
						}
						group.getFunctions().addAll(functions);
					});
				}
			}
		} else {
			group.getChildren().forEach(subGroup -> {
				loadWholeResources(subGroup, menuIds, functionIds);
			});
		}
		return group;
	}

	@Override
	public CoframeErrorCode getDeleteNotAllowedByHasChildrenErrorCode() {
		return CoframeErrorCode.RES_GROUP_DELETE_NOT_ALLOWED_BY_HAS_CHILDREN;
	}
}
