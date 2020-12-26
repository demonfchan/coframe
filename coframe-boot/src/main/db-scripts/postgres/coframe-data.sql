-- 资源组
DELETE FROM COF_RES_GROUP WHERE ID like 'cof-%';
INSERT INTO COF_RES_GROUP
(ID,NAME,TENANT_ID,CODE,TYPE,RES_TYPE,PARENT_ID,SEQ,TREE_LEVEL,SORT_NO,IS_LEAF,IS_FIXED)
VALUES
('cof-g-platform','平台权限','default','cof-g-platform','ROOT',null,null,'.1.',1,1,'0','1'),
('cof-g-menus','菜单','default','cof-g-menus','CATEGORY','MENU','cof-g-platform','.1.1.',2,1,'0','1'),
('cof-g-functions','功能','default','cof-g-functions','CATEGORY','FUNCTION','cof-g-platform','.1.2.',2,2,'0','1'),
('cof-mg-org','组织机构','default','cof-mg-org','MODULE','MENU','cof-g-menus','.1.1.1.',3,1,'1','1'),
('cof-mg-auth','权限管理','default','cof-mg-auth','MODULE','MENU','cof-g-menus','.1.1.2',3,2,'1','1'),
('cof-mg-sys','系统配置','default','cof-mg-sys','MODULE','MENU','cof-g-menus','.1.1.3.',3,3,'1','1'),
('cof-mg-audit','安全统计','default','cof-mg-audit','MODULE','MENU','cof-g-menus','.1.1.4.',3,4,'1','1'),
('cof-fg-dimension','机构树管理','default','cof-fg-dimension','MODULE','FUNCTION','cof-g-functions','.1.2.1.',3,1,'1','1'),
('cof-fg-org','机构管理','default','cof-fg-org','MODULE','FUNCTION','cof-g-functions','.1.2.2.',3,2,'1','1'),
('cof-fg-emp','员工管理','default','cof-fg-emp','MODULE','FUNCTION','cof-g-functions','.1.2.3.',3,3,'1','1'),
('cof-fg-position','岗位管理','default','cof-fg-position','MODULE','FUNCTION','cof-g-functions','.1.2.4.',3,4,'1','1'),
('cof-fg-workgroup','工作组管理','default','cof-fg-workgroup','MODULE','FUNCTION','cof-g-functions','.1.2.5.',3,5,'1','1'),
('cof-fg-user','用户管理','default','cof-fg-user','MODULE','FUNCTION','cof-g-functions','.1.2.6.',3,6,'1','1'),
('cof-fg-role','角色管理','default','cof-fg-role','MODULE','FUNCTION','cof-g-functions','.1.2.7.',3,7,'1','1'),
('cof-fg-member','成员管理','default','cof-fg-member','MODULE','FUNCTION','cof-g-functions','.1.2.8.',3,8,'1','1'),
('cof-fg-menu','菜单管理','default','cof-fg-menu','MODULE','FUNCTION','cof-g-functions','.1.2.9.',3,9,'1','1'),
('cof-fg-function','功能管理','default','cof-fg-function','MODULE','FUNCTION','cof-g-functions','.1.2.10.',3,10,'1','1'),
('cof-fg-res-group','资源组管理','default','cof-fg-res-group','MODULE','FUNCTION','cof-g-functions','.1.2.11.',3,11,'1','1'),
('cof-fg-auth-tpl','权限模板管理','default','cof-fg-auth-tpl','MODULE','FUNCTION','cof-g-functions','.1.2.12.',3,12,'1','1'),
('cof-fg-role-tpl-grp','角色模板组管理','default','cof-fg-role-tpl-grp','MODULE','FUNCTION','cof-g-functions','.1.2.13.',3,13,'1','1'),
('cof-fg-role-tpl','角色模板管理','default','cof-fg-role-tpl','MODULE','FUNCTION','cof-g-functions','.1.2.14.',3,14,'1','1'),
('cof-fg-dict','数据字典管理','default','cof-fg-dict','MODULE','FUNCTION','cof-g-functions','.1.2.15.',3,15,'1','1'),
('cof-fg-audit','审计日志管理','default','cof-fg-audit','MODULE','FUNCTION','cof-g-functions','.1.2.16.',3,16,'1','1');

