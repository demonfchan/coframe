/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2019/3/27 19:04:02                           */
/*==============================================================*/
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
drop table if exists COF_ROLE_TEMPLATE_GROUP;

/*==============================================================*/
/* Table: COF_AUTH_RES                                          */
/*==============================================================*/
create table COF_AUTH_RES
(
   ID                   varchar(64) not null comment '关联ID',
   TYPE                 varchar(64) comment '授权类型',
   AUTH_TYPE            varchar(64) comment '可授权者类型',
   AUTH_ID              varchar(64) comment '可授权者ID',
   RES_TYPE             varchar(64) comment '资源类型',
   RES_ID               varchar(64) comment '资源ID',
   IS_FIXED             bool comment '是否固定，如果是，则数据不能修改，不能删除',
   primary key (ID)
);

alter table COF_AUTH_RES comment '可授权者与资源的关联';
alter table COF_AUTH_RES add index COF_AUTH_RES_INDEX_AUTH (AUTH_ID, AUTH_TYPE);
alter table COF_AUTH_RES add index COF_AUTH_RES_INDEX_RES (RES_ID, RES_TYPE);
alter table COF_AUTH_RES add index COF_AUTH_RES_INDEX_TYPE (TYPE);

/*==============================================================*/
/* Table: COF_DICT_ENTRY                                        */
/*==============================================================*/
create table COF_DICT_ENTRY
(
   ID                   varchar(64) not null comment '字典项ID',
   CODE                 varchar(64) comment '字典项编码',
   DICT_TYPE_ID         varchar(64) comment '字典类型ID',
   NAME                 varchar(128) comment '字典项名称',
   DESCRIPTION          varchar(256) comment '描述',
   CREATE_TIME          datetime comment '创建时间',
   UPDATE_TIME          datetime comment '更新时间',
   TENANT_ID            varchar(64) comment '租户ID',
   PARENT_ID            varchar(64) comment '父字典项ID',
   LOCALE               varchar(64) comment '默认语言',
   STATUS               varchar(64) comment '状态 ',
   SORT_NO              int comment '排序字段',
   IS_LEAF              bool comment '是否叶节点',
   TREE_LEVEL           int comment '层级',
   SEQ                  varchar(256) comment '序列码',
   IS_FIXED             bool comment '是否固定，如果是，则数据不能修改，不能删除',
   primary key (ID)
)ROW_FORMAT=DYNAMIC;


alter table COF_DICT_ENTRY comment '字典项';
alter table COF_DICT_ENTRY add index COF_DICT_ENTRY_INDEX_NAME (NAME);
alter table COF_DICT_ENTRY add index COF_DICT_ENTRY_INDEX_CODE (CODE);
alter table COF_DICT_ENTRY add index COF_DICT_ENTRY_INDEX_SEQ (SEQ);

/*==============================================================*/
/* Table: COF_DICT_ENTRY_I18N                                   */
/*==============================================================*/
create table COF_DICT_ENTRY_I18N
(
   ID                   varchar(64) not null comment '国际化项ID',
   DICT_ENTRY_ID        varchar(64) comment '字典项ID',
   LOCALE               varchar(64) comment '国际化区域',
   DICT_ENTRY_NAME      varchar(256) comment '字典项名称',
   IS_FIXED             bool comment '是否固定，如果是，则数据不能修改，不能删除',
   primary key (ID)
);

alter table COF_DICT_ENTRY_I18N comment '字典项国际化';
alter table COF_DICT_ENTRY_I18N add index COF_DICT_ENTRY_I18N_INDEX(DICT_ENTRY_ID, LOCALE);

/*==============================================================*/
/* Table: COF_DICT_TYPE                                         */
/*==============================================================*/
create table COF_DICT_TYPE
(
   ID                   varchar(64) not null comment '字典类型ID',
   CODE                 varchar(64) comment '字典类型编码',
   NAME                 varchar(128) comment '字典类型名称',
   DESCRIPTION          varchar(256) comment '描述',
   CREATE_TIME          datetime comment '创建时间',
   UPDATE_TIME          datetime comment '更新时间',
   TENANT_ID            varchar(64) comment '租户ID',
   PARENT_ID            varchar(64) comment '父字典类型ID',
   LOCALE               varchar(64) comment '默认语言',
   SORT_NO              int comment '排序字段',
   IS_LEAF              bool comment '是否叶节点',
   TREE_LEVEL           int comment '层级',
   SEQ                  varchar(256) comment '序列码',
   IS_FIXED             bool comment '是否固定，如果是，则数据不能修改，不能删除',
   primary key (ID)
)ROW_FORMAT=DYNAMIC;

