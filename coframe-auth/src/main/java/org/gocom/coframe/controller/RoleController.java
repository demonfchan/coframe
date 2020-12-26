/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Feb 28, 2019
 *******************************************************************************/
package org.gocom.coframe.controller;

import static org.gocom.coframe.CoframeFunctionCode.ROLE_ADD;
import static org.gocom.coframe.CoframeFunctionCode.ROLE_BIND;
import static org.gocom.coframe.CoframeFunctionCode.ROLE_DEL;
import static org.gocom.coframe.CoframeFunctionCode.ROLE_EDIT;
import static org.gocom.coframe.CoframeFunctionCode.ROLE_EDIT_RES;
import static org.gocom.coframe.CoframeFunctionCode.ROLE_UNBIND;
import static org.gocom.coframe.sdk.CofConstants.ACTION_PAGING_BY_CRITERIA;
import static org.gocom.coframe.sdk.CofConstants.API_PATH_PREFIX;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
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
import org.gocom.coframe.model.User;
import org.gocom.coframe.sdk.annotation.ClassBoundFunctions;
import org.gocom.coframe.sdk.annotation.BoundFunction;
import org.gocom.coframe.sdk.annotation.BoundFunctions;
import org.gocom.coframe.service.RoleService;
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
 * 角色操作
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@RestController
@RequestMapping(value = API_PATH_PREFIX + "/roles", consumes = { APPLICATION_JSON_UTF8_VALUE }, produces = { APPLICATION_JSON_UTF8_VALUE })
@ClassBoundFunctions({ //
		@BoundFunctions(method = "create", functions = { @BoundFunction(ROLE_ADD) }), //
		@BoundFunctions(method = "update", functions = { @BoundFunction(ROLE_EDIT) }), //
		@BoundFunctions(method = "deleteByIds", functions = { @BoundFunction(ROLE_DEL) }),//
})
public class RoleController extends AbstractPersistentModelController<Role> {
	@Autowired
	private RoleService service;

	@Override
	protected AbstractPersistentModelService<Role> getService() {
		return service;
	}

	@ApiOperation("分页查询所有角色")
	@RequestMapping(value = "/" + ACTION_PAGING_BY_CRITERIA, method = GET, consumes = ALL_VALUE)
	public Page<Role> pagingByCriteria(//
			Role.Criteria criteria, //
			@NotNull CoframePageRequest pageRequest) {
		return service.pagingByCriteria(criteria, pageRequest.createPageable());
	}

	@ApiOperation("分页查询拥有某角色的用户")
	@RequestMapping(value = "/{id}/users", method = GET, consumes = ALL_VALUE)
	public Page<User> getUsersByRoleId(//
			@NotBlank @PathVariable("id") String roleId, //
			@RequestParam(name = "name", required = false) String userName, @NotNull CoframePageRequest pageRequest) {
		return service.getUsersByRoleId(roleId, userName, pageRequest.createPageable());
	}

	@ApiOperation("将角色绑定至用户(用户id以','分割)")
	@RequestMapping(value = "/{id}/users/{userIds}", method = PUT, consumes = ALL_VALUE)
	@BoundFunction(ROLE_BIND)
	public void bindRoleToUsers(@PathVariable(name = "id") String roleId, @PathVariable(name = "userIds") String... userIds) {
		service.bindRoleToUsers(roleId, userIds);
	}

	@ApiOperation("将角色绑定的用户解绑(用户id以','分割)")
	@RequestMapping(value = "/{id}/users/{userIds}", method = DELETE, consumes = ALL_VALUE)
	@BoundFunction(ROLE_UNBIND)
	public void unbindRoleToUsers(@PathVariable(name = "id") String roleId, @PathVariable(name = "userIds") String... userIds) {
		service.unbindRoleToUsers(roleId, userIds);
	}

	@ApiOperation("查询角色对应的全部资源")
	@RequestMapping(value = "/{id}/resources", method = GET, consumes = ALL_VALUE)
	public List<ResGroup> loadWholeResources(//
			@NotBlank @PathVariable("id") String roleId) {
		return service.loadWholeResources(roleId);
	}

	@ApiOperation("修改角色资源")
	@RequestMapping(value = "/{id}/resources", method = PUT, consumes = ALL_VALUE)
	@BoundFunction(ROLE_EDIT_RES)
	public void handleRoleTplResource(//
			@NotBlank @PathVariable("id") String roleId, //
			@NotNull @Validated @RequestBody OperatableResource[] operatableResources) {
		service.handleRoleTplResource(roleId, operatableResources);
	}
}
