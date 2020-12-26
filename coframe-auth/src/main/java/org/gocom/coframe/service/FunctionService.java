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

import org.gocom.coframe.core.jpa.CoframeJpaRepository;
import org.gocom.coframe.core.service.AbstractMainModelService;
import org.gocom.coframe.model.Function;
import org.gocom.coframe.repository.FunctionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@Service
public class FunctionService extends AbstractMainModelService<Function> implements IFunctionService {
	@Autowired
	private FunctionRepository repository;

	@Override
	protected CoframeJpaRepository<Function, String> getRepository() {
		return repository;
	}

	/**
	 * 根据条件查询
	 * 
	 * @param criteria
	 * @return
	 */
	public List<Function> findByCriteria(Function.Criteria criteria) {
		return repository.findAll(repository.toSpecification(criteria),new Sort(Sort.Direction.DESC,"createTime"));
	}

	/**
	 * 根据资源组 id 查询
	 * 
	 * @param resGroupId
	 * @return
	 */
	public List<Function> findByResGroupId(String resGroupId) {
		Function.Criteria criteria = new Function.Criteria();
		criteria.setGroupId(resGroupId);
		return repository.findAll(repository.toSpecification(criteria));
	}

	@Override
	public Optional<Function> findByCode(String code) {
		Function.Criteria criteria = new Function.Criteria();
		criteria.setCode(code);
		return repository.findOne(repository.toSpecification(criteria));
	}
}