alter table COF_DICT_TYPE comment '字典类型';
alter table COF_DICT_TYPE add index COF_DICT_TYPE_INDEX_NAME (NAME);
alter table COF_DICT_TYPE add index COF_DICT_TYPE_INDEX_CODE (CODE);
alter table COF_DICT_TYPE add index COF_DICT_TYPE_INDEX_SEQ (SEQ);

/*==============================================================*/
/* Table: COF_DICT_TYPE_I18N                                    */
/*==============================================================*/
create table COF_DICT_TYPE_I18N
(
   ID                   varchar(64) not null comment '国际化项ID',
   DICT_TYPE_ID         varchar(64) comment '字典类型ID',
   LOCALE               varchar(64) comment '区域',
   DICT_TYPE_NAME       varchar(256) comment '名称',
   IS_FIXED             bool comment '是否固定，如果是，则数据不能修改，不能删除',
   primary key (ID)
);

alter table COF_DICT_TYPE_I18N comment '字典类型国际化值';
alter table COF_DICT_TYPE_I18N add index COF_DICT_TYPE_I18N_INDEX(DICT_TYPE_ID, LOCALE);

/*==============================================================*/
/* Table: COF_DIMENSION                                         */
/*==============================================================*/
create table COF_DIMENSION
(
   ID                   varchar(64) not null comment '维度ID',
   CODE                 varchar(64) comment '维度编 码',
   NAME                 varchar(128) comment '维度名称',
   DESCRIPTION          varchar(256) comment '描述',
   CREATE_TIME          datetime comment '创建时间',
   UPDATE_TIME          datetime comment '更新时间',
   TENANT_ID            varchar(64) comment '租户ID',
   IS_MAIN              bool comment '是否主维度',
   IS_FIXED             bool comment '是否固定，如果是，则数据不能修改，不能删除',
   primary key (ID)
);

alter table COF_DIMENSION comment '维度，做为机构的顶层对象，前端展现为机构树';

/*==============================================================*/
/* Table: COF_EMP                                               */
/*==============================================================*/
create table COF_EMP
(
   ID                   varchar(64) not null comment '员工ID',
   CODE                 varchar(64) comment '员工编码',
   NAME                 varchar(128) comment '员工名称',
   REALNAME             varchar(128) comment '真实姓名',
   GENDER               varchar(8) comment '性别',
   BIRTHDAY             date comment '生日',
   STATUS               varchar(64) comment '员工状态',
   CARD_TYPE            varchar(64) comment '身份证件类型',
   CARD_NO              varchar(256) comment '证件编码',
   IN_DATE              date comment '入职时间',
   OUT_DATE             date comment '离职时间',
   FAN_NO               varchar(256) comment '传真',
   MOBILE_NO            varchar(64) comment '手机号',
   DESCRIPTION          varchar(256) comment '描述',
   CREATE_TIME          datetime comment '创建时间',
   UPDATE_TIME          datetime comment '更新时间',
   TENANT_ID            varchar(64) comment '租户ID',
   PARTY                varchar(64) comment '部门',
   DEGREE               varchar(256) comment '职称',
   REMARK               varchar(512) comment '备注',
   O_TEL                varchar(64) comment '办公室电话',
   O_ADDRESS            varchar(512) comment '办公室地址',
   O_EMAIL              varchar(256) comment '办公室邮箱',
   H_TEL                varchar(64) comment '家庭电话',
   H_ADDRESS            varchar(512) comment '家庭地址',
   H_ZIPCODE            varchar(64) comment '家庭邮编',
   P_EMAIL              varchar(256) comment '个人邮箱',
   QQ                   varchar(256) comment 'QQ号',
   WEIBO                varchar(256) comment '微博号',
   WECHAT               varchar(256) comment '微信号',
   IS_FIXED             bool comment '是否固定，如果是，则数据不能修改，不能删除',
   primary key (ID)
);

