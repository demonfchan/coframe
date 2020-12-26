/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Feb 28, 2019
 *******************************************************************************/
package org.gocom.coframe.controller;

import static org.gocom.coframe.CoframeFunctionCode.MEMBER_ADD;
import static org.gocom.coframe.CoframeFunctionCode.MEMBER_DEL;
import static org.gocom.coframe.sdk.CofConstants.ACTION_PAGING_BY_CRITERIA;
import static org.gocom.coframe.sdk.CofConstants.API_PATH_PREFIX;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.gocom.coframe.core.model.CoframePageRequest;
import org.gocom.coframe.model.Member;
import org.gocom.coframe.sdk.annotation.BoundFunction;
import org.gocom.coframe.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

/**
 * 角户操作
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@RestController
@RequestMapping(value = API_PATH_PREFIX + "/members", consumes = { APPLICATION_JSON_UTF8_VALUE }, produces = { APPLICATION_JSON_UTF8_VALUE })
public class MemberController {
	@Autowired
	private MemberService service;

	@ApiOperation("分页查询所有成员")
	@RequestMapping(value = "/" + ACTION_PAGING_BY_CRITERIA, method = GET, consumes = ALL_VALUE)
	public Page<Member> pagingMembers(//
			@NotBlank @RequestParam(name = "ownerType", required = true) String ownerType, //
			@NotBlank @RequestParam(name = "ownerId", required = true) String ownerId, //
			@RequestParam(name = "userName", required = false) String userName, //
			CoframePageRequest pageRequest) {
		return service.pagingMembers(ownerType, ownerId, userName, pageRequest.createPageable());
	}

	@ApiOperation("添加成员")
	@RequestMapping(value = "/", method = POST, consumes = ALL_VALUE)
	@BoundFunction(MEMBER_ADD)
	public Member[] addMembers(@Validated @NotNull @RequestBody Member... members) {
		return service.addMember(members);
	}

	@ApiOperation("删除成员")
	@RequestMapping(value = "/", method = DELETE, consumes = ALL_VALUE)
	@BoundFunction(MEMBER_DEL)
	public void deleteMembers(@Validated @NotNull @RequestBody Member[] members) {
		service.deleteMembers(members);
	}
}