-- 菜单资源
DELETE FROM COF_MENU WHERE ID like 'cof-%';
INSERT INTO COF_MENU
(ID,NAME,TENANT_ID,CODE,PARENT_ID,GROUP_ID,SEQ,TREE_LEVEL,SORT_NO,IS_LEAF,IS_FIXED)
VALUES
('cof-m-org','组织机构','default','cof-m-org','','cof-mg-org','.1.','1','1','0','1'),
('cof-m-org-tree','机构管理','default','cof-m-org-tree','cof-m-org','cof-mg-org','.1.1.','2','1','1','1'),
('cof-m-emp','人员管理','default','cof-m-emp','cof-m-org','cof-mg-org','.1.2.','2','2','1','1'),
('cof-m-auth','权限管理','default','cof-m-auth','','cof-mg-auth','.2.','1','2','0','1'),
('cof-m-member','角色成员','default','cof-m-member','cof-m-auth','cof-mg-auth','.2.2.','2','2','1','1'),
('cof-m-user','用户管理','default','cof-m-user','cof-m-auth','cof-mg-auth','.2.3.','2','3','1','1'),
('cof-m-sys','系统配置','default','cof-m-sys','','cof-mg-sys','.3.','1','3','0','1'),
('cof-m-role-template','角色模板','default','cof-m-role-template','cof-m-sys','cof-mg-sys','.3.1.','2','1','1','1'),
('cof-m-resouce-right','资源权限','default','cof-m-resouce-right','cof-m-sys','cof-mg-sys','.3.2.','2','2','1','1'),
('cof-m-dict','业务字典','default','cof-m-dict','cof-m-sys','cof-mg-sys','.3.3.','2','3','1','1'),
('cof-m-audit','安全统计','default','cof-m-audit','','cof-mg-audit','.4.','1','4','0','1'),
('cof-m-opt-log','操作日志','default','cof-m-opt-log','cof-m-audit','cof-mg-audit','.4.1.','2','1','1','1');

-- 功能资源
DELETE FROM COF_FUNCTION WHERE ID like 'cof-%';
INSERT INTO COF_FUNCTION
(ID,NAME,TENANT_ID,CODE,TYPE,GROUP_ID,IS_CHECK,IS_FIXED)
VALUES
('cof-f-dimension-add','添加机构树','default','cof-f-dimension-add','FUNCTION','cof-fg-dimension','1','1'),
('cof-f-dimension-edit','编辑机构树','default','cof-f-dimension-edit','FUNCTION','cof-fg-dimension','1','1'),
('cof-f-dimension-del','删除机构树','default','cof-f-dimension-del','FUNCTION','cof-fg-dimension','1','1'),
('cof-f-org-add','添加机构','default','cof-f-org-add','FUNCTION','cof-fg-org','1','1'),
('cof-f-org-edit','编辑机构','default','cof-f-org-edit','FUNCTION','cof-fg-org','1','1'),
('cof-f-org-del','删除机构','default','cof-f-org-del','FUNCTION','cof-fg-org','1','1'),
('cof-f-org-add-emp','添加机构员工','default','cof-f-org-add-emp','FUNCTION','cof-fg-org','1','1'),
('cof-f-org-rm-emp','移除机构员工','default','cof-f-org-rm-emp','FUNCTION','cof-fg-org','1','1'),
('cof-f-emp-add','添加员工','default','cof-f-emp-add','FUNCTION','cof-fg-emp','1','1'),
('cof-f-emp-edit','编辑员工','default','cof-f-emp-edit','FUNCTION','cof-fg-emp','1','1'),
('cof-f-emp-del','删除员工','default','cof-f-emp-del','FUNCTION','cof-fg-emp','1','1'),
('cof-f-position-add','添加岗位','default','cof-f-position-add','FUNCTION','cof-fg-position','1','1'),
('cof-f-position-edit','编辑岗位','default','cof-f-position-edit','FUNCTION','cof-fg-position','1','1'),
('cof-f-position-del','删除岗位','default','cof-f-position-del','FUNCTION','cof-fg-position','1','1'),
('cof-f-position-add-emp','岗位添加员工','default','cof-f-position-add-emp','FUNCTION','cof-fg-position','1','1'),
('cof-f-position-rm-emp','岗位移除员工','default','cof-f-position-rm-emp','FUNCTION','cof-fg-position','1','1'),
('cof-f-workgroup-add','添加工作组','default','cof-f-workgroup-add','FUNCTION','cof-fg-workgroup','1','1'),
('cof-f-workgroup-edit','编辑工作组','default','cof-f-workgroup-edit','FUNCTION','cof-fg-workgroup','1','1'),
('cof-f-workgroup-del','删除工作组','default','cof-f-workgroup-del','FUNCTION','cof-fg-workgroup','1','1'),
('cof-f-workgroup-add-emp','添加工作组员工','default','cof-f-workgroup-add-emp','FUNCTION','cof-fg-workgroup','1','1'),
('cof-f-workgroup-rm-emp','移除工作组员工','default','cof-f-workgroup-rm-emp','FUNCTION','cof-fg-workgroup','1','1'),
('cof-f-user-add','添加用户','default','cof-f-user-add','FUNCTION','cof-fg-user','1','1'),
('cof-f-user-edit','编辑用户','default','cof-f-user-edit','FUNCTION','cof-fg-user','1','1'),
('cof-f-user-del','删除用户','default','cof-f-user-del','FUNCTION','cof-fg-user','1','1'),
('cof-f-user-edit-status','启用/禁用用户','default','cof-f-user-edit-status','FUNCTION','cof-fg-user','1','1'),
('cof-f-user-change-pw','修改用户密码','default','cof-f-user-change-pw','FUNCTION','cof-fg-user','1','1'),
('cof-f-user-reset-pw','重置用户密码','default','cof-f-user-reset-pw','FUNCTION','cof-fg-user','1','1'),
('cof-f-role-add','添加角色','default','cof-f-role-add','FUNCTION','cof-fg-role','1','1'),
('cof-f-role-edit','编辑角色','default','cof-f-role-edit','FUNCTION','cof-fg-role','1','1'),
('cof-f-role-del','删除角色','default','cof-f-role-del','FUNCTION','cof-fg-role','1','1'),
('cof-f-role-bind','角色绑定用户','default','cof-f-role-bind','FUNCTION','cof-fg-role','1','1'),
('cof-f-role-unbind','角色与用户解绑','default','cof-f-role-unbind','FUNCTION','cof-fg-role','1','1'),
('cof-f-role-edit-res','角色授权','default','cof-f-role-edit-res','FUNCTION','cof-fg-role','1','1'),
('cof-f-member-add','添加成员','default','cof-f-member-add','FUNCTION','cof-fg-member','1','1'),
('cof-f-member-del','删除成员','default','cof-f-member-del','FUNCTION','cof-fg-member','1','1'),
('cof-f-menu-add','添加菜单','default','cof-f-menu-add','FUNCTION','cof-fg-menu','1','1'),
('cof-f-menu-edit','编辑菜单','default','cof-f-menu-edit','FUNCTION','cof-fg-menu','1','1'),
('cof-f-menu-del','删除菜单','default','cof-f-menu-del','FUNCTION','cof-fg-menu','1','1'),
('cof-f-function-add','添加功能','default','cof-f-function-add','FUNCTION','cof-fg-function','1','1'),
('cof-f-function-edit','编辑功能','default','cof-f-function-edit','FUNCTION','cof-fg-function','1','1'),
('cof-f-function-del','删除功能','default','cof-f-function-del','FUNCTION','cof-fg-function','1','1'),
('cof-f-function-scan','功能扫描','default','cof-f-function-scan','FUNCTION','cof-fg-function','1','1'),
('cof-f-res-group-add','添加资源组','default','cof-f-res-group-add','FUNCTION','cof-fg-res-group','1','1'),
('cof-f-res-group-edit','编辑资源组','default','cof-f-res-group-edit','FUNCTION','cof-fg-res-group','1','1'),
('cof-f-res-group-del','删除资源组','default','cof-f-res-group-del','FUNCTION','cof-fg-res-group','1','1'),

