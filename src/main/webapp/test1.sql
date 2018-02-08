/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50544
Source Host           : localhost:3306
Source Database       : test1

Target Server Type    : MYSQL
Target Server Version : 50544
File Encoding         : 65001

Date: 2018-02-09 00:50:51
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_acl
-- ----------------------------
DROP TABLE IF EXISTS `sys_acl`;
CREATE TABLE `sys_acl` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '权限id',
  `code` varchar(20) NOT NULL DEFAULT '' COMMENT '权限码',
  `name` varchar(20) NOT NULL DEFAULT '' COMMENT '权限名称',
  `acl_module_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '权限所在的权限模块id',
  `url` varchar(100) NOT NULL DEFAULT '' COMMENT '请求的url，可以填正则表达式',
  `type` int(10) NOT NULL DEFAULT '1' COMMENT '类型：1：菜单，2：按钮，3：其他',
  `status` int(10) NOT NULL DEFAULT '1' COMMENT '状态：1：正常，0：冻结',
  `seq` int(11) NOT NULL DEFAULT '0' COMMENT '权限在当前模块的顺序，由小到大',
  `remark` varchar(200) DEFAULT '' COMMENT '备注',
  `operator` varchar(20) NOT NULL DEFAULT '' COMMENT '最后一次操作者',
  `operate_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '最后一次修改时间',
  `operate_ip` varchar(20) NOT NULL DEFAULT '' COMMENT '最后一次更新的ip地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_acl
-- ----------------------------
INSERT INTO `sys_acl` VALUES ('1', '20180202233730', '部门树', '4', '/sys/dept/tree.json', '1', '1', '1', '222', 'ADMINN-system-update', '2018-02-02 23:37:45', '127.0.0.1');
INSERT INTO `sys_acl` VALUES ('2', '20180204230548', '部门主页面', '4', '/sys/dept/dept.page', '1', '1', '2', 'shan', 'ADMIN-system-update', '2018-02-07 15:57:09', '127.0.0.1');
INSERT INTO `sys_acl` VALUES ('3', '20180204231802', '主页面', '5', '/admin/index.page', '1', '1', '1', '13213', 'ADMIN-system-update', '2018-02-07 15:58:53', '127.0.0.1');
INSERT INTO `sys_acl` VALUES ('4', '20180206145228', '角色列表', '7', '/sys/role/role.page', '1', '1', '1', '1111', 'ADMIN-system-update', '2018-02-07 15:58:08', '127.0.0.1');
INSERT INTO `sys_acl` VALUES ('5', '20180206145511', '测试1-1', '3', '1fafafdsf', '1', '1', '1', '1111', 'ADMIN-system-update', '2018-02-07 15:58:23', '127.0.0.1');
INSERT INTO `sys_acl` VALUES ('6', '20180208151038', '测试权限点日志', '8', '1rwerwer', '1', '1', '1', '33333', 'ADMIN-system-update', '2018-02-08 15:11:03', '127.0.0.1');

