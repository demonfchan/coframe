/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Feb 28, 2019
 *******************************************************************************/
package org.gocom.coframe.controller;

import static org.gocom.coframe.CoframeFunctionCode.AUTH_TPL_ADD;
import static org.gocom.coframe.CoframeFunctionCode.AUTH_TPL_ADD_RES_GROUP;
import static org.gocom.coframe.CoframeFunctionCode.AUTH_TPL_DEL;
import static org.gocom.coframe.CoframeFunctionCode.AUTH_TPL_EDIT;
import static org.gocom.coframe.CoframeFunctionCode.AUTH_TPL_RM_RES_GROUP;
import static org.gocom.coframe.sdk.CofConstants.ACTION_FIND_BY_CRITERIA;
import static org.gocom.coframe.sdk.CofConstants.API_PATH_PREFIX;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.List;

import javax.validation.constraints.NotBlank;

import org.gocom.coframe.core.controller.AbstractPersistentModelController;
import org.gocom.coframe.core.service.AbstractPersistentModelService;
import org.gocom.coframe.model.AuthTpl;
import org.gocom.coframe.model.ResGroup;
import org.gocom.coframe.model.RoleTpl;
import org.gocom.coframe.sdk.annotation.ClassBoundFunctions;
import org.gocom.coframe.sdk.annotation.BoundFunction;
import org.gocom.coframe.sdk.annotation.BoundFunctions;
import org.gocom.coframe.service.AuthTplService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

/**
 * 权限模板操作
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@RestController
@RequestMapping(value = API_PATH_PREFIX + "/auth-templates", consumes = { APPLICATION_JSON_UTF8_VALUE }, produces = { APPLICATION_JSON_UTF8_VALUE })
@ClassBoundFunctions({ //
		@BoundFunctions(method = "create", functions = { @BoundFunction(AUTH_TPL_ADD) }), //
		@BoundFunctions(method = "update", functions = { @BoundFunction(AUTH_TPL_EDIT) }), //
		@BoundFunctions(method = "deleteByIds", functions = { @BoundFunction(AUTH_TPL_DEL) }),//
})
public class AuthTplController extends AbstractPersistentModelController<AuthTpl> {
	@Autowired
	private AuthTplService service;

	@Override
	protected AbstractPersistentModelService<AuthTpl> getService() {
		return service;
	}

	@ApiOperation("查询权限模板下的角色模板")
	@RequestMapping(value = "/{id}/role-templates", method = GET, consumes = ALL_VALUE)
	public List<RoleTpl> querySubRoleTemplates(//
			@NotBlank @PathVariable("id") String authTemplateId, //
			@RequestParam(name = "roleTemplateName", required = false) String roleTemplateName) {
		return service.querySubRoleTemplates(authTemplateId, roleTemplateName);
	}

	@ApiOperation("为权限模板添加根资源组")
	@RequestMapping(value = "/{id}/resource-grooups/{resGroupId}", method = PUT, consumes = ALL_VALUE)
	@BoundFunction(AUTH_TPL_ADD_RES_GROUP)
	public void addRootResourceGroups(//
			@NotBlank @PathVariable("id") String authTemplateId, //
			@NotBlank @PathVariable("resGroupId") String resGroupId) {
		service.addRootResourceGroups(authTemplateId, resGroupId);
	}

	@ApiOperation("从权限模板中移除根资源组")
	@RequestMapping(value = "/{id}/resource-grooups/{resGroupId}", method = DELETE, consumes = ALL_VALUE)
	@BoundFunction(AUTH_TPL_RM_RES_GROUP)
	public void removeRootResourceGroups(//
			@NotBlank @PathVariable("id") String authTemplateId, //
			@NotBlank @PathVariable("resGroupId") String resGroupId) {
		service.removeRootResourceGroups(authTemplateId, resGroupId);
	}

	@ApiOperation("查询某个权限模板下的根资源组")
	@RequestMapping(value = "/{id}/resource-grooups", method = GET, consumes = ALL_VALUE)
	public List<ResGroup> queryResGroups(@NotBlank @PathVariable("id") String authTplId, @RequestParam(name = "showAllRootResGroups", required = false, defaultValue = "false") boolean showAllRootResGroups) {
		return service.queryResGroups(authTplId, showAllRootResGroups);
	}

	@ApiOperation("列出所有的权限模板")
	@RequestMapping(value = "/" + ACTION_FIND_BY_CRITERIA, method = GET, consumes = ALL_VALUE)
	public List<AuthTpl> queryALLAuthTpls(AuthTpl.Criteria criteria) {
		return service.queryALLAuthTpls(criteria);
	}
}
