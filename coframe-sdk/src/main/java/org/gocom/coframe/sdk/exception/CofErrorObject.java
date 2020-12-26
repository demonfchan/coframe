/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Jun 1, 2019
 *******************************************************************************/
package org.gocom.coframe.sdk.exception;

import java.io.Serializable;

/**
 * 用于返回至前端的错误对象
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
public class CofErrorObject implements Serializable {
	private static final long serialVersionUID = 707947265851055321L;

	/**
	 * http 状态码
	 */
	private int status;

	/**
	 * 错误码或错误的简短信息
	 */
	private String error;

	/**
	 * 错误的详情信息
	 */
	private String message;

	/**
	 *
	 */
	public CofErrorObject() {
	}

	/**
	 * @param status
	 * @param error
	 * @param message
	 */
	public CofErrorObject(int status, String error, String message) {
		this.status = status;
		this.error = error;
		this.message = message;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the error
	 */
	public String getError() {
		return error;
	}

	/**
	 * @param error the error to set
	 */
	public void setError(String error) {
		this.error = error;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}
