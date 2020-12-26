/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Feb 28, 2019
 *******************************************************************************/
package org.gocom.coframe.controller;

import static org.gocom.coframe.CoframeFunctionCode.ORG_ADD;
import static org.gocom.coframe.CoframeFunctionCode.ORG_ADD_EMP;
import static org.gocom.coframe.CoframeFunctionCode.ORG_DEL;
import static org.gocom.coframe.CoframeFunctionCode.ORG_EDIT;
import static org.gocom.coframe.CoframeFunctionCode.ORG_RM_EMP;
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
import org.gocom.coframe.model.Organization;
import org.gocom.coframe.model.Position;
import org.gocom.coframe.model.Workgroup;
import org.gocom.coframe.sdk.annotation.ClassBoundFunctions;
import org.gocom.coframe.sdk.annotation.BoundFunction;
import org.gocom.coframe.sdk.annotation.BoundFunctions;
import org.gocom.coframe.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

/**
 * 机构操作
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@RestController
@RequestMapping(value = API_PATH_PREFIX + "/organizations", consumes = { APPLICATION_JSON_UTF8_VALUE }, produces = { APPLICATION_JSON_UTF8_VALUE })
@ClassBoundFunctions({ //
		@BoundFunctions(method = "create", functions = { @BoundFunction(ORG_ADD) }), //
		@BoundFunctions(method = "update", functions = { @BoundFunction(ORG_EDIT) }), //
		@BoundFunctions(method = "deleteByIds", functions = { @BoundFunction(ORG_DEL) }),//
})
public class OrganizationController extends AbstractTreeModelController<Organization, Organization.Criteria> {

	@Autowired
	private OrganizationService service;

	@Override
	protected AbstractTreeModelService<Organization, Organization.Criteria> getService() {
		return service;
	}

	@ApiOperation("查询机构下的员工")
	@RequestMapping(value = "/{id}/employees", method = GET, consumes = ALL_VALUE)
	public Page<Employee> querySubEmployees(//
			@NotBlank @PathVariable("id") String organizationId, //
			@RequestParam(name = "employeeName", required = false) String employeeName, //
			CoframePageRequest pageRequest) {
		return service.querySubEmployees(organizationId, employeeName, pageRequest.createPageable());
	}

	@ApiOperation("查询机构下的工作组")
	@RequestMapping(value = "/{id}/workgroups", method = GET, consumes = ALL_VALUE)
	public Page<Workgroup> querySubWorkgroups(//
			@NotBlank @PathVariable("id") String organizationId, //
			@RequestParam(name = "groupName", required = false) String groupName, //
			CoframePageRequest pageRequest) {
		return service.querySubWorkgroups(organizationId, groupName, pageRequest.createPageable());
	}

	@ApiOperation("查询机构下的岗位")
	@RequestMapping(value = "/{id}/positions", method = GET, consumes = ALL_VALUE)
	public Page<Position> querySubPositions(//
			@NotBlank @PathVariable("id") String organizationId, //
			@RequestParam(name = "positionName", required = false) String subPositionName, //
			@RequestParam(name = "positionCode", required = false) String subPositionCode, //
			CoframePageRequest pageRequest) {
		return service.querySubPositions(organizationId, subPositionName, subPositionCode, pageRequest.createPageable());
	}

	@ApiOperation("往机构下批量添加员工")
	@RequestMapping(value = "/{id}/employees/{employeeIds}", method = PUT, consumes = ALL_VALUE)
	@BoundFunction(ORG_ADD_EMP)
	public void addOrgEmployees(//
			@NotBlank @PathVariable("id") String organizationId, //
			@PathVariable(name = "employeeIds") String... employeeIds) {
		service.addOrgEmployees(organizationId, employeeIds);
	}

	@ApiOperation("从机构下批量移除员工")
	@RequestMapping(value = "/{id}/employees/{employeeIds}", method = DELETE, consumes = ALL_VALUE)
	@BoundFunction(ORG_RM_EMP)
	public void removeOrgEmployees(//
			@NotBlank @PathVariable("id") String organizationId, //
			@PathVariable(name = "employeeIds") String... employeeIds) {
		service.removeOrgEmployees(organizationId, employeeIds);
	}
}
