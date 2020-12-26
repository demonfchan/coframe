/*
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on 5/31/19 4:25 PM
 */

/**
 * 
 */
package org.gocom.coframe.sdk.util;

import org.apache.commons.lang3.StringUtils;
import org.gocom.coframe.sdk.exception.CofErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module.Feature;

/**
 * json 绑助类
 * 
 * @author qinsc
 *
 */
public class JsonUtil {
	private static Logger log = LoggerFactory.getLogger(JsonUtil.class);
	private static final ObjectMapper objectMapper = new ObjectMapper();

	static {
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); // 忽略空值

		Hibernate5Module hibernate5Module = new Hibernate5Module();
		hibernate5Module.configure(Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS, true);
		objectMapper.registerModule(hibernate5Module);
	}

	public static String toJson(Object obj) {
		if (obj == null) {
			return null;
		}
		try {
			String json = objectMapper.writeValueAsString(obj);
			return json;
		} catch (JsonProcessingException e) {
            log.error(e.toString(), e);
			throw CofErrorCode.OBJECT_TO_JSON_ERROR.runtimeException(e, obj, e.getMessage());
		}
	}

	public static <T> T toObject(String json, Class<T> clazz) {
		if (StringUtils.isBlank(json)) {
			return null;
		}
		try {
			T obj = objectMapper.readValue(json, clazz);
			return obj;
		} catch (Throwable e) {
		    log.error(e.toString(), e);
			throw CofErrorCode.JSON_TO_OBJECT_ERROR.runtimeException(e, clazz.getName(), e.getMessage());
		}
	}
}
