/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Feb 28, 2019
 *******************************************************************************/
package org.gocom.coframe.controller;

import static org.gocom.coframe.CoframeFunctionCode.DICT_DONWLOAD_TPL;
import static org.gocom.coframe.CoframeFunctionCode.DICT_EXPORT;
import static org.gocom.coframe.CoframeFunctionCode.DICT_IMPORT;
import static org.gocom.coframe.CoframeFunctionCode.DICT_TYPE_ADD;
import static org.gocom.coframe.CoframeFunctionCode.DICT_TYPE_DEL;
import static org.gocom.coframe.CoframeFunctionCode.DICT_TYPE_EDIT;
import static org.gocom.coframe.sdk.CofConstants.ACTION_PAGING_BY_CRITERIA;
import static org.gocom.coframe.sdk.CofConstants.API_PATH_PREFIX;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.gocom.coframe.core.model.CoframePageRequest;
import org.gocom.coframe.core.support.CoframeValidationGroups;
import org.gocom.coframe.model.DictEntry;
import org.gocom.coframe.model.DictResponse;
import org.gocom.coframe.model.DictType;
import org.gocom.coframe.sdk.annotation.BoundFunction;
import org.gocom.coframe.service.DictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiOperation;

/**
 * 数据字典类型操作
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@RestController
@RequestMapping(value = API_PATH_PREFIX + "/dict-types", consumes = { APPLICATION_JSON_UTF8_VALUE }, produces = {
		APPLICATION_JSON_UTF8_VALUE })
public class DictTypeController {
	@Autowired
	private DictTypeService dictTypeService;

	@ApiOperation("按主键查询")
	@RequestMapping(value = "/{id}", method = GET)
	public DictType findById(//
			@PathVariable(name = "id") String id, //
			@RequestParam(name = "locale", required = false) String locale, //
			@RequestParam(name = "loadChildren", required = false) boolean loadChildren, //
			@RequestParam(name = "loadLevel", required = false, defaultValue = "0") int loadLevel) {
		return dictTypeService.findById(id, locale, loadChildren, loadLevel);
	}

	@ApiOperation("新增")
	@RequestMapping(method = POST)
	@BoundFunction(DICT_TYPE_ADD)
	public DictType create(@Validated({ CoframeValidationGroups.Create.class }) @RequestBody DictType model) {
		return dictTypeService.create(model);
	}

	@ApiOperation("更新")
	@RequestMapping(method = PUT)
	@BoundFunction(DICT_TYPE_EDIT)
	public DictType update(@Validated({ CoframeValidationGroups.Update.class }) @RequestBody DictType model) {
		return dictTypeService.update(model);
	}

	@ApiOperation("按主键集合(以','分割)删除")
	@RequestMapping(value = "/{ids}", method = DELETE, consumes = ALL_VALUE)
	@BoundFunction(DICT_TYPE_DEL)
	public void deleteByIds(@PathVariable(name = "ids") String... ids) {
		dictTypeService.deleteByIds(ids);
	}

	@ApiOperation("分页查询字典类型")
	@RequestMapping(value = "/" + ACTION_PAGING_BY_CRITERIA, method = GET, consumes = ALL_VALUE)
	public Page<DictType> findByCriteria(DictType.Criteria criteria, CoframePageRequest pageRequest) {
		return dictTypeService.pagingByCriteria(criteria, pageRequest.createPageable());
	}

	@ApiOperation("查询类型下的字典项")
	@RequestMapping(value = "/{id}/dict-entries", method = GET, consumes = ALL_VALUE)
	public List<DictEntry> querySubDictEntries(//
			@NotBlank @PathVariable("id") String dictTypeId, //
			@RequestParam(name = "locale", required = false) String locale, //
			@RequestParam(name = "subDictEntryName", required = false) String subDictEntryName, //
			@RequestParam(name = "subDictEntryCode", required = false) String subDictEntryCode) {
		return dictTypeService.findByCriteriaForDictEntry(dictTypeId, subDictEntryName, subDictEntryCode);
	}

	@ApiOperation("导入字典")
	@RequestMapping(value = "/import", method = POST, consumes = ALL_VALUE)
	@BoundFunction(DICT_IMPORT)
	public DictResponse importExcel(//
			@RequestPart(name = "file") MultipartFile excel, //
			@RequestParam(name = "overideExists", required = false) boolean overideExists) {
		DictResponse dictResponse = null;
		try {
			dictResponse = dictTypeService.importExcel(excel.getInputStream(), overideExists);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dictResponse;
	}

	@ApiOperation("按给定的 id 批量导出数据字典")
	@RequestMapping(value = "/export-by-ids", method = GET, consumes = ALL_VALUE)
	@BoundFunction(DICT_EXPORT)
	public Object exportExcelByIds(//
			@RequestParam(name = "locale", required = false) String locale, //
			@RequestParam(name = "ids", required = false) String... ids) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		dictTypeService.exportExcelByIds(out, locale, ids);
		return ResponseEntity.ok().contentType(MediaType.valueOf("application/x-excel"))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=DictExport.xls").body(out.toByteArray());
	}

	@ApiOperation("按给定的 code 批量导出数据字典")
	@RequestMapping(value = "/export-by-codes", method = GET, consumes = ALL_VALUE)
	@BoundFunction(DICT_EXPORT)
	public Object exportExcelByCodes(//
			@RequestParam(name = "locale", required = false) String locale, //
			@RequestParam(name = "codes", required = false) String... codes) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		dictTypeService.exportExcelByCodes(out, locale, codes);
		return ResponseEntity.ok().contentType(MediaType.valueOf("application/x-excel"))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=DictExport.xls").body(out.toByteArray());
	}

	@ApiOperation("导出父类以及子类的数据字典")
	@RequestMapping(value = "/export-by-id/{id}", method = GET, consumes = ALL_VALUE)
	@BoundFunction(DICT_EXPORT)
	public Object exportExcelById(//
			@PathVariable("id") String id, @RequestParam(name = "loadChildren", required = false) boolean loadChildren,
			@RequestParam(name = "locale", required = false) String locale) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		dictTypeService.exportExcelById(out, locale, id, loadChildren);
		return ResponseEntity.ok().contentType(MediaType.valueOf("application/x-excel"))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=DictExport.xls").body(out.toByteArray());
	}

	@ApiOperation("导出所有数据字典")
	@RequestMapping(value = "/export-all", method = GET, consumes = ALL_VALUE)
	@BoundFunction(DICT_EXPORT)
	public Object exportExcelAll(@RequestParam(name = "locale", required = false) String locale) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		dictTypeService.exportExcelAll(out, locale);
		return ResponseEntity.ok().contentType(MediaType.valueOf("application/x-excel"))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=DictExport.xls").body(out.toByteArray());
	}

	@ApiOperation("下载导入模板")
	@RequestMapping(value = "/download-dict-template", method = GET, consumes = ALL_VALUE)
	@BoundFunction(DICT_DONWLOAD_TPL)
	public Object downloadDictTemplate() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		dictTypeService.downloadDictTemplate(out);
		return ResponseEntity.ok().contentType(MediaType.valueOf("application/x-excel"))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=DictTemplate.xls")
				.body(out.toByteArray());
	}
}
