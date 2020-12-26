/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Feb 28, 2019
 *******************************************************************************/
package org.gocom.coframe.controller;

import static org.gocom.coframe.CoframeConstants.API_PATH_PREFIX;
import static org.gocom.coframe.CoframeFunctionCode.MENU_ADD;
import static org.gocom.coframe.CoframeFunctionCode.MENU_DEL;
import static org.gocom.coframe.CoframeFunctionCode.MENU_EDIT;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import org.gocom.coframe.core.controller.AbstractTreeModelController;
import org.gocom.coframe.core.service.AbstractTreeModelService;
import org.gocom.coframe.model.Menu;
import org.gocom.coframe.sdk.annotation.ClassBoundFunctions;
import org.gocom.coframe.sdk.annotation.BoundFunction;
import org.gocom.coframe.sdk.annotation.BoundFunctions;
import org.gocom.coframe.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 权限操作
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@RestController
@RequestMapping(value = API_PATH_PREFIX + "/menus", consumes = { APPLICATION_JSON_UTF8_VALUE }, produces = { APPLICATION_JSON_UTF8_VALUE })
@ClassBoundFunctions({ //
		@BoundFunctions(method = "create", functions = { @BoundFunction(MENU_ADD) }), //
		@BoundFunctions(method = "update", functions = { @BoundFunction(MENU_EDIT) }), //
		@BoundFunctions(method = "deleteByIds", functions = { @BoundFunction(MENU_DEL) }),//
})
public class MenuController extends AbstractTreeModelController<Menu, Menu.Criteria> {
	@Autowired
	private MenuService service;

	@Override
	protected AbstractTreeModelService<Menu, Menu.Criteria> getService() {
		return service;
	}
}