alter table COF_EMP comment '员工';
alter table COF_EMP add index COF_EMP_INDEX_NAME(NAME);
alter table COF_EMP add index COF_EMP_INDEX_CODE(CODE);
alter table COF_EMP add index COF_EMP_INDEX_STATUS(STATUS);

/*==============================================================*/
/* Table: COF_FUNCTION                                          */
/*==============================================================*/
create table COF_FUNCTION
(
   ID                   varchar(64) not null comment '功能ID',
   GROUP_ID             varchar(64) comment '权限组ID',
   NAME                 varchar(128) comment '权限名称',
   CODE                 varchar(64) comment '功能编码',
   URLS                 text comment 'url 集合',
   DESCRIPTION          varchar(256) comment '描述',
   CREATE_TIME          datetime comment '创建时间',
   UPDATE_TIME          datetime comment '更新时间',
   TENANT_ID            varchar(64) comment '租户ID',
   TYPE                 varchar(64) comment '功能类型',
   IS_CHECK             bool comment '是否校验',
   IS_FIXED             bool comment '是否固定，如果是，则数据不能修改，不能删除',
   primary key (ID)
);

alter table COF_FUNCTION comment '功能权限';
alter table COF_FUNCTION add index COF_FUNCTION_INDEX_NAME(NAME);
alter table COF_FUNCTION add index COF_FUNCTION_INDEX_CODE(CODE);
alter table COF_FUNCTION add index COF_FUNCTION_INDEX_TYPE(TYPE);

/*==============================================================*/
/* Table: COF_MENU                                              */
/*==============================================================*/
create table COF_MENU
(
   ID                   varchar(64) not null comment '菜单ID',
   GROUP_ID             varchar(64) comment '权限组ID',
   NAME                 varchar(128) comment '权限名称',
   CODE                 varchar(64) comment '功能编码',
   DESCRIPTION          varchar(256) comment '描述',
   CREATE_TIME          datetime comment '创建时间',
   UPDATE_TIME          datetime comment '更新时间',
   TENANT_ID            varchar(64) comment '租户ID',
   PARENT_ID            varchar(64) comment '父菜单ID',
   SORT_NO              int comment '排序号',
   IS_LEAF              bool comment '是否叶节点',
   TREE_LEVEL           int comment '层级',
   SEQ                  varchar(256) comment '序列号',
   IS_FIXED             bool comment '是否固定，如果是，则数据不能修改，不能删除',
   primary key (ID)
)ROW_FORMAT=DYNAMIC;

alter table COF_MENU comment '菜单';
alter table COF_MENU add index COF_MENU_INDEX_NAME(NAME);
alter table COF_MENU add index COF_MENU_INDEX_CODE(CODE);
alter table COF_MENU add index COF_MENU_INDEX_SEQ (SEQ);

/*==============================================================*/
/* Table: COF_OPERATION_LOG                                     */
/*==============================================================*/
create table COF_OPERATION_LOG
(
   ID                   varchar(64) not null comment '操作日志ID',
   OPERATOR_ID          varchar(64) comment '操作者ID',
   OPERATOR_NAME        varchar(64) comment '操作者名称',
   OPERATE_TYPE         int comment '操作类型',
   OPERATE_DATE         datetime comment '时间',
   TARGET_TYPE          varchar(64) comment '目标类型',
   TARGET_MODEL_ID      varchar(64) comment '目标ID',
   TARGET_MODEL_NAME    varchar(64) comment '目标名称',
   MESSAGE              text comment '信息',
   TENANT_ID            varchar(64) comment '租户ID',
   primary key (ID)
);

alter table COF_OPERATION_LOG comment '操作日志';
alter table COF_OPERATION_LOG add index COF_OPERATION_LOG_INDEX_OPERATOR(OPERATOR_NAME);
alter table COF_OPERATION_LOG add index COF_OPERATION_LOG_INDEX_OPERATE_TYPE(OPERATE_TYPE);
alter table COF_OPERATION_LOG add index COF_OPERATION_LOG_INDEX_TARGET(TARGET_TYPE, TARGET_MODEL_ID, TARGET_MODEL_NAME);

