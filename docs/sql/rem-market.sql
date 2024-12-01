/*
 Navicat Premium Data Transfer

 Source Server         : 华为云
 Source Server Type    : MySQL
 Source Server Version : 80032
 Source Host           : 110.41.180.185:3306
 Source Schema         : rem-market

 Target Server Type    : MySQL
 Target Server Version : 80032
 File Encoding         : 65001

 Date: 01/12/2024 16:59:04
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

CREATE database if NOT EXISTS `rem_market` default character set utf8mb4 collate utf8mb4_0900_ai_ci;
use `rem_market`;

-- ----------------------------
-- Table structure for award
-- ----------------------------
DROP TABLE IF EXISTS `award`;
CREATE TABLE `award`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `award_id` int NOT NULL COMMENT '奖品id-内部使用',
  `award_key` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '奖品对接标识 - 每一个都是一个对应的发奖策略',
  `award_config` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '奖品配置信息',
  `award_desc` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '奖品描述',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_strategy_award`(`award_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of award
-- ----------------------------
INSERT INTO `award` VALUES (1, 101, 'user_credit_random', '1,100', '用户积分【优先透彻规则范围，如果没有则走配置】', '2023-12-09 11:07:06', '2023-12-09 11:21:31');
INSERT INTO `award` VALUES (2, 102, 'openai_use_count', '5', 'OpenAI 增加使用次数', '2023-12-09 11:07:06', '2023-12-09 11:12:59');
INSERT INTO `award` VALUES (3, 103, 'openai_use_count', '10', 'OpenAI 增加使用次数', '2023-12-09 11:07:06', '2023-12-09 11:12:59');
INSERT INTO `award` VALUES (4, 104, 'openai_use_count', '20', 'OpenAI 增加使用次数', '2023-12-09 11:07:06', '2023-12-09 11:12:58');
INSERT INTO `award` VALUES (5, 105, 'openai_model', 'gpt-4', 'OpenAI 增加模型', '2023-12-09 11:07:06', '2023-12-09 11:12:01');
INSERT INTO `award` VALUES (6, 106, 'openai_model', 'dall-e-2', 'OpenAI 增加模型', '2023-12-09 11:07:06', '2023-12-09 11:12:08');
INSERT INTO `award` VALUES (7, 107, 'openai_model', 'dall-e-3', 'OpenAI 增加模型', '2023-12-09 11:07:06', '2023-12-09 11:12:10');
INSERT INTO `award` VALUES (8, 108, 'openai_use_count', '100', 'OpenAI 增加使用次数', '2023-12-09 11:07:06', '2023-12-09 11:12:55');
INSERT INTO `award` VALUES (9, 109, 'openai_model', 'gpt-4,dall-e-2,dall-e-3', 'OpenAI 增加模型', '2023-12-09 11:07:06', '2023-12-09 11:12:39');
INSERT INTO `award` VALUES (10, 100, 'user_credit_blacklist', '1', '黑名单积分', '2024-01-06 12:30:40', '2024-01-06 12:30:46');

-- ----------------------------
-- Table structure for raffle_activity
-- ----------------------------
DROP TABLE IF EXISTS `raffle_activity`;
CREATE TABLE `raffle_activity`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `activity_id` bigint NOT NULL COMMENT '活动id',
  `activity_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '活动名称',
  `activity_desc` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '活动描述',
  `begin_time` datetime NOT NULL COMMENT '活动开始时间',
  `end_time` datetime NOT NULL COMMENT '活动结束时间',
  `stock_count` int NOT NULL COMMENT '库存总量',
  `stock_count_surplus` int NOT NULL COMMENT '库存剩余量',
  `activity_count_id` bigint NOT NULL COMMENT '活动参与次数配置',
  `strategy_id` bigint NOT NULL COMMENT '活动抽奖策略id',
  `status` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '活动状态',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uq_activity_id`(`activity_id` ASC) USING BTREE,
  INDEX `idx_begin_time`(`begin_time` ASC) USING BTREE,
  INDEX `idx_end_time`(`end_time` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of raffle_activity
-- ----------------------------

-- ----------------------------
-- Table structure for raffle_activity_account
-- ----------------------------
DROP TABLE IF EXISTS `raffle_activity_account`;
CREATE TABLE `raffle_activity_account`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户id',
  `activity_id` bigint NOT NULL COMMENT '活动id',
  `total_count` int NOT NULL COMMENT '总次数',
  `total_count_surplus` int NOT NULL COMMENT '总剩余次数',
  `day_count` int NOT NULL COMMENT '日次数',
  `day_count_surplus` int NOT NULL COMMENT '日剩余次数',
  `month_count` int NOT NULL COMMENT '月次数',
  `month_count_surplus` int NOT NULL COMMENT '月剩余次数',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `uq_user_id_activity_id`(`user_id` ASC, `activity_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of raffle_activity_account
-- ----------------------------

-- ----------------------------
-- Table structure for raffle_activity_account_flow
-- ----------------------------
DROP TABLE IF EXISTS `raffle_activity_account_flow`;
CREATE TABLE `raffle_activity_account_flow`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户id',
  `activity_id` bigint NOT NULL COMMENT '活动id',
  `total_count` int NOT NULL COMMENT '总次数',
  `day_count` int NOT NULL COMMENT '日次数',
  `month_count` int NOT NULL COMMENT '月次数',
  `flow_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '流水id',
  `flow_channel` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '流水渠道(activity-活动领取、sale-购买、redeem-兑换、free-免费领取)',
  `biz_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '业务id(活动id、订单id)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `uq_flow_id`(`flow_id` ASC) USING BTREE,
  INDEX `uq_biz_id`(`biz_id` ASC) USING BTREE,
  INDEX `idx_user_id_activity_id`(`user_id` ASC, `activity_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of raffle_activity_account_flow
-- ----------------------------

-- ----------------------------
-- Table structure for raffle_activity_count
-- ----------------------------
DROP TABLE IF EXISTS `raffle_activity_count`;
CREATE TABLE `raffle_activity_count`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `activity_count_id` bigint NOT NULL COMMENT '活动次数编号',
  `total_count` int NOT NULL COMMENT '总次数',
  `day_count` int NOT NULL COMMENT '日次数',
  `month_count` int NOT NULL COMMENT '月次数',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `uq_activity_count_id`(`activity_count_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of raffle_activity_count
-- ----------------------------

-- ----------------------------
-- Table structure for raffle_activity_order
-- ----------------------------
DROP TABLE IF EXISTS `raffle_activity_order`;
CREATE TABLE `raffle_activity_order`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户id',
  `activity_id` bigint NOT NULL COMMENT '活动id',
  `strategy_id` bigint NOT NULL COMMENT '抽奖策略id',
  `order_id` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单id',
  `order_time` datetime NOT NULL COMMENT '下单时间',
  `status` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单状态',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `uq_order_id`(`order_id` ASC) USING BTREE,
  INDEX `idx_user_id_activity_id`(`user_id` ASC, `activity_id` ASC, `status` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of raffle_activity_order
-- ----------------------------

-- ----------------------------
-- Table structure for rule
-- ----------------------------
DROP TABLE IF EXISTS `rule`;
CREATE TABLE `rule`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `strategy_id` bigint NOT NULL COMMENT '策略id',
  `award_id` int NULL DEFAULT NULL COMMENT '奖品id【规则为策略 则不需要奖品id】',
  `rule_type` tinyint(1) UNSIGNED ZEROFILL NOT NULL COMMENT '1-策略规则、 2-奖品规则',
  `rule_model` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '【rule_random - 随机值计算、 rule_lock - 抽奖几次后解锁、 rule_luck_award- 幸运奖(兜底奖品)】',
  `rule_value` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '抽奖规则比值',
  `rule_desc` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '抽奖规则描述',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_strategy_award`(`strategy_id` ASC, `award_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of rule
-- ----------------------------
INSERT INTO `rule` VALUES (1, 100001, 101, 2, 'rule_random', '1,1000', '随机积分策略', '2024-11-20 14:49:24', '2024-11-20 14:49:24');
INSERT INTO `rule` VALUES (2, 100001, 107, 2, 'rule_lock', '1', '抽奖1次后解锁', '2024-11-20 14:49:36', '2024-11-20 14:49:36');
INSERT INTO `rule` VALUES (3, 100001, 108, 2, 'rule_lock', '2', '抽奖2次后解锁', '2024-11-20 14:49:40', '2024-11-20 14:49:40');
INSERT INTO `rule` VALUES (4, 100001, 109, 2, 'rule_lock', '6', '抽奖6次后解锁', '2024-11-20 14:49:48', '2024-11-20 14:49:48');
INSERT INTO `rule` VALUES (5, 100001, 107, 2, 'rule_luck_award', '1,100', '兜底奖品100以内随机积分', '2024-11-20 14:50:15', '2024-11-20 14:50:15');
INSERT INTO `rule` VALUES (6, 100001, 108, 2, 'rule_luck_award', '1,100', '兜底奖品100以内随机积分', '2024-11-20 14:50:17', '2024-11-20 14:50:17');
INSERT INTO `rule` VALUES (7, 100001, 101, 2, 'rule_luck_award', '1,10', '兜底奖品10以内随机积分', '2024-11-20 14:50:19', '2024-11-20 14:50:19');
INSERT INTO `rule` VALUES (8, 100001, 102, 2, 'rule_luck_award', '1,20', '兜底奖品20以内随机积分', '2024-11-20 14:50:22', '2024-11-20 14:50:22');
INSERT INTO `rule` VALUES (9, 100001, 103, 2, 'rule_luck_award', '1,30', '兜底奖品30以内随机积分', '2024-11-20 14:50:26', '2024-11-20 14:50:26');
INSERT INTO `rule` VALUES (10, 100001, 104, 2, 'rule_luck_award', '1,40', '兜底奖品40以内随机积分', '2024-11-20 14:50:41', '2024-11-20 14:50:41');
INSERT INTO `rule` VALUES (11, 100001, 105, 2, 'rule_luck_award', '1,50', '兜底奖品50以内随机积分', '2024-11-20 14:50:31', '2024-11-20 14:50:31');
INSERT INTO `rule` VALUES (12, 100001, 106, 2, 'rule_luck_award', '1,60', '兜底奖品60以内随机积分', '2024-11-20 14:50:36', '2024-11-20 14:50:36');
INSERT INTO `rule` VALUES (13, 100001, NULL, 1, 'rule_weight', '4000:101,102,103,104,105,106 6000:102,103,104,105,106,107,108,109', '消耗6000分，必中奖范围', '2024-11-20 10:23:52', '2024-11-20 10:23:52');
INSERT INTO `rule` VALUES (14, 100001, NULL, 1, 'rule_blacklist', '1', '黑名单抽奖，积分兜底', '2023-12-09 12:59:45', '2023-12-09 13:42:23');
INSERT INTO `rule` VALUES (15, 100002, NULL, 1, 'rule_weight', '4000:101,102,103,104 6000:102,107,108,109', '权重规则', '2024-11-25 23:16:06', '2024-11-25 23:16:06');
INSERT INTO `rule` VALUES (16, 100002, NULL, 1, 'rule_blacklist', '1', '黑名单规则', '2024-11-25 23:16:17', '2024-11-25 23:16:19');
INSERT INTO `rule` VALUES (17, 100003, 101, 2, 'rule_random', '1,1000', '随机积分策略', '2024-11-29 08:25:50', '2024-11-29 08:25:50');
INSERT INTO `rule` VALUES (18, 100003, 102, 2, 'rule_luck_award', '1,100', '兜底积分', '2024-11-29 08:26:25', '2024-11-29 08:26:25');

-- ----------------------------
-- Table structure for rule_tree
-- ----------------------------
DROP TABLE IF EXISTS `rule_tree`;
CREATE TABLE `rule_tree`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `tree_id` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '规则树id',
  `tree_name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '规则树名称',
  `tree_desc` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '规则树描述',
  `tree_root_node_key` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '根节点',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tree_id`(`tree_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of rule_tree
-- ----------------------------
INSERT INTO `rule_tree` VALUES (1, 'rule_lock', '规则树', '规则树', 'rule_lock', '2024-11-27 09:24:34', '2024-11-27 09:24:34');

-- ----------------------------
-- Table structure for rule_tree_node
-- ----------------------------
DROP TABLE IF EXISTS `rule_tree_node`;
CREATE TABLE `rule_tree_node`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `tree_id` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '规则树id',
  `tree_node_key` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '规则树key',
  `rule_desc` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '规则描述',
  `rule_value` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '规则赋值',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tree_id`(`tree_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of rule_tree_node
-- ----------------------------
INSERT INTO `rule_tree_node` VALUES (1, 'rule_lock', 'rule_lock', '用户抽奖N次后解锁奖品', '1', '2024-11-27 08:40:06', '2024-11-27 08:40:06');
INSERT INTO `rule_tree_node` VALUES (2, 'rule_lock', 'rule_luck_award', '兜底奖品', '1,100', '2024-11-27 08:41:39', '2024-11-27 08:41:39');
INSERT INTO `rule_tree_node` VALUES (3, 'rule_lock', 'rule_stock', '库存扣减规则', NULL, '2024-11-27 08:42:31', '2024-11-27 08:42:31');

-- ----------------------------
-- Table structure for rule_tree_node_line
-- ----------------------------
DROP TABLE IF EXISTS `rule_tree_node_line`;
CREATE TABLE `rule_tree_node_line`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `tree_id` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '规则树id',
  `rule_node_key` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '节点',
  `rule_child_node` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '下一节点',
  `rule_limit_type` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '连接下一节点条件类型',
  `rule_limit_value` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '连接下一节点的值',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tree_id`(`tree_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of rule_tree_node_line
-- ----------------------------
INSERT INTO `rule_tree_node_line` VALUES (1, 'rule_lock', 'rule_lock', 'rule_luck_award', 'EQUAL', 'BLOCK', '2024-11-27 15:07:59', '2024-11-27 15:07:59');
INSERT INTO `rule_tree_node_line` VALUES (2, 'rule_lock', 'rule_lock', 'rule_stock', 'EQUAL', 'PASS', '2024-11-27 15:08:01', '2024-11-27 15:08:01');
INSERT INTO `rule_tree_node_line` VALUES (3, 'rule_lock', 'rule_stock', 'rule_luck_award', 'EQUAL', 'BLOCK', '2024-11-27 22:15:37', '2024-11-27 22:15:37');
INSERT INTO `rule_tree_node_line` VALUES (6, 'rule_lock', 'rule_stock', '', 'EQUAL', 'PASS', '2024-11-29 12:18:17', '2024-11-29 12:18:17');

-- ----------------------------
-- Table structure for strategy
-- ----------------------------
DROP TABLE IF EXISTS `strategy`;
CREATE TABLE `strategy`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `strategy_id` bigint NOT NULL COMMENT '抽奖策略id',
  `strategy_desc` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '抽奖策略描述',
  `rule_models` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '策略规则模型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_strateegy_id`(`strategy_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of strategy
-- ----------------------------
INSERT INTO `strategy` VALUES (1, 100001, '抽奖策略', 'rule_blacklist,rule_weight,rule_lock,rule_luck_award', '2024-11-23 22:27:42', '2024-11-23 22:27:42');
INSERT INTO `strategy` VALUES (2, 100002, '抽奖前置策略', 'rule_blacklist,rule_weight', '2024-11-25 23:40:16', '2024-11-25 23:40:16');
INSERT INTO `strategy` VALUES (3, 100003, '抽奖策略', 'rule_blacklist,rule_luck_award', '2024-11-29 08:35:20', '2024-11-29 08:35:20');

-- ----------------------------
-- Table structure for strategy_award
-- ----------------------------
DROP TABLE IF EXISTS `strategy_award`;
CREATE TABLE `strategy_award`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `strategy_id` bigint NOT NULL COMMENT '抽奖策略id',
  `award_id` int NOT NULL COMMENT '奖品id-内部使用',
  `award_title` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '奖品标题',
  `award_subtitle` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '奖品副标题',
  `award_count` int(8) UNSIGNED ZEROFILL NOT NULL COMMENT '奖品库存总量',
  `award_count_surplus` int(8) UNSIGNED ZEROFILL NOT NULL COMMENT '库存剩余',
  `rate` decimal(6, 4) NOT NULL COMMENT '中奖概率',
  `models` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '规则模型 rule配置模型同步到此表',
  `sort` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '排序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_strategy_award`(`strategy_id` ASC, `award_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of strategy_award
-- ----------------------------
INSERT INTO `strategy_award` VALUES (1, 100001, 101, '随机积分', NULL, 00080000, 00080000, 0.1000, 'rule_random,rule_luck_award', '1', '2024-11-19 14:46:32', '2024-11-19 14:46:32');
INSERT INTO `strategy_award` VALUES (2, 100001, 102, '5次使用', NULL, 00010000, 00010000, 0.1000, 'rule_luck_award', '2', '2024-11-19 14:46:39', '2024-11-19 14:46:39');
INSERT INTO `strategy_award` VALUES (3, 100001, 103, '10次使用', NULL, 00005000, 00005000, 0.1000, 'rule_luck_award', '3', '2024-11-19 14:46:42', '2024-11-19 14:46:42');
INSERT INTO `strategy_award` VALUES (4, 100001, 104, '20次使用', NULL, 00004000, 00004000, 0.1000, 'rule_luck_award', '4', '2024-11-19 14:46:44', '2024-11-19 14:46:44');
INSERT INTO `strategy_award` VALUES (5, 100001, 105, '增加gpt-4对话模型', NULL, 00000600, 00000600, 0.1000, 'rule_luck_award', '5', '2024-11-19 14:46:46', '2024-11-19 14:46:46');
INSERT INTO `strategy_award` VALUES (6, 100001, 106, '增加dall-e-2画图模型', NULL, 00000200, 00000200, 0.3000, 'rule_luck_award', '6', '2024-11-20 15:00:46', '2024-11-20 15:00:46');
INSERT INTO `strategy_award` VALUES (7, 100001, 107, '增加dall-e-3画图模型', '抽奖1次后解锁', 00000200, 00000200, 0.0007, 'rule_lock,rule_luck_award', '7', '2024-11-20 15:00:48', '2024-11-20 15:00:48');
INSERT INTO `strategy_award` VALUES (8, 100001, 108, '增加100次使用', '抽奖2次后解锁', 00000199, 00000199, 0.0893, 'rule_lock,rule_luck_award', '8', '2024-11-20 17:25:31', '2024-11-20 17:25:31');
INSERT INTO `strategy_award` VALUES (9, 100001, 109, '解锁全部模型', '抽奖6次后解锁', 00000001, 00000001, 0.1100, 'rule_lock,rule_luck_award', '9', '2024-11-20 15:00:53', '2024-11-20 15:00:53');
INSERT INTO `strategy_award` VALUES (10, 100002, 101, '随机积分', NULL, 00100000, 00100000, 0.4000, 'rule_random,rule_luck_award', '1', '2024-11-25 23:27:25', '2024-11-25 23:27:25');
INSERT INTO `strategy_award` VALUES (11, 100002, 102, '5次使用', NULL, 00100000, 00100000, 0.2000, 'rule_luck_award', '2', '2024-11-25 23:27:28', '2024-11-25 23:27:28');
INSERT INTO `strategy_award` VALUES (12, 100002, 103, '10次使用', NULL, 00100000, 00100000, 0.0500, 'rule_luck_award', '3', '2024-11-25 23:22:11', '2024-11-25 23:22:11');
INSERT INTO `strategy_award` VALUES (13, 100002, 104, '20次使用', NULL, 00100000, 00100000, 0.0700, 'rule_luck_award', '4', '2024-11-25 23:27:36', '2024-11-25 23:27:36');
INSERT INTO `strategy_award` VALUES (14, 100002, 107, '增加dall-e-2画图模型', NULL, 00010000, 00010000, 0.1000, 'rule_luck_award', '0', '2024-11-25 23:27:41', '2024-11-25 23:27:41');
INSERT INTO `strategy_award` VALUES (15, 100002, 108, '增加dall-e-3画图模型', '抽奖1次后解锁', 00003123, 00003123, 0.0500, 'rule_lock,rule_luck_award', '0', '2024-11-27 09:25:48', '2024-11-27 09:25:48');
INSERT INTO `strategy_award` VALUES (16, 100002, 108, '增加100次使用', '抽奖2次后解锁', 00312312, 00312312, 0.1000, 'rule_lock,rule_luck_award', '0', '2024-11-25 23:28:38', '2024-11-25 23:28:38');
INSERT INTO `strategy_award` VALUES (17, 100002, 109, '解锁全部模型', '抽奖6次后解锁', 00003232, 00000323, 0.0300, '3rule_lock,rule_luck_award', '0', '2024-11-25 23:28:44', '2024-11-25 23:28:44');
INSERT INTO `strategy_award` VALUES (18, 100003, 101, '随机积分', NULL, 00000050, 00000012, 0.6400, 'rule_lock', '1', '2024-11-29 15:46:38', '2024-11-29 15:46:38');
INSERT INTO `strategy_award` VALUES (19, 100003, 102, '5次使用', NULL, 00000050, 00000018, 0.5600, 'rule_lock', '1', '2024-11-29 15:46:37', '2024-11-29 15:46:37');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户id',
  `award_id` int UNSIGNED NULL DEFAULT NULL COMMENT '奖品id',
  `strategy_id` bigint NOT NULL COMMENT '策略id',
  `rule_model` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '规则模型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`award_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (3, '1', 101, 100001, 'rule_blacklist', '2024-11-21 23:16:55', '2024-11-21 23:16:55');
INSERT INTO `user` VALUES (4, '2', 101, 100001, 'rule_blacklist', '2024-11-23 00:03:09', '2024-11-23 00:03:09');
INSERT INTO `user` VALUES (5, '3', 101, 100001, '', '2024-11-23 00:03:13', '2024-11-23 00:03:13');
INSERT INTO `user` VALUES (6, '4', 101, 100002, 'rule_blacklist', '2024-11-25 23:41:39', '2024-11-25 23:41:39');
INSERT INTO `user` VALUES (7, '5', 102, 100002, NULL, '2024-11-25 23:41:48', '2024-11-25 23:41:48');

SET FOREIGN_KEY_CHECKS = 1;
