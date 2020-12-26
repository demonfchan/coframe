drop table if exists COF_AUTH_RES;
drop table if exists COF_DICT_ENTRY;
drop table if exists COF_DICT_ENTRY_I18N;
drop table if exists COF_DICT_TYPE;
drop table if exists COF_DICT_TYPE_I18N;
drop table if exists COF_DIMENSION;
drop table if exists COF_EMP;
drop table if exists COF_FUNCTION;
drop table if exists COF_MENU;
drop table if exists COF_OPERATION_LOG;
drop table if exists COF_OPERATION_LOG_DETAIL;
drop table if exists COF_ORG;
drop table if exists COF_ORG_EMP_MAPPING;
drop table if exists COF_PARTY_AUTH;
drop table if exists COF_POSITION;
drop table if exists COF_POSITION_EMP_MAPPING;
drop table if exists COF_RES_GROUP;
drop table if exists COF_ROLE;
drop table if exists COF_USER;
drop table if exists COF_WORKGROUP;
drop table if exists COF_WORKGROUP_EMP_MAPPING;
drop table if exists COF_AUTH_TEMPLATE;
drop table if exists COF_AUTH_TPL_RES_GROUP;
drop table if exists COF_ROLE_TEMPLATE;

/*==============================================================*/
/* Table: COF_AUTH_RES                                          */
/*==============================================================*/
create table COF_AUTH_RES (
   ID                   VARCHAR(64)          not null,
   TYPE                 VARCHAR(64)          null,
   AUTH_TYPE            VARCHAR(64)          null,
   AUTH_ID              VARCHAR(64)          null,
   RES_TYPE             VARCHAR(64)          null,
   RES_ID               VARCHAR(64)          null,
   IS_FIXED				BOOLEAN              null,
   constraint PK_COF_AUTH_RES primary key (ID)
);
comment on table COF_AUTH_RES is '可授权者与资源的关联';
comment on column COF_AUTH_RES.ID is '关联ID';
comment on column COF_AUTH_RES.TYPE is '授权类型';
comment on column COF_AUTH_RES.AUTH_TYPE is '可授权者类型';
comment on column COF_AUTH_RES.AUTH_ID is '可授权者ID';
comment on column COF_AUTH_RES.RES_TYPE is '资源类型';
comment on column COF_AUTH_RES.RES_ID is '资源ID';
comment on column COF_AUTH_RES.IS_FIXED is '是否固定，如果是，则数据不能修改，不能删除';

create index COF_AUTH_RES_INDEX_AUTH on COF_AUTH_RES(AUTH_ID, AUTH_TYPE);
create index COF_AUTH_RES_INDEX_RES on COF_AUTH_RES(RES_ID, RES_TYPE);
create index COF_AUTH_RES_INDEX_TYPE on COF_AUTH_RES(TYPE);

/*==============================================================*/
/* Table: COF_DICT_ENTRY                                        */
/*==============================================================*/
create table COF_DICT_ENTRY (
   ID                   VARCHAR(64)          not null,
   CODE                 VARCHAR(64)          null,
   DICT_TYPE_ID         VARCHAR(64)          null,
   NAME                 VARCHAR(128)         null,
   DESCRIPTION          VARCHAR(256)         null,
   CREATE_TIME          DATE                 null,
   UPDATE_TIME          DATE                 null,
   TENANT_ID            VARCHAR(64)          null,
   PARENT_ID            VARCHAR(64)          null,
   LOCALE               VARCHAR(64)          null,
   STATUS               VARCHAR(64)          null,
   SORT_NO              INT4                 null,
   IS_LEAF              BOOLEAN              null,
   TREE_LEVEL           INT4                 null,
   SEQ                  VARCHAR(256)         null,
   IS_FIXED				BOOLEAN              null,
   constraint PK_COF_DICT_ENTRY primary key (ID)
);
comment on table COF_DICT_ENTRY is '字典项';
comment on column COF_DICT_ENTRY.ID is '字典项ID';
comment on column COF_DICT_ENTRY.CODE is '字典项编码';
comment on column COF_DICT_ENTRY.DICT_TYPE_ID is '字典类型ID';
comment on column COF_DICT_ENTRY.NAME is '字典项名称';
comment on column COF_DICT_ENTRY.DESCRIPTION is '描述';
comment on column COF_DICT_ENTRY.CREATE_TIME is '创建时间';
comment on column COF_DICT_ENTRY.UPDATE_TIME is '更新时间';
comment on column COF_DICT_ENTRY.TENANT_ID is '租户ID';
comment on column COF_DICT_ENTRY.PARENT_ID is '父字典项ID';
comment on column COF_DICT_ENTRY.LOCALE is '默认语言';
comment on column COF_DICT_ENTRY.STATUS is '状态 ';
comment on column COF_DICT_ENTRY.SORT_NO is '排序字段';
comment on column COF_DICT_ENTRY.IS_LEAF is '是否叶节点';
comment on column COF_DICT_ENTRY.TREE_LEVEL is '层级';
comment on column COF_DICT_ENTRY.SEQ is '序列码';
comment on column COF_DICT_ENTRY.IS_FIXED is '是否固定，如果是，则数据不能修改，不能删除';

create index COF_DICT_ENTRY_INDEX_NAME on COF_DICT_ENTRY(NAME);
create index COF_DICT_ENTRY_INDEX_CODE on COF_DICT_ENTRY(CODE);
create index COF_DICT_ENTRY_INDEX_SEQ on COF_DICT_ENTRY(SEQ);

/*==============================================================*/
/* Table: COF_DICT_ENTRY_I18N                                   */
/*==============================================================*/
create table COF_DICT_ENTRY_I18N (
   ID                   VARCHAR(64)          not null,
   DICT_ENTRY_ID        VARCHAR(64)          null,
   LOCALE               VARCHAR(64)          null,
   DICT_ENTRY_NAME      VARCHAR(128)         null,
   IS_FIXED				BOOLEAN              null,
   constraint PK_COF_DICT_ENTRY_I18N primary key (ID)
);
comment on table COF_DICT_ENTRY_I18N is '字典项国际化';
comment on column COF_DICT_ENTRY_I18N.ID is '国际化项ID';
comment on column COF_DICT_ENTRY_I18N.DICT_ENTRY_ID is '字典项ID';
comment on column COF_DICT_ENTRY_I18N.LOCALE is '国际化区域';
comment on column COF_DICT_ENTRY_I18N.DICT_ENTRY_NAME is '字典项名称';
comment on column COF_DICT_ENTRY_I18N.IS_FIXED is '是否固定，如果是，则数据不能修改，不能删除';

