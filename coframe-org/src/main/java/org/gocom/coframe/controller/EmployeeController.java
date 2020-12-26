/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Feb 28, 2019
 *******************************************************************************/
package org.gocom.coframe.controller;

import static org.gocom.coframe.CoframeFunctionCode.EMP_ADD;
import static org.gocom.coframe.CoframeFunctionCode.EMP_DEL;
import static org.gocom.coframe.CoframeFunctionCode.EMP_EDIT;
import static org.gocom.coframe.sdk.CofConstants.ACTION_PAGING_BY_CRITERIA;
import static org.gocom.coframe.sdk.CofConstants.API_PATH_PREFIX;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import org.gocom.coframe.core.model.CoframePageRequest;
import org.gocom.coframe.core.support.CoframeValidationGroups;
import org.gocom.coframe.model.Employee;
import org.gocom.coframe.model.Employee.EmployeeInfo;
import org.gocom.coframe.sdk.annotation.BoundFunction;
import org.gocom.coframe.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

/**
 * 员工操作
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@RestController
@RequestMapping(value = API_PATH_PREFIX + "/employees", consumes = { APPLICATION_JSON_UTF8_VALUE }, produces = { APPLICATION_JSON_UTF8_VALUE })
public class EmployeeController {
	@Autowired
	private EmployeeService employeeService;

	@ApiOperation("新增员工，如提供用户信息，可选同步创建用户, 或与已有用户进行绑定")
	@RequestMapping(method = POST)
	@BoundFunction(EMP_ADD)
	public Employee create(@Validated({ CoframeValidationGroups.Create.class }) @RequestBody EmployeeInfo model) {
		return employeeService.create(model);
	}

	@ApiOperation("更新")
	@RequestMapping(method = PUT)
	@BoundFunction(EMP_EDIT)
	public Employee update(@Validated({ CoframeValidationGroups.Update.class }) @RequestBody EmployeeInfo model) {
		return employeeService.update(model);
	}

	@ApiOperation("按主键集合(以','分割)删除")
	@RequestMapping(value = "/bulk-delete", method = PUT, consumes = ALL_VALUE)
	@BoundFunction(EMP_DEL)
	public void deleteByIds(@RequestBody String... ids) {
		employeeService.deleteByIds(ids);
	}

	@ApiOperation("按主键查询")
	@RequestMapping(value = "/{id}", method = GET, consumes = ALL_VALUE)
	public Employee findById(@PathVariable(name = "id") String id) {
		return employeeService.findById(id);
	}

	@ApiOperation("分页查询所有员工")
	@RequestMapping(value = "/" + ACTION_PAGING_BY_CRITERIA, method = GET, consumes = ALL_VALUE)
	public Page<Employee> pagingByCriteria(//
			Employee.Criteria criteria, //
			CoframePageRequest pageRequest) {
		return employeeService.pagingByCriteria(criteria, pageRequest.createPageable());
	}
}