/*==============================================================*/
/* Table: COF_OPERATION_LOG_DETAIL                              */
/*==============================================================*/
create table COF_OPERATION_LOG_DETAIL
(
   ID                   varchar(64) not null comment '日志详情ID',
   OLD_DATA_JSON        longtext comment '旧值 ',
   NEW_DATA_JSON        longtext comment '新值',
   primary key (ID)
);

alter table COF_OPERATION_LOG_DETAIL comment '操作日志详情';

/*==============================================================*/
/* Table: COF_ORG                                               */
/*==============================================================*/
create table COF_ORG
(
   ID                   varchar(64) not null comment '机构ID',
   DIMENSION_ID         varchar(64) comment '维度ID',
   CODE                 varchar(64) comment '机构编码',
   NAME                 varchar(128) comment '机构名称',
   DESCRIPTION          varchar(256) comment '描述',
   CREATE_TIME          datetime comment '创建时间',
   UPDATE_TIME          datetime comment '更新时间',
   TENANT_ID            varchar(64) comment '租户ID',
   PARENT_ID            varchar(64) comment '父机构ID',
   TYPE                 varchar(64) comment '机构类型',
   STRATEGY			int comment '机构映射策略 - 0: 不同步, 1: 当前同步 2: 全同步',
   DEGREE               varchar(256) comment '机构等级',
   STATUS               varchar(64) comment '机构状态',
   AREA                 varchar(256) comment '区域',
   ADDRESS              varchar(512) comment '地址',
   ZIPCODE              varchar(64) comment '邮编',
   LINK_MAN             varchar(256) comment '联系人',
   LINK_TEL             varchar(64) comment '联系电话',
   EMAIL                varchar(256) comment '机构邮箱',
   MANAGER_ID           varchar(64) comment '机构管理者ID',
   MAIN_DIMENSION_ORG_ID varchar(64) comment '映射到的主维的机构ID',
   WEBSITE              varchar(512) comment '机构网站',
   SORT_NO              int comment '同级序号',
   IS_LEAF              bool comment '是否叶节点',
   TREE_LEVEL           int comment '层级',
   SEQ                  varchar(256) comment '序列码',
   IS_FIXED             bool comment '是否固定，如果是，则数据不能修改，不能删除',
   primary key (ID)
)ROW_FORMAT=DYNAMIC;

alter table COF_ORG comment '组织机构';
alter table COF_ORG add index COF_ORG_INDEX_NAME(NAME);
alter table COF_ORG add index COF_ORG_INDEX_CODE(CODE);
alter table COF_ORG add index COF_ORG_INDEX_TYPE(TYPE);
alter table COF_ORG add index COF_ORG_INDEX_SEQ (SEQ);

/*==============================================================*/
/* Table: COF_ORG_EMP_MAPPING                                   */
/*==============================================================*/
create table COF_ORG_EMP_MAPPING
(
   ID                   varchar(64) not null comment '关联ID',
   ORG_ID               varchar(64) comment '机构ID',
   EMP_ID               varchar(64) comment '员工ID',
   IS_FIXED             bool comment '是否固定，如果是，则数据不能修改，不能删除',
   primary key (ID)
);

alter table COF_ORG_EMP_MAPPING comment '员工与机构关联';
alter table COF_ORG_EMP_MAPPING add index COF_ORG_EMP_MAPPING_INDEX(ORG_ID, EMP_ID);

/*==============================================================*/
/* Table: COF_PARTY_AUTH                                        */
/*==============================================================*/
create table COF_PARTY_AUTH
(
   ID                   varchar(64) not null comment '关联ID',
   PARTY_TYPE           varchar(64) comment '参与者类型',
   PARTY_ID             varchar(64) comment '参与者ID',
   AUTH_TYPE            varchar(64) comment '可授权者类型',
   AUTH_ID              varchar(64) comment '可授权者ID',
   AUTH_OWNER_TYPE      varchar(64) comment '冗余属性，可授权者所属对象类型',
   AUTH_OWNER_ID     	varchar(64) comment '冗余属性，可授权者所属对象ID',
   IS_FIXED             bool comment '是否固定，如果是，则数据不能修改，不能删除',
   primary key (ID)
);

