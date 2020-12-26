/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Jun 1, 2019
 *******************************************************************************/
package org.gocom.coframe.sdk;

/**
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
public class CofConstants {
	public static final String API_PATH_PREFIX = "/api";
	
	public static final String SSO_TOKEN = "X-Auth-Token-0";
	public static final String CONTEXT_TOKEN = "token";
	public static final String CONTEXT_LOGINED_USER = "loginedUser";
	public static final String CONTEXT_LOGINED_USER_FUNCTION_CODES = "loginedUserFunctionCodes";
	public static final String HHN_AUTHORIZATION = "Authorization"; // HHN = Http Header Name
	public static final String HHN_CONTNT_TYPE = "Content-Type"; // HHN = Http Header Name
	public static final String COF_LOGIN_URI = "/api/users/login";
	public static final String COF_LOGOUT_URI = "/api/users/logout";
	public static final String FUNCTION_SCAN_URI = "/cof-tools/scan-functions";
	
	public static final int DEFAULT_BEGIN_PAGE = 0;
	public static final String DEFAULT_BEGIN_PAGE_VALUE = "" + DEFAULT_BEGIN_PAGE;
	public static final int DEFAULT_PAGE_SIZE = 10;
	public static final String DEFAULT_PAGE_SIZE_VALUE = "" + DEFAULT_PAGE_SIZE;
	public static final String ACTION_PAGING_BY_CRITERIA = "page-search";
	public static final String ACTION_FIND_BY_CRITERIA = "search";
	
	public static final String STATUS_ENABLED = "ENABLED";
	public static final String STATUS_DISABLED = "DISABLED";
	
	public static final String COF_REST_TEMPLATE = "cofRestTemplate";
	
	public static final String COF = "cof[";
	public static final String USER_NAME = "].user.name[";
	public static final String USER_ID = "].user.id[";
	public static final String ROLE_ID = "].role.id[";
	public static final String TO_USER = "].user";
	public static final String TO_ROLE = "].role";
}