('cof-f-auth-tpl-add','添加权限模板','default','cof-f-auth-tpl-add','FUNCTION','cof-fg-auth-tpl','1','1'),
('cof-f-auth-tpl-edit','编辑权限模板','default','cof-f-auth-tpl-edit','FUNCTION','cof-fg-auth-tpl','1','1'),
('cof-f-auth-tpl-del','删除权限模板','default','cof-f-auth-tpl-del','FUNCTION','cof-fg-auth-tpl','1','1'),
('cof-f-auth-tpl-add-res-group','权限模板添加资源组','default','cof-f-auth-tpl-add-res-group','FUNCTION','cof-fg-auth-tpl','1','1'),
('cof-f-auth-tpl-rm-res-group','权限模板移除资源组','default','cof-f-auth-tpl-rm-res-group','FUNCTION','cof-fg-auth-tpl','1','1'),

('cof-f-role-tpl-grp-add','添加角色模板组','default','cof-f-role-tpl-grp-add','FUNCTION','cof-fg-role-tpl-grp','1','1'),
('cof-f-role-tpl-grp-edit','编辑角色模板组','default','cof-f-role-tpl-grp-edit','FUNCTION','cof-fg-role-tpl-grp','1','1'),
('cof-f-role-tpl-grp-del','删除角色模板组','default','cof-f-role-tpl-grp-del','FUNCTION','cof-fg-role-tpl-grp','1','1'),

('cof-f-role-tpl-add','添加角色模板','default','cof-f-role-tpl-add','FUNCTION','cof-fg-role-tpl','1','1'),
('cof-f-role-tpl-edit','编辑角色模板','default','cof-f-role-tpl-edit','FUNCTION','cof-fg-role-tpl','1','1'),
('cof-f-role-tpl-del','删除角色模板','default','cof-f-role-tpl-del','FUNCTION','cof-fg-role-tpl','1','1'),
('cof-f-role-tpl-edit-res','角色模板授权','default','cof-f-role-tpl-edit-res','FUNCTION','cof-fg-role-tpl','1','1'),

('cof-f-dict-import','导入数据字典','default','cof-f-dict-import','FUNCTION','cof-fg-dict','1','1'),
('cof-f-dict-export','导出数据字典','default','cof-f-dict-export','FUNCTION','cof-fg-dict','1','1'),
('cof-f-dict-download-tpl','下载导入模板','default','cof-f-dict-download-tpl','FUNCTION','cof-fg-dict','1','1'),
('cof-f-dict-type-add','添加字典类型','default','cof-f-dict-type-add','FUNCTION','cof-fg-dict','1','1'),
('cof-f-dict-type-edit','编辑字典类型','default','cof-f-dict-type-edit','FUNCTION','cof-fg-dict','1','1'),
('cof-f-dict-type-del','删除字典类型','default','cof-f-dict-type-del','FUNCTION','cof-fg-dict','1','1'),
('cof-f-dict-entry-add','添加字典项','default','cof-f-dict-entry-add','FUNCTION','cof-fg-dict','1','1'),
('cof-f-dict-entry-edit','编辑字典项','default','cof-f-dict-entry-edit','FUNCTION','cof-fg-dict','1','1'),
('cof-f-dict-entry-del','删除字典项','default','cof-f-dict-entry-del','FUNCTION','cof-fg-dict','1','1'),
('cof-f-optlog-search','查询操作日志','default','cof-f-optlog-search','FUNCTION','cof-fg-audit','1','1'),
('cof-f-optlog-detail-get','查看操作日志详情','default','cof-f-optlog-detail-get','FUNCTION','cof-fg-audit','1','1');

