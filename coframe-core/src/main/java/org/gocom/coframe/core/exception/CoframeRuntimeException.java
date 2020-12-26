/**
 * 
 */
package org.gocom.coframe.core.exception;

import org.gocom.coframe.sdk.exception.CofRuntimeException;

/**
 * 异常
 *
 * @author wangwb (mailto:wangwb@primeton.com)
 */

public class CoframeRuntimeException extends CofRuntimeException {
	private static final long serialVersionUID = -3876319857610564892L;

	/**
	 * 构造方法.
	 *
	 * @param errorCode 异常码
	 */
	public CoframeRuntimeException(CoframeErrorCode errorCode) {
		this(errorCode, (Object[]) null);
	}

	/**
	 * 构造方法.
	 *
	 * @param errorCode 异常码
	 * @param param     消息参数
	 */
	public CoframeRuntimeException(CoframeErrorCode errorCode, Object... params) {
		this(errorCode, null, params);
	}

	/**
	 * 构造方法.
	 *
	 * @param errorCode 异常码
	 * @param param     消息参数
	 * @param cause     异常原因
	 */
	public CoframeRuntimeException(CoframeErrorCode errorCode, Throwable cause, Object... params) {
		super(errorCode.name(), errorCode.getResponseCode(), errorCode.getMessage(params), cause);
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