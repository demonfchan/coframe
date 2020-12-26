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
import static org.gocom.coframe.CoframeFunctionCode.ROLE_TPL_ADD;
import static org.gocom.coframe.CoframeFunctionCode.ROLE_TPL_DEL;
import static org.gocom.coframe.CoframeFunctionCode.ROLE_TPL_EDIT;
import static org.gocom.coframe.CoframeFunctionCode.ROLE_TPL_EDIT_RES;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.gocom.coframe.core.controller.AbstractPersistentModelController;
import org.gocom.coframe.core.model.CoframePageRequest;
import org.gocom.coframe.core.service.AbstractPersistentModelService;
import org.gocom.coframe.model.OperatableResource;
import org.gocom.coframe.model.ResGroup;
import org.gocom.coframe.model.Role;
import org.gocom.coframe.model.RoleTpl;
import org.gocom.coframe.sdk.annotation.ClassBoundFunctions;
import org.gocom.coframe.sdk.annotation.BoundFunction;
import org.gocom.coframe.sdk.annotation.BoundFunctions;
import org.gocom.coframe.service.RoleTplService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
@RequestMapping(value = API_PATH_PREFIX + "/role-templates", consumes = { APPLICATION_JSON_UTF8_VALUE }, produces = { APPLICATION_JSON_UTF8_VALUE })
@ClassBoundFunctions({ //
		@BoundFunctions(method = "create", functions = { @BoundFunction(ROLE_TPL_ADD) }), //
		@BoundFunctions(method = "update", functions = { @BoundFunction(ROLE_TPL_EDIT) }), //
		@BoundFunctions(method = "deleteByIds", functions = { @BoundFunction(ROLE_TPL_DEL) }),//
})
public class RoleTplController extends AbstractPersistentModelController<RoleTpl> {
	@Autowired
	private RoleTplService service;

	@Override
	protected AbstractPersistentModelService<RoleTpl> getService() {
		return service;
	}

	@ApiOperation("按条件查询")
	@RequestMapping(value = "/" + ACTION_FIND_BY_CRITERIA, method = GET, consumes = ALL_VALUE)
	public List<RoleTpl> findByCriteria(RoleTpl.Criteria criteria) {
		return service.findByCriteria(criteria);
	}

	@ApiOperation("查询使用此模板创建的角色")
	@RequestMapping(value = "/{id}/roles", method = GET, consumes = ALL_VALUE)
	public Page<Role> pageRoleByTemplateId(//
			@NotBlank @PathVariable("id") String roleTplId, //
			@RequestParam(value = "roleName", required = false) String roleName, //
			@NotNull CoframePageRequest pageRequest) {
		return service.pageRolesByRoleTplId(roleTplId, roleName, pageRequest.createPageable());
	}

	@ApiOperation("查询角色模板对应的资源树")
	@RequestMapping(value = "/{id}/resources", method = GET, consumes = ALL_VALUE)
	public List<ResGroup> loadWholeResources(//
			@NotBlank @PathVariable("id") String roleTplId) {
		return service.loadWholeResources(roleTplId);
	}

	@ApiOperation("修改角色模板资源")
	@RequestMapping(value = "/{id}/resources", method = PUT, consumes = ALL_VALUE)
	@BoundFunction(ROLE_TPL_EDIT_RES)
	public void handleRoleTplResource(//
			@NotBlank @PathVariable("id") String roleTplId, //
			@Validated @NotNull @RequestBody OperatableResource[] operatableResources) {
		service.handleRoleTplResource(roleTplId, operatableResources);
	}
}
