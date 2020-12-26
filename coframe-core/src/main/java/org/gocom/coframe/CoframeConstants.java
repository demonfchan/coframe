/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Feb 26, 2019
 *******************************************************************************/
package org.gocom.coframe;

import org.gocom.coframe.sdk.CofConstants;

/**
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
public class CoframeConstants extends CofConstants {
	public static final String TABLE_CORE_PREFIX = "COF_";
//	public static final String TABLE_EXT_PREFIX = "EXT_";
	 public static final String CRON_TIME = "coframe.cron.time";
    public static final String CONTEXT_TENANT_ID = "tenantId";

	public static final String DEFAULT_USER_PASSWORD = "000000";

	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String DATE2_FORMAT = "yyyy.MM.dd";
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_TIME2_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
	public static final String TIME_ZONE = "GMT+8";

	public static final String PROPERTY_PREFIX = "coframe.";

	public static final String GENDER_MAN = "MAN";
	public static final String GENDER_FEMALE = "FEMALE";

	public static final String RES_GROUP_TYPE_ROOT = "ROOT";
	public static final String RES_GROUP_TYPE_CATEGORY = "CATEGORY";
	public static final String RES_GROUP_TYPE_MODULE = "MODULE";

	public static final String PARTY_TYPE_USER = "USER";

	public static final String AUTH_TYPE_ROLE = "ROLE";
	public static final String AUTH_TYPE_ROLE_TEMPLATE = "ROLE-TEMPLATE";

	public static final String RESOURCE_TYPE_MENU = "MENU";
	public static final String RESOURCE_TYPE_FUNCTION = "FUNCTION";

	public static final String OPERATE_TYPE_ADD = "ADD";
	public static final String OPERATE_TYPE_REMOVE = "REMOVE";
	
	public static final String TEMPLATE_NAME = "DictTemplate.xls";
	
	public static final String AUTH_RES_TYPEMAPPING = "MAPPING";
	
	public static final String OWNER_TYPE_PLATFORM = "platform";
	public static final String OWNER_ID_PLATFORM = "platform";
	
	// 绑定状态
	public static final String BINDED_ONLY = "binded";
	public static final String UNBINDED_ONLY = "unBinded";
	
	// 成员状态
	public static final String IS_MEMBER = "isMember";
	public static final String NOT_MEMBER = "notMember";
}