alter table COF_PARTY_AUTH comment '参与者与可授权者的关联';
alter table COF_PARTY_AUTH add index COF_PARTY_AUTH_INDEX_PARTY(PARTY_TYPE, PARTY_ID);
alter table COF_PARTY_AUTH add index COF_PARTY_AUTH_INDEX_AUTH(AUTH_TYPE, AUTH_ID);
alter table COF_PARTY_AUTH add index COF_PARTY_AUTH_INDEX_AUTH_OWNER(AUTH_OWNER_TYPE, AUTH_OWNER_ID);

/*==============================================================*/
/* Table: COF_POSITION                                          */
/*==============================================================*/
create table COF_POSITION
(
   ID                   varchar(64) not null comment '岗位ID',
   ORG_ID               varchar(64) comment '机构ID',
   CODE                 varchar(64) comment '岗位编码',
   NAME                 varchar(128) comment '岗位名称',
   DESCRIPTION          varchar(256) comment '岗位描述',
   CREATE_TIME          datetime comment '创建时间',
   UPDATE_TIME          datetime comment '更新时间',
   TENANT_ID            varchar(64) comment '租户ID',
   TYPE                 varchar(64) comment '岗位类型',
   STATUS               varchar(64) comment '岗位状态',
   PARENT_ID            varchar(64) comment '父岗位ID',
   SORT_NO              int comment '同级序号',
   IS_LEAF              bool comment '是否叶节点',
   TREE_LEVEL           int comment '层级',
   SEQ                  varchar(256) comment '序列码',
   IS_FIXED             bool comment '是否固定，如果是，则数据不能修改，不能删除',
   primary key (ID)
)ROW_FORMAT=DYNAMIC;

alter table COF_POSITION comment '岗位';
alter table COF_POSITION add index COF_POSITION_INDEX_NAME(NAME);
alter table COF_POSITION add index COF_POSITION_INDEX_CODE(CODE);
alter table COF_POSITION add index COF_POSITION_INDEX_TYPE(TYPE);
alter table COF_POSITION add index COF_POSITION_INDEX_SEQ(SEQ);

/*==============================================================*/
/* Table: COF_POSITION_EMP_MAPPING                              */
/*==============================================================*/
create table COF_POSITION_EMP_MAPPING
(
   ID                   varchar(64) not null comment '关联ID',
   EMP_ID               varchar(64) comment '员工ID',
   POSITION_ID          varchar(64) comment '岗位ID',
   IS_FIXED             bool comment '是否固定，如果是，则数据不能修改，不能删除',
   primary key (ID)
);

alter table COF_POSITION_EMP_MAPPING comment '员工与岗位关联';
alter table COF_POSITION_EMP_MAPPING add index COF_POSITION_EMP_MAPPING_INDEX(POSITION_ID, EMP_ID);

/*==============================================================*/
/* Table: COF_RES_GROUP                                         */
/*==============================================================*/
create table COF_RES_GROUP
(
   ID                   varchar(64) not null comment '资源组ID',
   NAME                 varchar(128) comment '资源组名称',
   CODE                 varchar(64) comment '资源组编码',
   DESCRIPTION          varchar(256) comment '描述',
   CREATE_TIME          datetime comment '创建时间',
   UPDATE_TIME          datetime comment '更新时间',
   TENANT_ID            varchar(64) comment '租户ID',
   PARENT_ID            varchar(64) comment '父资源组ID',
   TYPE                 varchar(64) comment '资源组类型',
   RES_TYPE				varchar(64) comment '资源类型',
   SORT_NO              int comment '排序字段',
   IS_LEAF              bool comment '是否叶节点',
   TREE_LEVEL           int comment '层级',
   SEQ                  varchar(256) comment '序列码，用来优化树状加载',
   IS_FIXED             bool comment '是否固定，如果是，则数据不能修改，不能删除',
   primary key (ID)
)ROW_FORMAT=DYNAMIC;

alter table COF_RES_GROUP comment '资源分组';
alter table COF_RES_GROUP add index COF_RES_GROUP_INDEX_NAME(NAME);
alter table COF_RES_GROUP add index COF_RES_GROUP_INDEX_CODE(CODE);
alter table COF_RES_GROUP add index COF_RES_GROUP_INDEX_TYPE(TYPE);
alter table COF_RES_GROUP add index COF_RES_GROUP_INDEX_SEQ(SEQ);

