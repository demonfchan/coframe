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

import javax.servlet.http.HttpServletResponse;

/**
 * sdk 异常定义
 *
 * @author wangwb (mailto:wangwb@primeton.com)
 */

public class CofRuntimeException extends RuntimeException {
	private static final long serialVersionUID = -3876319857610564892L;

	protected String errorCode = "Internal Server Error";
	protected String message = "No value present";
	protected int responseCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

	/**
	 * 构造方法.
	 *
	 * @param errorCode 异常码
	 */
	public CofRuntimeException(CofErrorCode errorCode) {
		this(errorCode, (Object[]) null);
	}

	/**
	 * 构造方法.
	 *
	 * @param errorCode 异常码
	 * @param params    消息参数
	 */
	public CofRuntimeException(CofErrorCode errorCode, Object... params) {
		this(errorCode, null, params);
	}

	/**
	 * 构造方法.
	 *
	 * @param errorCode 异常码
	 * @param cause     异常原因
	 * @param params    消息参数
	 */
	public CofRuntimeException(CofErrorCode errorCode, Throwable cause, Object... params) {
		this(errorCode.name(), errorCode.getResponseCode(), errorCode.getMessage(params), cause);
	}

	/**
	 * 构造方法
	 * 
	 * @param errorCode
	 * @param responseCode
	 * @param message
	 * @param cause
	 */
	public CofRuntimeException(String errorCode, int responseCode, String message, Throwable cause) {
		this.errorCode = errorCode;
		this.responseCode = responseCode;
		this.message = message;
		if (cause != null) {
			initCause(cause);
		}
	}

	public String getMessage() {
		return message;
	}

	/**
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * @return the responseCode
	 */
	public int getResponseCode() {
		return responseCode;
	}

}