create index COF_DICT_ENTRY_I18N_INDEX on COF_DICT_ENTRY_I18N(DICT_ENTRY_ID, LOCALE);

/*==============================================================*/
/* Table: COF_DICT_TYPE                                         */
/*==============================================================*/
create table COF_DICT_TYPE (
   ID                   VARCHAR(64)          not null,
   CODE                 VARCHAR(64)          null,
   NAME                 VARCHAR(128)         null,
   DESCRIPTION          VARCHAR(256)         null,
   CREATE_TIME          DATE                 null,
   UPDATE_TIME          DATE                 null,
   TENANT_ID            VARCHAR(64)          null,
   PARENT_ID            VARCHAR(64)          null,
   LOCALE               VARCHAR(64)          null,
   SORT_NO              INT4                 null,
   IS_LEAF              BOOLEAN              null,
   TREE_LEVEL           INT4                 null,
   SEQ                  VARCHAR(256)         null,
   IS_FIXED				BOOLEAN              null,
   constraint PK_COF_DICT_TYPE primary key (ID)
);
comment on table COF_DICT_TYPE is '字典类型';
comment on column COF_DICT_TYPE.ID is '字典类型ID';
comment on column COF_DICT_TYPE.CODE is '字典类型编码';
comment on column COF_DICT_TYPE.NAME is '字典类型名称';
comment on column COF_DICT_TYPE.DESCRIPTION is '描述';
comment on column COF_DICT_TYPE.CREATE_TIME is '创建时间';
comment on column COF_DICT_TYPE.UPDATE_TIME is '更新时间';
comment on column COF_DICT_TYPE.TENANT_ID is '租户ID';
comment on column COF_DICT_TYPE.PARENT_ID is '父字典类型ID';
comment on column COF_DICT_TYPE.LOCALE is '默认语言';
comment on column COF_DICT_TYPE.SORT_NO is '排序字段';
comment on column COF_DICT_TYPE.IS_LEAF is '是否叶节点';
comment on column COF_DICT_TYPE.TREE_LEVEL is '层级';
comment on column COF_DICT_TYPE.SEQ is '序列码';
comment on column COF_DICT_TYPE.IS_FIXED is '是否固定，如果是，则数据不能修改，不能删除';

create index COF_DICT_TYPE_INDEX_NAME on COF_DICT_TYPE(NAME);
create index COF_DICT_TYPE_INDEX_CODE on COF_DICT_TYPE(CODE);
create index COF_DICT_TYPE_INDEX_SEQ on COF_DICT_TYPE(SEQ);

/*==============================================================*/
/* Table: COF_DICT_TYPE_I18N                                    */
/*==============================================================*/
create table COF_DICT_TYPE_I18N (
   ID                   VARCHAR(64)          not null,
   DICT_TYPE_ID         VARCHAR(64)          null,
   LOCALE               VARCHAR(64)          null,
   DICT_TYPE_NAME       VARCHAR(128)         null,
   IS_FIXED				BOOLEAN              null,
   constraint PK_COF_DICT_TYPE_I18N primary key (ID)
);
comment on table COF_DICT_TYPE_I18N is '字典国际化值';
comment on column COF_DICT_TYPE_I18N.ID is '国际化项ID';
comment on column COF_DICT_TYPE_I18N.DICT_TYPE_ID is '字典类型ID';
comment on column COF_DICT_TYPE_I18N.LOCALE is '区域';
comment on column COF_DICT_TYPE_I18N.DICT_TYPE_NAME is '名称';
comment on column COF_DICT_TYPE_I18N.IS_FIXED is '是否固定，如果是，则数据不能修改，不能删除';

create index COF_DICT_TYPE_I18N_INDEX on COF_DICT_TYPE_I18N(DICT_TYPE_ID, LOCALE);

/*==============================================================*/
/* Table: COF_DIMENSION                                         */
/*==============================================================*/
create table COF_DIMENSION (
   ID                   VARCHAR(64)          not null,
   CODE                 VARCHAR(64)          null,
   NAME                 VARCHAR(128)         null,
   DESCRIPTION          VARCHAR(256)         null,
   CREATE_TIME          DATE                 null,
   UPDATE_TIME          DATE                 null,
   TENANT_ID            VARCHAR(64)          null,
   IS_MAIN              BOOLEAN              null,
   IS_FIXED				BOOLEAN              null,
   constraint PK_COF_DIMENSION primary key (ID)
);
comment on table COF_DIMENSION is '维度，做为机构的顶层对象，前端展现为机构树';
comment on column COF_DIMENSION.ID is '维度ID';
comment on column COF_DIMENSION.CODE is '维度编 码';
comment on column COF_DIMENSION.NAME is '维度名称';
comment on column COF_DIMENSION.DESCRIPTION is '描述';
comment on column COF_DIMENSION.CREATE_TIME is '创建时间';
comment on column COF_DIMENSION.UPDATE_TIME is '更新时间';
comment on column COF_DIMENSION.TENANT_ID is '租户ID';
comment on column COF_DIMENSION.IS_MAIN is '是否主维度';
comment on column COF_DIMENSION.IS_FIXED is '是否固定，如果是，则数据不能修改，不能删除';

