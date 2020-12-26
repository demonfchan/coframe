/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Feb 28, 2019
 *******************************************************************************/
package org.gocom.coframe.controller;

import static org.gocom.coframe.CoframeConstants.API_PATH_PREFIX;
import static org.gocom.coframe.CoframeFunctionCode.POSITION_ADD;
import static org.gocom.coframe.CoframeFunctionCode.POSITION_ADD_EMP;
import static org.gocom.coframe.CoframeFunctionCode.POSITION_DEL;
import static org.gocom.coframe.CoframeFunctionCode.POSITION_EDIT;
import static org.gocom.coframe.CoframeFunctionCode.POSITION_RM_EMP;
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
import org.gocom.coframe.model.Position;
import org.gocom.coframe.sdk.annotation.ClassBoundFunctions;
import org.gocom.coframe.sdk.annotation.BoundFunction;
import org.gocom.coframe.sdk.annotation.BoundFunctions;
import org.gocom.coframe.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

/**
 * 岗位操作
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@RestController
@RequestMapping(value = API_PATH_PREFIX + "/positions", consumes = { APPLICATION_JSON_UTF8_VALUE }, produces = { APPLICATION_JSON_UTF8_VALUE })
@ClassBoundFunctions( { //
		@BoundFunctions(method = "create", functions = { @BoundFunction(POSITION_ADD) }), //
		@BoundFunctions(method = "update", functions = { @BoundFunction(POSITION_EDIT) }), //
		@BoundFunctions(method = "deleteByIds", functions = { @BoundFunction(POSITION_DEL) }),//
})
public class PositionController extends AbstractTreeModelController<Position, Position.Criteria> {
	@Autowired
	private PositionService service;

	@Override
	protected AbstractTreeModelService<Position, Position.Criteria> getService() {
		return service;
	}

	@ApiOperation("列出此岗位的员工")
	@RequestMapping(value = "/{id}/employees", method = GET, consumes = ALL_VALUE)
	public Page<Employee> querySubEmployees(//
			@NotBlank @PathVariable("id") String positionId, //
			@RequestParam(name = "employeeName", required = false) String employeeName, //
			CoframePageRequest pageRequest) {
		return service.querySubEmployees(positionId, employeeName, pageRequest.createPageable());
	}

	@ApiOperation("往岗位下添加员工")
	@RequestMapping(value = "/{id}/employees/{employeeIds}", method = PUT, consumes = ALL_VALUE)
	@BoundFunction(POSITION_ADD_EMP)
	public void addPositionEmployees(//
			@NotBlank @PathVariable("id") String positionId, //
			@PathVariable(name = "employeeIds") String... employeeIds) {
		service.addPositionEmployees(positionId, employeeIds);
	}

	@ApiOperation("从岗位下移除员工")
	@RequestMapping(value = "/{id}/employees/{employeeIds}", method = DELETE, consumes = ALL_VALUE)
	@BoundFunction(POSITION_RM_EMP)
	public void removePositionEmployees(//
			@NotBlank @PathVariable("id") String positionId, //
			@PathVariable(name = "employeeIds") String... employeeIds) {
		service.removePositionEmployees(positionId, employeeIds);
	}
}
