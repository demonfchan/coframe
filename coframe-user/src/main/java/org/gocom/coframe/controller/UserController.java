/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Feb 28, 2019
 *******************************************************************************/
package org.gocom.coframe.controller;

import static org.gocom.coframe.CoframeFunctionCode.USER_ADD;
import static org.gocom.coframe.CoframeFunctionCode.USER_CHANGE_PASSWORD;
import static org.gocom.coframe.CoframeFunctionCode.USER_DEL;
import static org.gocom.coframe.CoframeFunctionCode.USER_EDIT_STATUS;
import static org.gocom.coframe.CoframeFunctionCode.USER_RESET_PASSWORD;
import static org.gocom.coframe.sdk.CofConstants.ACTION_PAGING_BY_CRITERIA;
import static org.gocom.coframe.sdk.CofConstants.API_PATH_PREFIX;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;

import org.gocom.coframe.core.model.CoframePageRequest;
import org.gocom.coframe.core.support.CoframeValidationGroups;
import org.gocom.coframe.model.Function;
import org.gocom.coframe.model.Menu;
import org.gocom.coframe.model.Role;
import org.gocom.coframe.model.User;
import org.gocom.coframe.model.UserPassword;
import org.gocom.coframe.sdk.annotation.BoundFunction;
import org.gocom.coframe.sdk.model.CofUser;
import org.gocom.coframe.service.UserService;
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
 * ????????????
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@RestController
@RequestMapping(value = API_PATH_PREFIX + "/users", consumes = { APPLICATION_JSON_UTF8_VALUE }, produces = { APPLICATION_JSON_UTF8_VALUE })
public class UserController {
	@Autowired
	private UserService service;

	@ApiOperation("????????????, ??????????????????????????????????????????????????????????????????")
	@RequestMapping(method = POST)
	@BoundFunction(USER_ADD)
	public User create(@Validated({ CoframeValidationGroups.Create.class }) @RequestBody User user) {
		return service.create(user);
	}

	@ApiOperation("???????????????")
	@RequestMapping(value = "/{id}", method = GET, consumes = ALL_VALUE)
	public User findById(@PathVariable(name = "id") String id) {
		return service.findById(id);
	}

	@ApiOperation("????????????????????????")
	@RequestMapping(value = "/" + ACTION_PAGING_BY_CRITERIA, method = GET, consumes = ALL_VALUE)
	public Page<User> pagingByCriteria(//
			User.Criteria criteria, //
			CoframePageRequest pageRequest) {
		return service.pagingByCriteria(criteria, pageRequest.createPageable());
	}

	@ApiOperation("??????????????????")
	@RequestMapping(value = "/{id}/modify-password", method = PUT, consumes = ALL_VALUE)
	@BoundFunction(USER_CHANGE_PASSWORD)
	public void modifyPassword(//
			@NotBlank @PathVariable("id") String userId, //
			@Validated({ CoframeValidationGroups.Create.class }) @RequestBody UserPassword password) {
		service.changePassword(userId, password);
	}

	@ApiOperation("??????????????????")
	@RequestMapping(value = "/{id}/reset-password", method = PUT, consumes = ALL_VALUE)
	@BoundFunction(USER_RESET_PASSWORD)
	public void resetPassword(@NotBlank @PathVariable("id") String userId) {
		service.resetPassword(userId);
	}

	@ApiOperation("????????????")
	@RequestMapping(value = "/{id}/enable", method = POST, consumes = ALL_VALUE)
	@BoundFunction(USER_EDIT_STATUS)
	public void enableUser(@NotBlank @PathVariable("id") String userId) {
		service.enableUser(userId);
	}

	@ApiOperation("????????????")
	@RequestMapping(value = "/{id}/disable", method = POST, consumes = ALL_VALUE)
	@BoundFunction(USER_EDIT_STATUS)
	public void disableUser(@NotBlank @PathVariable("id") String userId) {
		service.disableUser(userId);
	}

	@ApiOperation("???????????????????????????")
	@RequestMapping(value = "/menus", method = GET, consumes = ALL_VALUE)
	public Set<Menu> listUserMenus( //
			@RequestParam(name = "ownerType", required = false) String ownerType, //
			@RequestParam(name = "ownerId", required = false) String ownerId) {
		return service.listUserMenus(service.getCurrentLoginedUser().getId(), ownerType, ownerId);
	}

	@ApiOperation("????????????????????????????????????")
	@RequestMapping(value = "/functions", method = GET, consumes = ALL_VALUE)
	public Set<Function> listUserFunctions( //
			@RequestParam(name = "ownerType", required = false) String ownerType, //
			@RequestParam(name = "ownerId", required = false) String ownerId) {
		return service.listUserFunctions(service.getCurrentLoginedUser().getId(), ownerType, ownerId);
	}

	@ApiOperation("??????????????????????????????????????????")
	@RequestMapping(value = "/function-codes", method = GET, consumes = ALL_VALUE)
	public Set<String> listUserFunctionCodes( //
			@RequestParam(name = "ownerType", required = false) String ownerType, //
			@RequestParam(name = "ownerId", required = false) String ownerId) {
		return service.getUserFuctionCodes(service.getCurrentLoginedUser().getId(), ownerType, ownerId);
	}

	@ApiOperation("????????????, ????????????, ?????????????????????")
	@RequestMapping(value = "/{ids}", method = DELETE, consumes = ALL_VALUE)
	@BoundFunction(USER_DEL)
	public void deleteByIds(@PathVariable(name = "ids") String... ids) {
		service.deleteByIds(ids);
	}

	@ApiOperation("???????????????????????????")
	@RequestMapping(value = "/{id}/roles", method = GET, consumes = ALL_VALUE)
	public Set<Role> listRoles(@NotBlank @PathVariable("id") String userId) {
		return service.listRoles(userId);
	}

	@ApiOperation("????????????????????????")
	@RequestMapping(value = "/get/current-user", method = GET, consumes = ALL_VALUE)
	public User getSsoUser(HttpServletRequest request) {
		CofUser cofUser = service.getCurrentLoginedUser();
		User user = new User();
		user.setId(cofUser.getId());
		user.setName(cofUser.getName());
		user.setTenantId(cofUser.getTenantId());
		return user;
	}
	
	@ApiOperation("????????????token")
	@RequestMapping(value = "/get/auth-token", method = GET, consumes = ALL_VALUE)
	public String getAuthToken(HttpServletRequest request) {
		return null;
	}
}