/*==============================================================*/
/* Table: COF_EMP                                               */
/*==============================================================*/
create table COF_EMP (
   ID                   VARCHAR(64)          not null,
   CODE                 VARCHAR(64)          null,
   NAME                 VARCHAR(128)         null,
   REALNAME             VARCHAR(128)         null,
   GENDER               VARCHAR(8)           null,
   BIRTHDAY             DATE                 null,
   STATUS               VARCHAR(64)          null,
   CARD_TYPE            VARCHAR(64)          null,
   CARD_NO              VARCHAR(256)         null,
   IN_DATE              DATE                 null,
   OUT_DATE             DATE                 null,
   FAN_NO               VARCHAR(256)         null,
   MOBILE_NO            VARCHAR(64)          null,
   DESCRIPTION          VARCHAR(512)         null,
   CREATE_TIME          DATE                 null,
   UPDATE_TIME          DATE                 null,
   TENANT_ID            VARCHAR(64)          null,
   PARTY                VARCHAR(64)          null,
   DEGREE               VARCHAR(256)         null,
   REMARK               VARCHAR(512)         null,
   O_TEL                VARCHAR(64)          null,
   O_ADDRESS            VARCHAR(512)         null,
   O_EMAIL              VARCHAR(256)         null,
   H_TEL                VARCHAR(64)          null,
   H_ADDRESS            VARCHAR(512)         null,
   H_ZIPCODE            VARCHAR(64)          null,
   P_EMAIL              VARCHAR(256)         null,
   QQ                   VARCHAR(256)         null,
   WEIBO                VARCHAR(256)         null,
   WECHAT               VARCHAR(256)         null,
   IS_FIXED				BOOLEAN              null,
   constraint PK_COF_EMP primary key (ID)
);
comment on table COF_EMP is '员工';
comment on column COF_EMP.ID is '员工ID';
comment on column COF_EMP.CODE is '员工编码';
comment on column COF_EMP.NAME is '员工名称';
comment on column COF_EMP.REALNAME is '真实姓名';
comment on column COF_EMP.GENDER is '性别';
comment on column COF_EMP.BIRTHDAY is '生日';
comment on column COF_EMP.STATUS is '员工状态';
comment on column COF_EMP.CARD_TYPE is '身份证件类型';
comment on column COF_EMP.CARD_NO is '证件编码';
comment on column COF_EMP.IN_DATE is '入职时间';
comment on column COF_EMP.OUT_DATE is '离职时间';
comment on column COF_EMP.FAN_NO is '传真';
comment on column COF_EMP.MOBILE_NO is '手机号';
comment on column COF_EMP.DESCRIPTION is '描述';
comment on column COF_EMP.CREATE_TIME is '创建时间';
comment on column COF_EMP.UPDATE_TIME is '更新时间';
comment on column COF_EMP.TENANT_ID is '租户ID';
comment on column COF_EMP.PARTY is '部门';
comment on column COF_EMP.DEGREE is '职称';
comment on column COF_EMP.REMARK is '备注';
comment on column COF_EMP.O_TEL is '办公室电话';
comment on column COF_EMP.O_ADDRESS is '办公室地址';
comment on column COF_EMP.O_EMAIL is '办公室邮箱';
comment on column COF_EMP.H_TEL is '家庭电话';
comment on column COF_EMP.H_ADDRESS is '家庭地址';
comment on column COF_EMP.H_ZIPCODE is '家庭邮编';
comment on column COF_EMP.P_EMAIL is '个人邮箱';
comment on column COF_EMP.QQ is 'QQ号';
comment on column COF_EMP.WEIBO is '微博号';
comment on column COF_EMP.WECHAT is '微信号';
comment on column COF_EMP.IS_FIXED is '是否固定，如果是，则数据不能修改，不能删除';

create index COF_EMP_INDEX_NAME on COF_EMP(NAME);
create index COF_EMP_INDEX_CODE on COF_EMP(CODE);
create index COF_EMP_INDEX_STATUS on COF_EMP(STATUS);

/*==============================================================*/
/* Table: COF_FUNCTION                                          */
/*==============================================================*/
create table COF_FUNCTION (
   ID                   VARCHAR(64)          not null,
   GROUP_ID             VARCHAR(64)          null,
   NAME                 VARCHAR(128)         null,
   CODE                 VARCHAR(64)          null,
   URLS                 TEXT                 null,
   DESCRIPTION          VARCHAR(256)         null,
   CREATE_TIME          DATE                 null,
   UPDATE_TIME          DATE                 null,
   TENANT_ID            VARCHAR(64)          null,
   TYPE                 VARCHAR(64)          null,
   IS_CHECK             BOOLEAN              null,
   IS_FIXED				BOOLEAN              null,
   constraint PK_COF_FUNCTION primary key (ID)
);
comment on table COF_FUNCTION is '功能权限';
comment on column COF_FUNCTION.ID is '功能ID';
comment on column COF_FUNCTION.GROUP_ID is '权限组ID';
comment on column COF_FUNCTION.NAME is '权限名称';
comment on column COF_FUNCTION.CODE is '功能编码';
comment on column COF_FUNCTION.URLS is 'url 集合';
comment on column COF_FUNCTION.DESCRIPTION is '描述';
comment on column COF_FUNCTION.CREATE_TIME is '创建时间';
comment on column COF_FUNCTION.UPDATE_TIME is '更新时间';
comment on column COF_FUNCTION.TENANT_ID is '租户ID';
comment on column COF_FUNCTION.TYPE is '功能类型';
comment on column COF_FUNCTION.IS_CHECK is '是否校验';
comment on column COF_FUNCTION.IS_FIXED is '是否固定，如果是，则数据不能修改，不能删除';

create index COF_FUNCTION_INDEX_NAME on COF_FUNCTION(NAME);
create index COF_FUNCTION_INDEX_CODE on COF_FUNCTION(CODE);
create index COF_FUNCTION_INDEX_TYPE on COF_FUNCTION(TYPE);

