/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Mar 26, 2019
 *******************************************************************************/
package org.gocom.coframe.service;

import java.util.List;

import org.gocom.coframe.core.exception.CoframeErrorCode;
import org.gocom.coframe.core.jpa.CoframeJpaRepository;
import org.gocom.coframe.core.service.AbstractMainModelService;
import org.gocom.coframe.model.Dimension;
import org.gocom.coframe.model.Dimension.Criteria;
import org.gocom.coframe.model.Organization;
import org.gocom.coframe.repository.DimensionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@Service
public class DimensionService extends AbstractMainModelService<Dimension> {
	@Autowired
	private DimensionRepository repository;

	@Autowired
	private OrganizationService orgService;

	@Override
	protected CoframeJpaRepository<Dimension, String> getRepository() {
		return repository;
	}

	@Override
	public void preCreate(Dimension model) {
		super.preCreate(model);
		checkMainDimension(model);
	}

	@Override
	public Dimension update(Dimension model) {
		// 想要改为主维度
		if (model.isMain()) {
			Dimension dimension = findById(model.getId());
			// 本身不为主维度
			if (!dimension.isMain()) {
				// 但已有其它主维度了，抛异常
				checkMainDimension(model);
			}
		}
		// 就算是 fixed 的维度，也可以修改名字等. 忽略 fixed 属性强制保存
		return repository.forceSave(model);
	}

	/**
	 * 如果为主维度，查一下是否已有主维度了。只允许有一个主维度
	 * 
	 * @param model
	 */
	private void checkMainDimension(Dimension model) {
		if (model.isMain()) {
			Criteria criteria = new Criteria();
			criteria.setMain(true);
			if (repository.count(repository.toSpecification(criteria)) > 0) {
				throw CoframeErrorCode.MAIN_DIMENSION_ONLY_ONE.runtimeException();
			}
		}
	}

	@Override
	public void preDelete(String id) {
		Organization.Criteria criteria = new Organization.Criteria();
		criteria.setDimensionId(id);
		if (orgService.countByCriteria(criteria) > 0) {
			throw CoframeErrorCode.DIMENSION_DELETE_NOT_ALLOWED_BY_HAS_CHILDREN.runtimeException();
		}
	}

	public List<Dimension> queryByCriteria(Criteria criteria) {
		return repository.findAll(repository.toSpecification(criteria));
	}
}