-- ----------------------------
-- Table structure for sys_acl_module
-- ----------------------------
DROP TABLE IF EXISTS `sys_acl_module`;
CREATE TABLE `sys_acl_module` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '权限模块名称',
  `name` varchar(20) NOT NULL DEFAULT '' COMMENT '权限模块名称',
  `parent_id` int(11) NOT NULL DEFAULT '0' COMMENT '上级模块的id',
  `level` varchar(200) NOT NULL DEFAULT '' COMMENT '权限模块层级',
  `seq` int(11) NOT NULL DEFAULT '0' COMMENT '权限模块在当前层级的次序，由小到大',
  `status` int(10) NOT NULL DEFAULT '1' COMMENT '权限模块的状态，1：正常，0：冻结',
  `remark` varchar(200) DEFAULT '' COMMENT '备注',
  `operator` varchar(20) NOT NULL DEFAULT '' COMMENT '操作者',
  `operate_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '最后一次操作时间',
  `operate_ip` varchar(20) NOT NULL DEFAULT '' COMMENT '最后一次更新操作的ip',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_acl_module
-- ----------------------------
INSERT INTO `sys_acl_module` VALUES ('1', '后台权限管理', '0', '0', '1', '1', '不错111', 'ADMIN', '2018-02-07 15:55:20', '127.0.0.1');
INSERT INTO `sys_acl_module` VALUES ('2', '订单管理', '0', '0', '2', '1', '111', 'ADMINN', '2018-02-02 20:54:15', '127.0.0.1');
INSERT INTO `sys_acl_module` VALUES ('3', '公告管理', '0', '0', '3', '1', '1111', 'ADMINN', '2018-02-02 20:54:38', '127.0.0.1');
INSERT INTO `sys_acl_module` VALUES ('4', '用户管理模块权限管理', '1', '0.1', '1', '1', '1111', 'ADMIN', '2018-02-07 15:55:41', '127.0.0.1');
INSERT INTO `sys_acl_module` VALUES ('5', '主页面权限管理', '1', '0.1', '1', '1', '1111', 'ADMIN', '2018-02-07 15:56:30', '127.0.0.1');
INSERT INTO `sys_acl_module` VALUES ('7', '角色模块权限管理', '1', '0.1', '1', '1', '1111', 'ADMIN', '2018-02-07 15:57:59', '127.0.0.1');
INSERT INTO `sys_acl_module` VALUES ('8', '测试日志', '2', '0.2', '1', '1', '111112222', 'ADMIN', '2018-02-08 15:10:15', '127.0.0.1');

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL DEFAULT '' COMMENT '部门名称',
  `parent_id` int(11) NOT NULL DEFAULT '0' COMMENT '上级部门的id',
  `level` varchar(200) NOT NULL DEFAULT '' COMMENT '部门层级',
  `seq` int(11) NOT NULL DEFAULT '0' COMMENT '部门在当前层级的次序，由小到大',
  `remark` varchar(200) DEFAULT '' COMMENT '备注',
  `operator` varchar(20) NOT NULL DEFAULT '' COMMENT '操作者',
  `operate_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '最后一次操作时间',
  `operate_ip` varchar(20) NOT NULL DEFAULT '' COMMENT '最后一次更新操作的ip',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES ('1', '技术部', '0', '0', '1', '技术部', 'system-update', '2018-01-30 22:18:51', '127.0.0.1');