-- 用户
DELETE FROM COF_USER WHERE ID like 'cof-%';
INSERT INTO COF_USER
(ID,EMP_ID,NAME,PASSWORD,TENANT_ID,STATUS,IS_FIXED)
VALUES
('cof-user-sysadmin',null,'sysadmin','$2a$10$imbrJtwe4QGdfwhQjqWqAeHtNmAZpaYgc2iE2bDRg/lVzA3FotTYS','default','ENABLED','1');

-- 角色
DELETE FROM COF_ROLE WHERE ID like 'cof-%';
INSERT INTO COF_ROLE
(ID,NAME,CODE,TENANT_ID,ROLE_TPL_ID,OWNER_TYPE,OWNER_ID,IS_FIXED)
VALUES 
('cof-role-sysadmin','平台管理员','sysadmin','default','cof-tpl-role-admin','platform','platform','1');

-- 用户角色关联
DELETE FROM COF_PARTY_AUTH WHERE ID like 'cof-%';
INSERT INTO COF_PARTY_AUTH
(ID,PARTY_TYPE,PARTY_ID,AUTH_TYPE,AUTH_ID,AUTH_OWNER_TYPE,AUTH_OWNER_ID,IS_FIXED)
VALUES 
('cof-sysadmin-mapping','USER','cof-user-sysadmin','ROLE','cof-role-sysadmin','platform','platform','1');

-- 权限模板
DELETE FROM COF_AUTH_TEMPLATE WHERE ID like 'cof-%';
INSERT INTO COF_AUTH_TEMPLATE
(ID,NAME,TENANT_ID,IS_FIXED)
VALUES 
('cof-tpl-auth-coframe','应用基础平台','default','1');

-- 角色模板
DELETE FROM COF_ROLE_TEMPLATE WHERE ID like 'cof-%';
INSERT INTO COF_ROLE_TEMPLATE
(ID,AUTH_TPL_ID,GROUP_ID,NAME,TENANT_ID,IS_FIXED)
VALUES
('cof-tpl-role-admin','cof-tpl-auth-coframe','cof-tpl-grp-coframe','管理员','default' ,'1'),
('cof-tpl-role-user','cof-tpl-auth-coframe','cof-tpl-grp-coframe','普通用户','default','1');

-- 权限模板与资源组
DELETE FROM COF_AUTH_TPL_RES_GROUP WHERE ID like 'cof-%';
INSERT INTO COF_AUTH_TPL_RES_GROUP
(ID,AUTH_TPL_ID,RES_GROUP_ID,IS_FIXED)
VALUES
('cof-tpl-auth-res-group-1','cof-tpl-auth-coframe','cof-g-platform','1');

