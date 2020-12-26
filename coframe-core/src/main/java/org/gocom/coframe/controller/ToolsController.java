/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Aug 1, 2019
 *******************************************************************************/
package org.gocom.coframe.controller;

import static org.gocom.coframe.sdk.CofConstants.API_PATH_PREFIX;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.Set;
import java.util.TreeSet;

import org.gocom.coframe.CoframeFunctionCode;
import org.gocom.coframe.core.exception.CoframeErrorCode;
import org.gocom.coframe.sdk.CofConstants;
import org.gocom.coframe.sdk.annotation.BoundFunction;
import org.gocom.coframe.sdk.model.CofFunction;
import org.gocom.coframe.sdk.util.HttpUtil.UrlBuilder;
import org.gocom.coframe.service.IFunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@RestController
@RequestMapping(value = API_PATH_PREFIX + "/tools/", consumes = { APPLICATION_JSON_UTF8_VALUE }, produces = { APPLICATION_JSON_UTF8_VALUE })
public class ToolsController {
	private RestTemplate restTemplate = new RestTemplate();

	@Autowired
	private IFunctionService functionService;

	@RequestMapping(method = GET, path = "/scan-functions")
	@ApiOperation("通过代码扫描, 获取应用的功能定义")
	@BoundFunction(CoframeFunctionCode.FUNCTION_SCAN)
	public Set<CofFunction> scanFunctions( //
			@ApiParam("应用地址, 格式为: http://ip:端口/contextPath") @RequestParam(name = "endpoint") String endpoint, //
			@ApiParam("需要额外扫描的包名. 扫描时, 默认会先扫描所有的ComponentScan注解, 获取所有的basePackage. 如果应用启动类没有配置此注解, 则可以通过此参数添加需要扫描的包") @RequestParam(name = "basePackage", required = false) String basePackage) {
		
		UrlBuilder builder = new UrlBuilder(endpoint).path(CofConstants.FUNCTION_SCAN_URI);
		if (basePackage != null) {
			builder.queryParam("basePackage", basePackage);
		}
		ParameterizedTypeReference<Set<CofFunction>> responseType = new ParameterizedTypeReference<Set<CofFunction>>() {};
		HttpHeaders headers = new HttpHeaders();
		headers.add(CofConstants.HHN_CONTNT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
		headers.add("Accept", MediaType.APPLICATION_JSON_UTF8_VALUE);
		ResponseEntity<Set<CofFunction>> result =null;
		try {
			 result = restTemplate.exchange(builder.build(), HttpMethod.GET, new HttpEntity<String>(headers), responseType);
		} catch (ResourceAccessException e) {
			throw CoframeErrorCode.CONNECT_IS_REFUSED.runtimeException();
		}catch (IllegalArgumentException e1) {
			throw CoframeErrorCode.URI_IS_WRONG.runtimeException();
		}
		Set<CofFunction> functions = result.getBody();
		if (functions != null) {
			functions.forEach(x -> {
				functionService.findByCode(x.getCode()).ifPresent(function -> {
					x.setImported(true);
					x.setGroupId(function.getGroupId());
				});;
			});
		}
		// Set无序, 使用TreeSet来进行排序. 要求 CofFunction 实现 Comparable 接口
		Set<CofFunction> rs = new TreeSet<>();
		rs.addAll(functions);
		return rs;
	}
}