INSERT INTO `sys_dept` VALUES ('2', '人力资源部', '0', '0', '1', '人力资源部', 'system', '2018-01-29 16:41:56', '127.0.0.1');
INSERT INTO `sys_dept` VALUES ('3', '前端开发', '1', '0.1', '1', '前端开发', 'system-update', '2018-01-31 15:26:26', '127.0.0.1');
INSERT INTO `sys_dept` VALUES ('4', '后端开发', '1', '0.1', '3', '后端开发', 'system-update', '2018-01-31 15:26:43', '127.0.0.1');
INSERT INTO `sys_dept` VALUES ('5', '终端开发', '1', '0.1', '2', '开发终端', 'system-update', '2018-01-31 15:26:34', '127.0.0.1');
INSERT INTO `sys_dept` VALUES ('6', 'js开发', '7', '0.1.3.7', '1', 'js开发', 'system-update', '2018-01-30 22:26:54', '127.0.0.1');
INSERT INTO `sys_dept` VALUES ('7', 'js绑定事件的开发', '3', '0.1.3', '1', 'js绑定事件', 'system-update', '2018-01-30 23:20:05', '127.0.0.1');
INSERT INTO `sys_dept` VALUES ('8', '数据库设计', '1', '0.1', '1', '数据库', 'system-update', '2018-01-31 15:26:48', '127.0.0.1');
INSERT INTO `sys_dept` VALUES ('9', '产品部', '0', '0', '4', '1111111222', 'ADMIN', '2018-02-09 00:27:14', '127.0.0.1');

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `type` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '权限更新类型：1：部门，2：用户，3：权限模块，4：权限，5：角色，6：角色用户关系，7：角色权限关系',
  `target_id` int(10) unsigned NOT NULL COMMENT 'type表里面的id',
  `old_value` text COMMENT '旧值',
  `new_value` text COMMENT '新值',
  `operator` varchar(20) NOT NULL DEFAULT '' COMMENT '操作者',
  `operate_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '最后一次修改时间',
  `operate_ip` varchar(20) NOT NULL DEFAULT '' COMMENT '最后一次修改的ip地址',
  `status` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '当前是否复原过，0：没有，1：复原过',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_log
-- ----------------------------
INSERT INTO `sys_log` VALUES ('1', '1', '9', '', '{\"id\":9,\"name\":\"产品部\",\"parentId\":0,\"level\":\"0\",\"seq\":4,\"remark\":\"111\",\"operator\":\"ADMIN-system\",\"operateTime\":1518073533165,\"operateIp\":\"127.0.0.1\"}', 'ADMIN', '2018-02-08 15:05:33', '127.0.0.1', '1');
INSERT INTO `sys_log` VALUES ('2', '1', '9', '{\"id\":9,\"name\":\"产品部\",\"parentId\":0,\"level\":\"0\",\"seq\":4,\"remark\":\"111\",\"operator\":\"ADMIN-system\",\"operateTime\":1518073533000,\"operateIp\":\"127.0.0.1\"}', '{\"id\":9,\"name\":\"产品部\",\"parentId\":0,\"level\":\"0\",\"seq\":4,\"remark\":\"1111111222\",\"operator\":\"ADMIN-system-update\",\"operateTime\":1518073571312,\"operateIp\":\"127.0.0.1\"}', 'ADMIN', '2018-02-08 15:06:11', '127.0.0.1', '1');
INSERT INTO `sys_log` VALUES ('3', '2', '6', '', '{\"id\":6,\"username\":\"王老五\",\"telephone\":\"13202126903\",\"mail\":\"wanglaowu@com\",\"password\":\"E10ADC3949BA59ABBE56E057F20F883E\",\"deptId\":1,\"status\":1,\"remark\":\"11111\",\"operator\":\"ADMIN\",\"operateTime\":1518073679880,\"operateIp\":\"127.0.0.1\"}', 'ADMIN', '2018-02-08 15:07:59', '127.0.0.1', '1');
INSERT INTO `sys_log` VALUES ('4', '2', '6', '{\"id\":6,\"username\":\"王老五\",\"telephone\":\"13202126903\",\"mail\":\"wanglaowu@com\",\"password\":\"E10ADC3949BA59ABBE56E057F20F883E\",\"deptId\":1,\"status\":1,\"remark\":\"11111\",\"operator\":\"ADMIN\",\"operateTime\":1518073679000,\"operateIp\":\"127.0.0.1\"}', '{\"id\":6,\"username\":\"王老五\",\"telephone\":\"13202126903\",\"mail\":\"wanglaowu@com\",\"deptId\":9,\"status\":1,\"remark\":\"11111\",\"operator\":\"ADMIN-system-update\",\"operateTime\":1518073704979,\"operateIp\":\"127.0.0.1\"}', 'ADMIN', '2018-02-08 15:08:25', '127.0.0.1', '1');
INSERT INTO `sys_log` VALUES ('5', '3', '8', '', '{\"id\":8,\"name\":\"测试日志\",\"parentId\":2,\"level\":\"0.2\",\"seq\":1,\"status\":1,\"remark\":\"11111\",\"operator\":\"ADMIN\",\"operateTime\":1518073804726,\"operateIp\":\"127.0.0.1\"}', 'ADMIN', '2018-02-08 15:10:04', '127.0.0.1', '1');
INSERT INTO `sys_log` VALUES ('6', '3', '8', '{\"id\":8,\"name\":\"测试日志\",\"parentId\":2,\"level\":\"0.2\",\"seq\":1,\"status\":1,\"remark\":\"11111\",\"operator\":\"ADMIN\",\"operateTime\":1518073804000,\"operateIp\":\"127.0.0.1\"}', '{\"id\":8,\"name\":\"测试日志\",\"parentId\":2,\"level\":\"0.2\",\"seq\":1,\"status\":1,\"remark\":\"111112222\",\"operator\":\"ADMIN\",\"operateTime\":1518073815688,\"operateIp\":\"127.0.0.1\"}', 'ADMIN', '2018-02-08 15:10:15', '127.0.0.1', '1');
INSERT INTO `sys_log` VALUES ('7', '4', '6', '', '{\"id\":6,\"code\":\"20180208151038\",\"name\":\"测试权限点日志\",\"aclModuleId\":1,\"url\":\"1rwerwer\",\"type\":1,\"status\":1,\"seq\":1,\"remark\":\"33333\",\"operator\":\"ADMIN-system\",\"operateTime\":1518073838463,\"operateIp\":\"127.0.0.1\"}', 'ADMIN', '2018-02-08 15:10:38', '127.0.0.1', '1');
INSERT INTO `sys_log` VALUES ('8', '4', '6', '{\"id\":6,\"code\":\"20180208151038\",\"name\":\"测试权限点日志\",\"aclModuleId\":1,\"url\":\"1rwerwer\",\"type\":1,\"status\":1,\"seq\":1,\"remark\":\"33333\",\"operator\":\"ADMIN-system\",\"operateTime\":1518073838000,\"operateIp\":\"127.0.0.1\"}', '{\"id\":6,\"name\":\"测试权限点日志\",\"aclModuleId\":8,\"url\":\"1rwerwer\",\"type\":1,\"status\":1,\"seq\":1,\"remark\":\"33333\",\"operator\":\"ADMIN-system-update\",\"operateTime\":1518073863869,\"operateIp\":\"127.0.0.1\"}', 'ADMIN', '2018-02-08 15:11:03', '127.0.0.1', '1');
INSERT INTO `sys_log` VALUES ('9', '1', '9', '{\"id\":9,\"name\":\"产品部\",\"parentId\":0,\"level\":\"0\",\"seq\":4,\"remark\":\"1111111222\",\"operator\":\"ADMIN-system-update\",\"operateTime\":1518073571000,\"operateIp\":\"127.0.0.1\"}', '{\"id\":9,\"name\":\"产品部1\",\"parentId\":0,\"level\":\"0\",\"seq\":4,\"remark\":\"1111111222\",\"operator\":\"ADMIN-system-update\",\"operateTime\":1518107101742,\"operateIp\":\"127.0.0.1\"}', 'ADMIN', '2018-02-09 00:25:01', '127.0.0.1', '1');
INSERT INTO `sys_log` VALUES ('10', '1', '9', '{\"id\":9,\"name\":\"产品部1\",\"parentId\":0,\"level\":\"0\",\"seq\":4,\"remark\":\"1111111222\",\"operator\":\"ADMIN-system-update\",\"operateTime\":1518107101000,\"operateIp\":\"127.0.0.1\"}', '{\"id\":9,\"name\":\"产品部\",\"parentId\":0,\"level\":\"0\",\"seq\":4,\"remark\":\"1111111222\",\"operator\":\"ADMIN\",\"operateTime\":1518107234399,\"operateIp\":\"127.0.0.1\"}', 'ADMIN', '2018-02-09 00:27:14', '127.0.0.1', '1');
INSERT INTO `sys_log` VALUES ('11', '7', '1', '[1]', '[1,4]', 'ADMIN', '2018-02-09 00:27:32', '127.0.0.1', '1');
INSERT INTO `sys_log` VALUES ('12', '7', '1', '[1,4]', '[1]', 'ADMIN', '2018-02-09 00:27:41', '127.0.0.1', '1');
INSERT INTO `sys_log` VALUES ('13', '6', '2', '[1,2,3]', '[3]', 'ADMIN', '2018-02-09 00:28:24', '127.0.0.1', '1');
INSERT INTO `sys_log` VALUES ('14', '6', '2', '[1,2,3,3]', '[3]', 'ADMIN', '2018-02-09 00:28:46', '127.0.0.1', '1');
INSERT INTO `sys_log` VALUES ('15', '6', '2', '[1,2,3,3,3]', '[1,2]', 'ADMIN', '2018-02-09 00:29:48', '127.0.0.1', '1');
INSERT INTO `sys_log` VALUES ('16', '6', '2', '[1,2,3,3,3,1,2]', '[1,2]', 'ADMIN', '2018-02-09 00:37:09', '127.0.0.1', '1');
INSERT INTO `sys_log` VALUES ('17', '6', '2', '[1,2]', '[1,2]', 'ADMIN', '2018-02-09 00:39:13', '127.0.0.1', '1');
INSERT INTO `sys_log` VALUES ('18', '6', '2', '[1,2]', '[]', 'ADMIN', '2018-02-09 00:39:23', '127.0.0.1', '1');
INSERT INTO `sys_log` VALUES ('19', '6', '2', '[]', '[1,2]', 'ADMIN', '2018-02-09 00:39:43', '127.0.0.1', '1');
INSERT INTO `sys_log` VALUES ('20', '6', '2', '[1,2]', '[]', 'ADMIN', '2018-02-09 00:40:02', '127.0.0.1', '1');
INSERT INTO `sys_log` VALUES ('21', '6', '2', '[]', '[1,2]', 'ADMIN', '2018-02-09 00:40:22', '127.0.0.1', '1');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `name` varchar(20) NOT NULL DEFAULT '' COMMENT '角色名称',
  `type` int(11) NOT NULL DEFAULT '0' COMMENT '角色的类型，1：管理员角色,2：其他',
  `status` int(10) NOT NULL DEFAULT '1' COMMENT '状态：1:可用，0:冻结',
  `remark` varchar(200) DEFAULT '' COMMENT '备注',
  `operator` varchar(20) NOT NULL DEFAULT '' COMMENT '最后一次操作者',
  `operate_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '最后一次操作时间',
  `operate_ip` varchar(20) NOT NULL DEFAULT '' COMMENT '最后一次更新ip地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', '产品管理员', '1', '1', '11111', 'ADMINN-system', '2018-02-03 21:24:56', '127.0.0.1');
