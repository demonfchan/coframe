/*
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on 5/31/19 3:27 PM
 */

/**
 *
 */
package org.gocom.coframe.sdk.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * 异常统一处理(只处理 CofRuntimeException 异常，优优先级最高）
 *
 * @author qinsc (mailto:qinsc@primeton.com)
 */
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class OnlyCofExceptionHandler {
	private static final Logger log = LoggerFactory.getLogger(OnlyCofExceptionHandler.class);

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ExceptionHandler(value = CofRuntimeException.class)
	protected ResponseEntity<CofErrorObject> handleCoframeRuntimeException(CofRuntimeException ex, WebRequest request) {
		log.error(ex.getMessage(), ex);
		CofErrorObject errorObject = new CofErrorObject(ex.getResponseCode(), ex.getErrorCode(), ex.getMessage());
		return new ResponseEntity(errorObject, HttpStatus.resolve(errorObject.getStatus()));
	}
}