/*==============================================================*/
/* Table: COF_ROLE                                              */
/*==============================================================*/
create table COF_ROLE
(
   ID                   varchar(64) not null comment '角色ID',
   NAME                 varchar(128) comment '角色名称',
   CODE                 varchar(64) comment '角色编码',
   DESCRIPTION          varchar(256) comment '描述',
   TENANT_ID            varchar(64) comment '租户ID',
   CREATE_TIME          datetime comment '创建时间',
   UPDATE_TIME          datetime comment '更新时间',
   ROLE_TPL_ID     		varchar(64) comment '角色模板ID',
   AUTH_TPL_ID     		varchar(64) comment '权限模板ID',
   OWNER_TYPE          	varchar(64) comment '角色所属对象类型',
   OWNER_ID     		varchar(64) comment '角色所属对象ID',
   IS_FIXED             bool comment '是否固定，如果是，则数据不能修改，不能删除',
   primary key (ID)
);

alter table COF_ROLE comment '角色';
alter table COF_ROLE add index COF_ROLE_INDEX_NAME(NAME);
alter table COF_ROLE add index COF_ROLE_INDEX_CODE(CODE);
alter table COF_ROLE add index COF_ROLE_INDEX_ROLE_TPL(ROLE_TPL_ID);
alter table COF_ROLE add index COF_ROLE_INDEX_AUTH_OWNER(OWNER_TYPE,OWNER_ID);

/*==============================================================*/
/* Table: COF_USER                                              */
/*==============================================================*/
create table COF_USER
(
   ID                   varchar(64) not null comment '用户ID',
   EMP_ID               varchar(64) comment '员工ID',
   NAME                 varchar(128) comment '用户名称',
   DESCRIPTION          varchar(256) comment '描述',
   CREATE_TIME          datetime comment '创建时间',
   UPDATE_TIME          datetime comment '更新时间',
   EMAIL                varchar(256) comment '用户邮箱',
   TENANT_ID            varchar(64) comment '租户ID',
   PASSWORD             varchar(128) comment '用户密码',
   SALT                 varchar(64) comment 'TOKEN加密密钥',
   STATUS               varchar(64) comment '状态',
   AUTH_MODE            varchar(64) comment '认证模式',
   UNLOCK_TIME          datetime comment '解锁时间',
   LAST_LOGIN           datetime comment '最后登陆时间',
   START_DATE           date comment '有效开始日期',
   END_DATE             date comment '有效结束日期',
   IS_FIXED             bool comment '是否固定，如果是，则数据不能修改，不能删除',
   primary key (ID)
);

alter table COF_USER comment '用户';
alter table COF_USER add index COF_USER_INDEX_NAME(NAME);
alter table COF_USER add index COF_USER_INDEX_STATUS(STATUS);

/*==============================================================*/
/* Table: COF_WORKGROUP                                         */
/*==============================================================*/
create table COF_WORKGROUP
(
   ID                   varchar(64) not null comment '工作组ID',
   ORG_ID               varchar(64) comment '机构ID',
   CODE                 varchar(64) comment '工作组编码',
   NAME                 varchar(128) comment '工作组名称',
   DESCRIPTION          varchar(256) comment '描述',
   CREATE_TIME          datetime comment '创建时间',
   UPDATE_TIME          datetime comment '更新时间',
   TENANT_ID            varchar(64) comment '租户ID',
   PARENT_ID            varchar(64) comment '父工作组ID',
   TYPE                 varchar(64) comment '工作组类型',
   STATUS               varchar(64) comment '工作组状态',
   MANAGER_ID            varchar(64) comment '负责人ID',
   SORT_NO              int comment '同级序号',
   IS_LEAF              bool comment '是否叶节点',
   TREE_LEVEL           int comment '层级',
   SEQ                  varchar(256) comment '序列码',
   IS_FIXED             bool comment '是否固定，如果是，则数据不能修改，不能删除',
   primary key (ID)
)ROW_FORMAT=DYNAMIC;

alter table COF_WORKGROUP comment '工作组';
alter table COF_WORKGROUP add index COF_WORKGROUP_INDEX_NAME(NAME);
alter table COF_WORKGROUP add index COF_WORKGROUP_INDEX_CODE(CODE);
alter table COF_WORKGROUP add index COF_WORKGROUP_INDEX_TYPE(TYPE);
alter table COF_WORKGROUP add index COF_WORKGROUP_INDEX_SEQ(SEQ);

