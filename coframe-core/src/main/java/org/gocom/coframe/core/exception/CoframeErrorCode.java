/**
 * 
 */
package org.gocom.coframe.core.exception;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

/**
 * 
 * 错误码定义
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 */

public enum CoframeErrorCode {
	// common
	NULL_PARAM,
    NOT_FOUND_MODEL_BY_ID, //
    FOUND_DUPLICATED_MODEL_WITH_SAME_CODE, //
	MODEL_CODE_CHANGE_NOT_ALLOWED, //
	FIXED_MODEL_CAN_NOT_UPDATE,
	FIXED_MODEL_CAN_NOT_DELETE,
	
	// user
	USER_NOT_FOUND_BY_NAME,
	USER_NAME_DUPLICATED,
	USER_NAME_CHANGE_NOT_ALLOWED,
	USER_ALREADY_BIND_EMPLOYEE,
	USER_OLD_PASSWORD_WRONG,
	USER_NOT_ENABLED,
	
	// employee
	EMPLOYEE_ALREADY_BIND_USER,
	
	// organization
	ORG_ALREADY_HAD_THE_EMPLOYEE,
	ORG_DELETE_NOT_ALLOWED_BY_HAS_CHILDREN,
	
	// position
	POSITION_ALREADY_HAD_THE_EMPLOYEE,
	POSITION_DELETE_NOT_ALLOWED_BY_HAS_CHILDREN,
	
	// workgroup
	WORKGROUP_ALREADY_HAD_THE_EMPLOYEE,
	WORKGROUP_DELETE_NOT_ALLOWED_BY_HAS_CHILDREN,
	
	// dicttype
	DICT_SORTNO_MUST_INTEGER,
	DICTTYPE_CODE_IS_NOT_NULL,
	DICTTYPE_NAME_IS_NOT_NULL,
	DICTTYPE_ALREADY_HAD_THE_ENTRY,
	DICT_TYPE_DELETE_NOT_ALLOWED_BY_HAS_CHILDREN,
	DICT_TYPE_EXPORT_FAILTURE,
	DICT_TYPE_IMPORT_FAILTURE,
	DICT_TYPE_DOWNLOAD_FAILTURE,
	DICT_TYPE_IMPORT_DATA_IS_NOT_NULL,
	DONT_HAVE_PARENT_DICTTYPE,
	
	// dictEntry
	DICTENTRY_SORTNO_MUST_INTEGER,
	DICTENTRY_CODE_IS_NOT_NULL,
	DICTENTRY_NAME_IS_NOT_NULL,
	PARENT_DICTYTYPE_NAME_IS_NOT_NULL,
	DICT_ENTRY_DELETE_NOT_ALLOWED_BY_HAS_CHILDREN,
	
	// member
	MEMBER_MUST_HAS_ONE_ROLE_AT_LEAST,
	MEMBER_ROLE_MUST_BELONG_TO_GIVEN_OWNER,
	USER_IN_MEMBER_IS_NULL,
	USER_IN_MEMBER_ID_IS_NULL,
	ROLE_IN_MEMBER_ID_IS_NULL,
	MEMBER_OWNER_TYPE_IS_NULL,
	MEMBER_OWNER_ID_IS_NULL,
	
	// authTpl
	AUTH_TPL_CAN_ONLY_ADD_ROOT_RES_GROUP,
	AUTH_TPL_DELETE_NOT_ALLOWED_BY_HAS_CHILDREN,
	
	// roleTpl
	ROLE_TPL_DELETE_NOT_ALLOWED_BY_HAS_CHILDREN,
	
	// roleTplGroup
	ROLE_TPL_GRP_DELETE_NOT_ALLOWED_BY_HAS_CHILDREN,
	
	// dimension
	MAIN_DIMENSION_ONLY_ONE,
	DIMENSION_DELETE_NOT_ALLOWED_BY_HAS_CHILDREN,
	
	// res group
	RES_GROUP_DELETE_NOT_ALLOWED_BY_HAS_CHILDREN,
	
	// menu
	MENU_DELETE_NOT_ALLOWED_BY_HAS_CHILDREN,
	
	//tools
	URI_IS_WRONG,
	CONNECT_IS_REFUSED,
	;

	/*
	 * ===================== =====================
	 */
	private CoframeErrorCode() {
	}

	/**
	 * 创建运行时异常.<br>
	 *
	 * @param params 异常信息参数
	 * @return 运行时异常
	 */
	public CoframeRuntimeException runtimeException(Object... params) {
		return new CoframeRuntimeException(this, params);
	}

	/**
	 * 创建运行时异常.<br>
	 *
	 * @param cause  异常原因
	 * @param params 异常信息参数
	 * @return 运行时异常
	 */
	public CoframeRuntimeException runtimeException(Throwable cause, Object... params) {
		return new CoframeRuntimeException(this, params, cause);
	}

	/**
	 * 创建运行时异常.<br>
	 *
	 * @param cause  异常原因
	 * @param params 异常信息参数
	 * @return 运行时异常
	 */
	public CoframeRuntimeException runtimeException(int responseCode, Throwable cause, Object... params) {
		return new CoframeRuntimeException(this, params, cause);
	}

	private int responseCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;


	private CoframeErrorCode(int responseCode) {
		this.responseCode = responseCode;
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
			ResourceBundle bundle = ResourceBundle.getBundle("i18n.exception.CoframeMessages", locale);
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