/*==============================================================*/
/* Table: COF_MENU                                              */
/*==============================================================*/
create table COF_MENU (
   ID                   VARCHAR(64)          not null,
   GROUP_ID             VARCHAR(64)          null,
   NAME                 VARCHAR(128)         null,
   CODE                 VARCHAR(64)          null,
   DESCRIPTION          VARCHAR(256)         null,
   CREATE_TIME          DATE                 null,
   UPDATE_TIME          DATE                 null,
   TENANT_ID            VARCHAR(64)          null,
   PARENT_ID            VARCHAR(64)          null,
   SORT_NO              INT4                 null,
   IS_LEAF              BOOLEAN              null,
   TREE_LEVEL           INT4                 null,
   SEQ                  VARCHAR(256)         null,
   IS_FIXED				BOOLEAN              null,
   constraint PK_COF_MENU primary key (ID)
);
comment on table COF_MENU is '菜单';
comment on column COF_MENU.ID is '菜单ID';
comment on column COF_MENU.GROUP_ID is '权限组ID';
comment on column COF_MENU.NAME is '权限名称';
comment on column COF_MENU.CODE is '功能编码';
comment on column COF_MENU.DESCRIPTION is '描述';
comment on column COF_MENU.CREATE_TIME is '创建时间';
comment on column COF_MENU.UPDATE_TIME is '更新时间';
comment on column COF_MENU.TENANT_ID is '租户ID';
comment on column COF_MENU.PARENT_ID is '父菜单ID';
comment on column COF_MENU.SORT_NO is '排序号';
comment on column COF_MENU.IS_LEAF is '是否叶节点';
comment on column COF_MENU.TREE_LEVEL is '层级';
comment on column COF_MENU.SEQ is '序列号';
comment on column COF_MENU.IS_FIXED is '是否固定，如果是，则数据不能修改，不能删除';

create index COF_MENU_INDEX_NAME on COF_MENU(NAME);
create index COF_MENU_INDEX_CODE on COF_MENU(CODE);
create index COF_MENU_INDEX_SEQ on COF_MENU(SEQ);

/*==============================================================*/
/* Table: COF_OPERATION_LOG                                     */
/*==============================================================*/
create table COF_OPERATION_LOG (
   ID                   VARCHAR(64)          not null,
   OPERATOR_ID          VARCHAR(64)          null,
   OPERATOR_NAME        VARCHAR(128)         null,
   OPERATE_TYPE         INT4                 null,
   OPERATE_DATE         DATE                 null,
   TARGET_TYPE          VARCHAR(64)          null,
   TARGET_MODEL_ID      VARCHAR(64)          null,
   TARGET_MODEL_NAME    VARCHAR(256)         null,
   MESSAGE              TEXT                 null,
   TENANT_ID            VARCHAR(64)          null,
   constraint PK_COF_OPERATION_LOG primary key (ID)
);
comment on table COF_OPERATION_LOG is '操作日志';
comment on column COF_OPERATION_LOG.ID is '操作日志ID';
comment on column COF_OPERATION_LOG.OPERATOR_ID is '操作者ID';
comment on column COF_OPERATION_LOG.OPERATOR_NAME is '操作者名称';
comment on column COF_OPERATION_LOG.OPERATE_TYPE is '操作类型';
comment on column COF_OPERATION_LOG.OPERATE_DATE is '时间';
comment on column COF_OPERATION_LOG.TARGET_TYPE is '目标类型';
comment on column COF_OPERATION_LOG.TARGET_MODEL_ID is '目标ID';
comment on column COF_OPERATION_LOG.TARGET_MODEL_NAME is '目标名称';
comment on column COF_OPERATION_LOG.MESSAGE is '信息';
comment on column COF_OPERATION_LOG.TENANT_ID is '租户ID';

create index COF_OPT_LOG_INDEX_OPERATOR on COF_OPERATION_LOG(OPERATOR_NAME);
create index COF_OPT_LOG_INDEX_OPERATE_TYPE on COF_OPERATION_LOG(OPERATE_TYPE);
create index COF_OPT_LOG_INDEX_TARGET on COF_OPERATION_LOG(TARGET_TYPE, TARGET_MODEL_ID, TARGET_MODEL_NAME);

/*==============================================================*/
/* Table: COF_OPERATION_LOG_DETAIL                              */
/*==============================================================*/
create table COF_OPERATION_LOG_DETAIL (
   ID                   VARCHAR(64)          not null,
   OLD_DATA_JSON        TEXT                 null,
   NEW_DATA_JSON        TEXT                 null,
   constraint PK_COF_OPERATION_LOG_DETAIL primary key (ID)
);
comment on table COF_OPERATION_LOG_DETAIL is '操作日志详情';
comment on column COF_OPERATION_LOG_DETAIL.ID is '日志详情ID';
comment on column COF_OPERATION_LOG_DETAIL.OLD_DATA_JSON is '旧值 ';
comment on column COF_OPERATION_LOG_DETAIL.NEW_DATA_JSON is '新值';

/*==============================================================*/
/* Table: COF_ORG                                               */
/*==============================================================*/
create table COF_ORG (
   ID                   VARCHAR(64)          not null,
   DIMENSION_ID         VARCHAR(64)          null,
   CODE                 VARCHAR(64)          null,
   NAME                 VARCHAR(128)         null,
   DESCRIPTION          VARCHAR(256)         null,
   CREATE_TIME          DATE                 null,
   UPDATE_TIME          DATE                 null,
   TENANT_ID            VARCHAR(64)          null,
   PARENT_ID            VARCHAR(64)          null,
   TYPE                 VARCHAR(64)          null,
   DEGREE               VARCHAR(256)         null,
   STATUS               VARCHAR(64)          null,
   AREA                 VARCHAR(256)         null,
   ADDRESS              VARCHAR(512)         null,
   ZIPCODE              VARCHAR(64)          null,
   LINK_MAN             VARCHAR(256)         null,
   LINK_TEL             VARCHAR(64)          null,
   EMAIL                VARCHAR(256)         null,
   MANAGER_ID           VARCHAR(64)          null,
   MAIN_DIMENSION_ORG_ID VARCHAR(64)         null,
   WEBSITE              VARCHAR(512)         null,
   SORT_NO              INT4                 null,
   STRATEGY				INT4				 null,
   IS_LEAF              BOOLEAN              null,
   TREE_LEVEL           INT4                 null,
   SEQ                  VARCHAR(256)         null,
   IS_FIXED				BOOLEAN              null,
   constraint PK_COF_ORG primary key (ID)
);
comment on table COF_ORG is '组织机构';
comment on column COF_ORG.ID is '机构ID';
comment on column COF_ORG.DIMENSION_ID is '维度ID';
comment on column COF_ORG.CODE is '机构编码';
comment on column COF_ORG.NAME is '机构名称';
comment on column COF_ORG.DESCRIPTION is '描述';
comment on column COF_ORG.CREATE_TIME is '创建时间';
comment on column COF_ORG.UPDATE_TIME is '更新时间';
comment on column COF_ORG.TENANT_ID is '租户ID';
comment on column COF_ORG.PARENT_ID is '父机构ID';
comment on column COF_ORG.TYPE is '机构类型';
comment on column COF_ORG.DEGREE is '机构等级';
comment on column COF_ORG.STATUS is '机构状态';
comment on column COF_ORG.AREA is '区域';
comment on column COF_ORG.ADDRESS is '地址';
comment on column COF_ORG.ZIPCODE is '邮编';
comment on column COF_ORG.LINK_MAN is '联系人';
comment on column COF_ORG.LINK_TEL is '联系电话';
comment on column COF_ORG.EMAIL is '机构邮箱';
comment on column COF_ORG.MANAGER_ID is '机构管理者ID';
comment on column COF_ORG.MAIN_DIMENSION_ORG_ID is '映射到的主维的机构ID';
comment on column COF_ORG.WEBSITE is '机构网站';
comment on column COF_ORG.SORT_NO is '同级序号';
comment on column COF_ORG.IS_LEAF is '是否叶节点';
comment on column COF_ORG.TREE_LEVEL is '层级';
comment on column COF_ORG.SEQ is '序列码';
comment on column COF_ORG.IS_FIXED is '是否固定，如果是，则数据不能修改，不能删除';

