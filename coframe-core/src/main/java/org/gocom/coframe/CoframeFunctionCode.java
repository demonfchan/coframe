/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Apr 4, 2019
 *******************************************************************************/
package org.gocom.coframe;

/**
 * coframe功能码定义（须与数据库中保持一致)
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
public interface CoframeFunctionCode {
	public static final String DIMENSION_ADD = "cof-f-dimension-add";
	public static final String DIMENSION_EDIT = "cof-f-dimension-edit";
	public static final String DIMENSION_DEL = "cof-f-dimension-del";

	public static final String ORG_ADD = "cof-f-org-add";
	public static final String ORG_EDIT = "cof-f-org-edit";
	public static final String ORG_DEL = "cof-f-org-del";
	public static final String ORG_ADD_EMP = "cof-f-org-emp-add";
	public static final String ORG_RM_EMP = "cof-f-org-emp-del";

	public static final String EMP_ADD = "cof-f-emp-add";
	public static final String EMP_EDIT = "cof-f-emp-edit";
	public static final String EMP_DEL = "cof-f-emp-del";

	public static final String POSITION_ADD = "cof-f-position-add";
	public static final String POSITION_EDIT = "cof-f-position-edit";
	public static final String POSITION_DEL = "cof-f-position-del";
	public static final String POSITION_ADD_EMP = "cof-f-position-emp-add";
	public static final String POSITION_RM_EMP = "cof-f-position-emp-del";

	public static final String WORKGROUP_ADD = "cof-f-workgroup-add";
	public static final String WORKGROUP_EDIT = "cof-f-workgroup-edit";
	public static final String WORKGROUP_DEL = "cof-f-workgroup-del";
	public static final String WORKGROUP_ADD_EMP = "cof-f-workgroup-add-emp";
	public static final String WORKGROUP_RM_EMP = "cof-f-workgroup-rm-emp";

	public static final String USER_ADD = "cof-f-user-add";
	public static final String USER_EDIT = "cof-f-user-edit";
	public static final String USER_DEL = "cof-f-user-del";
	public static final String USER_EDIT_STATUS = "cof-f-user-edit-status";
	public static final String USER_CHANGE_PASSWORD = "cof-f-user-change-pw";
	public static final String USER_RESET_PASSWORD = "cof-f-user-reset-pw";

	public static final String ROLE_ADD = "cof-f-role-add";
	public static final String ROLE_EDIT = "cof-f-role-edit";
	public static final String ROLE_DEL = "cof-f-role-del";
	public static final String ROLE_BIND = "cof-f-role-bind";
	public static final String ROLE_UNBIND = "cof-f-role-unbind";
	public static final String ROLE_EDIT_RES = "cof-f-role-edit-res";

	public static final String MEMBER_ADD = "cof-f-member-add";
	public static final String MEMBER_DEL = "cof-f-member-del";

	public static final String MENU_ADD = "cof-f-menu-add";
	public static final String MENU_EDIT = "cof-f-menu-edit";
	public static final String MENU_DEL = "cof-f-menu-del";

	public static final String FUNCTION_ADD = "cof-f-function-add";
	public static final String FUNCTION_EDIT = "cof-f-function-edit";
	public static final String FUNCTION_DEL = "cof-f-function-del";
	public static final String FUNCTION_SCAN = "cof-f-function-scan";

	public static final String RES_GROUP_ADD = "cof-f-res-group-add";
	public static final String RES_GROUP_EDIT = "cof-f-res-group-edit";
	public static final String RES_GROUP_DEL = "cof-f-res-group-del";

	public static final String AUTH_TPL_ADD = "cof-f-auth-tpl-add";
	public static final String AUTH_TPL_EDIT = "cof-f-auth-tpl-edit";
	public static final String AUTH_TPL_DEL = "cof-f-auth-tpl-del";
	public static final String AUTH_TPL_ADD_RES_GROUP = "cof-f-auth-tpl-add-res-group";
	public static final String AUTH_TPL_RM_RES_GROUP = "cof-f-auth-tpl-rm-res-group";

	public static final String ROLE_TPL_GRP_ADD = "cof-f-role-tpl-grp-add";
	public static final String ROLE_TPL_GRP_EDIT = "cof-f-role-tpl-grp-edit";
	public static final String ROLE_TPL_GRP_DEL = "cof-f-role-tpl-grp-del";
	
	public static final String ROLE_TPL_ADD = "cof-f-role-tpl-add";
	public static final String ROLE_TPL_EDIT = "cof-f-role-tpl-edit";
	public static final String ROLE_TPL_DEL = "cof-f-role-tpl-del";
	public static final String ROLE_TPL_EDIT_RES = "cof-f-role-tpl-edit-res";

	public static final String DICT_IMPORT = "cof-f-dict-import";
	public static final String DICT_EXPORT = "cof-f-dict-export";
	public static final String DICT_DONWLOAD_TPL = "cof-f-dict-download-tpl";
	
	public static final String DICT_TYPE_ADD = "cof-f-dict-type-add";
	public static final String DICT_TYPE_EDIT = "cof-f-dict-type-edit";
	public static final String DICT_TYPE_DEL = "cof-f-dict-type-del";

	public static final String DICT_ENTRY_ADD = "cof-f-dict-entry-add";
	public static final String DICT_ENTRY_EDIT = "cof-f-dict-entry-edit";
	public static final String DICT_ENTRY_DEL = "cof-f-dict-entry-del";
	
	public static final String OPT_LOG_SEARCH = "cof-f-optlog-search";
	public static final String OPT_LOG_DETAIL_GET = "cof-f-optlog-detail-get";
}
