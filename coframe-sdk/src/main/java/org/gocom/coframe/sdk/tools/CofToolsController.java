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
		// ??? spring boot ??????????????? api, /actuator/mappings???????????????????????? ??????+????????? ??? rest api ?????????
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

		// ????????????????????? ??????+????????? ??? ??????????????????
		Set<ActuatorMapping> scanedMappingSet = null;
		try {
			scanedMappingSet = functionAnnotationScanner.scanFunctionAnnotations(basePackage);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Map<String, CofFunction> functionsMap = new HashMap<>();
// ??????????????????????????? RequestMapping ??????????????????rest api?????????		
//		if (scanedMappingSet != null) {
//			scanedMappingSet.forEach(scanedMapping -> {
//				// ?????????????????????????????????
//				if (scanedMapping.getFunctions() != null && scanedMapping.getFunctions().size() > 0) {
//					// ??????????????????????????????
//					scanedMapping.getFunctions().forEach(scanedFunction -> {
//						String code = scanedFunction.getCode();
//						if (code != null) {
//							// ????????????????????????
//							CofFunction function = functionsMap.get(code);
//							// ???????????????, ???????????????
//							if (function == null) {
//								function = scanedFunction;
//								functionsMap.put(code, function);
//							}
//							// ?????? rest api ??????
//							for (String httpMethod : scanedMapping.getHttpMethod()) {
//								function.getUrls().add(new CofFunctionURL(httpMethod, scanedMapping.getPath()));
//							}
//						}
//					});
//				}
//			});
//		}

		// ??????????????????????????? ???????????? rest api ?????????
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
						// ?????? rest api ??????
						for (String httpMethod : parsedMapping.getHttpMethod()) {
							function.getUrls().add(new CofFunctionURL(httpMethod, parsedMapping.getPath()));
						}
						// ????????????????????????, ??????code??????????????????
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
	 * ??????????????????????????? spring ????????????????????? /actuator/mappings ?????????????????????????????? rest api ???????????????
	 * 
	 * @return
	 */
	@RequestMapping(method = GET, path = "/scan-functions")
	public Set<CofFunction> scanFunctions(@RequestParam(name = "basePackage", required = false) String basePackage) {
		return parseActuatorMappings(basePackage);
	}
}
