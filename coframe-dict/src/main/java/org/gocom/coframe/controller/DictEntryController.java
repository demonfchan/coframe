/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Feb 28, 2019
 *******************************************************************************/
package org.gocom.coframe.controller;

import static org.gocom.coframe.CoframeFunctionCode.DICT_ENTRY_ADD;
import static org.gocom.coframe.CoframeFunctionCode.DICT_ENTRY_DEL;
import static org.gocom.coframe.CoframeFunctionCode.DICT_ENTRY_EDIT;
import static org.gocom.coframe.sdk.CofConstants.ACTION_PAGING_BY_CRITERIA;
import static org.gocom.coframe.sdk.CofConstants.API_PATH_PREFIX;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import org.gocom.coframe.core.model.CoframePageRequest;
import org.gocom.coframe.core.support.CoframeValidationGroups;
import org.gocom.coframe.model.DictEntry;
import org.gocom.coframe.sdk.annotation.BoundFunction;
import org.gocom.coframe.service.DictEntryService;
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
 * 数据字典项操作
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@RestController
@RequestMapping(value = API_PATH_PREFIX + "/dict-entries", consumes = { APPLICATION_JSON_UTF8_VALUE }, produces = { APPLICATION_JSON_UTF8_VALUE })
public class DictEntryController {
	@Autowired
	private DictEntryService dictEntryService;

	@ApiOperation("按主键查询")
	@RequestMapping(value = "/{id}", method = GET, consumes = ALL_VALUE)
	public DictEntry findById(//
			@PathVariable(name = "id") String id, //
			@RequestParam(name = "locale", required = false) String locale, //
			@RequestParam(name = "loadChildren", required = false) boolean loadChildren, //
			@RequestParam(name = "loadLevel", required = false, defaultValue = "0") int loadLevel) {
		return dictEntryService.findById(id, locale, loadChildren, loadLevel);
	}

	@ApiOperation("新增")
	@RequestMapping(method = POST)
	@BoundFunction(DICT_ENTRY_ADD)
	public DictEntry create(@Validated({ CoframeValidationGroups.Create.class }) @RequestBody DictEntry model) {
		return dictEntryService.create(model);
	}

	@ApiOperation("更新")
	@RequestMapping(method = PUT)
	@BoundFunction(DICT_ENTRY_EDIT)
	public DictEntry update(@Validated({ CoframeValidationGroups.Update.class }) @RequestBody DictEntry model) {
		return dictEntryService.update(model);
	}

	@ApiOperation("按主键集合(以','分割)删除")
	@RequestMapping(value = "/{ids}", method = DELETE, consumes = ALL_VALUE)
	@BoundFunction(DICT_ENTRY_DEL)
	public void deleteByIds(@PathVariable(name = "ids") String... ids) {
		dictEntryService.deleteByIds(ids);
	}

	@ApiOperation("分页查询字典项")
	@RequestMapping(value = "/" + ACTION_PAGING_BY_CRITERIA, method = GET, consumes = ALL_VALUE)
	public Page<DictEntry> findByCriteria(DictEntry.Criteria criteria, CoframePageRequest pageRequest) {
		return dictEntryService.pagingByCriteria(criteria, pageRequest.createPageable());
	}
}
