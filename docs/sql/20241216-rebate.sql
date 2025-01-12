/*
 Navicat Premium Data Transfer

 Source Server         : 华为云
 Source Server Type    : MySQL
 Source Server Version : 90100
 Source Host           : 110.41.180.185:3306
 Source Schema         : big_market

 Target Server Type    : MySQL
 Target Server Version : 90100
 File Encoding         : 65001

 Date: 12/01/2025 19:02:35
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for award
-- ----------------------------
DROP TABLE IF EXISTS `award`;
CREATE TABLE `award`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `award_id` int NOT NULL COMMENT '抽奖奖品ID - 内部流转使用',
  `award_key` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '奖品对接标识 - 每一个都是一个对应的发奖策略',
  `award_config` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '奖品配置信息',
  `award_desc` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '奖品内容描述',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '奖品表' ROW_FORMAT = Dynamic;

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
-- Table structure for daily_behavior_rebate
-- ----------------------------
DROP TABLE IF EXISTS `daily_behavior_rebate`;
CREATE TABLE `daily_behavior_rebate`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `behavior_type` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '行为类型(sign_in- 签到 、 pay-支付)',
  `rebate_desc` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '返利描述',
  `rebate_type` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '返利类型（sku 活动库存充值商品、integral 用户活动积分）',
  `rebate_config` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '配置',
  `status` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '状态（open-开启、 close-关闭）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_behavior_type`(`behavior_type` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '日常返利行为活动配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of daily_behavior_rebate
-- ----------------------------
INSERT INTO `daily_behavior_rebate` VALUES (1, 'sign', '签到返利-sku额度', 'sku', '9011', 'open', '2024-04-30 09:32:46', '2024-04-30 18:05:23');
INSERT INTO `daily_behavior_rebate` VALUES (2, 'sign', '签到返利-积分', 'integral', '10', 'open', '2024-04-30 09:32:46', '2024-04-30 18:05:27');

-- ----------------------------
-- Table structure for raffle_activity
-- ----------------------------
DROP TABLE IF EXISTS `raffle_activity`;
CREATE TABLE `raffle_activity`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `activity_id` bigint NOT NULL COMMENT '活动ID',
  `activity_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '活动名称',
  `activity_desc` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '活动描述',
  `begin_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `strategy_id` bigint NOT NULL COMMENT '抽奖策略ID',
  `status` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'create' COMMENT '活动状态',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uq_activity_id`(`activity_id` ASC) USING BTREE,
  INDEX `idx_begin_date_time`(`begin_time` ASC) USING BTREE,
  INDEX `idx_end_date_time`(`end_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '抽奖活动表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of raffle_activity
-- ----------------------------
INSERT INTO `raffle_activity` VALUES (1, 100301, '测试活动', '测试活动', '2024-03-09 10:15:10', '2034-03-09 10:15:10', 100006, 'open', '2024-03-09 10:15:10', '2024-03-30 12:07:36');

-- ----------------------------
-- Table structure for raffle_activity_account
-- ----------------------------
DROP TABLE IF EXISTS `raffle_activity_account`;
CREATE TABLE `raffle_activity_account`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户ID',
  `activity_id` bigint NOT NULL COMMENT '活动ID',
  `total_count` int NOT NULL COMMENT '总次数',
  `total_count_surplus` int NOT NULL COMMENT '总次数-剩余',
  `day_count` int NOT NULL COMMENT '日次数',
  `day_count_surplus` int NOT NULL COMMENT '日次数-剩余',
  `month_count` int NOT NULL COMMENT '月次数',
  `month_count_surplus` int NOT NULL COMMENT '月次数-剩余',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uq_user_id_activity_id`(`user_id` ASC, `activity_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '抽奖活动账户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of raffle_activity_account
-- ----------------------------
INSERT INTO `raffle_activity_account` VALUES (3, 'xiaofuge', 100301, 44, 43, 44, 43, 44, 43, '2024-03-23 16:38:57', '2024-03-30 17:10:06');
INSERT INTO `raffle_activity_account` VALUES (4, 'rem', 100301, 44, 36, 44, 36, 44, 36, '2024-12-08 01:56:49', '2024-12-14 16:54:49');

-- ----------------------------
-- Table structure for raffle_activity_account_day_count
-- ----------------------------
DROP TABLE IF EXISTS `raffle_activity_account_day_count`;
CREATE TABLE `raffle_activity_account_day_count`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户ID',
  `activity_id` bigint NOT NULL COMMENT '活动ID',
  `day` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '日期（yyyy-mm-dd）',
  `day_count` int NOT NULL COMMENT '日次数',
  `day_count_surplus` int NOT NULL COMMENT '日次数-剩余',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uq_user_id_activity_id_day`(`user_id` ASC, `activity_id` ASC, `day` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '抽奖活动账户表-日次数' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of raffle_activity_account_day_count
-- ----------------------------
INSERT INTO `raffle_activity_account_day_count` VALUES (1, 'rem', 100301, '2024-12-08', 2, 1, '2024-12-08 15:11:29', '2024-12-08 15:11:29');
INSERT INTO `raffle_activity_account_day_count` VALUES (2, 'rem', 100301, '2024-12-12', 2, 0, '2024-12-12 23:30:26', '2024-12-12 23:30:26');
INSERT INTO `raffle_activity_account_day_count` VALUES (3, 'rem', 100301, '2024-12-13', 2, 0, '2024-12-13 00:27:09', '2024-12-14 16:49:23');
INSERT INTO `raffle_activity_account_day_count` VALUES (4, 'rem', 100301, '2024-12-14', 44, 38, '2024-12-14 16:54:36', '2024-12-14 16:54:50');

-- ----------------------------
-- Table structure for raffle_activity_account_month_count
-- ----------------------------
DROP TABLE IF EXISTS `raffle_activity_account_month_count`;
CREATE TABLE `raffle_activity_account_month_count`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户ID',
  `activity_id` bigint NOT NULL COMMENT '活动ID',
  `month` varchar(7) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '月（yyyy-mm）',
  `month_count` int NOT NULL COMMENT '月次数',
  `month_count_surplus` int NOT NULL COMMENT '月次数-剩余',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uq_user_id_activity_id_month`(`user_id` ASC, `activity_id` ASC, `month` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '抽奖活动账户表-月次数' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of raffle_activity_account_month_count
-- ----------------------------
INSERT INTO `raffle_activity_account_month_count` VALUES (1, 'rem', 100301, '2024-12', 44, 34, '2024-12-08 15:11:29', '2024-12-14 16:54:49');

-- ----------------------------
-- Table structure for raffle_activity_count
-- ----------------------------
DROP TABLE IF EXISTS `raffle_activity_count`;
CREATE TABLE `raffle_activity_count`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `activity_count_id` bigint NOT NULL COMMENT '活动次数编号',
  `total_count` int NOT NULL COMMENT '总次数',
  `day_count` int NOT NULL COMMENT '日次数',
  `month_count` int NOT NULL COMMENT '月次数',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uq_activity_count_id`(`activity_count_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '抽奖活动次数配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of raffle_activity_count
-- ----------------------------
INSERT INTO `raffle_activity_count` VALUES (1, 11101, 1, 1, 1, '2024-03-09 10:15:42', '2024-03-16 12:30:54');

-- ----------------------------
-- Table structure for raffle_activity_order
-- ----------------------------
DROP TABLE IF EXISTS `raffle_activity_order`;
CREATE TABLE `raffle_activity_order`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户ID',
  `sku` bigint NOT NULL COMMENT '商品sku',
  `activity_id` bigint NOT NULL COMMENT '活动ID',
  `activity_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '活动名称',
  `strategy_id` bigint NOT NULL COMMENT '抽奖策略ID',
  `order_id` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单ID',
  `order_time` datetime NOT NULL COMMENT '下单时间',
  `total_count` int NOT NULL COMMENT '总次数',
  `day_count` int NOT NULL COMMENT '日次数',
  `month_count` int NOT NULL COMMENT '月次数',
  `status` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'complete' COMMENT '订单状态（complete）',
  `out_business_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '业务仿重ID - 外部透传的，确保幂等',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uq_order_id`(`order_id` ASC) USING BTREE,
  UNIQUE INDEX `uq_out_business_no`(`out_business_no` ASC) USING BTREE,
  INDEX `idx_user_id_activity_id`(`user_id` ASC, `activity_id` ASC, `status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '抽奖活动单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of raffle_activity_order
-- ----------------------------
INSERT INTO `raffle_activity_order` VALUES (1, 'rem', 9011, 100301, '测试活动', 100006, '55765237241', '2024-12-08 01:56:49', 1, 1, 1, 'completed', '700091009127', '2024-12-08 01:56:49', '2024-12-08 01:56:49');
INSERT INTO `raffle_activity_order` VALUES (2, 'rem', 9011, 100301, '测试活动', 100006, '71018075533', '2024-12-08 01:58:19', 1, 1, 1, 'completed', '700091009128', '2024-12-08 01:58:19', '2024-12-08 01:58:19');

-- ----------------------------
-- Table structure for raffle_activity_sku
-- ----------------------------
DROP TABLE IF EXISTS `raffle_activity_sku`;
CREATE TABLE `raffle_activity_sku`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `sku` bigint NOT NULL COMMENT '商品sku - 把每一个组合当做一个商品',
  `activity_id` bigint NOT NULL COMMENT '活动ID',
  `activity_count_id` bigint NOT NULL COMMENT '活动个人参与次数ID',
  `stock_count` int NOT NULL COMMENT '商品库存',
  `stock_count_surplus` int NOT NULL COMMENT '剩余库存',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uq_sku`(`sku` ASC) USING BTREE,
  INDEX `idx_activity_id_activity_count_id`(`activity_id` ASC, `activity_count_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of raffle_activity_sku
-- ----------------------------
INSERT INTO `raffle_activity_sku` VALUES (1, 9011, 100301, 11101, 20, 13, '2024-03-16 11:41:09', '2024-12-08 15:12:26');

-- ----------------------------
-- Table structure for rule
-- ----------------------------
DROP TABLE IF EXISTS `rule`;
CREATE TABLE `rule`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `strategy_id` int NOT NULL COMMENT '抽奖策略ID',
  `award_id` int NULL DEFAULT NULL COMMENT '抽奖奖品ID【规则类型为策略，则不需要奖品ID】',
  `rule_type` tinyint(1) NOT NULL DEFAULT 0 COMMENT '抽象规则类型；1-策略规则、2-奖品规则',
  `rule_model` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '抽奖规则类型【rule_random - 随机值计算、rule_lock - 抽奖几次后解锁、rule_luck_award - 幸运奖(兜底奖品)】',
  `rule_value` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '抽奖规则比值',
  `rule_desc` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '抽奖规则描述',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_strategy_id_award_id`(`strategy_id` ASC, `award_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '抽奖策略规则' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of rule
-- ----------------------------
INSERT INTO `rule` VALUES (13, 100001, NULL, 1, 'rule_weight', '4000:102,103,104,105 5000:102,103,104,105,106,107 6000:102,103,104,105,106,107,108,109', '消耗6000分，必中奖范围', '2023-12-09 10:30:43', '2023-12-31 14:51:50');
INSERT INTO `rule` VALUES (14, 100001, NULL, 1, 'rule_blacklist', '101:user001,user002,user003', '黑名单抽奖，积分兜底', '2023-12-09 12:59:45', '2024-02-14 18:16:20');

-- ----------------------------
-- Table structure for rule_tree
-- ----------------------------
DROP TABLE IF EXISTS `rule_tree`;
CREATE TABLE `rule_tree`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `tree_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '规则树ID',
  `tree_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '规则树名称',
  `tree_desc` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '规则树描述',
  `tree_root_node_key` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '规则树根入口规则',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uq_tree_id`(`tree_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '规则表-树' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of rule_tree
-- ----------------------------
INSERT INTO `rule_tree` VALUES (1, 'tree_lock_1', '规则树', '规则树', 'rule_lock', '2024-01-27 10:01:59', '2024-02-15 07:49:59');
INSERT INTO `rule_tree` VALUES (2, 'tree_luck_award', '规则树-兜底奖励', '规则树-兜底奖励', 'rule_stock', '2024-02-15 07:35:06', '2024-02-15 07:50:20');
INSERT INTO `rule_tree` VALUES (3, 'tree_lock_2', '规则树', '规则树', 'rule_lock', '2024-01-27 10:01:59', '2024-02-15 07:49:59');

-- ----------------------------
-- Table structure for rule_tree_node
-- ----------------------------
DROP TABLE IF EXISTS `rule_tree_node`;
CREATE TABLE `rule_tree_node`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `tree_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '规则树ID',
  `tree_node_key` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '规则Key',
  `rule_desc` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '规则描述',
  `rule_value` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '规则比值',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '规则表-树节点' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of rule_tree_node
-- ----------------------------
INSERT INTO `rule_tree_node` VALUES (1, 'tree_lock_1', 'rule_lock', '限定用户已完成N次抽奖后解锁', '1', '2024-01-27 10:03:09', '2024-02-15 07:50:57');
INSERT INTO `rule_tree_node` VALUES (2, 'tree_lock_1', 'rule_luck_award', '兜底奖品随机积分', '101:1,100', '2024-01-27 10:03:09', '2024-02-15 07:51:00');
INSERT INTO `rule_tree_node` VALUES (3, 'tree_lock_1', 'rule_stock', '库存扣减规则', NULL, '2024-01-27 10:04:43', '2024-02-15 07:51:02');
INSERT INTO `rule_tree_node` VALUES (4, 'tree_luck_award', 'rule_stock', '库存扣减规则', NULL, '2024-02-15 07:35:55', '2024-02-15 07:39:19');
INSERT INTO `rule_tree_node` VALUES (5, 'tree_luck_award', 'rule_luck_award', '兜底奖品随机积分', '101:1,100', '2024-02-15 07:35:55', '2024-02-15 07:39:23');
INSERT INTO `rule_tree_node` VALUES (6, 'tree_lock_2', 'rule_lock', '限定用户已完成N次抽奖后解锁', '2', '2024-01-27 10:03:09', '2024-02-15 07:52:20');
INSERT INTO `rule_tree_node` VALUES (7, 'tree_lock_2', 'rule_luck_award', '兜底奖品随机积分', '101:1,100', '2024-01-27 10:03:09', '2024-02-08 19:59:43');
INSERT INTO `rule_tree_node` VALUES (8, 'tree_lock_2', 'rule_stock', '库存扣减规则', NULL, '2024-01-27 10:04:43', '2024-02-03 10:40:21');

-- ----------------------------
-- Table structure for rule_tree_node_line
-- ----------------------------
DROP TABLE IF EXISTS `rule_tree_node_line`;
CREATE TABLE `rule_tree_node_line`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `tree_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '规则树ID',
  `rule_node_key` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '规则Key节点 From',
  `rule_child_node` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '规则Key节点 To',
  `rule_limit_type` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '限定类型；1:=;2:>;3:<;4:>=;5<=;6:enum[枚举范围];',
  `rule_limit_value` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '限定值（到下个节点）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '规则表-树节点连线' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of rule_tree_node_line
-- ----------------------------
INSERT INTO `rule_tree_node_line` VALUES (1, 'tree_lock_1', 'rule_lock', 'rule_stock', 'EQUAL', 'PASS', '0000-00-00 00:00:00', '2024-12-08 01:35:17');
INSERT INTO `rule_tree_node_line` VALUES (2, 'tree_lock_1', 'rule_lock', 'rule_luck_award', 'EQUAL', 'BLOCK', '0000-00-00 00:00:00', '2024-12-08 01:35:24');
INSERT INTO `rule_tree_node_line` VALUES (3, 'tree_lock_1', 'rule_stock', 'rule_luck_award', 'EQUAL', 'BLOCK', '0000-00-00 00:00:00', '2024-12-13 01:08:01');
INSERT INTO `rule_tree_node_line` VALUES (4, 'tree_luck_award', 'rule_stock', 'rule_luck_award', 'EQUAL', 'BLOCK', '2024-02-15 07:37:31', '2024-12-13 01:08:08');
INSERT INTO `rule_tree_node_line` VALUES (5, 'tree_lock_2', 'rule_lock', 'rule_stock', 'EQUAL', 'PASS', '0000-00-00 00:00:00', '2024-12-08 01:35:30');
INSERT INTO `rule_tree_node_line` VALUES (6, 'tree_lock_2', 'rule_lock', 'rule_luck_award', 'EQUAL', 'BLOCK', '0000-00-00 00:00:00', '2024-12-08 01:35:36');
INSERT INTO `rule_tree_node_line` VALUES (7, 'tree_lock_2', 'rule_stock', 'rule_luck_award', 'EQUAL', 'BLOCK', '0000-00-00 00:00:00', '2024-12-13 01:08:30');

-- ----------------------------
-- Table structure for strategy
-- ----------------------------
DROP TABLE IF EXISTS `strategy`;
CREATE TABLE `strategy`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `strategy_id` bigint NOT NULL COMMENT '抽奖策略ID',
  `strategy_desc` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '抽奖策略描述',
  `rule_models` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '规则模型，rule配置的模型同步到此表，便于使用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_strategy_id`(`strategy_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '抽奖策略' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of strategy
-- ----------------------------
INSERT INTO `strategy` VALUES (1, 100001, '抽奖策略', 'rule_blacklist,rule_weight', '2023-12-09 09:37:19', '2024-01-20 11:39:23');
INSERT INTO `strategy` VALUES (2, 100003, '抽奖策略-验证lock', NULL, '2024-01-13 10:34:06', '2024-04-03 16:03:21');
INSERT INTO `strategy` VALUES (3, 100002, '抽奖策略-非完整1概率', NULL, '2023-12-09 09:37:19', '2024-02-03 10:14:17');
INSERT INTO `strategy` VALUES (4, 100004, '抽奖策略-随机抽奖', NULL, '2023-12-09 09:37:19', '2024-01-20 19:21:03');
INSERT INTO `strategy` VALUES (5, 100005, '抽奖策略-测试概率计算', NULL, '2023-12-09 09:37:19', '2024-01-21 21:54:58');
INSERT INTO `strategy` VALUES (6, 100006, '抽奖策略-规则树', NULL, '2024-02-03 09:53:40', '2024-02-03 09:53:40');

-- ----------------------------
-- Table structure for strategy_award
-- ----------------------------
DROP TABLE IF EXISTS `strategy_award`;
CREATE TABLE `strategy_award`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `strategy_id` bigint NOT NULL COMMENT '抽奖策略ID',
  `award_id` int NOT NULL COMMENT '抽奖奖品ID - 内部流转使用',
  `award_title` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '抽奖奖品标题',
  `award_subtitle` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '抽奖奖品副标题',
  `award_count` int NOT NULL DEFAULT 0 COMMENT '奖品库存总量',
  `award_count_surplus` int NOT NULL DEFAULT 0 COMMENT '奖品库存剩余',
  `rate` decimal(6, 4) NOT NULL COMMENT '奖品中奖概率',
  `models` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '规则模型，rule配置的模型同步到此表，便于使用',
  `sort` int NOT NULL DEFAULT 0 COMMENT '排序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_strategy_id_award_id`(`strategy_id` ASC, `award_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '抽奖策略奖品概率' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of strategy_award
-- ----------------------------
INSERT INTO `strategy_award` VALUES (1, 100001, 101, '随机积分', NULL, 80000, 79998, 0.3000, 'tree_luck_award', 1, '2023-12-09 09:38:31', '2024-02-25 22:40:50');
INSERT INTO `strategy_award` VALUES (2, 100001, 102, '5次使用', NULL, 10000, 9999, 0.2000, 'tree_luck_award', 2, '2023-12-09 09:39:18', '2024-02-25 22:40:55');
INSERT INTO `strategy_award` VALUES (3, 100001, 103, '10次使用', NULL, 5000, 4998, 0.2000, 'tree_luck_award', 3, '2023-12-09 09:42:36', '2024-02-25 22:40:45');
INSERT INTO `strategy_award` VALUES (4, 100001, 104, '20次使用', NULL, 4000, 3999, 0.1000, 'tree_luck_award', 4, '2023-12-09 09:43:15', '2024-02-25 22:40:00');
INSERT INTO `strategy_award` VALUES (5, 100001, 105, '增加gpt-4对话模型', NULL, 600, 600, 0.1000, 'tree_luck_award', 5, '2023-12-09 09:43:47', '2024-02-15 07:42:13');
INSERT INTO `strategy_award` VALUES (6, 100001, 106, '增加dall-e-2画图模型', NULL, 200, 200, 0.0500, 'tree_luck_award', 6, '2023-12-09 09:44:20', '2024-02-15 07:42:14');
INSERT INTO `strategy_award` VALUES (7, 100001, 107, '增加dall-e-3画图模型', '抽奖1次后解锁', 200, 200, 0.0400, 'tree_luck_award', 7, '2023-12-09 09:45:38', '2024-02-15 07:42:17');
INSERT INTO `strategy_award` VALUES (8, 100001, 108, '增加100次使用', '抽奖2次后解锁', 199, 199, 0.0099, 'tree_luck_award', 8, '2023-12-09 09:46:02', '2024-02-15 07:42:21');
INSERT INTO `strategy_award` VALUES (9, 100001, 109, '解锁全部模型', '抽奖6次后解锁', 1, 1, 0.0001, 'tree_luck_award', 9, '2023-12-09 09:46:39', '2024-02-15 07:42:26');
INSERT INTO `strategy_award` VALUES (10, 100002, 101, '随机积分', NULL, 1, 1, 0.5000, 'tree_luck_award', 1, '2023-12-09 09:46:39', '2024-02-15 07:42:29');
INSERT INTO `strategy_award` VALUES (11, 100002, 102, '5次使用', NULL, 1, 1, 0.1000, 'tree_luck_award', 2, '2023-12-09 09:46:39', '2024-02-15 07:42:32');
INSERT INTO `strategy_award` VALUES (12, 100002, 106, '增加dall-e-2画图模型', NULL, 1, 1, 0.0100, 'tree_luck_award', 3, '2023-12-09 09:46:39', '2024-02-15 07:42:35');
INSERT INTO `strategy_award` VALUES (13, 100003, 107, '增加dall-e-3画图模型', '抽奖1次后解锁', 200, 200, 0.0400, 'tree_luck_award', 7, '2023-12-09 09:45:38', '2024-02-15 07:42:38');
INSERT INTO `strategy_award` VALUES (14, 100003, 108, '增加100次使用', '抽奖2次后解锁', 199, 199, 0.0099, 'tree_luck_award', 8, '2023-12-09 09:46:02', '2024-02-15 07:42:41');
INSERT INTO `strategy_award` VALUES (15, 100003, 109, '解锁全部模型', '抽奖6次后解锁', 1, 1, 0.0001, 'tree_luck_award', 9, '2023-12-09 09:46:39', '2024-02-15 07:42:44');
INSERT INTO `strategy_award` VALUES (16, 100004, 109, '解锁全部模型', '抽奖6次后解锁', 1, 1, 1.0000, 'tree_luck_award', 9, '2023-12-09 09:46:39', '2024-02-15 07:42:46');
INSERT INTO `strategy_award` VALUES (17, 100005, 101, '随机积分', NULL, 80000, 80000, 0.0300, 'tree_luck_award', 1, '2023-12-09 09:38:31', '2024-02-15 07:42:47');
INSERT INTO `strategy_award` VALUES (18, 100005, 102, '随机积分', NULL, 80000, 80000, 0.0300, 'tree_luck_award', 1, '2023-12-09 09:38:31', '2024-02-15 07:42:48');
INSERT INTO `strategy_award` VALUES (19, 100005, 103, '随机积分', NULL, 80000, 80000, 0.0300, 'tree_luck_award', 1, '2023-12-09 09:38:31', '2024-02-15 07:42:50');
INSERT INTO `strategy_award` VALUES (20, 100005, 104, '随机积分', NULL, 80000, 80000, 0.0300, 'tree_luck_award', 1, '2023-12-09 09:38:31', '2024-02-15 07:42:51');
INSERT INTO `strategy_award` VALUES (21, 100005, 105, '随机积分', NULL, 80000, 80000, 0.0010, 'tree_luck_award', 1, '2023-12-09 09:38:31', '2024-02-15 07:42:52');
INSERT INTO `strategy_award` VALUES (22, 100006, 101, '随机积分', NULL, 100, 62, 0.0200, 'tree_luck_award', 1, '2023-12-09 09:38:31', '2024-12-13 00:27:37');
INSERT INTO `strategy_award` VALUES (23, 100006, 102, '7等奖', NULL, 100, 27, 0.0300, 'tree_luck_award', 2, '2023-12-09 09:38:31', '2024-12-13 00:34:37');
INSERT INTO `strategy_award` VALUES (24, 100006, 103, '6等奖', NULL, 100, 43, 0.0300, 'tree_luck_award', 3, '2023-12-09 09:38:31', '2024-12-14 16:54:34');
INSERT INTO `strategy_award` VALUES (25, 100006, 104, '5等奖', NULL, 100, 39, 0.0300, 'tree_luck_award', 4, '2023-12-09 09:38:31', '2024-12-14 15:26:27');
INSERT INTO `strategy_award` VALUES (26, 100006, 105, '4等奖', NULL, 100, 35, 0.0300, 'tree_luck_award', 5, '2023-12-09 09:38:31', '2024-12-14 16:55:34');
INSERT INTO `strategy_award` VALUES (27, 100006, 106, '3等奖', '抽奖1次后解锁', 100, 28, 0.0300, 'tree_lock_1', 6, '2023-12-09 09:38:31', '2024-12-14 16:54:35');
INSERT INTO `strategy_award` VALUES (28, 100006, 107, '2等奖', '抽奖1次后解锁', 100, 28, 0.0300, 'tree_lock_1', 7, '2023-12-09 09:38:31', '2024-12-13 01:14:37');
INSERT INTO `strategy_award` VALUES (29, 100006, 108, '1等奖', '抽奖2次后解锁', 100, 35, 0.0300, 'tree_lock_2', 8, '2023-12-09 09:38:31', '2024-02-29 08:06:35');

-- ----------------------------
-- Table structure for task
-- ----------------------------
DROP TABLE IF EXISTS `task`;
CREATE TABLE `task`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户id',
  `topic` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '消息主题',
  `message_id` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '信息id',
  `message` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '消息主体',
  `status` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'create' COMMENT '任务状态；create-创建、completed-完成、fail-失败',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 213 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '任务表，发送MQ' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of task
-- ----------------------------
INSERT INTO `task` VALUES (1, 'rem', 'send_award', 'eCCJSPOMkC9', '{\"eventId\":\"eCCJSPOMkC9\",\"timestamp\":\"Dec 8, 2024 11:50:16 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:50:17', '2024-12-08 23:50:03');
INSERT INTO `task` VALUES (2, 'rem', 'send_award', 'ToCvMz5kjbf', '{\"eventId\":\"ToCvMz5kjbf\",\"timestamp\":\"Dec 8, 2024 11:50:18 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:50:19', '2024-12-08 23:50:04');
INSERT INTO `task` VALUES (3, 'rem', 'send_award', 'fH264V69O80', '{\"eventId\":\"fH264V69O80\",\"timestamp\":\"Dec 8, 2024 11:50:19 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:50:20', '2024-12-08 23:50:05');
INSERT INTO `task` VALUES (4, 'rem', 'send_award', 'cRQdjexGnEO', '{\"eventId\":\"cRQdjexGnEO\",\"timestamp\":\"Dec 8, 2024 11:50:21 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:50:21', '2024-12-08 23:50:06');
INSERT INTO `task` VALUES (5, 'rem', 'send_award', 'MrAqAMAYzQX', '{\"eventId\":\"MrAqAMAYzQX\",\"timestamp\":\"Dec 8, 2024 11:50:22 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:50:22', '2024-12-08 23:50:08');
INSERT INTO `task` VALUES (6, 'rem', 'send_award', 'fwoeqEQnMEB', '{\"eventId\":\"fwoeqEQnMEB\",\"timestamp\":\"Dec 8, 2024 11:50:23 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:50:23', '2024-12-08 23:50:09');
INSERT INTO `task` VALUES (7, 'rem', 'send_award', 'm3wX4tR1Aq7', '{\"eventId\":\"m3wX4tR1Aq7\",\"timestamp\":\"Dec 8, 2024 11:50:24 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:50:24', '2024-12-08 23:50:10');
INSERT INTO `task` VALUES (8, 'rem', 'send_award', 'KQr9QqNLYds', '{\"eventId\":\"KQr9QqNLYds\",\"timestamp\":\"Dec 8, 2024 11:50:25 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:50:26', '2024-12-08 23:50:11');
INSERT INTO `task` VALUES (9, 'rem', 'send_award', '1khmaZ5TvPS', '{\"eventId\":\"1khmaZ5TvPS\",\"timestamp\":\"Dec 8, 2024 11:50:26 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:50:27', '2024-12-08 23:50:12');
INSERT INTO `task` VALUES (10, 'rem', 'send_award', 'cMsSWPmyWhk', '{\"eventId\":\"cMsSWPmyWhk\",\"timestamp\":\"Dec 8, 2024 11:50:27 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:50:28', '2024-12-08 23:50:13');
INSERT INTO `task` VALUES (11, 'rem', 'send_award', '14ighRMMgeN', '{\"eventId\":\"14ighRMMgeN\",\"timestamp\":\"Dec 8, 2024 11:50:29 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:50:29', '2024-12-08 23:50:15');
INSERT INTO `task` VALUES (12, 'rem', 'send_award', 'kMoNVqGhZU5', '{\"eventId\":\"kMoNVqGhZU5\",\"timestamp\":\"Dec 8, 2024 11:50:30 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:50:30', '2024-12-08 23:50:16');
INSERT INTO `task` VALUES (13, 'rem', 'send_award', 'esJTCI7Svse', '{\"eventId\":\"esJTCI7Svse\",\"timestamp\":\"Dec 8, 2024 11:50:31 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:50:31', '2024-12-08 23:50:17');
INSERT INTO `task` VALUES (14, 'rem', 'send_award', 'FzJ8tPvQbPd', '{\"eventId\":\"FzJ8tPvQbPd\",\"timestamp\":\"Dec 8, 2024 11:50:32 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:50:33', '2024-12-08 23:50:18');
INSERT INTO `task` VALUES (15, 'rem', 'send_award', 'lgvCFvkIJWL', '{\"eventId\":\"lgvCFvkIJWL\",\"timestamp\":\"Dec 8, 2024 11:50:33 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:50:34', '2024-12-08 23:50:19');
INSERT INTO `task` VALUES (16, 'rem', 'send_award', 'loZ725SJymf', '{\"eventId\":\"loZ725SJymf\",\"timestamp\":\"Dec 8, 2024 11:50:34 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:50:35', '2024-12-08 23:50:20');
INSERT INTO `task` VALUES (17, 'rem', 'send_award', 'BnZxXuZkzdW', '{\"eventId\":\"BnZxXuZkzdW\",\"timestamp\":\"Dec 8, 2024 11:50:36 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:50:36', '2024-12-08 23:50:22');
INSERT INTO `task` VALUES (18, 'rem', 'send_award', 'dVwYUQx8Haj', '{\"eventId\":\"dVwYUQx8Haj\",\"timestamp\":\"Dec 8, 2024 11:50:37 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:50:37', '2024-12-08 23:50:23');
INSERT INTO `task` VALUES (19, 'rem', 'send_award', 'aAOP1NROJpT', '{\"eventId\":\"aAOP1NROJpT\",\"timestamp\":\"Dec 8, 2024 11:50:38 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:50:39', '2024-12-08 23:50:24');
INSERT INTO `task` VALUES (20, 'rem', 'send_award', '5WIvziLSTvt', '{\"eventId\":\"5WIvziLSTvt\",\"timestamp\":\"Dec 8, 2024 11:50:39 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:50:40', '2024-12-08 23:50:25');
INSERT INTO `task` VALUES (21, 'rem', 'send_award', 'RjJ9ro8kzsE', '{\"eventId\":\"RjJ9ro8kzsE\",\"timestamp\":\"Dec 8, 2024 11:50:40 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:50:41', '2024-12-08 23:50:27');
INSERT INTO `task` VALUES (22, 'rem', 'send_award', 'cf8VRYC9I8l', '{\"eventId\":\"cf8VRYC9I8l\",\"timestamp\":\"Dec 8, 2024 11:50:42 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:50:42', '2024-12-08 23:50:28');
INSERT INTO `task` VALUES (23, 'rem', 'send_award', 'Bng8xhDQ5va', '{\"eventId\":\"Bng8xhDQ5va\",\"timestamp\":\"Dec 8, 2024 11:50:43 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:50:44', '2024-12-08 23:50:29');
INSERT INTO `task` VALUES (24, 'rem', 'send_award', '70qY8koTESl', '{\"eventId\":\"70qY8koTESl\",\"timestamp\":\"Dec 8, 2024 11:50:44 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:50:45', '2024-12-08 23:50:30');
INSERT INTO `task` VALUES (25, 'rem', 'send_award', 'iBkHpUe4Zuy', '{\"eventId\":\"iBkHpUe4Zuy\",\"timestamp\":\"Dec 8, 2024 11:50:45 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:50:46', '2024-12-08 23:50:31');
INSERT INTO `task` VALUES (26, 'rem', 'send_award', 'fq1Ex60TafP', '{\"eventId\":\"fq1Ex60TafP\",\"timestamp\":\"Dec 8, 2024 11:50:47 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:50:47', '2024-12-08 23:50:32');
INSERT INTO `task` VALUES (27, 'rem', 'send_award', 'iGyhN3AcItb', '{\"eventId\":\"iGyhN3AcItb\",\"timestamp\":\"Dec 8, 2024 11:50:48 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:50:48', '2024-12-08 23:50:34');
INSERT INTO `task` VALUES (28, 'rem', 'send_award', 'uXAVaWOOxdB', '{\"eventId\":\"uXAVaWOOxdB\",\"timestamp\":\"Dec 8, 2024 11:50:49 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:50:49', '2024-12-08 23:50:35');
INSERT INTO `task` VALUES (29, 'rem', 'send_award', 'TKI2WCFNaIS', '{\"eventId\":\"TKI2WCFNaIS\",\"timestamp\":\"Dec 8, 2024 11:50:50 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:50:51', '2024-12-08 23:50:36');
INSERT INTO `task` VALUES (30, 'rem', 'send_award', 's70GeungUA3', '{\"eventId\":\"s70GeungUA3\",\"timestamp\":\"Dec 8, 2024 11:50:51 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:50:52', '2024-12-08 23:50:37');
INSERT INTO `task` VALUES (31, 'rem', 'send_award', 'uEuXDACi1qf', '{\"eventId\":\"uEuXDACi1qf\",\"timestamp\":\"Dec 8, 2024 11:50:52 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:50:53', '2024-12-08 23:50:38');
INSERT INTO `task` VALUES (32, 'rem', 'send_award', '2rkjSXcORCX', '{\"eventId\":\"2rkjSXcORCX\",\"timestamp\":\"Dec 8, 2024 11:50:53 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:50:54', '2024-12-08 23:50:39');
INSERT INTO `task` VALUES (33, 'rem', 'send_award', 'Qa3eXS2xAag', '{\"eventId\":\"Qa3eXS2xAag\",\"timestamp\":\"Dec 8, 2024 11:50:55 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:50:55', '2024-12-08 23:50:41');
INSERT INTO `task` VALUES (34, 'rem', 'send_award', 'sxWCOxDsO1x', '{\"eventId\":\"sxWCOxDsO1x\",\"timestamp\":\"Dec 8, 2024 11:50:56 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:50:56', '2024-12-08 23:50:42');
INSERT INTO `task` VALUES (35, 'rem', 'send_award', 'mOP3ql9waI4', '{\"eventId\":\"mOP3ql9waI4\",\"timestamp\":\"Dec 8, 2024 11:50:57 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:50:57', '2024-12-08 23:50:43');
INSERT INTO `task` VALUES (36, 'rem', 'send_award', 'PDEwbuXxKKt', '{\"eventId\":\"PDEwbuXxKKt\",\"timestamp\":\"Dec 8, 2024 11:50:58 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:50:59', '2024-12-08 23:50:44');
INSERT INTO `task` VALUES (37, 'rem', 'send_award', 'qZsB40BNJw0', '{\"eventId\":\"qZsB40BNJw0\",\"timestamp\":\"Dec 8, 2024 11:50:59 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:51:00', '2024-12-08 23:50:45');
INSERT INTO `task` VALUES (38, 'rem', 'send_award', 'YN74BNokZS6', '{\"eventId\":\"YN74BNokZS6\",\"timestamp\":\"Dec 8, 2024 11:51:01 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:51:01', '2024-12-08 23:50:47');
INSERT INTO `task` VALUES (39, 'rem', 'send_award', 'NAMs9KE71Je', '{\"eventId\":\"NAMs9KE71Je\",\"timestamp\":\"Dec 8, 2024 11:51:02 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:51:02', '2024-12-08 23:50:48');
INSERT INTO `task` VALUES (40, 'rem', 'send_award', 'DtCoGleX9AL', '{\"eventId\":\"DtCoGleX9AL\",\"timestamp\":\"Dec 8, 2024 11:51:03 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:51:04', '2024-12-08 23:50:49');
INSERT INTO `task` VALUES (41, 'rem', 'send_award', 'vtThpkSBZHV', '{\"eventId\":\"vtThpkSBZHV\",\"timestamp\":\"Dec 8, 2024 11:51:04 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:51:05', '2024-12-08 23:50:50');
INSERT INTO `task` VALUES (42, 'rem', 'send_award', 'aUTaYPqfkjD', '{\"eventId\":\"aUTaYPqfkjD\",\"timestamp\":\"Dec 8, 2024 11:51:06 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:51:06', '2024-12-08 23:50:52');
INSERT INTO `task` VALUES (43, 'rem', 'send_award', 'O6RRtBFP8kR', '{\"eventId\":\"O6RRtBFP8kR\",\"timestamp\":\"Dec 8, 2024 11:51:07 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:51:08', '2024-12-08 23:50:53');
INSERT INTO `task` VALUES (44, 'rem', 'send_award', 'IwRTZlGBpsC', '{\"eventId\":\"IwRTZlGBpsC\",\"timestamp\":\"Dec 8, 2024 11:51:09 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:51:09', '2024-12-08 23:50:54');
INSERT INTO `task` VALUES (45, 'rem', 'send_award', 'UjEwJ7EjmwL', '{\"eventId\":\"UjEwJ7EjmwL\",\"timestamp\":\"Dec 8, 2024 11:51:10 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:51:10', '2024-12-08 23:50:56');
INSERT INTO `task` VALUES (46, 'rem', 'send_award', 'TRt9ii801DA', '{\"eventId\":\"TRt9ii801DA\",\"timestamp\":\"Dec 8, 2024 11:51:11 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:51:11', '2024-12-08 23:50:57');
INSERT INTO `task` VALUES (47, 'rem', 'send_award', 'zyzqsjZ1PBB', '{\"eventId\":\"zyzqsjZ1PBB\",\"timestamp\":\"Dec 8, 2024 11:51:12 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:51:12', '2024-12-08 23:50:58');
INSERT INTO `task` VALUES (48, 'rem', 'send_award', '0GC7eljAczm', '{\"eventId\":\"0GC7eljAczm\",\"timestamp\":\"Dec 8, 2024 11:51:13 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:51:14', '2024-12-08 23:51:00');
INSERT INTO `task` VALUES (49, 'rem', 'send_award', 'oYVDkVnLVnh', '{\"eventId\":\"oYVDkVnLVnh\",\"timestamp\":\"Dec 8, 2024 11:51:15 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:51:15', '2024-12-08 23:51:01');
INSERT INTO `task` VALUES (50, 'rem', 'send_award', 'tqFYewMpPeL', '{\"eventId\":\"tqFYewMpPeL\",\"timestamp\":\"Dec 8, 2024 11:51:16 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:51:16', '2024-12-08 23:51:02');
INSERT INTO `task` VALUES (51, 'rem', 'send_award', 'dB4Ylxyjk1W', '{\"eventId\":\"dB4Ylxyjk1W\",\"timestamp\":\"Dec 8, 2024 11:51:17 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:51:17', '2024-12-08 23:51:03');
INSERT INTO `task` VALUES (52, 'rem', 'send_award', 's7uI3ywlUFD', '{\"eventId\":\"s7uI3ywlUFD\",\"timestamp\":\"Dec 8, 2024 11:51:18 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:51:19', '2024-12-08 23:51:04');
INSERT INTO `task` VALUES (53, 'rem', 'send_award', 'n2tmuJDm3gp', '{\"eventId\":\"n2tmuJDm3gp\",\"timestamp\":\"Dec 8, 2024 11:51:20 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:51:20', '2024-12-08 23:51:06');
INSERT INTO `task` VALUES (54, 'rem', 'send_award', 'ME1qwvaJCXJ', '{\"eventId\":\"ME1qwvaJCXJ\",\"timestamp\":\"Dec 8, 2024 11:51:21 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:51:21', '2024-12-08 23:51:07');
INSERT INTO `task` VALUES (55, 'rem', 'send_award', 'oueQRFbrfoC', '{\"eventId\":\"oueQRFbrfoC\",\"timestamp\":\"Dec 8, 2024 11:51:22 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:51:23', '2024-12-08 23:51:08');
INSERT INTO `task` VALUES (56, 'rem', 'send_award', '9NkVexTuYwH', '{\"eventId\":\"9NkVexTuYwH\",\"timestamp\":\"Dec 8, 2024 11:51:24 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:51:24', '2024-12-08 23:51:10');
INSERT INTO `task` VALUES (57, 'rem', 'send_award', 'H8C3c34xsdM', '{\"eventId\":\"H8C3c34xsdM\",\"timestamp\":\"Dec 8, 2024 11:51:25 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:51:25', '2024-12-08 23:51:11');
INSERT INTO `task` VALUES (58, 'rem', 'send_award', 'x1TJZIoJKag', '{\"eventId\":\"x1TJZIoJKag\",\"timestamp\":\"Dec 8, 2024 11:51:26 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:51:26', '2024-12-08 23:51:13');
INSERT INTO `task` VALUES (59, 'rem', 'send_award', '802xPJ2iHpl', '{\"eventId\":\"802xPJ2iHpl\",\"timestamp\":\"Dec 8, 2024 11:51:28 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:51:28', '2024-12-08 23:51:14');
INSERT INTO `task` VALUES (60, 'rem', 'send_award', '3LnxZOcYMsm', '{\"eventId\":\"3LnxZOcYMsm\",\"timestamp\":\"Dec 8, 2024 11:51:29 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:51:30', '2024-12-08 23:51:15');
INSERT INTO `task` VALUES (61, 'rem', 'send_award', 'KmepRm7vI97', '{\"eventId\":\"KmepRm7vI97\",\"timestamp\":\"Dec 8, 2024 11:51:30 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:51:31', '2024-12-08 23:51:16');
INSERT INTO `task` VALUES (62, 'rem', 'send_award', 'dCBlWM5I2jq', '{\"eventId\":\"dCBlWM5I2jq\",\"timestamp\":\"Dec 8, 2024 11:51:31 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:51:32', '2024-12-08 23:51:18');
INSERT INTO `task` VALUES (63, 'rem', 'send_award', 'oxLuKY7coa8', '{\"eventId\":\"oxLuKY7coa8\",\"timestamp\":\"Dec 8, 2024 11:51:33 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:51:33', '2024-12-08 23:51:19');
INSERT INTO `task` VALUES (64, 'rem', 'send_award', 'xXfSm45A836', '{\"eventId\":\"xXfSm45A836\",\"timestamp\":\"Dec 8, 2024 11:51:34 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:51:35', '2024-12-08 23:51:20');
INSERT INTO `task` VALUES (65, 'rem', 'send_award', 'HW4DiZCh3eX', '{\"eventId\":\"HW4DiZCh3eX\",\"timestamp\":\"Dec 8, 2024 11:51:35 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:51:36', '2024-12-08 23:51:21');
INSERT INTO `task` VALUES (66, 'rem', 'send_award', 'TyBcdpCNSb1', '{\"eventId\":\"TyBcdpCNSb1\",\"timestamp\":\"Dec 8, 2024 11:51:37 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:51:37', '2024-12-08 23:51:23');
INSERT INTO `task` VALUES (67, 'rem', 'send_award', 'XGiykWPFmjJ', '{\"eventId\":\"XGiykWPFmjJ\",\"timestamp\":\"Dec 8, 2024 11:51:38 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:51:38', '2024-12-08 23:51:24');
INSERT INTO `task` VALUES (68, 'rem', 'send_award', 'sH8NZH1DYoN', '{\"eventId\":\"sH8NZH1DYoN\",\"timestamp\":\"Dec 8, 2024 11:51:39 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:51:40', '2024-12-08 23:51:25');
INSERT INTO `task` VALUES (69, 'rem', 'send_award', 'OmPgkSRvTLk', '{\"eventId\":\"OmPgkSRvTLk\",\"timestamp\":\"Dec 8, 2024 11:51:40 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:51:41', '2024-12-08 23:51:26');
INSERT INTO `task` VALUES (70, 'rem', 'send_award', 'q5hVeJqh9xL', '{\"eventId\":\"q5hVeJqh9xL\",\"timestamp\":\"Dec 8, 2024 11:51:42 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:51:42', '2024-12-08 23:51:28');
INSERT INTO `task` VALUES (71, 'rem', 'send_award', '0OqkGn5F1Lf', '{\"eventId\":\"0OqkGn5F1Lf\",\"timestamp\":\"Dec 8, 2024 11:51:43 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:51:44', '2024-12-08 23:51:29');
INSERT INTO `task` VALUES (72, 'rem', 'send_award', 'p56OCMJNasK', '{\"eventId\":\"p56OCMJNasK\",\"timestamp\":\"Dec 8, 2024 11:51:44 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:51:45', '2024-12-08 23:51:30');
INSERT INTO `task` VALUES (73, 'rem', 'send_award', 'ZR036J17Gb4', '{\"eventId\":\"ZR036J17Gb4\",\"timestamp\":\"Dec 8, 2024 11:51:46 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:51:46', '2024-12-08 23:51:32');
INSERT INTO `task` VALUES (74, 'rem', 'send_award', 'k7yhMTphSsj', '{\"eventId\":\"k7yhMTphSsj\",\"timestamp\":\"Dec 8, 2024 11:51:47 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:51:47', '2024-12-08 23:51:33');
INSERT INTO `task` VALUES (75, 'rem', 'send_award', 'nRPi6pPoZ2z', '{\"eventId\":\"nRPi6pPoZ2z\",\"timestamp\":\"Dec 8, 2024 11:51:48 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:51:49', '2024-12-08 23:51:34');
INSERT INTO `task` VALUES (76, 'rem', 'send_award', 'M9hiXEDymzV', '{\"eventId\":\"M9hiXEDymzV\",\"timestamp\":\"Dec 8, 2024 11:51:50 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:51:50', '2024-12-08 23:51:35');
INSERT INTO `task` VALUES (77, 'rem', 'send_award', 'E14fp4wTcj1', '{\"eventId\":\"E14fp4wTcj1\",\"timestamp\":\"Dec 8, 2024 11:51:51 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:51:51', '2024-12-08 23:51:37');
INSERT INTO `task` VALUES (78, 'rem', 'send_award', 'Ro3nfggI4vX', '{\"eventId\":\"Ro3nfggI4vX\",\"timestamp\":\"Dec 8, 2024 11:51:52 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:51:52', '2024-12-08 23:51:38');
INSERT INTO `task` VALUES (79, 'rem', 'send_award', 'izinpKazD6d', '{\"eventId\":\"izinpKazD6d\",\"timestamp\":\"Dec 8, 2024 11:51:53 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:51:54', '2024-12-08 23:51:39');
INSERT INTO `task` VALUES (80, 'rem', 'send_award', 'FxlEzM6zaop', '{\"eventId\":\"FxlEzM6zaop\",\"timestamp\":\"Dec 8, 2024 11:51:54 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:51:55', '2024-12-08 23:51:40');
INSERT INTO `task` VALUES (81, 'rem', 'send_award', 'ScIiEv4nchE', '{\"eventId\":\"ScIiEv4nchE\",\"timestamp\":\"Dec 8, 2024 11:51:55 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:51:56', '2024-12-08 23:51:41');
INSERT INTO `task` VALUES (82, 'rem', 'send_award', 'NgCPXi7oOen', '{\"eventId\":\"NgCPXi7oOen\",\"timestamp\":\"Dec 8, 2024 11:51:56 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:51:57', '2024-12-08 23:51:42');
INSERT INTO `task` VALUES (83, 'rem', 'send_award', 'vkV40Dh2ml7', '{\"eventId\":\"vkV40Dh2ml7\",\"timestamp\":\"Dec 8, 2024 11:51:58 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:51:58', '2024-12-08 23:51:44');
INSERT INTO `task` VALUES (84, 'rem', 'send_award', 'nzMeO6l6CJj', '{\"eventId\":\"nzMeO6l6CJj\",\"timestamp\":\"Dec 8, 2024 11:51:59 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:51:59', '2024-12-08 23:51:45');
INSERT INTO `task` VALUES (85, 'rem', 'send_award', 'tORBArXKouD', '{\"eventId\":\"tORBArXKouD\",\"timestamp\":\"Dec 8, 2024 11:52:00 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:52:01', '2024-12-08 23:51:46');
INSERT INTO `task` VALUES (86, 'rem', 'send_award', 'Ge6aSogwG7a', '{\"eventId\":\"Ge6aSogwG7a\",\"timestamp\":\"Dec 8, 2024 11:52:01 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:52:02', '2024-12-08 23:51:47');
INSERT INTO `task` VALUES (87, 'rem', 'send_award', 'QXJrr6I70zi', '{\"eventId\":\"QXJrr6I70zi\",\"timestamp\":\"Dec 8, 2024 11:52:03 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:52:03', '2024-12-08 23:51:49');
INSERT INTO `task` VALUES (88, 'rem', 'send_award', 'RfFLCCeqzmN', '{\"eventId\":\"RfFLCCeqzmN\",\"timestamp\":\"Dec 8, 2024 11:52:04 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:52:04', '2024-12-08 23:51:50');
INSERT INTO `task` VALUES (89, 'rem', 'send_award', 'XHDjaR2pXPM', '{\"eventId\":\"XHDjaR2pXPM\",\"timestamp\":\"Dec 8, 2024 11:52:05 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:52:06', '2024-12-08 23:51:51');
INSERT INTO `task` VALUES (90, 'rem', 'send_award', 'exMttMLbe9M', '{\"eventId\":\"exMttMLbe9M\",\"timestamp\":\"Dec 8, 2024 11:52:07 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:52:07', '2024-12-08 23:51:53');
INSERT INTO `task` VALUES (91, 'rem', 'send_award', '2nDP3NQLwVc', '{\"eventId\":\"2nDP3NQLwVc\",\"timestamp\":\"Dec 8, 2024 11:52:08 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:52:08', '2024-12-08 23:51:54');
INSERT INTO `task` VALUES (92, 'rem', 'send_award', 'XCSuE0J49Y0', '{\"eventId\":\"XCSuE0J49Y0\",\"timestamp\":\"Dec 8, 2024 11:52:09 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:52:10', '2024-12-08 23:51:55');
INSERT INTO `task` VALUES (93, 'rem', 'send_award', 'rsiyNoCskPR', '{\"eventId\":\"rsiyNoCskPR\",\"timestamp\":\"Dec 8, 2024 11:52:11 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:52:11', '2024-12-08 23:51:57');
INSERT INTO `task` VALUES (94, 'rem', 'send_award', 'Fu6nT84mQX4', '{\"eventId\":\"Fu6nT84mQX4\",\"timestamp\":\"Dec 8, 2024 11:52:12 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:52:12', '2024-12-08 23:51:58');
INSERT INTO `task` VALUES (95, 'rem', 'send_award', 'RXqmmXgg2sT', '{\"eventId\":\"RXqmmXgg2sT\",\"timestamp\":\"Dec 8, 2024 11:52:13 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:52:14', '2024-12-08 23:51:59');
INSERT INTO `task` VALUES (96, 'rem', 'send_award', 'QDsrSu9cYaV', '{\"eventId\":\"QDsrSu9cYaV\",\"timestamp\":\"Dec 8, 2024 11:52:14 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:52:15', '2024-12-08 23:52:01');
INSERT INTO `task` VALUES (97, 'rem', 'send_award', 'hrHbHRmtbBa', '{\"eventId\":\"hrHbHRmtbBa\",\"timestamp\":\"Dec 8, 2024 11:52:16 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:52:16', '2024-12-08 23:52:02');
INSERT INTO `task` VALUES (98, 'rem', 'send_award', 'xtDdxYuZD8c', '{\"eventId\":\"xtDdxYuZD8c\",\"timestamp\":\"Dec 8, 2024 11:52:17 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:52:18', '2024-12-08 23:52:04');
INSERT INTO `task` VALUES (99, 'rem', 'send_award', 'eGWBFGM9yDN', '{\"eventId\":\"eGWBFGM9yDN\",\"timestamp\":\"Dec 8, 2024 11:52:19 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:52:19', '2024-12-08 23:52:05');
INSERT INTO `task` VALUES (100, 'rem', 'send_award', '7N3xrqI6Z22', '{\"eventId\":\"7N3xrqI6Z22\",\"timestamp\":\"Dec 8, 2024 11:52:20 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:52:20', '2024-12-08 23:52:06');
INSERT INTO `task` VALUES (101, 'rem', 'send_award', 'p6p4i7DNzqT', '{\"eventId\":\"p6p4i7DNzqT\",\"timestamp\":\"Dec 8, 2024 11:55:37 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:55:38', '2024-12-08 23:55:25');
INSERT INTO `task` VALUES (102, 'rem', 'send_award', '3Xiocm8YsS0', '{\"eventId\":\"3Xiocm8YsS0\",\"timestamp\":\"Dec 8, 2024 11:55:40 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:55:41', '2024-12-08 23:55:26');
INSERT INTO `task` VALUES (103, 'rem', 'send_award', 'zlGPGxcyRUw', '{\"eventId\":\"zlGPGxcyRUw\",\"timestamp\":\"Dec 8, 2024 11:55:41 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:55:42', '2024-12-08 23:55:27');
INSERT INTO `task` VALUES (104, 'rem', 'send_award', 'g6pfzLLi2V1', '{\"eventId\":\"g6pfzLLi2V1\",\"timestamp\":\"Dec 8, 2024 11:55:43 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:55:43', '2024-12-08 23:55:28');
INSERT INTO `task` VALUES (105, 'rem', 'send_award', 'Aw5n0SLOeHu', '{\"eventId\":\"Aw5n0SLOeHu\",\"timestamp\":\"Dec 8, 2024 11:55:44 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:55:44', '2024-12-08 23:55:30');
INSERT INTO `task` VALUES (106, 'rem', 'send_award', 'WhjvcTXv0fr', '{\"eventId\":\"WhjvcTXv0fr\",\"timestamp\":\"Dec 8, 2024 11:55:45 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:55:46', '2024-12-08 23:55:31');
INSERT INTO `task` VALUES (107, 'rem', 'send_award', 'lmxqI486A4o', '{\"eventId\":\"lmxqI486A4o\",\"timestamp\":\"Dec 8, 2024 11:55:46 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:55:47', '2024-12-08 23:55:32');
INSERT INTO `task` VALUES (108, 'rem', 'send_award', 'M0Leg4gXmyL', '{\"eventId\":\"M0Leg4gXmyL\",\"timestamp\":\"Dec 8, 2024 11:55:47 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:55:48', '2024-12-08 23:55:34');
INSERT INTO `task` VALUES (109, 'rem', 'send_award', 'CtHgqBHr6yg', '{\"eventId\":\"CtHgqBHr6yg\",\"timestamp\":\"Dec 8, 2024 11:55:49 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:55:49', '2024-12-08 23:55:35');
INSERT INTO `task` VALUES (110, 'rem', 'send_award', 'RmOwIEdnspg', '{\"eventId\":\"RmOwIEdnspg\",\"timestamp\":\"Dec 8, 2024 11:55:50 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:55:51', '2024-12-08 23:55:36');
INSERT INTO `task` VALUES (111, 'rem', 'send_award', 'Zb0xl8S71xK', '{\"eventId\":\"Zb0xl8S71xK\",\"timestamp\":\"Dec 8, 2024 11:55:51 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:55:52', '2024-12-08 23:55:37');
INSERT INTO `task` VALUES (112, 'rem', 'send_award', 'YfQ3D5WF1ey', '{\"eventId\":\"YfQ3D5WF1ey\",\"timestamp\":\"Dec 8, 2024 11:55:53 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:55:53', '2024-12-08 23:55:39');
INSERT INTO `task` VALUES (113, 'rem', 'send_award', 'm2LO3ccnXLE', '{\"eventId\":\"m2LO3ccnXLE\",\"timestamp\":\"Dec 8, 2024 11:55:54 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:55:55', '2024-12-08 23:55:40');
INSERT INTO `task` VALUES (114, 'rem', 'send_award', 'mU4K2FG4ZsB', '{\"eventId\":\"mU4K2FG4ZsB\",\"timestamp\":\"Dec 8, 2024 11:55:55 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:55:56', '2024-12-08 23:55:42');
INSERT INTO `task` VALUES (115, 'rem', 'send_award', 'wDhCDMKWtps', '{\"eventId\":\"wDhCDMKWtps\",\"timestamp\":\"Dec 8, 2024 11:55:57 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:55:57', '2024-12-08 23:55:43');
INSERT INTO `task` VALUES (116, 'rem', 'send_award', 'wF3PoqfpfUy', '{\"eventId\":\"wF3PoqfpfUy\",\"timestamp\":\"Dec 8, 2024 11:55:58 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:55:59', '2024-12-08 23:55:44');
INSERT INTO `task` VALUES (117, 'rem', 'send_award', 'gwXJCrPcqVG', '{\"eventId\":\"gwXJCrPcqVG\",\"timestamp\":\"Dec 8, 2024 11:55:59 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:56:00', '2024-12-08 23:55:45');
INSERT INTO `task` VALUES (118, 'rem', 'send_award', 'mKjcYx65JXf', '{\"eventId\":\"mKjcYx65JXf\",\"timestamp\":\"Dec 8, 2024 11:56:01 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:56:01', '2024-12-08 23:55:46');
INSERT INTO `task` VALUES (119, 'rem', 'send_award', 'QOhPMvPFkwN', '{\"eventId\":\"QOhPMvPFkwN\",\"timestamp\":\"Dec 8, 2024 11:56:02 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:56:02', '2024-12-08 23:55:48');
INSERT INTO `task` VALUES (120, 'rem', 'send_award', 'gB0UeJBM7Ex', '{\"eventId\":\"gB0UeJBM7Ex\",\"timestamp\":\"Dec 8, 2024 11:56:03 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:56:03', '2024-12-08 23:55:49');
INSERT INTO `task` VALUES (121, 'rem', 'send_award', 'OiywvY80ndT', '{\"eventId\":\"OiywvY80ndT\",\"timestamp\":\"Dec 8, 2024 11:56:04 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:56:05', '2024-12-08 23:55:50');
INSERT INTO `task` VALUES (122, 'rem', 'send_award', 'qPUHod313WO', '{\"eventId\":\"qPUHod313WO\",\"timestamp\":\"Dec 8, 2024 11:56:05 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:56:06', '2024-12-08 23:55:52');
INSERT INTO `task` VALUES (123, 'rem', 'send_award', 'pS6NBCxvasQ', '{\"eventId\":\"pS6NBCxvasQ\",\"timestamp\":\"Dec 8, 2024 11:56:07 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:56:08', '2024-12-08 23:55:53');
INSERT INTO `task` VALUES (124, 'rem', 'send_award', 'vtXij18KCAk', '{\"eventId\":\"vtXij18KCAk\",\"timestamp\":\"Dec 8, 2024 11:56:08 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:56:09', '2024-12-08 23:55:55');
INSERT INTO `task` VALUES (125, 'rem', 'send_award', '8FzFAES52e0', '{\"eventId\":\"8FzFAES52e0\",\"timestamp\":\"Dec 8, 2024 11:56:10 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:56:10', '2024-12-08 23:55:56');
INSERT INTO `task` VALUES (126, 'rem', 'send_award', 'XAlZauJdd2I', '{\"eventId\":\"XAlZauJdd2I\",\"timestamp\":\"Dec 8, 2024 11:56:11 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:56:12', '2024-12-08 23:55:58');
INSERT INTO `task` VALUES (127, 'rem', 'send_award', 'j4HKTWBnTu7', '{\"eventId\":\"j4HKTWBnTu7\",\"timestamp\":\"Dec 8, 2024 11:56:13 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:56:13', '2024-12-08 23:55:59');
INSERT INTO `task` VALUES (128, 'rem', 'send_award', 'hUh5s2EgY5O', '{\"eventId\":\"hUh5s2EgY5O\",\"timestamp\":\"Dec 8, 2024 11:56:14 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:56:15', '2024-12-08 23:56:01');
INSERT INTO `task` VALUES (129, 'rem', 'send_award', 'G74grRlnPT1', '{\"eventId\":\"G74grRlnPT1\",\"timestamp\":\"Dec 8, 2024 11:56:16 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:56:16', '2024-12-08 23:56:02');
INSERT INTO `task` VALUES (130, 'rem', 'send_award', 'Fa9z3YAvHyg', '{\"eventId\":\"Fa9z3YAvHyg\",\"timestamp\":\"Dec 8, 2024 11:56:17 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:56:18', '2024-12-08 23:56:04');
INSERT INTO `task` VALUES (131, 'rem', 'send_award', 'NMJWqANRK7R', '{\"eventId\":\"NMJWqANRK7R\",\"timestamp\":\"Dec 8, 2024 11:56:19 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:56:19', '2024-12-08 23:56:05');
INSERT INTO `task` VALUES (132, 'rem', 'send_award', 'NKIJbPicbYV', '{\"eventId\":\"NKIJbPicbYV\",\"timestamp\":\"Dec 8, 2024 11:56:20 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:56:21', '2024-12-08 23:56:07');
INSERT INTO `task` VALUES (133, 'rem', 'send_award', 'kYTYu1AvJxj', '{\"eventId\":\"kYTYu1AvJxj\",\"timestamp\":\"Dec 8, 2024 11:56:22 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:56:22', '2024-12-08 23:56:08');
INSERT INTO `task` VALUES (134, 'rem', 'send_award', 'f0V0kJdVWxN', '{\"eventId\":\"f0V0kJdVWxN\",\"timestamp\":\"Dec 8, 2024 11:56:23 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:56:24', '2024-12-08 23:56:10');
INSERT INTO `task` VALUES (135, 'rem', 'send_award', '3TC2ztaCqnD', '{\"eventId\":\"3TC2ztaCqnD\",\"timestamp\":\"Dec 8, 2024 11:56:25 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:56:25', '2024-12-08 23:56:11');
INSERT INTO `task` VALUES (136, 'rem', 'send_award', 'VGehvFGWmKp', '{\"eventId\":\"VGehvFGWmKp\",\"timestamp\":\"Dec 8, 2024 11:56:26 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:56:27', '2024-12-08 23:56:12');
INSERT INTO `task` VALUES (137, 'rem', 'send_award', 'ompSHjWiG2P', '{\"eventId\":\"ompSHjWiG2P\",\"timestamp\":\"Dec 8, 2024 11:56:27 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:56:28', '2024-12-08 23:56:13');
INSERT INTO `task` VALUES (138, 'rem', 'send_award', 'b3OxPOjgSeR', '{\"eventId\":\"b3OxPOjgSeR\",\"timestamp\":\"Dec 8, 2024 11:56:29 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:56:29', '2024-12-08 23:56:14');
INSERT INTO `task` VALUES (139, 'rem', 'send_award', 'JxWpXHSNrbn', '{\"eventId\":\"JxWpXHSNrbn\",\"timestamp\":\"Dec 8, 2024 11:56:30 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:56:30', '2024-12-08 23:56:16');
INSERT INTO `task` VALUES (140, 'rem', 'send_award', 'co3BELLpzWG', '{\"eventId\":\"co3BELLpzWG\",\"timestamp\":\"Dec 8, 2024 11:56:31 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:56:31', '2024-12-08 23:56:17');
INSERT INTO `task` VALUES (141, 'rem', 'send_award', 'cDkYgHuC3lf', '{\"eventId\":\"cDkYgHuC3lf\",\"timestamp\":\"Dec 8, 2024 11:56:32 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:56:33', '2024-12-08 23:56:18');
INSERT INTO `task` VALUES (142, 'rem', 'send_award', '9MsRsQmRhjH', '{\"eventId\":\"9MsRsQmRhjH\",\"timestamp\":\"Dec 8, 2024 11:56:33 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:56:34', '2024-12-08 23:56:19');
INSERT INTO `task` VALUES (143, 'rem', 'send_award', 'QEvx54JF1f9', '{\"eventId\":\"QEvx54JF1f9\",\"timestamp\":\"Dec 8, 2024 11:56:35 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:56:35', '2024-12-08 23:56:20');
INSERT INTO `task` VALUES (144, 'rem', 'send_award', 'fEQBdPo1pQ9', '{\"eventId\":\"fEQBdPo1pQ9\",\"timestamp\":\"Dec 8, 2024 11:56:36 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:56:36', '2024-12-08 23:56:22');
INSERT INTO `task` VALUES (145, 'rem', 'send_award', '3GK2GgXNXo8', '{\"eventId\":\"3GK2GgXNXo8\",\"timestamp\":\"Dec 8, 2024 11:56:37 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:56:37', '2024-12-08 23:56:23');
INSERT INTO `task` VALUES (146, 'rem', 'send_award', 'FLSrq0WuXVn', '{\"eventId\":\"FLSrq0WuXVn\",\"timestamp\":\"Dec 8, 2024 11:56:39 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:56:39', '2024-12-08 23:56:25');
INSERT INTO `task` VALUES (147, 'rem', 'send_award', 'H6YiuaeBgX4', '{\"eventId\":\"H6YiuaeBgX4\",\"timestamp\":\"Dec 8, 2024 11:56:40 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:56:40', '2024-12-08 23:56:26');
INSERT INTO `task` VALUES (148, 'rem', 'send_award', '9wLinTmYDR6', '{\"eventId\":\"9wLinTmYDR6\",\"timestamp\":\"Dec 8, 2024 11:56:41 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:56:42', '2024-12-08 23:56:27');
INSERT INTO `task` VALUES (149, 'rem', 'send_award', 'eaVV19Fk1a9', '{\"eventId\":\"eaVV19Fk1a9\",\"timestamp\":\"Dec 8, 2024 11:56:42 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:56:43', '2024-12-08 23:56:29');
INSERT INTO `task` VALUES (150, 'rem', 'send_award', '9rfSMNLUGha', '{\"eventId\":\"9rfSMNLUGha\",\"timestamp\":\"Dec 8, 2024 11:56:44 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:56:44', '2024-12-08 23:56:30');
INSERT INTO `task` VALUES (151, 'rem', 'send_award', '7zmLSC3LHTe', '{\"eventId\":\"7zmLSC3LHTe\",\"timestamp\":\"Dec 8, 2024 11:56:45 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:56:46', '2024-12-08 23:56:31');
INSERT INTO `task` VALUES (152, 'rem', 'send_award', 'fOAdtkmAEcA', '{\"eventId\":\"fOAdtkmAEcA\",\"timestamp\":\"Dec 8, 2024 11:56:47 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:56:47', '2024-12-08 23:56:33');
INSERT INTO `task` VALUES (153, 'rem', 'send_award', '3rfiF6e6uqR', '{\"eventId\":\"3rfiF6e6uqR\",\"timestamp\":\"Dec 8, 2024 11:56:48 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:56:49', '2024-12-08 23:56:34');
INSERT INTO `task` VALUES (154, 'rem', 'send_award', '8erGeoIfErV', '{\"eventId\":\"8erGeoIfErV\",\"timestamp\":\"Dec 8, 2024 11:56:49 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:56:50', '2024-12-08 23:56:35');
INSERT INTO `task` VALUES (155, 'rem', 'send_award', 'i0pAOQUoPXc', '{\"eventId\":\"i0pAOQUoPXc\",\"timestamp\":\"Dec 8, 2024 11:56:51 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:56:51', '2024-12-08 23:56:36');
INSERT INTO `task` VALUES (156, 'rem', 'send_award', '2k6h3edqZBe', '{\"eventId\":\"2k6h3edqZBe\",\"timestamp\":\"Dec 8, 2024 11:56:52 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:56:52', '2024-12-08 23:56:38');
INSERT INTO `task` VALUES (157, 'rem', 'send_award', 'yf43QhLtwtC', '{\"eventId\":\"yf43QhLtwtC\",\"timestamp\":\"Dec 8, 2024 11:56:53 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:56:54', '2024-12-08 23:56:39');
INSERT INTO `task` VALUES (158, 'rem', 'send_award', 'Mf30150qCDI', '{\"eventId\":\"Mf30150qCDI\",\"timestamp\":\"Dec 8, 2024 11:56:54 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:56:55', '2024-12-08 23:56:40');
INSERT INTO `task` VALUES (159, 'rem', 'send_award', 'f9DqmWhYG8T', '{\"eventId\":\"f9DqmWhYG8T\",\"timestamp\":\"Dec 8, 2024 11:56:56 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:56:56', '2024-12-08 23:56:42');
INSERT INTO `task` VALUES (160, 'rem', 'send_award', 'LAuIAnwM7Nx', '{\"eventId\":\"LAuIAnwM7Nx\",\"timestamp\":\"Dec 8, 2024 11:56:57 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:56:57', '2024-12-08 23:56:43');
INSERT INTO `task` VALUES (161, 'rem', 'send_award', 'QK6oa5OYzMe', '{\"eventId\":\"QK6oa5OYzMe\",\"timestamp\":\"Dec 8, 2024 11:56:58 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:56:59', '2024-12-08 23:56:44');
INSERT INTO `task` VALUES (162, 'rem', 'send_award', '9Z8SRgA9LU8', '{\"eventId\":\"9Z8SRgA9LU8\",\"timestamp\":\"Dec 8, 2024 11:56:59 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:57:00', '2024-12-08 23:56:46');
INSERT INTO `task` VALUES (163, 'rem', 'send_award', 'ntXWWUipMzz', '{\"eventId\":\"ntXWWUipMzz\",\"timestamp\":\"Dec 8, 2024 11:57:01 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:57:01', '2024-12-08 23:56:47');
INSERT INTO `task` VALUES (164, 'rem', 'send_award', 'sH61raSKPZM', '{\"eventId\":\"sH61raSKPZM\",\"timestamp\":\"Dec 8, 2024 11:57:02 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:57:03', '2024-12-08 23:56:48');
INSERT INTO `task` VALUES (165, 'rem', 'send_award', 'Uas6zBCtVc7', '{\"eventId\":\"Uas6zBCtVc7\",\"timestamp\":\"Dec 8, 2024 11:57:04 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:57:04', '2024-12-08 23:56:50');
INSERT INTO `task` VALUES (166, 'rem', 'send_award', 'laUMulnDmjw', '{\"eventId\":\"laUMulnDmjw\",\"timestamp\":\"Dec 8, 2024 11:57:05 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:57:06', '2024-12-08 23:56:51');
INSERT INTO `task` VALUES (167, 'rem', 'send_award', 'hrM2DuCstjp', '{\"eventId\":\"hrM2DuCstjp\",\"timestamp\":\"Dec 8, 2024 11:57:06 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:57:07', '2024-12-08 23:56:52');
INSERT INTO `task` VALUES (168, 'rem', 'send_award', '34eZzpRc73Q', '{\"eventId\":\"34eZzpRc73Q\",\"timestamp\":\"Dec 8, 2024 11:57:07 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:57:08', '2024-12-08 23:56:54');
INSERT INTO `task` VALUES (169, 'rem', 'send_award', 'uzQO4059Nts', '{\"eventId\":\"uzQO4059Nts\",\"timestamp\":\"Dec 8, 2024 11:57:09 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:57:09', '2024-12-08 23:56:55');
INSERT INTO `task` VALUES (170, 'rem', 'send_award', 'dBfbhxtU7RC', '{\"eventId\":\"dBfbhxtU7RC\",\"timestamp\":\"Dec 8, 2024 11:57:10 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:57:11', '2024-12-08 23:56:57');
INSERT INTO `task` VALUES (171, 'rem', 'send_award', 'TndTD8GPaO3', '{\"eventId\":\"TndTD8GPaO3\",\"timestamp\":\"Dec 8, 2024 11:57:12 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:57:12', '2024-12-08 23:56:58');
INSERT INTO `task` VALUES (172, 'rem', 'send_award', 'dOMQFOh1DgR', '{\"eventId\":\"dOMQFOh1DgR\",\"timestamp\":\"Dec 8, 2024 11:57:13 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:57:14', '2024-12-08 23:56:59');
INSERT INTO `task` VALUES (173, 'rem', 'send_award', '9Xk1gUk3unR', '{\"eventId\":\"9Xk1gUk3unR\",\"timestamp\":\"Dec 8, 2024 11:57:14 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:57:15', '2024-12-08 23:57:00');
INSERT INTO `task` VALUES (174, 'rem', 'send_award', 'wDPNyvETt1V', '{\"eventId\":\"wDPNyvETt1V\",\"timestamp\":\"Dec 8, 2024 11:57:15 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:57:16', '2024-12-08 23:57:01');
INSERT INTO `task` VALUES (175, 'rem', 'send_award', 'chL1s4oBAzp', '{\"eventId\":\"chL1s4oBAzp\",\"timestamp\":\"Dec 8, 2024 11:57:17 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:57:17', '2024-12-08 23:57:02');
INSERT INTO `task` VALUES (176, 'rem', 'send_award', 'W021rqNKmRS', '{\"eventId\":\"W021rqNKmRS\",\"timestamp\":\"Dec 8, 2024 11:57:18 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:57:18', '2024-12-08 23:57:04');
INSERT INTO `task` VALUES (177, 'rem', 'send_award', 'JDDH6OfZABB', '{\"eventId\":\"JDDH6OfZABB\",\"timestamp\":\"Dec 8, 2024 11:57:19 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:57:19', '2024-12-08 23:57:05');
INSERT INTO `task` VALUES (178, 'rem', 'send_award', 'O8WThN5cWpH', '{\"eventId\":\"O8WThN5cWpH\",\"timestamp\":\"Dec 8, 2024 11:57:20 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:57:21', '2024-12-08 23:57:06');
INSERT INTO `task` VALUES (179, 'rem', 'send_award', 'KLyWFOYxTyH', '{\"eventId\":\"KLyWFOYxTyH\",\"timestamp\":\"Dec 8, 2024 11:57:21 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:57:22', '2024-12-08 23:57:08');
INSERT INTO `task` VALUES (180, 'rem', 'send_award', 'iGB1EkWhwKh', '{\"eventId\":\"iGB1EkWhwKh\",\"timestamp\":\"Dec 8, 2024 11:57:23 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:57:23', '2024-12-08 23:57:09');
INSERT INTO `task` VALUES (181, 'rem', 'send_award', 'MbH3ueHSK7d', '{\"eventId\":\"MbH3ueHSK7d\",\"timestamp\":\"Dec 8, 2024 11:57:24 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:57:25', '2024-12-08 23:57:10');
INSERT INTO `task` VALUES (182, 'rem', 'send_award', '0THer7Ado6N', '{\"eventId\":\"0THer7Ado6N\",\"timestamp\":\"Dec 8, 2024 11:57:25 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:57:26', '2024-12-08 23:57:12');
INSERT INTO `task` VALUES (183, 'rem', 'send_award', '7RdHctEo9DQ', '{\"eventId\":\"7RdHctEo9DQ\",\"timestamp\":\"Dec 8, 2024 11:57:27 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:57:27', '2024-12-08 23:57:13');
INSERT INTO `task` VALUES (184, 'rem', 'send_award', 'F12aaslHkDe', '{\"eventId\":\"F12aaslHkDe\",\"timestamp\":\"Dec 8, 2024 11:57:28 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:57:29', '2024-12-08 23:57:14');
INSERT INTO `task` VALUES (185, 'rem', 'send_award', 'azt48KETye7', '{\"eventId\":\"azt48KETye7\",\"timestamp\":\"Dec 8, 2024 11:57:30 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:57:30', '2024-12-08 23:57:15');
INSERT INTO `task` VALUES (186, 'rem', 'send_award', 'kKRi7gAEOhk', '{\"eventId\":\"kKRi7gAEOhk\",\"timestamp\":\"Dec 8, 2024 11:57:31 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:57:31', '2024-12-08 23:57:17');
INSERT INTO `task` VALUES (187, 'rem', 'send_award', 'UHxX8Ai3yW3', '{\"eventId\":\"UHxX8Ai3yW3\",\"timestamp\":\"Dec 8, 2024 11:57:32 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:57:33', '2024-12-08 23:57:18');
INSERT INTO `task` VALUES (188, 'rem', 'send_award', 'Ibo29PCJxRN', '{\"eventId\":\"Ibo29PCJxRN\",\"timestamp\":\"Dec 8, 2024 11:57:34 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:57:34', '2024-12-08 23:57:20');
INSERT INTO `task` VALUES (189, 'rem', 'send_award', 'c0AHocbmxj8', '{\"eventId\":\"c0AHocbmxj8\",\"timestamp\":\"Dec 8, 2024 11:57:35 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:57:36', '2024-12-08 23:57:21');
INSERT INTO `task` VALUES (190, 'rem', 'send_award', 'lGGsJEZrV6l', '{\"eventId\":\"lGGsJEZrV6l\",\"timestamp\":\"Dec 8, 2024 11:57:37 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:57:37', '2024-12-08 23:57:23');
INSERT INTO `task` VALUES (191, 'rem', 'send_award', 'W9TpyQWt0n7', '{\"eventId\":\"W9TpyQWt0n7\",\"timestamp\":\"Dec 8, 2024 11:57:38 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:57:39', '2024-12-08 23:57:24');
INSERT INTO `task` VALUES (192, 'rem', 'send_award', 'wAUFvOjJZTC', '{\"eventId\":\"wAUFvOjJZTC\",\"timestamp\":\"Dec 8, 2024 11:57:40 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:57:40', '2024-12-08 23:57:26');
INSERT INTO `task` VALUES (193, 'rem', 'send_award', 'ALG7BhQYl9l', '{\"eventId\":\"ALG7BhQYl9l\",\"timestamp\":\"Dec 8, 2024 11:57:41 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:57:41', '2024-12-08 23:57:27');
INSERT INTO `task` VALUES (194, 'rem', 'send_award', 'UpuRSFiWIPw', '{\"eventId\":\"UpuRSFiWIPw\",\"timestamp\":\"Dec 8, 2024 11:57:43 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:57:43', '2024-12-08 23:57:29');
INSERT INTO `task` VALUES (195, 'rem', 'send_award', '9fMjbpI2GC9', '{\"eventId\":\"9fMjbpI2GC9\",\"timestamp\":\"Dec 8, 2024 11:57:44 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:57:45', '2024-12-08 23:57:30');
INSERT INTO `task` VALUES (196, 'rem', 'send_award', 'v5Yd6kf24Bq', '{\"eventId\":\"v5Yd6kf24Bq\",\"timestamp\":\"Dec 8, 2024 11:57:46 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:57:46', '2024-12-08 23:57:32');
INSERT INTO `task` VALUES (197, 'rem', 'send_award', 'ABiJK7N5vHl', '{\"eventId\":\"ABiJK7N5vHl\",\"timestamp\":\"Dec 8, 2024 11:57:47 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:57:47', '2024-12-08 23:57:33');
INSERT INTO `task` VALUES (198, 'rem', 'send_award', 'PHeGpRjOSMb', '{\"eventId\":\"PHeGpRjOSMb\",\"timestamp\":\"Dec 8, 2024 11:57:48 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:57:49', '2024-12-08 23:57:35');
INSERT INTO `task` VALUES (199, 'rem', 'send_award', 'S73K7n1jiki', '{\"eventId\":\"S73K7n1jiki\",\"timestamp\":\"Dec 8, 2024 11:57:50 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:57:50', '2024-12-08 23:57:36');
INSERT INTO `task` VALUES (200, 'rem', 'send_award', '9stYj6fQRMH', '{\"eventId\":\"9stYj6fQRMH\",\"timestamp\":\"Dec 8, 2024 11:57:51 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-08 23:57:52', '2024-12-08 23:57:37');
INSERT INTO `task` VALUES (201, 'rem', 'send_award', 'si0gH8g95BZ', '{\"eventId\":\"si0gH8g95BZ\",\"timestamp\":\"Dec 9, 2024 12:03:44 AM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"OpenAI 增加使用次数\"}}', 'completed', '2024-12-09 00:03:45', '2024-12-09 00:03:32');
INSERT INTO `task` VALUES (202, 'rem', 'send_award', 'IwsVaLNnQSh', '{\"eventId\":\"IwsVaLNnQSh\",\"timestamp\":\"Dec 12, 2024 10:51:01 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"随机积分\"}}', 'completed', '2024-12-12 22:51:01', '2024-12-12 22:50:39');
INSERT INTO `task` VALUES (203, 'rem', 'send_award', '0bXja3DpqPJ', '{\"eventId\":\"0bXja3DpqPJ\",\"timestamp\":\"Dec 12, 2024 10:57:26 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"随机积分\"}}', 'completed', '2024-12-12 22:57:26', '2024-12-12 22:57:04');
INSERT INTO `task` VALUES (204, 'rem', 'send_award', '6njWXGUgesv', '{\"eventId\":\"6njWXGUgesv\",\"timestamp\":\"Dec 12, 2024 11:30:26 PM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"随机积分\"}}', 'completed', '2024-12-12 23:30:27', '2024-12-12 23:30:04');
INSERT INTO `task` VALUES (205, 'rem', 'send_award', 'IcfcfXLW0nd', '{\"eventId\":\"IcfcfXLW0nd\",\"timestamp\":\"Dec 13, 2024 12:27:10 AM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"随机积分\"}}', 'completed', '2024-12-13 00:27:10', '2024-12-13 00:26:48');
INSERT INTO `task` VALUES (206, 'rem', 'send_award', 'iaXDixwST1T', '{\"eventId\":\"iaXDixwST1T\",\"timestamp\":\"Dec 13, 2024 12:34:25 AM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"随机积分\"}}', 'completed', '2024-12-13 00:34:25', '2024-12-13 00:34:03');
INSERT INTO `task` VALUES (207, 'rem', 'send_award', '1D7bNnOmuhD', '{\"eventId\":\"1D7bNnOmuhD\",\"timestamp\":\"Dec 13, 2024 12:54:16 AM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"随机积分\"}}', 'completed', '2024-12-13 00:54:17', '2024-12-13 00:53:54');
INSERT INTO `task` VALUES (208, 'rem', 'send_award', 'NBeWiNdzIQk', '{\"eventId\":\"NBeWiNdzIQk\",\"timestamp\":\"Dec 13, 2024 1:12:30 AM\",\"data\":{\"awardId\":101,\"userId\":\"rem\",\"awardTitle\":\"随机积分\"}}', 'completed', '2024-12-13 01:12:30', '2024-12-13 01:12:08');
INSERT INTO `task` VALUES (209, 'rem', 'send_award', '0lQEHa9MlXg', '{\"eventId\":\"0lQEHa9MlXg\",\"timestamp\":\"Dec 13, 2024 1:14:49 AM\",\"data\":{\"awardId\":104,\"userId\":\"rem\",\"awardTitle\":\"5等奖\"}}', 'completed', '2024-12-13 01:14:50', '2024-12-13 01:14:27');
INSERT INTO `task` VALUES (210, 'rem', 'send_award', '7OMV1ZGgkky', '{\"eventId\":\"7OMV1ZGgkky\",\"timestamp\":\"Dec 13, 2024 1:15:30 AM\",\"data\":{\"awardId\":104,\"userId\":\"rem\",\"awardTitle\":\"5等奖\"}}', 'completed', '2024-12-13 01:15:30', '2024-12-13 01:15:08');
INSERT INTO `task` VALUES (211, 'rem', 'send_award', 'brxAoRCJXAD', '{\"eventId\":\"brxAoRCJXAD\",\"timestamp\":\"Dec 14, 2024 4:54:37 PM\",\"data\":{\"awardId\":106,\"userId\":\"rem\",\"awardTitle\":\"3等奖\"}}', 'completed', '2024-12-14 16:54:37', '2024-12-14 16:54:12');
INSERT INTO `task` VALUES (212, 'rem', 'send_award', '5205CSexYCD', '{\"eventId\":\"5205CSexYCD\",\"timestamp\":\"Dec 14, 2024 4:55:16 PM\",\"data\":{\"awardId\":105,\"userId\":\"rem\",\"awardTitle\":\"4等奖\"}}', 'completed', '2024-12-14 16:55:16', '2024-12-14 16:54:51');
INSERT INTO `task` VALUES (219, 'rem', 'send_rebate', '01583312028', '{\"eventId\":\"01583312028\",\"timestamp\":\"Jan 12, 2025 6:52:23 PM\",\"data\":{\"userId\":\"rem\",\"rebateType\":\"sign\",\"rebateConfig\":\"9011\",\"bizId\":\"rem_sku_20250110\"}}', 'failed', '2025-01-12 18:52:14', '2025-01-12 18:52:14');
INSERT INTO `task` VALUES (220, 'rem', 'send_rebate', '30311911925', '{\"eventId\":\"30311911925\",\"timestamp\":\"Jan 12, 2025 6:52:23 PM\",\"data\":{\"userId\":\"rem\",\"rebateType\":\"sign\",\"rebateConfig\":\"10\",\"bizId\":\"rem_integral_20250110\"}}', 'failed', '2025-01-12 18:52:14', '2025-01-12 18:52:14');
INSERT INTO `task` VALUES (221, 'rem', 'send_rebate', '57399174746', '{\"eventId\":\"57399174746\",\"timestamp\":\"Jan 12, 2025 6:58:40 PM\",\"data\":{\"userId\":\"rem\",\"rebateType\":\"sign\",\"rebateConfig\":\"9011\",\"bizId\":\"rem_sku_20250112\"}}', 'completed', '2025-01-12 18:58:31', '2025-01-12 18:58:31');
INSERT INTO `task` VALUES (222, 'rem', 'send_rebate', '01995029971', '{\"eventId\":\"01995029971\",\"timestamp\":\"Jan 12, 2025 6:58:40 PM\",\"data\":{\"userId\":\"rem\",\"rebateType\":\"sign\",\"rebateConfig\":\"10\",\"bizId\":\"rem_integral_20250112\"}}', 'completed', '2025-01-12 18:58:31', '2025-01-12 18:58:31');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL COMMENT '自增id ',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `strategy_id` bigint NOT NULL,
  `rule_model` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id_strategy_id`(`user_id` ASC, `strategy_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (0, 'rem', 100006, 'rule_luck_award', '2024-12-08 01:33:08', '2024-12-08 01:33:08');

-- ----------------------------
-- Table structure for user_award_record
-- ----------------------------
DROP TABLE IF EXISTS `user_award_record`;
CREATE TABLE `user_award_record`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户ID',
  `activity_id` bigint NOT NULL COMMENT '活动ID',
  `strategy_id` bigint NOT NULL COMMENT '抽奖策略ID',
  `order_id` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '抽奖订单ID【作为幂等使用】',
  `award_id` int NOT NULL COMMENT '奖品ID',
  `award_title` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '奖品标题（名称）',
  `award_time` datetime NOT NULL COMMENT '中奖时间',
  `status` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'create' COMMENT '奖品状态；create-创建、completed-发奖完成',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uq_order_id`(`order_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_activity_id`(`activity_id` ASC) USING BTREE,
  INDEX `idx_award_id`(`strategy_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 229 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户中奖记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_award_record
-- ----------------------------
INSERT INTO `user_award_record` VALUES (208, 'rem', 100301, 100006, '406284951110', 101, '随机积分', '2024-12-12 22:57:26', 'create', '2024-12-12 22:57:26', '2024-12-12 22:57:26');
INSERT INTO `user_award_record` VALUES (216, 'rem', 100301, 100006, '058163794197', 101, '随机积分', '2024-12-12 23:30:27', 'create', '2024-12-12 23:30:27', '2024-12-12 23:30:27');
INSERT INTO `user_award_record` VALUES (218, 'rem', 100301, 100006, '433821122706', 101, '随机积分', '2024-12-13 00:27:10', 'create', '2024-12-13 00:27:10', '2024-12-13 00:27:10');
INSERT INTO `user_award_record` VALUES (220, 'rem', 100301, 100006, '509558131247', 101, '随机积分', '2024-12-13 00:34:25', 'create', '2024-12-13 00:34:25', '2024-12-13 00:34:25');
INSERT INTO `user_award_record` VALUES (221, 'rem', 100301, 100006, '608964007094', 101, '随机积分', '2024-12-13 00:54:15', 'create', '2024-12-13 00:54:17', '2024-12-13 00:54:17');
INSERT INTO `user_award_record` VALUES (222, 'rem', 100301, 100006, '623748265624', 101, '随机积分', '2024-12-13 01:12:30', 'create', '2024-12-13 01:12:30', '2024-12-13 01:12:30');
INSERT INTO `user_award_record` VALUES (224, 'rem', 100301, 100006, '410241322396', 104, '5等奖', '2024-12-13 01:14:50', 'create', '2024-12-13 01:14:50', '2024-12-13 01:14:50');
INSERT INTO `user_award_record` VALUES (225, 'rem', 100301, 100006, '502238656302', 104, '5等奖', '2024-12-13 01:15:30', 'create', '2024-12-13 01:15:30', '2024-12-13 01:15:30');
INSERT INTO `user_award_record` VALUES (227, 'rem', 100301, 100006, '512536250289', 106, '3等奖', '2024-12-14 16:54:37', 'create', '2024-12-14 16:54:37', '2024-12-14 16:54:37');
INSERT INTO `user_award_record` VALUES (228, 'rem', 100301, 100006, '939354384659', 105, '4等奖', '2024-12-14 16:55:16', 'create', '2024-12-14 16:55:16', '2024-12-14 16:55:16');

-- ----------------------------
-- Table structure for user_behavior_rebate_order
-- ----------------------------
DROP TABLE IF EXISTS `user_behavior_rebate_order`;
CREATE TABLE `user_behavior_rebate_order`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户id',
  `order_id` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单id',
  `behavior_type` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '行为类型（sign 签到、pay 支付）',
  `rebate_desc` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '返利描述',
  `rebate_type` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '返利类型（sku 活动库存充值商品、integral 用户活动积分）',
  `rebate_config` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '返利配置【sku值、 积分值】',
  `biz_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '业务ID-  拼接的唯一值',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uq_order_id`(`order_id` ASC) USING BTREE,
  UNIQUE INDEX `uq_biz_id`(`biz_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户行为返利流水订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_behavior_rebate_order
-- ----------------------------
INSERT INTO `user_behavior_rebate_order` VALUES (13, 'rem', '052495586558', 'sign', NULL, 'sku', '9011', 'rem_sku_20250110', '2025-01-12 18:52:14', '2025-01-12 18:52:14');
INSERT INTO `user_behavior_rebate_order` VALUES (14, 'rem', '011007286650', 'sign', NULL, 'integral', '10', 'rem_integral_20250110', '2025-01-12 18:52:14', '2025-01-12 18:52:14');
INSERT INTO `user_behavior_rebate_order` VALUES (15, 'rem', '678896666028', 'sign', '签到返利-sku额度', 'sku', '9011', 'rem_sku_20250112', '2025-01-12 18:58:31', '2025-01-12 18:58:31');
INSERT INTO `user_behavior_rebate_order` VALUES (16, 'rem', '143932243410', 'sign', '签到返利-积分', 'integral', '10', 'rem_integral_20250112', '2025-01-12 18:58:31', '2025-01-12 18:58:31');

-- ----------------------------
-- Table structure for user_raffle_order
-- ----------------------------
DROP TABLE IF EXISTS `user_raffle_order`;
CREATE TABLE `user_raffle_order`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户ID',
  `activity_id` bigint NOT NULL COMMENT '活动ID',
  `activity_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '活动名称',
  `strategy_id` bigint NOT NULL COMMENT '抽奖策略ID',
  `order_id` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单ID',
  `order_time` datetime NOT NULL COMMENT '下单时间',
  `status` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'create' COMMENT '订单状态；create-创建、used-已使用、cancle-已作废',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uq_order_id`(`order_id` ASC) USING BTREE,
  INDEX `idx_user_id_activity_id`(`user_id` ASC, `activity_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户抽奖订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_raffle_order
-- ----------------------------
INSERT INTO `user_raffle_order` VALUES (1, 'rem', 100301, '测试活动', 100006, '406284951110', '2024-12-08 15:11:29', 'used', '2024-12-08 15:11:29', '2024-12-12 23:22:12');
INSERT INTO `user_raffle_order` VALUES (2, 'rem', 100301, '测试活动', 100006, '058163794197', '2024-12-12 23:30:26', 'used', '2024-12-12 23:30:26', '2024-12-13 00:24:41');
INSERT INTO `user_raffle_order` VALUES (3, 'rem', 100301, '测试活动', 100006, '433821122706', '2024-12-13 00:27:09', 'used', '2024-12-13 00:27:09', '2024-12-13 00:27:51');
INSERT INTO `user_raffle_order` VALUES (4, 'rem', 100301, '测试活动', 100006, '509558131247', '2024-12-13 00:34:23', 'used', '2024-12-13 00:34:24', '2024-12-13 00:48:04');
INSERT INTO `user_raffle_order` VALUES (5, 'rem', 100301, '测试活动', 100006, '608964007094', '2024-12-13 00:50:50', 'used', '2024-12-13 00:50:51', '2024-12-13 00:56:36');
INSERT INTO `user_raffle_order` VALUES (6, 'rem', 100301, '测试活动', 100006, '623748265624', '2024-12-13 00:58:46', 'uesd', '2024-12-13 00:58:46', '2024-12-13 01:14:21');
INSERT INTO `user_raffle_order` VALUES (7, 'rem', 100301, '测试活动', 100006, '410241322396', '2024-12-13 01:14:48', 'used', '2024-12-13 01:14:49', '2024-12-13 01:15:02');
INSERT INTO `user_raffle_order` VALUES (8, 'rem', 100301, '测试活动', 100006, '502238656302', '2024-12-13 01:15:29', 'used', '2024-12-13 01:15:29', '2024-12-14 16:54:05');
INSERT INTO `user_raffle_order` VALUES (9, 'rem', 100301, '测试活动', 100006, '512536250289', '2024-12-14 16:54:36', 'used', '2024-12-14 16:54:36', '2024-12-14 16:54:45');
INSERT INTO `user_raffle_order` VALUES (10, 'rem', 100301, '测试活动', 100006, '939354384659', '2024-12-14 16:55:15', 'create', '2024-12-14 16:55:15', '2024-12-14 16:55:15');

SET FOREIGN_KEY_CHECKS = 1;