-- 资源授权
DELETE FROM COF_AUTH_RES WHERE ID like 'cof-%';
INSERT INTO COF_AUTH_RES
(ID,TYPE,AUTH_TYPE,AUTH_ID,RES_TYPE,RES_ID,IS_FIXED)
VALUES
('cof-role-res-1','MAPPING','ROLE','cof-role-sysadmin','MENU','cof-m-org','1'),
('cof-role-res-2','MAPPING','ROLE','cof-role-sysadmin','MENU','cof-m-auth','1'),
('cof-role-res-3','MAPPING','ROLE','cof-role-sysadmin','MENU','cof-m-sys','1'),
('cof-role-res-4','MAPPING','ROLE','cof-role-sysadmin','MENU','cof-m-audit','1'),
('cof-role-res-5','MAPPING','ROLE','cof-role-sysadmin','MENU','cof-m-org-tree','1'),
('cof-role-res-6','MAPPING','ROLE','cof-role-sysadmin','MENU','cof-m-emp','1'),
('cof-role-res-7','MAPPING','ROLE','cof-role-sysadmin','MENU','cof-m-user','1'),
('cof-role-res-9','MAPPING','ROLE','cof-role-sysadmin','MENU','cof-m-member','1'),
('cof-role-res-10','MAPPING','ROLE','cof-role-sysadmin','MENU','cof-m-auth-tpl','1'),
('cof-role-res-11','MAPPING','ROLE','cof-role-sysadmin','MENU','cof-m-dict','1'),
('cof-role-res-12','MAPPING','ROLE','cof-role-sysadmin','MENU','cof-m-opt-log','1'),
('cof-role-res-13','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-dimension-add','1'),
('cof-role-res-14','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-dimension-edit','1'),
('cof-role-res-15','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-dimension-del','1'),
('cof-role-res-16','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-org-add','1'),
('cof-role-res-17','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-org-edit','1'),
('cof-role-res-18','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-org-del','1'),
('cof-role-res-19','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-org-add-emp','1'),
('cof-role-res-20','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-org-rm-emp','1'),
('cof-role-res-21','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-emp-add','1'),
('cof-role-res-22','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-emp-edit','1'),
('cof-role-res-23','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-emp-del','1'),
('cof-role-res-24','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-position-add','1'),
('cof-role-res-25','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-position-edit','1'),
('cof-role-res-26','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-position-del','1'),
('cof-role-res-27','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-position-add-emp','1'),
('cof-role-res-28','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-position-rm-emp','1'),
('cof-role-res-29','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-workgroup-add','1'),
('cof-role-res-30','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-workgroup-edit','1'),
('cof-role-res-31','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-workgroup-del','1'),
('cof-role-res-32','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-workgroup-add-emp','1'),
('cof-role-res-33','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-workgroup-rm-emp','1'),
('cof-role-res-34','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-user-add','1'),
('cof-role-res-35','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-user-edit','1'),
('cof-role-res-36','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-user-del','1'),
('cof-role-res-37','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-user-edit-status','1'),
('cof-role-res-38','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-user-change-pw','1'),
('cof-role-res-39','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-user-reset-pw','1'),
('cof-role-res-40','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-role-add','1'),
('cof-role-res-41','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-role-edit','1'),
('cof-role-res-42','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-role-del','1'),
('cof-role-res-43','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-role-bind','1'),
('cof-role-res-44','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-role-unbind','1'),
('cof-role-res-45','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-role-edit-res','1'),
('cof-role-res-46','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-member-add','1'),
('cof-role-res-47','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-member-del','1'),
('cof-role-res-48','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-menu-add','1'),
('cof-role-res-49','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-menu-edit','1'),
('cof-role-res-50','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-menu-del','1'),
('cof-role-res-51','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-function-add','1'),
('cof-role-res-52','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-function-edit','1'),
('cof-role-res-53','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-function-del','1'),
('cof-role-res-54','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-res-group-add','1'),
('cof-role-res-55','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-res-group-edit','1'),
('cof-role-res-56','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-res-group-del','1'),
('cof-role-res-57','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-auth-tpl-add','1'),
('cof-role-res-58','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-auth-tpl-edit','1'),
('cof-role-res-59','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-auth-tpl-del','1'),
('cof-role-res-60','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-auth-tpl-add-res-group','1'),
('cof-role-res-61','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-auth-tpl-rm-res-group','1'),
('cof-role-res-62','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-role-tpl-add','1'),
('cof-role-res-63','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-role-tpl-edit','1'),
('cof-role-res-64','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-role-tpl-del','1'),
('cof-role-res-65','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-role-tpl-edit-res','1'),
('cof-role-res-66','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-dict-import','1'),
('cof-role-res-67','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-dict-export','1'),
('cof-role-res-68','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-dict-download-tpl','1'),
('cof-role-res-69','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-dict-type-add','1'),
('cof-role-res-70','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-dict-type-edit','1'),
('cof-role-res-71','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-dict-type-del','1'),
('cof-role-res-72','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-dict-entry-add','1'),
('cof-role-res-73','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-dict-entry-edit','1'),
('cof-role-res-74','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-dict-entry-del','1'),
('cof-role-res-75','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-optlog-search','1'),
('cof-role-res-76','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-optlog-detail-get','1'),
('cof-roletpl-res-77','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','MENU','cof-m-org','1'),
('cof-roletpl-res-78','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','MENU','cof-m-auth','1'),
('cof-roletpl-res-79','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','MENU','cof-m-sys','1'),
('cof-roletpl-res-80','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','MENU','cof-m-audit','1'),
('cof-roletpl-res-81','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','MENU','cof-m-org-tree','1'),
('cof-roletpl-res-82','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','MENU','cof-m-emp','1'),
('cof-roletpl-res-83','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','MENU','cof-m-user','1'),
('cof-roletpl-res-85','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','MENU','cof-m-member','1'),
('cof-roletpl-res-86','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','MENU','cof-m-auth-tpl','1'),
('cof-roletpl-res-87','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','MENU','cof-m-dict','1'),
('cof-roletpl-res-88','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','MENU','cof-m-opt-log','1'),
('cof-roletpl-res-89','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-dimension-add','1'),
('cof-roletpl-res-90','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-dimension-edit','1'),
('cof-roletpl-res-91','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-dimension-del','1'),
('cof-roletpl-res-92','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-org-add','1'),
('cof-roletpl-res-93','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-org-edit','1'),
('cof-roletpl-res-94','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-org-del','1'),
('cof-roletpl-res-95','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-org-add-emp','1'),
('cof-roletpl-res-96','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-org-rm-emp','1'),
('cof-roletpl-res-97','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-emp-add','1'),
('cof-roletpl-res-98','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-emp-edit','1'),
('cof-roletpl-res-99','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-emp-del','1'),
('cof-roletpl-res-100','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-position-add','1'),
('cof-roletpl-res-101','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-position-edit','1'),
('cof-roletpl-res-102','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-position-del','1'),
('cof-roletpl-res-103','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-position-add-emp','1'),
('cof-roletpl-res-104','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-position-rm-emp','1'),
('cof-roletpl-res-105','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-workgroup-add','1'),
('cof-roletpl-res-106','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-workgroup-edit','1'),
('cof-roletpl-res-107','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-workgroup-del','1'),
('cof-roletpl-res-108','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-workgroup-add-emp','1'),
('cof-roletpl-res-109','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-workgroup-rm-emp','1'),
('cof-roletpl-res-110','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-user-add','1'),
('cof-roletpl-res-111','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-user-edit','1'),
('cof-roletpl-res-112','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-user-del','1'),
('cof-roletpl-res-113','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-user-edit-status','1'),
('cof-roletpl-res-114','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-user-change-pw','1'),
('cof-roletpl-res-115','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-user-reset-pw','1'),
('cof-roletpl-res-116','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-role-add','1'),
('cof-roletpl-res-117','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-role-edit','1'),
('cof-roletpl-res-118','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-role-del','1'),
('cof-roletpl-res-119','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-role-bind','1'),
('cof-roletpl-res-120','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-role-unbind','1'),
('cof-roletpl-res-121','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-role-edit-res','1'),
('cof-roletpl-res-122','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-member-add','1'),
('cof-roletpl-res-123','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-member-del','1'),
('cof-roletpl-res-124','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-menu-add','1'),
('cof-roletpl-res-125','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-menu-edit','1'),
('cof-roletpl-res-126','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-menu-del','1'),
('cof-roletpl-res-127','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-function-add','1'),
('cof-roletpl-res-128','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-function-edit','1'),
('cof-roletpl-res-129','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-function-del','1'),
('cof-roletpl-res-130','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-res-group-add','1'),
('cof-roletpl-res-131','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-res-group-edit','1'),
('cof-roletpl-res-132','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-res-group-del','1'),
('cof-roletpl-res-133','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-auth-tpl-add','1'),
('cof-roletpl-res-134','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-auth-tpl-edit','1'),
('cof-roletpl-res-135','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-auth-tpl-del','1'),
('cof-roletpl-res-136','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-auth-tpl-add-res-group','1'),
('cof-roletpl-res-137','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-auth-tpl-rm-res-group','1'),
('cof-roletpl-res-138','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-role-tpl-add','1'),
('cof-roletpl-res-139','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-role-tpl-edit','1'),
('cof-roletpl-res-140','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-role-tpl-del','1'),
('cof-roletpl-res-141','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-role-tpl-edit-res','1'),
('cof-roletpl-res-142','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-dict-import','1'),
('cof-roletpl-res-143','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-dict-export','1'),
('cof-roletpl-res-144','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-dict-download-tpl','1'),
('cof-roletpl-res-145','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-dict-type-add','1'),
('cof-roletpl-res-146','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-dict-type-edit','1'),
('cof-roletpl-res-147','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-dict-type-del','1'),
('cof-roletpl-res-148','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-dict-entry-add','1'),
('cof-roletpl-res-149','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-dict-entry-edit','1'),
('cof-roletpl-res-150','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-dict-entry-del','1'),
('cof-roletpl-res-151','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-optlog-search','1'),
('cof-roletpl-res-152','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-optlog-detail-get','1'),
('cof-roletpl-res-153','MAPPING','ROLE-TEMPLATE','cof-tpl-role-user','MENU','cof-m-org','1'),
('cof-roletpl-res-154','MAPPING','ROLE-TEMPLATE','cof-tpl-role-user','MENU','cof-m-org-tree','1'),
('cof-roletpl-res-155','MAPPING','ROLE-TEMPLATE','cof-tpl-role-user','MENU','cof-m-sys','1'),
('cof-roletpl-res-156','MAPPING','ROLE-TEMPLATE','cof-tpl-role-user','MENU','cof-m-dict','1'),
('cof-roletpl-res-157','MAPPING','ROLE-TEMPLATE','cof-tpl-role-user','FUNCTION','cof-f-user-change-pw','1'),
('cof-role-res-158','MAPPING','ROLE','cof-role-sysadmin','MENU','cof-m-resouce-right','1'),
('cof-role-res-159','MAPPING','ROLE','cof-role-sysadmin','MENU','cof-m-role-template','1'),
('cof-roletpl-res-160','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','MENU','cof-m-resouce-right','1'),
('cof-roletpl-res-161','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','MENU','cof-m-role-template','1'),
('cof-role-res-162','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-role-tpl-grp-add','1'),
('cof-role-res-163','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-role-tpl-grp-edit','1'),
('cof-role-res-164','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-role-tpl-grp-del','1'),
('cof-roletpl-res-165','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-role-tpl-grp-add','1'),
('cof-roletpl-res-166','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-role-tpl-grp-edit','1'),
('cof-roletpl-res-167','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-role-tpl-grp-del','1'),
('cof-roletpl-res-168','MAPPING','ROLE','cof-role-sysadmin','FUNCTION','cof-f-function-scan','1'),
('cof-roletpl-res-169','MAPPING','ROLE-TEMPLATE','cof-tpl-role-admin','FUNCTION','cof-f-function-scan','1');

