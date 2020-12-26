/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Mar 4, 2019
 *******************************************************************************/
package org.gocom.coframe.service;

import java.util.List;

import org.gocom.coframe.core.exception.CoframeErrorCode;
import org.gocom.coframe.core.jpa.CoframeJpaRepository;
import org.gocom.coframe.core.service.AbstractTreeModelService;
import org.gocom.coframe.model.Menu;
import org.gocom.coframe.model.Menu.Criteria;
import org.gocom.coframe.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

/**
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@Service
public class MenuService extends AbstractTreeModelService<Menu, Menu.Criteria> {
	@Autowired
	private MenuRepository repository;

	@Override
	protected CoframeJpaRepository<Menu, String> getRepository() {
		return repository;
	}

	/**
	 * 根据条件查询
	 * 
	 * @param criteria
	 * @return
	 */
	public List<Menu> findByCriteria(Menu.Criteria criteria) {
		return repository.findAll(repository.toSpecification(criteria),new Sort(Direction.DESC,"createTime"));
	}

	@Override
	public Page<Menu> pagingByCriteria(Criteria criteria, Pageable pageable) {
		Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), new Sort(Direction.ASC, "sortNo"));
		return repository.findAll(repository.toSpecification(criteria), pageRequest);
	}

	/**
	 * 根据资源组 id 查询
	 * 
	 * @param resGroupId
	 * @return
	 */
	public List<Menu> findByResGroupId(String resGroupId) {
		Menu.Criteria criteria = new Menu.Criteria();
		criteria.setGroupId(resGroupId);
		return repository.findAll(repository.toSpecification(criteria));
	}

	@Override
	public CoframeErrorCode getDeleteNotAllowedByHasChildrenErrorCode() {
		return CoframeErrorCode.MENU_DELETE_NOT_ALLOWED_BY_HAS_CHILDREN;
	}
}
