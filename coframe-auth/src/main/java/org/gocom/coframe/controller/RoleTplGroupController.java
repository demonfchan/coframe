/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Feb 28, 2019
 *******************************************************************************/
package org.gocom.coframe.controller;

import static org.gocom.coframe.CoframeConstants.ACTION_FIND_BY_CRITERIA;
import static org.gocom.coframe.CoframeConstants.API_PATH_PREFIX;
import static org.gocom.coframe.CoframeFunctionCode.ROLE_TPL_GRP_ADD;
import static org.gocom.coframe.CoframeFunctionCode.ROLE_TPL_GRP_DEL;
import static org.gocom.coframe.CoframeFunctionCode.ROLE_TPL_GRP_EDIT;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import javax.validation.constraints.NotBlank;

import org.gocom.coframe.core.controller.AbstractPersistentModelController;
import org.gocom.coframe.core.service.AbstractPersistentModelService;
import org.gocom.coframe.model.Role;
import org.gocom.coframe.model.RoleTplGroup;
import org.gocom.coframe.sdk.annotation.ClassBoundFunctions;
import org.gocom.coframe.sdk.annotation.BoundFunction;
import org.gocom.coframe.sdk.annotation.BoundFunctions;
import org.gocom.coframe.service.RoleTplGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

/**
 * 角色模板操作
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@RestController
@RequestMapping(value = API_PATH_PREFIX + "/role-template-groups", consumes = { APPLICATION_JSON_UTF8_VALUE }, produces = { APPLICATION_JSON_UTF8_VALUE })
@ClassBoundFunctions({ //
		@BoundFunctions(method = "create", functions = { @BoundFunction(ROLE_TPL_GRP_ADD) }), //
		@BoundFunctions(method = "update", functions = { @BoundFunction(ROLE_TPL_GRP_EDIT) }), //
		@BoundFunctions(method = "deleteByIds", functions = { @BoundFunction(ROLE_TPL_GRP_DEL) }),//
})
public class RoleTplGroupController extends AbstractPersistentModelController<RoleTplGroup> {
	@Autowired
	private RoleTplGroupService service;

	@Override
	protected AbstractPersistentModelService<RoleTplGroup> getService() {
		return service;
	}

	@ApiOperation("按条件查询")
	@RequestMapping(value = "/" + ACTION_FIND_BY_CRITERIA, method = GET, consumes = ALL_VALUE)
	public List<RoleTplGroup> findByCriteria(RoleTplGroup.Criteria criteria) {
		return service.findByCriteria(criteria);
	}

	@ApiOperation("按条件查询")
	@RequestMapping(value = "/{id}/new-role-instances", method = POST, consumes = ALL_VALUE)
	public List<Role> newRoleInstances( //
			@NotBlank @PathVariable("id") String roleTplGroupId, //
			@RequestParam(value = "ownerType", required = false) String ownerType, //
			@RequestParam(value = "ownerId", required = false) String ownerId //
	) {
		return service.newRoleInstances(roleTplGroupId, ownerType, ownerId);
	}
}