-- 数据字典类型
DELETE FROM COF_DICT_TYPE WHERE ID like 'cof-%';
INSERT INTO COF_DICT_TYPE	
(ID,CODE,NAME,TENANT_ID,PARENT_ID,LOCALE,SORT_NO,IS_LEAF,TREE_LEVEL,SEQ,IS_FIXED)
VALUES
('cof-rules-enable','cof-rules-enable','访问规则是否生效','default',null,'zh',1,'1',1,'.1.','0'),
('cof-rules-type','cof-rules-type','访问规则类型','default',null,'zh',2,'1',1,'.2.','0'),
('cof-apptype','cof-apptype','应用类型','default',null,'zh',3,'1',1,'.3.','0'),
('cof-authmode','cof-authmode','认证模式','default',null,'zh',4,'1',1,'.4.','0'),
('cof-cardtype','cof-cardtype','证件类型','default',null,'zh',5,'1',1,'.5.','0'),
('cof-emplevel','cof-emplevel','人员级别','default',null,'zh',6,'1',1,'.6.','0'),
('cof-empstatus','cof-empstatus','人员状态','default',null,'zh',7,'1',1,'.7.','0'),
('cof-functype','cof-functype','功能类型','default',null,'zh',8,'1',1,'.8.','0'),
('cof-gender','cof-gender','性别','default',null,'zh',9,'1',1,'.9.','0'),
('cof-operstatus','cof-operstatus','操作员状态','default',null,'zh',10,'1',1,'.10.','0'),
('cof-orgdegree','cof-orgdegree','机构等级','default',null,'zh',11,'1',1,'.11.','0'),
('cof-orgstatus','cof-orgstatus','机构状态','default',null,'zh',12,'1',1,'.12.','0'),
('cof-orgtype','cof-orgtype','机构类型','default',null,'zh',13,'1',1,'.13.','0'),
('cof-partyvisage','cof-partyvisage','政治面貌','default',null,'zh',14,'1',1,'.14.','0'),
('cof-skinlayout','cof-skinlayout','菜单布局','default',null,'zh',15,'1',1,'.15.','0'),
('cof-userstatus','cof-userstatus','用户状态','default',null,'zh',16,'1',1,'.16.','0'),
('cof-yesorno','cof-yesorno','开关','default',null,'zh',17,'1',1,'.17.','0');

