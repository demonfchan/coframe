/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Feb 28, 2019
 *******************************************************************************/
package org.gocom.coframe.controller;

import static org.gocom.coframe.CoframeFunctionCode.WORKGROUP_ADD;
import static org.gocom.coframe.CoframeFunctionCode.WORKGROUP_ADD_EMP;
import static org.gocom.coframe.CoframeFunctionCode.WORKGROUP_DEL;
import static org.gocom.coframe.CoframeFunctionCode.WORKGROUP_EDIT;
import static org.gocom.coframe.CoframeFunctionCode.WORKGROUP_RM_EMP;
import static org.gocom.coframe.sdk.CofConstants.API_PATH_PREFIX;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import javax.validation.constraints.NotBlank;

import org.gocom.coframe.core.controller.AbstractTreeModelController;
import org.gocom.coframe.core.model.CoframePageRequest;
import org.gocom.coframe.core.service.AbstractTreeModelService;
import org.gocom.coframe.model.Employee;
import org.gocom.coframe.model.Workgroup;
import org.gocom.coframe.sdk.annotation.ClassBoundFunctions;
import org.gocom.coframe.sdk.annotation.BoundFunction;
import org.gocom.coframe.sdk.annotation.BoundFunctions;
import org.gocom.coframe.service.WorkgroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

/**
 * 工作组操作
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@RestController
@RequestMapping(value = API_PATH_PREFIX + "/workgroups", consumes = { APPLICATION_JSON_UTF8_VALUE }, produces = { APPLICATION_JSON_UTF8_VALUE })
@ClassBoundFunctions({ //
		@BoundFunctions(method = "create", functions = { @BoundFunction(WORKGROUP_ADD) }), //
		@BoundFunctions(method = "update", functions = { @BoundFunction(WORKGROUP_EDIT) }), //
		@BoundFunctions(method = "deleteByIds", functions = { @BoundFunction(WORKGROUP_DEL) }),//
})
public class WorkgroupController extends AbstractTreeModelController<Workgroup, Workgroup.Criteria> {
	@Autowired
	private WorkgroupService service;

	@Override
	protected AbstractTreeModelService<Workgroup, Workgroup.Criteria> getService() {
		return service;
	}

	@ApiOperation("查询工作组下的员工")
	@RequestMapping(value = "/{id}/employees", method = GET, consumes = ALL_VALUE)
	public Page<Employee> querySubEmployees(//
			@NotBlank @PathVariable("id") String workGroupId, //
			@RequestParam(name = "employeeName", required = false) String employeeName, //
			CoframePageRequest pageRequest) {
		return service.querySubEmployees(workGroupId, employeeName, pageRequest.createPageable());
	}

	@ApiOperation("往工作组下添加员工")
	@RequestMapping(value = "/{id}/employees/{employeeIds}", method = PUT, consumes = ALL_VALUE)
	@BoundFunction(WORKGROUP_ADD_EMP)
	public void addWorkgroupEmployees(//
			@NotBlank @PathVariable("id") String workgroupId, //
			@PathVariable(name = "employeeIds") String... employeeIds) {
		service.addWorkgroupEmployees(workgroupId, employeeIds);
	}

	@ApiOperation("从工作组下移除员工")
	@RequestMapping(value = "/{id}/employees/{employeeIds}", method = DELETE, consumes = ALL_VALUE)
	@BoundFunction(WORKGROUP_RM_EMP)
	public void removeWorkgroupEmployees(//
			@NotBlank @PathVariable("id") String workgroupId, //
			@PathVariable(name = "employeeIds") String... employeeIds) {
		service.removeWorkgroupEmployees(workgroupId, employeeIds);
	}
}
