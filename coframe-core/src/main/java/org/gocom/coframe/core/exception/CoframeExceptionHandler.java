/**
 * 
 */
package org.gocom.coframe.core.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.gocom.coframe.CoframeConstants;
import org.gocom.coframe.sdk.exception.CofErrorObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.context.request.WebRequest;

/**
 * 异常统一处理
 *
 * @author wangwb (mailto:wangwb@primeton.com)
 */
@ControllerAdvice
@SuppressWarnings({ "unchecked", "rawtypes" })
@ConditionalOnProperty(name = CoframeConstants.PROPERTY_PREFIX + "beanEnabled.exception-handler-all", havingValue = "true", matchIfMissing = true)
public class CoframeExceptionHandler {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	protected ResponseEntity<CofErrorObject> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
		logger.error(ex.getMessage(), ex);
		List<String> errors = new ArrayList<String>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			if (error instanceof FieldError) {
				FieldError fieldError = ((FieldError) error);
				errors.add("'" + fieldError.getObjectName() + "." + fieldError.getField() + "' " + fieldError.getDefaultMessage());
			} else {
				errors.add("'" + error.getObjectName() + "' " + error.getDefaultMessage());
			}
		});
		CofErrorObject errorObject = new CofErrorObject(HttpStatus.BAD_REQUEST.value(), ex.getClass().getSimpleName(), errors.stream().collect(Collectors.joining(",")));
		return new ResponseEntity(errorObject, HttpStatus.resolve(errorObject.getStatus()));
	}

	@ExceptionHandler(value = BindException.class)
	public ResponseEntity<CofErrorObject> handleBindException(BindException ex, WebRequest request) {
		logger.error(ex.getMessage(), ex);
		List<String> errors = new ArrayList<String>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			if (error instanceof FieldError) {
				FieldError fieldError = ((FieldError) error);
				errors.add("'" + fieldError.getObjectName() + "." + fieldError.getField() + "' " + fieldError.getDefaultMessage());
			} else {
				errors.add("'" + error.getObjectName() + "' " + error.getDefaultMessage());
			}
		});
		CofErrorObject errorObject = new CofErrorObject(HttpStatus.BAD_REQUEST.value(), camel2Underline(ex.getClass().getSimpleName()), errors.stream().collect(Collectors.joining(",")));
		return new ResponseEntity(errorObject, HttpStatus.resolve(errorObject.getStatus()));
	}

	@ExceptionHandler(value = Throwable.class)
	protected ResponseEntity<CofErrorObject> handleThrowable(Throwable ex, WebRequest request) {
		logger.error(ex.getMessage(), ex);
		CofErrorObject errorObject = new CofErrorObject(HttpStatus.BAD_REQUEST.value(), camel2Underline(ex.getClass().getSimpleName()), ex.getMessage());
		return new ResponseEntity(errorObject, HttpStatus.resolve(errorObject.getStatus()));
	}

	@ExceptionHandler(value = CoframeRuntimeException.class)
	protected ResponseEntity<CofErrorObject> handleCoframeRuntimeException(CoframeRuntimeException ex, WebRequest request) {
		logger.error(ex.getMessage(), ex);
		CofErrorObject errorObject = new CofErrorObject(ex.getResponseCode(), ex.getErrorCode(), ex.getMessage());
		return new ResponseEntity(errorObject, HttpStatus.resolve(errorObject.getStatus()));
	}

	@ExceptionHandler(value = RestClientResponseException.class)
	protected ResponseEntity<CofErrorObject> handleRestClientResponseException(RestClientResponseException ex, WebRequest request) {
		logger.error(ex.getMessage() + ":" + ex.getResponseBodyAsString(), ex);
		CofErrorObject errorObject = new CofErrorObject(HttpStatus.BAD_REQUEST.value(), camel2Underline(ex.getClass().getSimpleName()), ex.getMessage() + ":" + ex.getResponseBodyAsString());
		return new ResponseEntity(errorObject, HttpStatus.resolve(errorObject.getStatus()));
	}

	private static Pattern pattern = Pattern.compile("[A-Z]");

	private static String camel2Underline(String camel) {
		Matcher matcher = pattern.matcher(camel);
		while (matcher.find()) {
			String w = matcher.group().trim();
			camel = camel.replace(w, "_" + w);
		}
		return camel.toUpperCase();
	}
}