-- 数据字典项
DELETE FROM COF_DICT_ENTRY WHERE ID like 'cof-%';
INSERT INTO COF_DICT_ENTRY
(ID,CODE,DICT_TYPE_ID,NAME,TENANT_ID,PARENT_ID,LOCALE,STATUS,SORT_NO,IS_LEAF,TREE_LEVEL,SEQ,IS_FIXED)
VALUES
('cof-apptype-0','0','cof-apptype','本地','default',null,'zh',null,1,'1',1,'.1.','0'),
('cof-apptype-1','1','cof-apptype','远程','default',null,'zh',null,2,'1',1,'.2.','0'),
('cof-authmode-ldap','ldap','cof-authmode','LDAP认证','default',null,'zh',null,3,'1',1,'.3.','0'),
('cof-authmode-local','local','cof-authmode','本地密码认证','default',null,'zh',null,4,'1',1,'.4.','0'),
('cof-authmode-portal','portal','cof-authmode','Portal认证','default',null,'zh',null,5,'1',1,'.5.','0'),
('cof-authmode-remote','remote','cof-authmode','远程认证','default',null,'zh',null,6,'1',1,'.6.','0'),
('cof-cardtype-id','id','cof-cardtype','身份证','default',null,'zh',null,7,'1',1,'.7.','0'),
('cof-cardtype-junguan','junguan','cof-cardtype','军官证','default',null,'zh',null,8,'1',1,'.8.','0'),
('cof-cardtype-passport','passport','cof-cardtype','护照','default',null,'zh',null,9,'1',1,'.9.','0'),
('cof-cardtype-student','student','cof-cardtype','学生证','default',null,'zh',null,10,'1',1,'.10.','0'),
('cof-cardtype-zhanzhu','zhanzhu','cof-cardtype','暂住证','default',null,'zh',null,11,'1',1,'.11.','0'),
('cof-EMPLEVEL-level1','level1','cof-EMPLEVEL','级别1','default',null,'zh',null,12,'1',1,'.12.','0'),
('cof-EMPLEVEL-level2','level2','cof-EMPLEVEL','级别2','default',null,'zh',null,13,'1',1,'.13.','0'),
('cof-EMPLEVEL-level3','level3','cof-EMPLEVEL','级别3','default',null,'zh',null,14,'1',1,'.14.','0'),
('cof-empstatus-leave','leave','cof-empstatus','离职','default',null,'zh',null,15,'1',1,'.15.','0'),
('cof-empstatus-off','off','cof-empstatus','退休','default',null,'zh',null,16,'1',1,'.16.','0'),
('cof-empstatus-on','on','cof-empstatus','在岗','default',null,'zh',null,17,'1',1,'.17.','0'),
('cof-empstatus-wait','wait','cof-empstatus','待岗','default',null,'zh',null,18,'1',1,'.18.','0'),
('cof-gender-f','f','cof-gender','女','default',null,'zh',null,19,'1',1,'.19.','0'),
('cof-gender-m','m','cof-gender','男','default',null,'zh',null,20,'1',1,'.20.','0'),
('cof-gender-n','n','cof-gender','未知','default',null,'zh',null,21,'1',1,'.21.','0'),
('cof-operstatus-running','running','cof-operstatus','正常','default',null,'zh',null,22,'1',1,'.22.','0'),
('cof-operstatus-stop','stop','cof-operstatus','不正常','default',null,'zh',null,23,'1',1,'.23.','0'),
('cof-orgdegree-branch','branch','cof-orgdegree','分行','default',null,'zh',null,24,'1',1,'.24.','0'),
('cof-orgdegree-hq','hq','cof-orgdegree','总行','default',null,'zh',null,25,'1',1,'.25.','0'),
('cof-orgdegree-oversea','oversea','cof-orgdegree','海外分行','default',null,'zh',null,26,'1',1,'.26.','0'),
('cof-orgstatus-cancel','cancel','cof-orgstatus','注销','default',null,'zh',null,27,'1',1,'.27.','0'),
('cof-orgstatus-running','running','cof-orgstatus','正常','default',null,'zh',null,28,'1',1,'.28.','0'),
('cof-orgtype-b','b','cof-orgtype','分公司','default',null,'zh',null,29,'1',1,'.29.','0'),
('cof-orgtype-bd','bd','cof-orgtype','分公司部门','default',null,'zh',null,30,'1',1,'.30.','0'),
('cof-orgtype-h','h','cof-orgtype','总公司','default',null,'zh',null,31,'1',1,'.31.','0'),
('cof-orgtype-hd','hd','cof-orgtype','总公司部门','default',null,'zh',null,32,'1',1,'.32.','0'),
('cof-partyvisage-comsomol','comsomol','cof-partyvisage','团员','default',null,'zh',null,33,'1',1,'.33.','0'),
('cof-partyvisage-crowd','crowd','cof-partyvisage','群众','default',null,'zh',null,34,'1',1,'.34.','0'),
('cof-partyvisage-partymember','partymember','cof-partyvisage','党员','default',null,'zh',null,35,'1',1,'.35.','0'),
('cof-skinlayout-0','0','cof-skinlayout','否','default',null,'zh',null,36,'1',1,'.36.','0'),
('cof-skinlayout-1','1','cof-skinlayout','是','default',null,'zh',null,37,'1',1,'.37.','0'),
('cof-skinlayout-allow','allow','cof-skinlayout','允许访问','default',null,'zh',null,38,'1',1,'.38.','0'),
('cof-skinlayout-default','default','cof-skinlayout','默认布局','default',null,'zh',null,39,'1',1,'.39.','0'),
('cof-skinlayout-flow','flow','cof-skinlayout','页面流','default',null,'zh',null,40,'1',1,'.40.','0'),
('cof-skinlayout-form','form','cof-skinlayout','表单','default',null,'zh',null,41,'1',1,'.41.','0'),
('cof-skinlayout-menubar','menubar','cof-skinlayout','menubar','default',null,'zh',null,42,'1',1,'.42.','0'),
('cof-skinlayout-N','N','cof-skinlayout','不生效','default',null,'zh',null,43,'1',1,'.43.','0'),
('cof-skinlayout-navtree','navtree','cof-skinlayout','navtree','default',null,'zh',null,44,'1',1,'.44.','0'),
('cof-skinlayout-other','other','cof-skinlayout','其他','default',null,'zh',null,45,'1',1,'.45.','0'),
('cof-skinlayout-outlookmenu','outlookmenu','cof-skinlayout','outlookmenu','default',null,'zh',null,46,'1',1,'.46.','0'),
('cof-skinlayout-outlooktree','outlooktree','cof-skinlayout','outlooktree','default',null,'zh',null,47,'1',1,'.47.','0'),
('cof-skinlayout-page','page','cof-skinlayout','页面','default',null,'zh',null,48,'1',1,'.48.','0'),
('cof-skinlayout-prohibit','prohibit','cof-skinlayout','禁止访问','default',null,'zh',null,49,'1',1,'.49.','0'),
('cof-skinlayout-startprocess','startprocess','cof-skinlayout','启动流程','default',null,'zh',null,50,'1',1,'.50.','0'),
('cof-skinlayout-view','view','cof-skinlayout','视图','default',null,'zh',null,51,'1',1,'.51.','0'),
('cof-skinlayout-win7','win7','cof-skinlayout','win7','default',null,'zh',null,52,'1',1,'.52.','0'),
('cof-skinlayout-Y','Y','cof-skinlayout','生效','default',null,'zh',null,53,'1',1,'.53.','0'),
('cof-userstatus-0','0','cof-userstatus','挂起','default',null,'zh',null,54,'1',1,'.54.','0'),
('cof-userstatus-1','1','cof-userstatus','正常','default',null,'zh',null,55,'1',1,'.55.','0'),
('cof-userstatus-2','2','cof-userstatus','锁定','default',null,'zh',null,56,'1',1,'.56.','0'),
('cof-userstatus-9','9','cof-userstatus','注销','default',null,'zh',null,57,'1',1,'.57.','0');