/*==============================================================*/
/* Table: COF_WORKGROUP_EMP_MAPPING                             */
/*==============================================================*/
create table COF_WORKGROUP_EMP_MAPPING
(
   ID                   varchar(64) not null comment '关联ID',
   EMP_ID               varchar(64) comment '员工ID',
   WORKGROUP_ID         varchar(64) comment '工作组ID',
   IS_FIXED             bool comment '是否固定，如果是，则数据不能修改，不能删除',
   primary key (ID)
);

alter table COF_WORKGROUP_EMP_MAPPING comment '员工与工作组的关联';
alter table COF_WORKGROUP_EMP_MAPPING add index COF_WORKGROUP_EMP_MAPPING_INDEX(WORKGROUP_ID, EMP_ID);

/*==============================================================*/
/* Table: COF_AUTH_TEMPLATE                                     */
/*==============================================================*/
create table COF_AUTH_TEMPLATE
(
   ID                   varchar(64) not null comment '权限模板ID',
   NAME                 varchar(128) comment '权限模板名称',
   DESCRIPTION          varchar(256) comment '描述',
   CREATE_TIME          datetime comment '创建时间',
   UPDATE_TIME          datetime comment '更新时间',
   TENANT_ID            varchar(64) comment '租户ID',
   IS_FIXED             bool comment '是否固定，如果是，则数据不能修改，不能删除',
   primary key (ID)
);

alter table COF_AUTH_TEMPLATE comment '权限模板';

/*==============================================================*/
/* Table: COF_AUTH_TPL_RES_GROUP                                */
/*==============================================================*/
create table COF_AUTH_TPL_RES_GROUP
(
   ID                   varchar(64) not null,
   AUTH_TPL_ID          varchar(64) comment '权限模板ID',
   RES_GROUP_ID         varchar(64) comment '资源组ID',
   IS_FIXED             bool comment '是否固定，如果是，则数据不能修改，不能删除',
   primary key (ID)
);

alter table COF_AUTH_TPL_RES_GROUP comment '权限模板与资源组的关联';

/*==============================================================*/
/* Table: COF_ROLE_TEMPLATE                                     */
/*==============================================================*/
create table COF_ROLE_TEMPLATE
(
   ID                   varchar(64) not null comment 'ID',
   AUTH_TPL_ID          varchar(64) comment '权限模板ID',
   GROUP_ID             varchar(64) comment '角色模板组ID',
   NAME                 varchar(128) comment '名称',
   DESCRIPTION          varchar(256) comment '描述',
   CREATE_TIME          datetime comment '创建时间',
   UPDATE_TIME          datetime comment '更新时间',
   TENANT_ID            varchar(64) comment '租户ID',
   IS_FIXED             bool comment '是否固定，如果是，则数据不能修改，不能删除',
   primary key (ID)
);

alter table COF_ROLE_TEMPLATE comment '角色模板';
alter table COF_ROLE_TEMPLATE add index COF_ROLE_TEMPLATE_INDEX_NAME(NAME);
alter table COF_ROLE_TEMPLATE add index COF_ROLE_TEMPLATE_INDEX_AUTH_TPL_ID(AUTH_TPL_ID);

/*==============================================================*/
/* Table: COF_ROLE_TEMPLATE_GROUP                               */
/*==============================================================*/
create table COF_ROLE_TEMPLATE_GROUP 
(
   ID                   varchar(64) not null comment 'ID',
   NAME                 varchar(128) comment '名称',
   DESCRIPTION          varchar(256) comment '描述',
   CREATE_TIME          datetime comment '创建时间',
   UPDATE_TIME          datetime comment '更新时间',
   TENANT_ID            varchar(64) comment '租户ID',
   IS_FIXED             bool comment '是否固定，如果是，则数据不能修改，不能删除',
   constraint PK_COF_ROLE_TEMPLATE_GROUP primary key (ID)
);

alter table COF_ROLE_TEMPLATE_GROUP comment '角色模板组';
alter table COF_ROLE_TEMPLATE_GROUP add index COF_ROLE_TEMPLATE_GROUP_INDEX_NAME(NAME);

