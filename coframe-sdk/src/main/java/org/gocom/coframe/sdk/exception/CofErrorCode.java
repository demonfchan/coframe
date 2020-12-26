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
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 
 * 错误码定义
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 */

public enum CofErrorCode {
    OBJECT_TO_JSON_ERROR, //
    JSON_TO_OBJECT_ERROR, //
    NO_PERMISSION_ON_MRTHOD,
    NO_PERMISSION_ON_URL,
    
    USER_NOT_LOGIN,
    USER_NAME_NOT_FOUND,
    USER_PASSWORD_ERROR,
    USER_DISABLED,
    
    TOKEN_EMPTY,
    TOKEN_BROKEN,
    TOKEN_INVALID,
    TOKEN_EXPIRED,
	;

	/*
	 * ===================== =====================
	 */
	private CofErrorCode() {
	}

	/**
	 * 创建运行时异常.<br>
	 *
	 * @param params 异常信息参数
	 * @return 运行时异常
	 */
	public CofRuntimeException runtimeException(Object... params) {
		return new CofRuntimeException(this, params);
	}

	/**
	 * 创建运行时异常.<br>
	 *
	 * @param cause  异常原因
	 * @param params 异常信息参数
	 * @return 运行时异常
	 */
	public CofRuntimeException runtimeException(Throwable cause, Object... params) {
		return new CofRuntimeException(this, cause, params);
	}

	/**
	 * 创建运行时异常.<br>
	 *
	 * @param cause  异常原因
	 * @param params 异常信息参数
	 * @return 运行时异常
	 */
	public CofRuntimeException runtimeException(int responseCode, Throwable cause, Object... params) {
		return new CofRuntimeException(this, cause, params);
	}

	private int responseCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;


	private CofErrorCode(int responseCode) {
		this.responseCode = responseCode;
	}
	
	/**
	 * 链式设置返回码
	 * @param responseCode
	 * @return
	 */
	public CofErrorCode responseCode(int responseCode) {
		this.responseCode = responseCode;
		return this;
	}

	/**
	 * @return the responseCode
	 */
	public int getResponseCode() {
		return responseCode;
	}

	/**
	 * @param responseCode the responseCode to set
	 */
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public String getMessage() {
		return getMessage(Locale.getDefault(), (Object[]) null);
	}

	public String getMessage(Object... params) {
		return getMessage(Locale.getDefault(), params);
	}

	private String getMessage(Locale locale, Object... params) {
		String message = "NO MESSAGE!!!";
		if (locale == null) {
			locale = Locale.getDefault();
		}
		try {
			ResourceBundle bundle = ResourceBundle.getBundle("i18n.exception.CofSDKMessages", locale);
			message = bundle.getString(this.name());
		} catch (Throwable e) {
			/* 不记入日志, 可以减少依赖 */
			e.printStackTrace();
		}
		try {
			return new MessageFormat(message).format(params);
		} catch (Throwable e) {
			return message;
		}
	}
}