create index COF_ORG_INDEX_NAME on COF_ORG(NAME);
create index COF_ORG_INDEX_CODE on COF_ORG(CODE);
create index COF_ORG_INDEX_TYPE on COF_ORG(TYPE);
create index COF_ORG_INDEX_SEQ on COF_ORG(SEQ);

/*==============================================================*/
/* Table: COF_ORG_EMP_MAPPING                                   */
/*==============================================================*/
create table COF_ORG_EMP_MAPPING (
   ID                   VARCHAR(64)          not null,
   ORG_ID               VARCHAR(64)          null,
   EMP_ID               VARCHAR(64)          null,
   IS_FIXED				BOOLEAN              null,
   constraint PK_COF_ORG_EMP_MAPPING primary key (ID)
);
comment on table COF_ORG_EMP_MAPPING is '员工与机构关联';
comment on column COF_ORG_EMP_MAPPING.ID is '关联ID';
comment on column COF_ORG_EMP_MAPPING.ORG_ID is '机构ID';
comment on column COF_ORG_EMP_MAPPING.EMP_ID is '员工ID';
comment on column COF_ORG_EMP_MAPPING.IS_FIXED is '是否固定，如果是，则数据不能修改，不能删除';

create index COF_ORG_EMP_MAPPING_INDEX on COF_ORG_EMP_MAPPING(ORG_ID, EMP_ID);

/*==============================================================*/
/* Table: COF_PARTY_AUTH                                        */
/*==============================================================*/
create table COF_PARTY_AUTH (
   ID                   VARCHAR(64)          not null,
   PARTY_TYPE           VARCHAR(64)          null,
   PARTY_ID             VARCHAR(64)          null,
   AUTH_TYPE            VARCHAR(64)          null,
   AUTH_ID              VARCHAR(64)          null,
   AUTH_OWNER_TYPE      VARCHAR(64)          null,
   AUTH_OWNER_ID        VARCHAR(64)          null,
   IS_FIXED				BOOLEAN              null,
   constraint PK_COF_PARTY_AUTH primary key (ID)
);
comment on table COF_PARTY_AUTH is '参与者与可授权者的关联';
comment on column COF_PARTY_AUTH.ID is '关联ID';
comment on column COF_PARTY_AUTH.AUTH_TYPE is '可授权者类型';
comment on column COF_PARTY_AUTH.AUTH_ID is '可授权者ID';
comment on column COF_PARTY_AUTH.PARTY_TYPE is '参与者类型';
comment on column COF_PARTY_AUTH.PARTY_ID is '参与者ID';
comment on column COF_PARTY_AUTH.AUTH_OWNER_TYPE is '冗余属性，可授权者所属对象类型';
comment on column COF_PARTY_AUTH.AUTH_OWNER_ID is '冗余属性，可授权者所属对象ID';
comment on column COF_PARTY_AUTH.IS_FIXED is '是否固定，如果是，则数据不能修改，不能删除';

create index COF_PARTY_AUTH_INDEX_PARTY on COF_PARTY_AUTH(PARTY_TYPE, PARTY_ID);
create index COF_PARTY_AUTH_INDEX_AUTH on COF_PARTY_AUTH(AUTH_TYPE, AUTH_ID);
create index COF_PARTY_AUTH_INDEX_AUTH_OWNER on COF_PARTY_AUTH(AUTH_OWNER_TYPE, AUTH_OWNER_ID);

/*==============================================================*/
/* Table: COF_POSITION                                          */
/*==============================================================*/
create table COF_POSITION (
   ID                   VARCHAR(64)          not null,
   ORG_ID               VARCHAR(64)          null,
   CODE                 VARCHAR(64)          null,
   NAME                 VARCHAR(128)         null,
   DESCRIPTION          VARCHAR(256)         null,
   CREATE_TIME          DATE                 null,
   UPDATE_TIME          DATE                 null,
   TENANT_ID            VARCHAR(64)          null,
   TYPE                 VARCHAR(64)          null,
   STATUS               VARCHAR(64)          null,
   PARENT_ID            VARCHAR(64)          null,
   SORT_NO              INT4                 null,
   IS_LEAF              BOOLEAN              null,
   TREE_LEVEL           INT4                 null,
   SEQ                  VARCHAR(256)         null,
   IS_FIXED				BOOLEAN              null,
   constraint PK_COF_POSITION primary key (ID)
);
comment on table COF_POSITION is '岗位';
comment on column COF_POSITION.ID is '岗位ID';
comment on column COF_POSITION.ORG_ID is '机构ID';
comment on column COF_POSITION.CODE is '岗位编码';
comment on column COF_POSITION.NAME is '岗位名称';
comment on column COF_POSITION.DESCRIPTION is '岗位描述';
comment on column COF_POSITION.CREATE_TIME is '创建时间';
comment on column COF_POSITION.UPDATE_TIME is '更新时间';
comment on column COF_POSITION.TENANT_ID is '租户ID';
comment on column COF_POSITION.TYPE is '岗位类型';
comment on column COF_POSITION.STATUS is '岗位状态';
comment on column COF_POSITION.PARENT_ID is '父岗位ID';
comment on column COF_POSITION.SORT_NO is '同级序号';
comment on column COF_POSITION.IS_LEAF is '是否叶节点';
comment on column COF_POSITION.TREE_LEVEL is '层级';
comment on column COF_POSITION.SEQ is '序列码';
comment on column COF_POSITION.IS_FIXED is '是否固定，如果是，则数据不能修改，不能删除';

