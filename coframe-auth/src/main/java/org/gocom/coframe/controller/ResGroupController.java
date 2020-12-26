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
import static org.gocom.coframe.CoframeFunctionCode.RES_GROUP_ADD;
import static org.gocom.coframe.CoframeFunctionCode.RES_GROUP_DEL;
import static org.gocom.coframe.CoframeFunctionCode.RES_GROUP_EDIT;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;

import javax.validation.constraints.NotBlank;

import org.gocom.coframe.core.controller.AbstractTreeModelController;
import org.gocom.coframe.core.service.AbstractTreeModelService;
import org.gocom.coframe.model.Function;
import org.gocom.coframe.model.Menu;
import org.gocom.coframe.model.ResGroup;
import org.gocom.coframe.sdk.annotation.ClassBoundFunctions;
import org.gocom.coframe.sdk.annotation.BoundFunction;
import org.gocom.coframe.sdk.annotation.BoundFunctions;
import org.gocom.coframe.service.ResGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

/**
 * 权限组操作
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@RestController
@RequestMapping(value = API_PATH_PREFIX + "/resource-groups", consumes = { APPLICATION_JSON_UTF8_VALUE }, produces = { APPLICATION_JSON_UTF8_VALUE })
@ClassBoundFunctions({ //
		@BoundFunctions(method = "create", functions = { @BoundFunction(RES_GROUP_ADD) }), //
		@BoundFunctions(method = "update", functions = { @BoundFunction(RES_GROUP_EDIT) }), //
		@BoundFunctions(method = "deleteByIds", functions = { @BoundFunction(RES_GROUP_DEL) }),//
})
public class ResGroupController extends AbstractTreeModelController<ResGroup, ResGroup.Criteria> {
	@Autowired
	private ResGroupService service;

	@Override
	protected AbstractTreeModelService<ResGroup, ResGroup.Criteria> getService() {
		return service;
	}

	@ApiOperation("按条件查询")
	@RequestMapping(value = "/" + ACTION_FIND_BY_CRITERIA, method = GET, consumes = ALL_VALUE)
	public List<ResGroup> findByCriteria(ResGroup.Criteria criteria) {
		return service.findByCriteria(criteria);
	}

	@ApiOperation("查询某资源组下的菜单")
	@RequestMapping(value = "/{groupId}/menus", method = GET, consumes = ALL_VALUE)
	public List<Menu> findSubMenuByCriteria(//
			@NotBlank @PathVariable("groupId") String groupId, //
			Menu.Criteria criteria) {
		return service.findSubMenuByCriteria(groupId, criteria);
	}

	@ApiOperation("查询某资源组下的功能")
	@RequestMapping(value = "/{groupId}/functions", method = GET, consumes = ALL_VALUE)
	public List<Function> findSubFunctionByCriteria(//
			@NotBlank @PathVariable("groupId") String groupId, //
			Function.Criteria criteria) {
		return service.findSubFunctionByCriteria(groupId, criteria);
	}
}
