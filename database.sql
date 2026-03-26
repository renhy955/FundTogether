/*
 Navicat Premium Dump SQL

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80408 (8.4.8)
 Source Host           : localhost:3306
 Source Schema         : fund_together

 Target Server Type    : MySQL
 Target Server Version : 80408 (8.4.8)
 File Encoding         : 65001

 Date: 25/03/2026 00:45:50
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for project
-- ----------------------------
DROP TABLE IF EXISTS `project`;
CREATE TABLE `project` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `sponsor_id` bigint NOT NULL COMMENT '发起人ID',
  `category_id` bigint DEFAULT NULL COMMENT '分类ID',
  `title` varchar(100) NOT NULL COMMENT '项目标题',
  `summary` varchar(255) NOT NULL COMMENT '项目简介',
  `cover_image` varchar(255) NOT NULL COMMENT '项目封面',
  `video_url` varchar(255) DEFAULT NULL COMMENT '项目视频',
  `content` longtext NOT NULL COMMENT '项目详情',
  `target_amount` decimal(10,2) NOT NULL COMMENT '目标金额',
  `current_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '当前已筹金额',
  `supporter_count` int NOT NULL DEFAULT '0' COMMENT '支持人数',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态: 0-待审核, 1-筹款中, 2-已驳回, 3-已取消, 4-已下架, 5-已完成',
  `heat` int NOT NULL DEFAULT '0' COMMENT '热度',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='众筹项目表';

-- ----------------------------
-- Records of project
-- ----------------------------
BEGIN;
INSERT INTO `project` (`id`, `sponsor_id`, `category_id`, `title`, `summary`, `cover_image`, `video_url`, `content`, `target_amount`, `current_amount`, `supporter_count`, `start_time`, `end_time`, `status`, `heat`, `created_at`, `updated_at`, `deleted`) VALUES (1, 3, 1, '智能助眠眼罩', '利用AI白噪音与温感助眠的创新眼罩', 'https://images.unsplash.com/photo-1541781774459-bb2af2f05b55', NULL, '<p>这是一款革命性的智能助眠眼罩...</p>', 50000.00, 15000.00, 120, '2023-10-01 10:00:00', '2023-11-30 23:59:59', 1, 800, '2026-03-25 00:09:03', '2026-03-25 00:09:03', 0);
INSERT INTO `project` (`id`, `sponsor_id`, `category_id`, `title`, `summary`, `cover_image`, `video_url`, `content`, `target_amount`, `current_amount`, `supporter_count`, `start_time`, `end_time`, `status`, `heat`, `created_at`, `updated_at`, `deleted`) VALUES (2, 3, 2, '乡村儿童图书角', '为偏远地区儿童建立阅读图书角', 'https://images.unsplash.com/photo-1456513080510-7bf3a84b82f8', NULL, '<p>每人献出一点爱，点亮乡村儿童的阅读梦...</p>', 10000.00, 12000.00, 300, '2023-09-01 00:00:00', '2023-10-31 23:59:59', 1, 1500, '2026-03-25 00:09:03', '2026-03-25 00:09:03', 0);
INSERT INTO `project` (`id`, `sponsor_id`, `category_id`, `title`, `summary`, `cover_image`, `video_url`, `content`, `target_amount`, `current_amount`, `supporter_count`, `start_time`, `end_time`, `status`, `heat`, `created_at`, `updated_at`, `deleted`) VALUES (3, 3, 1, '便携式咖啡机(待审核)', '随时随地享受现磨咖啡', 'https://images.unsplash.com/photo-1511920170033-f8396924c348', NULL, '<p>超便携设计的电动咖啡机...</p>', 30000.00, 0.00, 0, NULL, '2024-01-31 23:59:59', 0, 0, '2026-03-25 00:09:03', '2026-03-25 00:09:03', 0);
INSERT INTO `project` (`id`, `sponsor_id`, `category_id`, `title`, `summary`, `cover_image`, `video_url`, `content`, `target_amount`, `current_amount`, `supporter_count`, `start_time`, `end_time`, `status`, `heat`, `created_at`, `updated_at`, `deleted`) VALUES (4, 4, NULL, '1', '1', 'https://gips2.baidu.com/it/u=195724436,3554684702&fm=3028&app=3028&f=JPEG&fmt=auto?w=1280&h=960', '', '1', 10000.00, 0.00, 0, NULL, '2026-03-26 00:00:00', 0, 0, '2026-03-25 00:16:39', '2026-03-25 00:16:39', 0);
INSERT INTO `project` (`id`, `sponsor_id`, `category_id`, `title`, `summary`, `cover_image`, `video_url`, `content`, `target_amount`, `current_amount`, `supporter_count`, `start_time`, `end_time`, `status`, `heat`, `created_at`, `updated_at`, `deleted`) VALUES (11, 11, 1, '待审核项目11', '这是一个待审核项目，用于测试取消和审核', 'https://example.com/cover11.jpg', NULL, '<p>详细内容</p>', 10000.00, 0.00, 0, NULL, '2026-04-24 00:43:56', 0, 0, '2026-03-25 00:43:56', '2026-03-25 00:43:56', 0);
INSERT INTO `project` (`id`, `sponsor_id`, `category_id`, `title`, `summary`, `cover_image`, `video_url`, `content`, `target_amount`, `current_amount`, `supporter_count`, `start_time`, `end_time`, `status`, `heat`, `created_at`, `updated_at`, `deleted`) VALUES (12, 11, 1, '筹款中项目12', '这是一个筹款中的项目，用于测试下架和支持者名单', 'https://example.com/cover12.jpg', NULL, '<p>详细内容</p>', 20000.00, 0.00, 0, NULL, '2026-04-24 00:43:56', 1, 0, '2026-03-25 00:43:56', '2026-03-25 00:43:56', 0);
COMMIT;

-- ----------------------------
-- Table structure for project_category
-- ----------------------------
DROP TABLE IF EXISTS `project_category`;
CREATE TABLE `project_category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '分类名称',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='项目分类表';

-- ----------------------------
-- Records of project_category
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for project_reward
-- ----------------------------
DROP TABLE IF EXISTS `project_reward`;
CREATE TABLE `project_reward` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `project_id` bigint NOT NULL COMMENT '项目ID',
  `amount` decimal(10,2) NOT NULL COMMENT '支持金额',
  `content` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '回报内容描述',
  `reward_count` int NOT NULL DEFAULT '-1' COMMENT '限制名额(-1表示不限量)',
  `delivery_time` int NOT NULL COMMENT '预计发放时间(项目成功后多少天内)',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_project_id` (`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='项目支持回报档位表';

-- ----------------------------
-- Records of project_reward
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for project_update
-- ----------------------------
DROP TABLE IF EXISTS `project_update`;
CREATE TABLE `project_update` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `project_id` bigint NOT NULL COMMENT '项目ID',
  `title` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '动态标题',
  `content` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '动态内容',
  `images` json DEFAULT NULL COMMENT '图片URL数组(JSON格式)',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_project_id` (`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='项目动态进展表';

-- ----------------------------
-- Records of project_update
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for support_order
-- ----------------------------
DROP TABLE IF EXISTS `support_order`;
CREATE TABLE `support_order` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_no` varchar(64) NOT NULL COMMENT '订单号',
  `user_id` bigint NOT NULL COMMENT '支持者ID',
  `project_id` bigint NOT NULL COMMENT '项目ID',
  `amount` decimal(10,2) NOT NULL COMMENT '支持金额',
  `message` varchar(255) DEFAULT NULL COMMENT '留言',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '0-待支付 1-已支付 2-已取消',
  `pay_channel` varchar(32) DEFAULT NULL COMMENT '支付渠道',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='支持订单表';

-- ----------------------------
-- Records of support_order
-- ----------------------------
BEGIN;
INSERT INTO `support_order` (`id`, `order_no`, `user_id`, `project_id`, `amount`, `message`, `status`, `pay_channel`, `pay_time`, `created_at`, `updated_at`, `deleted`) VALUES (1, 'ORD20231015001', 2, 1, 100.00, '期待早日发货！', 1, 'ALIPAY', '2023-10-15 12:30:00', '2026-03-25 00:09:03', '2026-03-25 00:09:03', 0);
INSERT INTO `support_order` (`id`, `order_no`, `user_id`, `project_id`, `amount`, `message`, `status`, `pay_channel`, `pay_time`, `created_at`, `updated_at`, `deleted`) VALUES (2, 'ORD20231016002', 2, 2, 50.00, '支持乡村教育', 1, 'WECHAT', '2023-10-16 09:15:00', '2026-03-25 00:09:03', '2026-03-25 00:09:03', 0);
INSERT INTO `support_order` (`id`, `order_no`, `user_id`, `project_id`, `amount`, `message`, `status`, `pay_channel`, `pay_time`, `created_at`, `updated_at`, `deleted`) VALUES (3, 'f1bbf950064e41b6830cc660f6b872ea', 4, 2, 10.00, '', 0, NULL, NULL, '2026-03-25 00:12:25', '2026-03-25 00:12:25', 0);
INSERT INTO `support_order` (`id`, `order_no`, `user_id`, `project_id`, `amount`, `message`, `status`, `pay_channel`, `pay_time`, `created_at`, `updated_at`, `deleted`) VALUES (4, 'c9570a62b1b34052b681884d376afed5', 4, 2, 10.00, '', 0, NULL, NULL, '2026-03-25 00:12:27', '2026-03-25 00:12:27', 0);
INSERT INTO `support_order` (`id`, `order_no`, `user_id`, `project_id`, `amount`, `message`, `status`, `pay_channel`, `pay_time`, `created_at`, `updated_at`, `deleted`) VALUES (11, 'ORDER202603251020001', 12, 12, 100.00, '支持一下！', 1, NULL, NULL, '2026-03-25 00:40:19', '2026-03-25 00:40:19', 0);
INSERT INTO `support_order` (`id`, `order_no`, `user_id`, `project_id`, `amount`, `message`, `status`, `pay_channel`, `pay_time`, `created_at`, `updated_at`, `deleted`) VALUES (12, 'ORDER202603251020002', 12, 12, 200.00, '加油！', 1, NULL, NULL, '2026-03-25 00:40:19', '2026-03-25 00:40:19', 0);
COMMIT;

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '公告标题',
  `content` longtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '公告内容(富文本)',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态: 0-未发布, 1-已发布',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='平台系统公告表';

-- ----------------------------
-- Records of sys_notice
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `account` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '登录账号(手机号/邮箱)',
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '加密密码',
  `nickname` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户昵称',
  `avatar` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像URL',
  `phone` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机号',
  `email` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `role` tinyint NOT NULL DEFAULT '1' COMMENT '角色: 1-普通支持者, 2-项目发起人, 3-系统管理员',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态: 1-正常, 0-禁用',
  `disable_reason` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '禁用原因',
  `bio` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '个人简介',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除: 0-未删除, 1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_account` (`account`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户基础信息表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` (`id`, `account`, `password`, `nickname`, `avatar`, `phone`, `email`, `role`, `status`, `disable_reason`, `bio`, `created_at`, `updated_at`, `deleted`) VALUES (1, 'admin', '$2a$10$QQLCe751k6pvUwkNXHACxelYan2J3Y5cGM4BOuZlB85foy.G0Mq.y', '系统管理员', NULL, '13800138000', 'admin@test.com', 3, 1, NULL, NULL, '2026-03-24 23:28:47', '2026-03-24 23:53:17', 0);
INSERT INTO `sys_user` (`id`, `account`, `password`, `nickname`, `avatar`, `phone`, `email`, `role`, `status`, `disable_reason`, `bio`, `created_at`, `updated_at`, `deleted`) VALUES (2, 'user1', '$2a$10$QQLCe751k6pvUwkNXHACxelYan2J3Y5cGM4BOuZlB85foy.G0Mq.y', '普通用户A', NULL, '13800138001', 'user1@test.com', 1, 1, NULL, NULL, '2026-03-24 23:28:47', '2026-03-24 23:53:51', 0);
INSERT INTO `sys_user` (`id`, `account`, `password`, `nickname`, `avatar`, `phone`, `email`, `role`, `status`, `disable_reason`, `bio`, `created_at`, `updated_at`, `deleted`) VALUES (3, 'sponsor1', '$2a$10$xyz', '发起人A', NULL, '13800138002', 'sponsor1@test.com', 2, 1, NULL, NULL, '2026-03-24 23:28:47', '2026-03-24 23:28:47', 0);
INSERT INTO `sys_user` (`id`, `account`, `password`, `nickname`, `avatar`, `phone`, `email`, `role`, `status`, `disable_reason`, `bio`, `created_at`, `updated_at`, `deleted`) VALUES (4, '17200606763', '$2a$10$QQLCe751k6pvUwkNXHACxelYan2J3Y5cGM4BOuZlB85foy.G0Mq.y', NULL, NULL, '17200606763', NULL, 2, 1, NULL, NULL, '2026-03-24 23:43:03', '2026-03-24 23:43:03', 0);
INSERT INTO `sys_user` (`id`, `account`, `password`, `nickname`, `avatar`, `phone`, `email`, `role`, `status`, `disable_reason`, `bio`, `created_at`, `updated_at`, `deleted`) VALUES (11, 'sponsor11@test.com', '$2a$10$X8mR0fN/Qd/fJ01s40QOPO/8H1/9X.w5N8E/7x4XyE/wR1m6E/O2e', 'Sponsor 11', NULL, NULL, NULL, 2, 1, NULL, NULL, '2026-03-25 00:39:47', '2026-03-25 00:39:47', 0);
INSERT INTO `sys_user` (`id`, `account`, `password`, `nickname`, `avatar`, `phone`, `email`, `role`, `status`, `disable_reason`, `bio`, `created_at`, `updated_at`, `deleted`) VALUES (12, 'supporter12@test.com', '$2a$10$X8mR0fN/Qd/fJ01s40QOPO/8H1/9X.w5N8E/7x4XyE/wR1m6E/O2e', 'Supporter 12', NULL, NULL, NULL, 1, 1, NULL, NULL, '2026-03-25 00:39:47', '2026-03-25 00:39:47', 0);
INSERT INTO `sys_user` (`id`, `account`, `password`, `nickname`, `avatar`, `phone`, `email`, `role`, `status`, `disable_reason`, `bio`, `created_at`, `updated_at`, `deleted`) VALUES (13, 'admin13@test.com', '$2a$10$X8mR0fN/Qd/fJ01s40QOPO/8H1/9X.w5N8E/7x4XyE/wR1m6E/O2e', 'Admin 13', NULL, NULL, NULL, 3, 1, NULL, NULL, '2026-03-25 00:39:47', '2026-03-25 00:39:47', 0);
COMMIT;

-- ----------------------------
-- Table structure for sys_user_message
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_message`;
CREATE TABLE `sys_user_message` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '接收方用户ID(0代表全站广播)',
  `type` tinyint NOT NULL COMMENT '消息类型: 1-系统公告, 2-项目动态, 3-评论回复, 4-订单及状态通知',
  `title` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '消息标题',
  `content` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '消息内容简述',
  `related_id` bigint DEFAULT NULL COMMENT '关联业务ID(公告ID/项目ID/订单ID等)',
  `is_read` tinyint NOT NULL DEFAULT '0' COMMENT '是否已读: 0-未读, 1-已读',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户站内信/通知消息表';

-- ----------------------------
-- Records of sys_user_message
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for user_auth_info
-- ----------------------------
DROP TABLE IF EXISTS `user_auth_info`;
CREATE TABLE `user_auth_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `real_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '真实姓名',
  `id_card` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '身份证号',
  `id_card_front` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '身份证正面照片URL',
  `id_card_back` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '身份证反面照片URL',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '审核状态: 0-待审核, 1-审核通过, 2-审核驳回',
  `audit_reason` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '驳回原因',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户实名认证表';

-- ----------------------------
-- Records of user_auth_info
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for user_comment
-- ----------------------------
DROP TABLE IF EXISTS `user_comment`;
CREATE TABLE `user_comment` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `project_id` bigint NOT NULL COMMENT '项目ID',
  `parent_id` bigint NOT NULL DEFAULT '0' COMMENT '父评论ID(用于回复评论, 0为直接评论项目)',
  `content` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '评论/留言内容',
  `likes_count` int NOT NULL DEFAULT '0' COMMENT '点赞数',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态: 1-正常, 0-发起人或管理员删除',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_project_id` (`project_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户项目评论及留言表';

-- ----------------------------
-- Records of user_comment
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for user_like
-- ----------------------------
DROP TABLE IF EXISTS `user_like`;
CREATE TABLE `user_like` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `target_type` tinyint NOT NULL COMMENT '目标类型: 1-项目, 2-评论',
  `target_id` bigint NOT NULL COMMENT '目标ID(项目ID或评论ID)',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '点赞时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_target` (`user_id`,`target_type`,`target_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户点赞记录表';

-- ----------------------------
-- Records of user_like
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for user_payment_method
-- ----------------------------
DROP TABLE IF EXISTS `user_payment_method`;
CREATE TABLE `user_payment_method` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `type` tinyint NOT NULL COMMENT '类型: 1-微信, 2-支付宝, 3-银行卡',
  `account` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '账号(微信号/支付宝号/银行卡号)',
  `name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '真实姓名(如持卡人)',
  `bank_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '银行名称(银行卡时填写)',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '绑定时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户支付方式绑定表';

-- ----------------------------
-- Records of user_payment_method
-- ----------------------------
BEGIN;
INSERT INTO `user_payment_method` (`id`, `user_id`, `type`, `account`, `name`, `bank_name`, `created_at`, `updated_at`, `deleted`) VALUES (11, 12, 1, 'wx_supporter12', '张三', NULL, '2026-03-25 00:40:19', '2026-03-25 00:40:19', 0);
INSERT INTO `user_payment_method` (`id`, `user_id`, `type`, `account`, `name`, `bank_name`, `created_at`, `updated_at`, `deleted`) VALUES (12, 12, 3, '6222020000000000000', '张三', '招商银行', '2026-03-25 00:40:19', '2026-03-25 00:40:19', 0);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