create index COF_POSITION_INDEX_NAME on COF_POSITION(NAME);
create index COF_POSITION_INDEX_CODE on COF_POSITION(CODE);
create index COF_POSITION_INDEX_TYPE on COF_POSITION(TYPE);
create index COF_POSITION_INDEX_SEQ on COF_POSITION(SEQ);

/*==============================================================*/
/* Table: COF_POSITION_EMP_MAPPING                              */
/*==============================================================*/
create table COF_POSITION_EMP_MAPPING (
   ID                   VARCHAR(64)          not null,
   EMP_ID               VARCHAR(64)          null,
   POSITION_ID          VARCHAR(64)          null,
   IS_FIXED				BOOLEAN              null,
   constraint PK_COF_POSITION_EMP_MAPPING primary key (ID)
);
comment on table COF_POSITION_EMP_MAPPING is '员工与岗位关联';
comment on column COF_POSITION_EMP_MAPPING.ID is '关联ID';
comment on column COF_POSITION_EMP_MAPPING.EMP_ID is '员工ID';
comment on column COF_POSITION_EMP_MAPPING.POSITION_ID is '岗位ID';
comment on column COF_POSITION_EMP_MAPPING.IS_FIXED is '是否固定，如果是，则数据不能修改，不能删除';

create index COF_POSITION_EMP_MAPPING_INDEX on COF_POSITION_EMP_MAPPING(POSITION_ID, EMP_ID);

/*==============================================================*/
/* Table: COF_RES_GROUP                                         */
/*==============================================================*/
create table COF_RES_GROUP (
   ID                   VARCHAR(64)          not null,
   NAME                 VARCHAR(128)         null,
   CODE                 VARCHAR(64)          null,
   DESCRIPTION          VARCHAR(256)         null,
   CREATE_TIME          DATE                 null,
   UPDATE_TIME          DATE                 null,
   TENANT_ID            VARCHAR(64)          null,
   PARENT_ID            VARCHAR(64)          null,
   TYPE                 VARCHAR(64)          null,
   RES_TYPE             VARCHAR(64)          null,
   SORT_NO              INT4                 null,
   IS_LEAF              BOOLEAN              null,
   TREE_LEVEL           INT4                 null,
   SEQ                  VARCHAR(256)         null,
   IS_FIXED				BOOLEAN              null,
   constraint PK_COF_RES_GROUP primary key (ID)
);
comment on table COF_RES_GROUP is '资源分组';
comment on column COF_RES_GROUP.ID is '资源组ID';
comment on column COF_RES_GROUP.NAME is '资源组名称';
comment on column COF_RES_GROUP.CODE is '资源组编码';
comment on column COF_RES_GROUP.DESCRIPTION is '描述';
comment on column COF_RES_GROUP.CREATE_TIME is '创建时间';
comment on column COF_RES_GROUP.UPDATE_TIME is '更新时间';
comment on column COF_RES_GROUP.TENANT_ID is '租户ID';
comment on column COF_RES_GROUP.PARENT_ID is '父资源组ID';
comment on column COF_RES_GROUP.TYPE is '资源组类型';
comment on column COF_RES_GROUP.RES_TYPE is '资源类型';
comment on column COF_RES_GROUP.SORT_NO is '排序字段';
comment on column COF_RES_GROUP.IS_LEAF is '是否叶节点';
comment on column COF_RES_GROUP.TREE_LEVEL is '层级';
comment on column COF_RES_GROUP.SEQ is '序列码，用来优化树状加载';
comment on column COF_RES_GROUP.IS_FIXED is '是否固定，如果是，则数据不能修改，不能删除';

create index COF_RES_GROUP_INDEX_NAME on COF_RES_GROUP(NAME);
create index COF_RES_GROUP_INDEX_CODE on COF_RES_GROUP(CODE);
create index COF_RES_GROUP_INDEX_TYPE on COF_RES_GROUP(TYPE);
create index COF_RES_GROUP_INDEX_SEQ on COF_RES_GROUP(SEQ);

/*==============================================================*/
/* Table: COF_ROLE                                              */
/*==============================================================*/
create table COF_ROLE (
   ID                   VARCHAR(64)          not null,
   NAME                 VARCHAR(128)         null,
   CODE                 VARCHAR(64)          null,
   DESCRIPTION          VARCHAR(256)         null,
   TENANT_ID            VARCHAR(64)          null,
   CREATE_TIME          DATE                 null,
   UPDATE_TIME          DATE                 null,
   ROLE_TPL_ID          VARCHAR(64)          null,
   AUTH_TPL_ID          VARCHAR(64)          null,
   OWNER_TYPE           VARCHAR(64)          null,
   OWNER_ID             VARCHAR(64)          null,
   IS_FIXED				BOOLEAN              null,
   constraint PK_COF_ROLE primary key (ID)
);
comment on table COF_ROLE is '角色';
comment on column COF_ROLE.ID is '角色ID';
comment on column COF_ROLE.NAME is '角色名称';
comment on column COF_ROLE.CODE is '角色编码';
comment on column COF_ROLE.DESCRIPTION is '描述';
comment on column COF_ROLE.TENANT_ID is '租户ID';
comment on column COF_ROLE.CREATE_TIME is '创建时间';
comment on column COF_ROLE.UPDATE_TIME is '更新时间';
comment on column COF_ROLE.ROLE_TPL_ID is '角色模板ID';
comment on column COF_ROLE.AUTH_TPL_ID is '权限模板ID';
comment on column COF_ROLE.OWNER_TYPE is '角色所属对象类型';
comment on column COF_ROLE.OWNER_ID is '角色所属对象ID';