INSERT INTO `sys_role` VALUES ('2', '用户管理员', '1', '1', '11111', 'ADMIN-system', '2018-02-07 16:00:35', '127.0.0.1');

-- ----------------------------
-- Table structure for sys_role_acl
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_acl`;
CREATE TABLE `sys_role_acl` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `role_id` int(10) unsigned NOT NULL,
  `acl_id` int(10) unsigned NOT NULL,
  `operator` varchar(20) NOT NULL DEFAULT '' COMMENT '最后一次操作者',
  `operator_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '最后一次操作时间',
  `operator_ip` varchar(20) NOT NULL DEFAULT '' COMMENT '最后一次操作的ip地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role_acl
-- ----------------------------
INSERT INTO `sys_role_acl` VALUES ('17', '2', '1', 'ADMIN-system', '2018-02-09 00:40:22', '127.0.0.1');
INSERT INTO `sys_role_acl` VALUES ('18', '2', '2', 'ADMIN-system', '2018-02-09 00:40:22', '127.0.0.1');

-- ----------------------------
-- Table structure for sys_role_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_user`;
CREATE TABLE `sys_role_user` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `role_id` int(10) unsigned NOT NULL,
  `user_id` int(10) unsigned DEFAULT NULL,
  `operator` varchar(20) NOT NULL DEFAULT '' COMMENT '最后一次操作者',
  `operate_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '最后一次修改时间',
  `operate_ip` varchar(20) NOT NULL DEFAULT '' COMMENT '最后一次修改地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role_user
