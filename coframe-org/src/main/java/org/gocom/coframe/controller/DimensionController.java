/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Mar 26, 2019
 *******************************************************************************/
package org.gocom.coframe.controller;

import static org.gocom.coframe.CoframeFunctionCode.DIMENSION_ADD;
import static org.gocom.coframe.CoframeFunctionCode.DIMENSION_EDIT;
import static org.gocom.coframe.CoframeFunctionCode.DIMENSION_DEL;
import static org.gocom.coframe.sdk.CofConstants.ACTION_FIND_BY_CRITERIA;
import static org.gocom.coframe.sdk.CofConstants.API_PATH_PREFIX;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;

import org.gocom.coframe.core.controller.AbstractPersistentModelController;
import org.gocom.coframe.core.service.AbstractPersistentModelService;
import org.gocom.coframe.model.Dimension;
import org.gocom.coframe.sdk.annotation.ClassBoundFunctions;
import org.gocom.coframe.sdk.annotation.BoundFunction;
import org.gocom.coframe.sdk.annotation.BoundFunctions;
import org.gocom.coframe.service.DimensionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

/**
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@RestController
@RequestMapping(value = API_PATH_PREFIX + "/dimensions", consumes = { APPLICATION_JSON_UTF8_VALUE }, produces = { APPLICATION_JSON_UTF8_VALUE })
@ClassBoundFunctions({ //
		@BoundFunctions(method = "create", functions = { @BoundFunction(DIMENSION_ADD) }), //
		@BoundFunctions(method = "update", functions = { @BoundFunction(DIMENSION_EDIT) }), //
		@BoundFunctions(method = "deleteByIds", functions = { @BoundFunction(DIMENSION_DEL) }),//
})
public class DimensionController extends AbstractPersistentModelController<Dimension> {
	@Autowired
	private DimensionService service;

	@Override
	protected AbstractPersistentModelService<Dimension> getService() {
		return service;
	}

	@ApiOperation("按条件查询维度")
	@RequestMapping(value = "/" + ACTION_FIND_BY_CRITERIA, method = GET, consumes = ALL_VALUE)
	public List<Dimension> queryByCriteria(//
			Dimension.Criteria criteria) {
		return service.queryByCriteria(criteria);
	}
}