comment on column COF_ROLE.IS_FIXED is '是否固定，如果是，则数据不能修改，不能删除';

create index COF_ROLE_INDEX_NAME on COF_ROLE(NAME);
create index COF_ROLE_INDEX_CODE on COF_ROLE(CODE);
create index COF_ROLE_INDEX_ROLE_TPL on COF_ROLE(ROLE_TPL_ID);
create index COF_ROLE_INDEX_OWNER on COF_ROLE(OWNER_TYPE,OWNER_ID);

/*==============================================================*/
/* Table: COF_USER                                              */
/*==============================================================*/
create table COF_USER (
   ID                   VARCHAR(64)          not null,
   EMP_ID               VARCHAR(64)          null,
   NAME                 VARCHAR(128)         null,
   DESCRIPTION          VARCHAR(256)         null,
   CREATE_TIME          DATE                 null,
   UPDATE_TIME          DATE                 null,
   EMAIL                VARCHAR(256)         null,
   TENANT_ID            VARCHAR(64)          null,
   PASSWORD             VARCHAR(128)         null,
   SALT                 VARCHAR(64)          null,
   STATUS               VARCHAR(64)          null,
   AUTH_MODE            VARCHAR(64)          null,
   UNLOCK_TIME          DATE                 null,
   LAST_LOGIN           DATE                 null,
   START_DATE           DATE                 null,
   END_DATE             DATE                 null,
   IS_FIXED				BOOLEAN              null,
   constraint PK_COF_USER primary key (ID)
);
comment on table COF_USER is '用户';
comment on column COF_USER.ID is '用户ID';
comment on column COF_USER.EMP_ID is '员工ID';
comment on column COF_USER.NAME is '用户名称';
comment on column COF_USER.DESCRIPTION is '描述';
comment on column COF_USER.CREATE_TIME is '创建时间';
comment on column COF_USER.UPDATE_TIME is '更新时间';
comment on column COF_USER.EMAIL is '用户邮箱';
comment on column COF_USER.TENANT_ID is '租户ID';
comment on column COF_USER.PASSWORD is '用户密码';
comment on column COF_USER.SALT is 'TOKEN加密密钥';
comment on column COF_USER.STATUS is '状态';
comment on column COF_USER.AUTH_MODE is '认证模式';
comment on column COF_USER.UNLOCK_TIME is '解锁时间';
comment on column COF_USER.LAST_LOGIN is '最后登陆时间';
comment on column COF_USER.START_DATE is '有效开始日期';
comment on column COF_USER.END_DATE is '有效结束日期';
comment on column COF_USER.IS_FIXED is '是否固定，如果是，则数据不能修改，不能删除';

create index COF_USER_INDEX_NAME on COF_USER(NAME);
create index COF_USER_INDEX_STATUS on COF_USER(STATUS);

/*==============================================================*/
/* Table: COF_WORKGROUP                                         */
/*==============================================================*/
create table COF_WORKGROUP (
   ID                   VARCHAR(64)          not null,
   ORG_ID               VARCHAR(64)          null,
   CODE                 VARCHAR(64)          null,
   NAME                 VARCHAR(128)         null,
   DESCRIPTION          VARCHAR(256)         null,
   CREATE_TIME          DATE                 null,
   UPDATE_TIME          DATE                 null,
   TENANT_ID            VARCHAR(64)          null,
   PARENT_ID            VARCHAR(64)          null,
   TYPE                 VARCHAR(64)          null,
   STATUS               VARCHAR(64)          null,
   MANAGER_ID           VARCHAR(64)          null,
   SORT_NO              INT4                 null,
   IS_LEAF              BOOLEAN              null,
   TREE_LEVEL           INT4                 null,
   SEQ                  VARCHAR(256)         null,
   IS_FIXED				BOOLEAN              null,
   constraint PK_COF_WORKGROUP primary key (ID)
);
comment on table COF_WORKGROUP is '工作组';
comment on column COF_WORKGROUP.ID is '工作组ID';
comment on column COF_WORKGROUP.ORG_ID is '机构ID';
comment on column COF_WORKGROUP.CODE is '工作组编码';
comment on column COF_WORKGROUP.NAME is '工作组名称';
comment on column COF_WORKGROUP.DESCRIPTION is '描述';
comment on column COF_WORKGROUP.CREATE_TIME is '创建时间';
comment on column COF_WORKGROUP.UPDATE_TIME is '更新时间';
comment on column COF_WORKGROUP.TENANT_ID is '租户ID';
comment on column COF_WORKGROUP.PARENT_ID is '父工作组ID';
comment on column COF_WORKGROUP.TYPE is '工作组类型';
comment on column COF_WORKGROUP.STATUS is '工作组状态';
comment on column COF_WORKGROUP.MANAGER_ID is '负责人ID';
comment on column COF_WORKGROUP.SORT_NO is '同级序号';
comment on column COF_WORKGROUP.IS_LEAF is '是否叶节点';
comment on column COF_WORKGROUP.TREE_LEVEL is '层级';
comment on column COF_WORKGROUP.SEQ is '序列码';
comment on column COF_WORKGROUP.IS_FIXED is '是否固定，如果是，则数据不能修改，不能删除';

create index COF_WORKGROUP_INDEX_NAME on COF_WORKGROUP(NAME);
create index COF_WORKGROUP_INDEX_CODE on COF_WORKGROUP(CODE);
create index COF_WORKGROUP_INDEX_TYPE on COF_WORKGROUP(TYPE);
create index COF_WORKGROUP_INDEX_SEQ on COF_WORKGROUP(SEQ);

/*==============================================================*/
/* Table: COF_WORKGROUP_EMP_MAPPING                             */
/*==============================================================*/
create table COF_WORKGROUP_EMP_MAPPING (
   ID                   VARCHAR(64)          not null,
   EMP_ID               VARCHAR(64)          null,
   WORKGROUP_ID         VARCHAR(64)          null,
   IS_FIXED				BOOLEAN              null,
   constraint PK_COF_WORKGROUP_EMP_MAPPING primary key (ID)
);
comment on table COF_WORKGROUP_EMP_MAPPING is '员工与工作组的关联';
comment on column COF_WORKGROUP_EMP_MAPPING.ID is '关联ID';
comment on column COF_WORKGROUP_EMP_MAPPING.EMP_ID is '员工ID';
comment on column COF_WORKGROUP_EMP_MAPPING.WORKGROUP_ID is '工作组ID';
comment on column COF_WORKGROUP_EMP_MAPPING.IS_FIXED is '是否固定，如果是，则数据不能修改，不能删除';