-- ----------------------------
INSERT INTO `sys_role_user` VALUES ('2', '2', '3', 'ADMIN-system', '2018-02-07 16:01:09', '127.0.0.1');
INSERT INTO `sys_role_user` VALUES ('5', '1', '1', 'ADMIN-system', '2018-02-09 00:27:41', '127.0.0.1');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(20) NOT NULL DEFAULT '' COMMENT '用户名称',
  `telephone` varchar(13) NOT NULL DEFAULT '' COMMENT '手机号',
  `mail` varchar(20) NOT NULL DEFAULT '' COMMENT '邮箱',
  `password` varchar(40) NOT NULL DEFAULT '' COMMENT '加密后的密码',
  `dept_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '部门id',
  `status` int(10) unsigned NOT NULL DEFAULT '1' COMMENT '用户状态，1正常 0,冻结，3删除',
  `remark` varchar(200) DEFAULT '' COMMENT '备注',
  `operator` varchar(20) NOT NULL DEFAULT '' COMMENT '最后一次修改者',
  `operate_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '最后一次操作的时间',
  `operate_ip` varchar(20) NOT NULL DEFAULT '' COMMENT '最后一次更新者的ip地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', 'ADMIN', '13202126900', 'admin@qq.com', 'E10ADC3949BA59ABBE56E057F20F883E', '1', '1', 'abc', 'dgjkdj-system-update', '2018-02-01 21:20:32', '127.0.0.1');
INSERT INTO `sys_user` VALUES ('2', 'dgjkdj', '13202126901', 'dgjkdj@qq.com', 'E10ADC3949BA59ABBE56E057F20F883E', '1', '1', '1', 'ADMIN-system-update', '2018-02-01 21:18:53', '127.0.0.1');
INSERT INTO `sys_user` VALUES ('3', 'kk', '13202126902', 'kk@qq.com', 'E10ADC3949BA59ABBE56E057F20F883E', '1', '1', 'laji', 'ADMINN-system-update', '2018-02-01 22:21:53', '127.0.0.1');
INSERT INTO `sys_user` VALUES ('4', '天地一号', 'acb', '123456@qq.com', 'E10ADC3949BA59ABBE56E057F20F883E', '1', '1', '111', 'ADMINN', '2018-02-06 14:40:06', '127.0.0.1');
INSERT INTO `sys_user` VALUES ('5', '天地二号', '123456', '123@qq.com', 'E10ADC3949BA59ABBE56E057F20F883E', '8', '1', '11', 'ADMINN', '2018-02-06 14:40:45', '127.0.0.1');
INSERT INTO `sys_user` VALUES ('6', '王老五', '13202126903', 'wanglaowu@com', 'E10ADC3949BA59ABBE56E057F20F883E', '9', '1', '11111', 'ADMIN-system-update', '2018-02-08 15:08:24', '127.0.0.1');
