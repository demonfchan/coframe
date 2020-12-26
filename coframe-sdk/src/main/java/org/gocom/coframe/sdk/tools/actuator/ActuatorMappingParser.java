/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Jul 3, 2019
 *******************************************************************************/
package org.gocom.coframe.sdk.tools.actuator;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 解析通过调用springboot Actuator接口 mappings 返回的 json, 获取所有的 rest 接口信息 (例如：
 * springboot 端口为8080, context-path 为/coframe, ip 为192.168.7.63，则接 口地址为：
 * http://192.168.7.63:8080/coframe/actuator/mappings)
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@SuppressWarnings("unchecked")
public class ActuatorMappingParser {
	private static JsonFactory jasonFactory = new JsonFactory();

	/**
	 * 解析通过调用springboot Actuator接口 mappings 返回的 json, 获取所有的 rest 接口信息 (例如：
	 * springboot 端口为8080, context-path 为/coframe, ip 为192.168.7.63，则接 口地址为：
	 * http://192.168.7.63:8080/coframe/actuator/mappings)
	 * 
	 * @param json
	 * @return
	 * @throws JsonParseException
	 * @throws IOException
	 */
	public static Set<ActuatorMapping> parse(String json) throws JsonParseException, IOException {
		Set<ActuatorMapping> mappings = new HashSet<>();
		JsonParser jp = jasonFactory.createParser(json);
		ObjectNode root = (ObjectNode) new ObjectMapper().reader().readTree(jp);
		Iterator<Map.Entry<String, JsonNode>> elementsIterator = root.fields();
		while (elementsIterator.hasNext()) {
			mappings.addAll(parseApp(elementsIterator.next().getValue()));
		}
		jp.close();
		return mappings;
	}

	/**
	 * 解析应用节点
	 * 
	 * @param node
	 * @return
	 */
	private static Set<ActuatorMapping> parseApp(JsonNode node) {
		if (node == null)
			return Collections.EMPTY_SET;
		return parseMappings(node.findValue("mappings"));
	}

	/**
	 * 解析 mappings 节点
	 * 
	 * @param node
	 * @return
	 */
	private static Set<ActuatorMapping> parseMappings(JsonNode node) {
		if (node == null)
			return Collections.EMPTY_SET;
		return parseDispatcherServlets(node.findValue("dispatcherServlets"));
	}

	/**
	 * 解析 dispatcherServlets 节点
	 * 
	 * @param node
	 * @return
	 */
	private static Set<ActuatorMapping> parseDispatcherServlets(JsonNode node) {
		if (node == null)
			return Collections.EMPTY_SET;
		return parseDispatcherServlet(node.findValue("dispatcherServlet"));
	}

	/**
	 * 解析 parseDispatcherServlet
	 * 
	 * @param node
	 * @return
	 */
	private static Set<ActuatorMapping> parseDispatcherServlet(JsonNode node) {
		if (node == null)
			return Collections.EMPTY_SET;
		Set<ActuatorMapping> mappings = new HashSet<>();
		node.forEach(subNode -> {
			parseMapping(subNode).ifPresent(mapping -> mappings.add(mapping));
		});
		return mappings;
	}

	/**
	 * 解析 handler， 从中取得 ActuatorMapping 信息
	 * 
	 * @param node
	 * @return
	 */
	private static Optional<ActuatorMapping> parseMapping(JsonNode node) {
		ActuatorMapping mapping = null;
		String predicate = node.get("predicate").asText();
		if (predicate.startsWith("{")) {
			mapping = new ActuatorMapping();
			mapping.getHttpMethod().add(predicate.substring(1, predicate.indexOf("/") - 1));
			int endIndex = predicate.indexOf(",");
			if (endIndex == -1) {
				endIndex = predicate.length() - 2;
			}
			mapping.setPath(predicate.substring(predicate.indexOf("/"), endIndex));

			JsonNode details = node.findValue("details");
			if (details == null) {
				mapping = null;
			} else {
				JsonNode handlerMethod = details.findValue("handlerMethod");
				if (handlerMethod == null) {
					mapping = null;
				} else {
					mapping.setClassName(handlerMethod.findValue("className").asText());
					mapping.setMethodName(handlerMethod.findValue("name").asText());
				}
			}
		}
		return Optional.ofNullable(mapping);
	}
}