create index COF_WG_EMP_MAPPING_INDEX on COF_WORKGROUP_EMP_MAPPING(WORKGROUP_ID, EMP_ID);

/*==============================================================*/
/* Table: COF_AUTH_TEMPLATE                                     */
/*==============================================================*/
create table COF_AUTH_TEMPLATE (
   ID                   VARCHAR(64)          not null,
   NAME                 VARCHAR(256)         null,
   DESCRIPTION          VARCHAR(512)         null,
   CREATE_TIME          DATE                 null,
   UPDATE_TIME          DATE                 null,
   TENANT_ID            VARCHAR(64)          null,
   IS_FIXED				BOOLEAN              null,
   constraint PK_COF_AUTH_TEMPLATE primary key (ID)
);
comment on table COF_AUTH_TEMPLATE is '权限模板';
comment on column COF_AUTH_TEMPLATE.ID is '权限模板ID';
comment on column COF_AUTH_TEMPLATE.NAME is '权限模板名称';
comment on column COF_AUTH_TEMPLATE.DESCRIPTION is '描述';
comment on column COF_AUTH_TEMPLATE.CREATE_TIME is '创建时间';
comment on column COF_AUTH_TEMPLATE.UPDATE_TIME is '更新时间';
comment on column COF_AUTH_TEMPLATE.TENANT_ID is '租户ID';
comment on column COF_AUTH_TEMPLATE.IS_FIXED is '是否固定，如果是，则数据不能修改，不能删除';

/*==============================================================*/
/* Table: COF_AUTH_TPL_RES_GROUP                                */
/*==============================================================*/
create table COF_AUTH_TPL_RES_GROUP (
   ID                   VARCHAR(64)          not null,
   AUTH_TPL_ID          VARCHAR(64)          null,
   RES_GROUP_ID         VARCHAR(64)          null,
   IS_FIXED				BOOLEAN              null,
   constraint PK_COF_AUTH_TPL_RES_GROUP primary key (ID)
);
comment on table COF_AUTH_TPL_RES_GROUP is '权限模板与资源组的关联';
comment on column COF_AUTH_TPL_RES_GROUP.AUTH_TPL_ID is '权限模板ID';
comment on column COF_AUTH_TPL_RES_GROUP.RES_GROUP_ID is '资源组ID';
comment on column COF_AUTH_TPL_RES_GROUP.IS_FIXED is '是否固定，如果是，则数据不能修改，不能删除';

/*==============================================================*/
/* Table: COF_ROLE_TEMPLATE                                     */
/*==============================================================*/
create table COF_ROLE_TEMPLATE (
   ID                   VARCHAR(64)          not null,
   AUTH_TPL_ID          VARCHAR(64)          null,
   GROUP_ID             VARCHAR(64)          null,
   NAME                 VARCHAR(128)         null,
   DESCRIPTION          VARCHAR(256)         null,
   CREATE_TIME          DATE                 null,
   UPDATE_TIME          DATE                 null,
   TENANT_ID            VARCHAR(64)          null,
   IS_FIXED				BOOLEAN              null,
   constraint PK_COF_ROLE_TEMPLATE primary key (ID)
);
comment on table COF_ROLE_TEMPLATE is '角色模板';
comment on column COF_ROLE_TEMPLATE.ID is 'ID';
comment on column COF_ROLE_TEMPLATE.AUTH_TPL_ID is '权限模板ID';
comment on column COF_ROLE_TEMPLATE.AUTH_TPL_ID is '角色模板组ID';
comment on column COF_ROLE_TEMPLATE.NAME is '名称';
comment on column COF_ROLE_TEMPLATE.DESCRIPTION is '描述';
comment on column COF_ROLE_TEMPLATE.CREATE_TIME is '创建时间';
comment on column COF_ROLE_TEMPLATE.UPDATE_TIME is '更新时间';
comment on column COF_ROLE_TEMPLATE.TENANT_ID is '租户ID';
comment on column COF_ROLE_TEMPLATE.IS_FIXED is '是否固定，如果是，则数据不能修改，不能删除';

create index COF_ROLE_TPL_INDEX_NAME on COF_ROLE_TEMPLATE(NAME);
create index COF_ROLE_TPL_INDEX_AUTH_TPL on COF_ROLE_TEMPLATE(AUTH_TPL_ID);

/*==============================================================*/
/* Table: COF_ROLE_TEMPLATE_GROUP                               */
/*==============================================================*/
create table COF_ROLE_TEMPLATE_GROUP 
(
   ID                   VARCHAR(64)          not null,
   NAME                 VARCHAR(128)         null,
   DESCRIPTION          VARCHAR(256)         null,
   CREATE_TIME          DATE                 null,
   UPDATE_TIME          DATE                 null,
   TENANT_ID            VARCHAR(64)          null,
   IS_FIXED				BOOLEAN              null,
   constraint PK_COF_ROLE_TEMPLATE_GROUP primary key (ID)
);

comment on table COF_ROLE_TEMPLATE_GROUP is '角色模板组';
comment on column COF_ROLE_TEMPLATE_GROUP.ID is 'ID';
comment on column COF_ROLE_TEMPLATE_GROUP.NAME is '角色模板组名称';
comment on column COF_ROLE_TEMPLATE_GROUP.DESCRIPTION is '描述';
comment on column COF_ROLE_TEMPLATE_GROUP.CREATE_TIME is '创建时间';
comment on column COF_ROLE_TEMPLATE_GROUP.UPDATE_TIME is '更新时间';
comment on column COF_ROLE_TEMPLATE_GROUP.TENANT_ID is '租户ID';
comment on column COF_ROLE_TEMPLATE_GROUP.IS_FIXED is '是否固定，如果是，则数据不能修改，不能删除';

create index COF_ROLE_TPL_GROUP_INDEX_NAME on COF_ROLE_TEMPLATE_GROUP(NAME);