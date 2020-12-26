/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Jul 4, 2019
 *******************************************************************************/
package org.gocom.coframe.sdk.tools;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.gocom.coframe.sdk.model.CofFunction;
import org.gocom.coframe.sdk.model.CofFunctionURL;
import org.gocom.coframe.sdk.tools.actuator.ActuatorMapping;
import org.gocom.coframe.sdk.tools.actuator.ActuatorMappingParser;
import org.gocom.coframe.sdk.tools.function.FunctionAnnotationScanner;
import org.gocom.coframe.sdk.util.HttpUtil.UrlBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@RestController
@RequestMapping(value = "/cof-tools/", consumes = { APPLICATION_JSON_UTF8_VALUE }, produces = { APPLICATION_JSON_UTF8_VALUE })
@ConditionalOnProperty(name = "coframe.tools.enabled", havingValue = "true", matchIfMissing = false)
public class CofToolsController {
	@Value("${server.servlet.context-path:/}")
	private String contextPath;

	@Value("${server.port}")
	private int serverPort;

	@Autowired
	private FunctionAnnotationScanner functionAnnotationScanner;

	private Set<CofFunction> parseActuatorMappings(String basePackage) {
		Set<CofFunction> functions = new HashSet<>();
		// 从 spring boot 的健康检查 api, /actuator/mappings中取得信息，解析 类名+方法名 与 rest api 的映射
		String ctx = "/".equals(contextPath) ? "" : contextPath;
		UrlBuilder builder = new UrlBuilder("http://127.0.0.1:" + serverPort + ctx + "/actuator/mappings");
		ResponseEntity<String> mappingsJson = new RestTemplate().getForEntity(builder.build(), String.class);
		Set<ActuatorMapping> parsedMappingSet = null;
		try {
			parsedMappingSet = ActuatorMappingParser.parse(mappingsJson.getBody());
		} catch (IOException e) {
			e.printStackTrace();
		}
		Map<String, ActuatorMapping> parsedMappings = new HashMap<>();
		if (parsedMappingSet != null) {
			parsedMappingSet.forEach(mapping -> {
				parsedMappings.put(key(mapping), mapping);
			});
		}

		// 扫描代码，找出 类名+方法名 与 功能码的映射
		Set<ActuatorMapping> scanedMappingSet = null;
		try {
			scanedMappingSet = functionAnnotationScanner.scanFunctionAnnotations(basePackage);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Map<String, CofFunction> functionsMap = new HashMap<>();
// 如果扫描时通过扫描 RequestMapping 取得了功能与rest api的映射		
//		if (scanedMappingSet != null) {
//			scanedMappingSet.forEach(scanedMapping -> {
//				// 只处理扫描到功能定义的
//				if (scanedMapping.getFunctions() != null && scanedMapping.getFunctions().size() > 0) {
//					// 依次处理扫描到的功能
//					scanedMapping.getFunctions().forEach(scanedFunction -> {
//						String code = scanedFunction.getCode();
//						if (code != null) {
//							// 看功能是否已缓存
//							CofFunction function = functionsMap.get(code);
//							// 还没有缓存, 先缓存起来
//							if (function == null) {
//								function = scanedFunction;
//								functionsMap.put(code, function);
//							}
//							// 添加 rest api 关联
//							for (String httpMethod : scanedMapping.getHttpMethod()) {
//								function.getUrls().add(new CofFunctionURL(httpMethod, scanedMapping.getPath()));
//							}
//						}
//					});
//				}
//			});
//		}

		// 合并两个映射，得出 功能码与 rest api 的映射
		if (scanedMappingSet != null) {
			scanedMappingSet.forEach(scanedMapping -> {
//				System.out.println("\nScanedMapping: " + scanedMapping);
				ActuatorMapping parsedMapping = parsedMappings.get(key(scanedMapping));
				if (parsedMapping != null) {
//					System.out.println("ParsedMapping: " + parsedMapping);
					for (CofFunction scanedFunction : scanedMapping.getFunctions()) {
						String code = scanedFunction.getCode();
						CofFunction function = functionsMap.get(code);
						if (function == null) {
							function = scanedFunction;
							functionsMap.put(code, function);
						}
						// 添加 rest api 关联
						for (String httpMethod : parsedMapping.getHttpMethod()) {
							function.getUrls().add(new CofFunctionURL(httpMethod, parsedMapping.getPath()));
						}
						// 如果功能没有名字, 使用code作为它的名字
						if (StringUtils.isBlank(function.getName())) {
							function.setName(function.getCode());
						}
//						System.out.println("Add function: " + function);
					}
				}
			});
		}
		functions.addAll(functionsMap.values());
		return functions;
	}

	private String key(ActuatorMapping mapping) {
		return mapping.getClassName() + "." + mapping.getMethodName();
	}

	/**
	 * 通过扫描代码与分析 spring 的健康检查接口 /actuator/mappings 的结果，得到功能码与 rest api 信息的映射
	 * 
	 * @return
	 */
	@RequestMapping(method = GET, path = "/scan-functions")
	public Set<CofFunction> scanFunctions(@RequestParam(name = "basePackage", required = false) String basePackage) {
		return parseActuatorMappings(basePackage);
	}
}
