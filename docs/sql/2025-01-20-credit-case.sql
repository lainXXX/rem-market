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

 Date: 21/01/2025 18:47:50
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
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '奖品表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '日常返利行为活动配置' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '抽奖活动表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '抽奖活动账户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of raffle_activity_account
-- ----------------------------
INSERT INTO `raffle_activity_account` VALUES (3, 'xiaofuge', 100301, 44, 43, 44, 43, 44, 43, '2024-03-23 16:38:57', '2024-03-30 17:10:06');
INSERT INTO `raffle_activity_account` VALUES (4, 'rem', 100301, 75, 67, 75, 67, 75, 67, '2024-12-08 01:56:49', '2025-01-21 18:33:33');
INSERT INTO `raffle_activity_account` VALUES (5, 'user001', 100301, 23, 16, 23, 16, 23, 16, '2025-01-16 20:11:59', '2025-01-18 21:38:38');
INSERT INTO `raffle_activity_account` VALUES (6, 'user002', 100301, 2, 2, 2, 2, 2, 2, '2025-01-18 21:59:11', '2025-01-18 21:59:11');
INSERT INTO `raffle_activity_account` VALUES (7, 'user003', 100301, 2, 2, 2, 2, 2, 2, '2025-01-18 23:17:47', '2025-01-18 23:17:47');
INSERT INTO `raffle_activity_account` VALUES (8, 'user004', 100301, 2, 2, 2, 2, 2, 2, '2025-01-21 18:33:56', '2025-01-21 18:33:56');

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
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '抽奖活动账户表-日次数' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of raffle_activity_account_day_count
-- ----------------------------
INSERT INTO `raffle_activity_account_day_count` VALUES (1, 'rem', 100301, '2024-12-08', 2, 1, '2024-12-08 15:11:29', '2024-12-08 15:11:29');
INSERT INTO `raffle_activity_account_day_count` VALUES (2, 'rem', 100301, '2024-12-12', 2, 0, '2024-12-12 23:30:26', '2024-12-12 23:30:26');
INSERT INTO `raffle_activity_account_day_count` VALUES (3, 'rem', 100301, '2024-12-13', 2, 0, '2024-12-13 00:27:09', '2024-12-14 16:49:23');
INSERT INTO `raffle_activity_account_day_count` VALUES (4, 'rem', 100301, '2024-12-14', 44, 38, '2024-12-14 16:54:36', '2024-12-14 16:54:50');
INSERT INTO `raffle_activity_account_day_count` VALUES (5, 'rem', 100301, '2025-01-13', 57, 57, '2025-01-13 15:43:50', '2025-01-13 17:40:01');
INSERT INTO `raffle_activity_account_day_count` VALUES (6, 'user001', 100301, '2025-01-16', 22, 15, '2025-01-16 20:54:37', '2025-01-16 22:16:11');

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
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '抽奖活动账户表-月次数' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of raffle_activity_account_month_count
-- ----------------------------
INSERT INTO `raffle_activity_account_month_count` VALUES (1, 'rem', 100301, '2024-12', 44, 34, '2024-12-08 15:11:29', '2024-12-14 16:54:49');
INSERT INTO `raffle_activity_account_month_count` VALUES (2, 'rem', 100301, '2025-01', 71, 71, '2025-01-13 15:44:01', '2025-01-21 18:33:33');
INSERT INTO `raffle_activity_account_month_count` VALUES (3, 'user001', 100301, '2025-01', 23, 16, '2025-01-16 20:54:37', '2025-01-18 21:38:38');

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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '抽奖活动次数配置表' ROW_FORMAT = Dynamic;

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
  `pay_amount` decimal(10, 2) NOT NULL COMMENT '支付金额',
  `activity_order_type` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '活动订单类型【credit_pay_trade-积分兑换，需要支付类交易、 rebate_trade-返利类型，无需交易】',
  `status` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'complete' COMMENT '订单状态（created、complete）',
  `out_business_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '业务仿重ID - 外部透传的，确保幂等',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uq_order_id`(`order_id` ASC) USING BTREE,
  UNIQUE INDEX `uq_out_business_no`(`out_business_no` ASC) USING BTREE,
  INDEX `idx_user_id_activity_id`(`user_id` ASC, `activity_id` ASC, `status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 45 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '抽奖活动单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of raffle_activity_order
-- ----------------------------
INSERT INTO `raffle_activity_order` VALUES (1, 'rem', 9011, 100301, '测试活动', 100006, '55765237241', '2024-12-08 01:56:49', 1, 1, 1, 0.00, '', 'completed', '700091009127', '2024-12-08 01:56:49', '2024-12-08 01:56:49');
INSERT INTO `raffle_activity_order` VALUES (2, 'rem', 9011, 100301, '测试活动', 100006, '71018075533', '2024-12-08 01:58:19', 1, 1, 1, 0.00, '', 'completed', '700091009128', '2024-12-08 01:58:19', '2024-12-08 01:58:19');
INSERT INTO `raffle_activity_order` VALUES (19, 'rem', 9011, 100301, '测试活动', 100006, '84677981222', '2025-01-13 16:16:07', 1, 1, 1, 0.00, '', 'completed', 'rem_sku_20250110', '2025-01-13 16:16:07', '2025-01-13 16:16:07');
INSERT INTO `raffle_activity_order` VALUES (20, 'rem', 9011, 100301, '测试活动', 100006, '63405337440', '2025-01-13 16:16:07', 1, 1, 1, 0.00, '', 'completed', 'rem_sku_20250111', '2025-01-13 16:16:07', '2025-01-13 16:16:07');
INSERT INTO `raffle_activity_order` VALUES (21, 'rem', 9011, 100301, '测试活动', 100006, '99078481730', '2025-01-13 16:24:36', 1, 1, 1, 0.00, '', 'completed', 'rem_sku_20250112', '2025-01-13 16:24:36', '2025-01-13 16:24:36');
INSERT INTO `raffle_activity_order` VALUES (22, 'rem', 9011, 100301, '测试活动', 100006, '41554197025', '2025-01-13 16:24:37', 1, 1, 1, 0.00, '', 'completed', 'rem_sku_20250103', '2025-01-13 16:24:37', '2025-01-13 16:24:37');
INSERT INTO `raffle_activity_order` VALUES (23, 'rem', 9011, 100301, '测试活动', 100006, '57059196567', '2025-01-13 16:33:48', 1, 1, 1, 0.00, '', 'completed', 'rem_sku_20250104', '2025-01-13 16:33:48', '2025-01-13 16:33:48');
INSERT INTO `raffle_activity_order` VALUES (24, 'rem', 9011, 100301, '测试活动', 100006, '25389151485', '2025-01-13 16:33:49', 1, 1, 1, 0.00, '', 'completed', 'rem_sku_20250106', '2025-01-13 16:33:49', '2025-01-13 16:33:49');
INSERT INTO `raffle_activity_order` VALUES (25, 'rem', 9011, 100301, '测试活动', 100006, '43666714386', '2025-01-13 16:34:21', 1, 1, 1, 0.00, '', 'completed', 'rem_sku_20250107', '2025-01-13 16:34:21', '2025-01-13 16:34:21');
INSERT INTO `raffle_activity_order` VALUES (26, 'rem', 9011, 100301, '测试活动', 100006, '33026378514', '2025-01-13 16:34:23', 1, 1, 1, 0.00, '', 'completed', 'rem_sku_20250108', '2025-01-13 16:34:23', '2025-01-13 16:34:23');
INSERT INTO `raffle_activity_order` VALUES (27, 'rem', 9011, 100301, '测试活动', 100006, '26330247441', '2025-01-13 16:39:33', 1, 1, 1, 0.00, '', 'completed', 'rem_sku_20250109', '2025-01-13 16:39:33', '2025-01-13 16:39:33');
INSERT INTO `raffle_activity_order` VALUES (34, 'rem', 9011, 100301, '测试活动', 100006, '57672670618', '2025-01-13 17:40:12', 1, 1, 1, 0.00, '', 'completed', 'rem_sku_20250113', '2025-01-13 17:40:12', '2025-01-13 17:40:12');
INSERT INTO `raffle_activity_order` VALUES (35, 'rem', 9011, 100301, '测试活动', 100006, '71425737058', '2025-01-15 16:54:44', 1, 1, 1, 0.00, '', 'completed', 'rem_sku_20250115', '2025-01-15 16:54:44', '2025-01-15 16:54:44');
INSERT INTO `raffle_activity_order` VALUES (41, 'rem', 9011, 100301, '测试活动', 100006, '93724792870', '2025-01-18 19:10:02', 1, 1, 1, 1.68, '', 'completed', '700091009135', '2025-01-18 19:10:02', '2025-01-18 19:10:58');
INSERT INTO `raffle_activity_order` VALUES (42, 'rem', 9011, 100301, '测试活动', 100006, '46774728996', '2025-01-18 19:12:39', 1, 1, 1, 0.00, '', 'completed', 'rem_sku_20250118', '2025-01-18 19:12:39', '2025-01-18 19:12:39');
INSERT INTO `raffle_activity_order` VALUES (43, 'user001', 9011, 100301, '测试活动', 100006, '36227614053', '2025-01-18 21:38:40', 1, 1, 1, 0.00, '', 'completed', 'user001_sku_20250118', '2025-01-18 21:38:40', '2025-01-18 21:38:40');
INSERT INTO `raffle_activity_order` VALUES (44, 'user002', 9011, 100301, '测试活动', 100006, '05296789851', '2025-01-18 21:59:13', 1, 1, 1, 0.00, '', 'completed', 'user002_sku_20250118', '2025-01-18 21:59:13', '2025-01-18 21:59:13');
INSERT INTO `raffle_activity_order` VALUES (45, 'user003', 9011, 100301, '测试活动', 100006, '09793566762', '2025-01-18 23:17:50', 1, 1, 1, 0.00, '', 'completed', 'user003_sku_20250118', '2025-01-18 23:17:50', '2025-01-18 23:17:50');
INSERT INTO `raffle_activity_order` VALUES (46, 'rem', 9011, 100301, '测试活动', 100006, '05143627391', '2025-01-21 16:59:45', 1, 1, 1, 0.00, '', 'completed', 'rem_sku_20250121', '2025-01-21 16:59:45', '2025-01-21 16:59:45');
INSERT INTO `raffle_activity_order` VALUES (47, 'rem', 9011, 100301, '测试活动', 100006, '70168877495', '2025-01-21 17:00:37', 1, 1, 1, 1.68, '', 'completed', '843574410235', '2025-01-21 17:00:37', '2025-01-21 17:00:38');
INSERT INTO `raffle_activity_order` VALUES (48, 'rem', 9011, 100301, '测试活动', 100006, '36583467200', '2025-01-21 17:04:51', 1, 1, 1, 1.68, '', 'completed', '838307787957', '2025-01-21 17:04:51', '2025-01-21 17:04:52');
INSERT INTO `raffle_activity_order` VALUES (49, 'rem', 9011, 100301, '测试活动', 100006, '27948260915', '2025-01-21 18:16:55', 1, 1, 1, 1.68, 'credit_pay_trade', 'completed', '195830752458', '2025-01-21 18:16:55', '2025-01-21 18:16:56');
INSERT INTO `raffle_activity_order` VALUES (50, 'rem', 9011, 100301, '测试活动', 100006, '64775856079', '2025-01-21 18:18:09', 1, 1, 1, 1.68, 'credit_pay_trade', 'completed', '196154478727', '2025-01-21 18:18:09', '2025-01-21 18:18:10');
INSERT INTO `raffle_activity_order` VALUES (51, 'rem', 9011, 100301, '测试活动', 100006, '52076304634', '2025-01-21 18:18:24', 1, 1, 1, 1.68, 'credit_pay_trade', 'completed', '601168775828', '2025-01-21 18:18:24', '2025-01-21 18:18:24');
INSERT INTO `raffle_activity_order` VALUES (52, 'rem', 9011, 100301, '测试活动', 100006, '15398242751', '2025-01-21 18:18:30', 1, 1, 1, 1.68, 'credit_pay_trade', 'completed', '120601711567', '2025-01-21 18:18:30', '2025-01-21 18:18:31');
INSERT INTO `raffle_activity_order` VALUES (53, 'rem', 9011, 100301, '测试活动', 100006, '62120836977', '2025-01-21 18:26:09', 1, 1, 1, 1.68, 'credit_pay_trade', 'completed', '870315337545', '2025-01-21 18:26:09', '2025-01-21 18:27:00');
INSERT INTO `raffle_activity_order` VALUES (54, 'rem', 9011, 100301, '测试活动', 100006, '37889716510', '2025-01-21 18:27:27', 1, 1, 1, 1.68, 'credit_pay_trade', 'completed', '967330176242', '2025-01-21 18:27:27', '2025-01-21 18:27:27');
INSERT INTO `raffle_activity_order` VALUES (55, 'rem', 9011, 100301, '测试活动', 100006, '23407117974', '2025-01-21 18:32:38', 1, 1, 1, 1.68, 'credit_pay_trade', 'completed', '733389754979', '2025-01-21 18:32:38', '2025-01-21 18:32:39');
INSERT INTO `raffle_activity_order` VALUES (56, 'rem', 9011, 100301, '测试活动', 100006, '97064630856', '2025-01-21 18:33:34', 1, 1, 1, 1.68, 'credit_pay_trade', 'completed', '960669138565', '2025-01-21 18:33:34', '2025-01-21 18:33:34');
INSERT INTO `raffle_activity_order` VALUES (57, 'user004', 9011, 100301, '测试活动', 100006, '10873159993', '2025-01-21 18:33:58', 1, 1, 1, 1.68, 'credit_pay_trade', 'completed', '556543272784', '2025-01-21 18:33:58', '2025-01-21 18:33:58');

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
  `product_amount` decimal(10, 2) NOT NULL COMMENT '商品金额',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uq_sku`(`sku` ASC) USING BTREE,
  INDEX `idx_activity_id_activity_count_id`(`activity_id` ASC, `activity_count_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of raffle_activity_sku
-- ----------------------------
INSERT INTO `raffle_activity_sku` VALUES (1, 9011, 100301, 11101, 100, 97, 1.68, '2024-03-16 11:41:09', '2025-01-21 18:34:30');

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
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '抽奖策略规则' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of rule
-- ----------------------------
INSERT INTO `rule` VALUES (13, 100001, NULL, 1, 'rule_weight', '4000:102,103,104,105 5000:102,103,104,105,106,107 6000:102,103,104,105,106,107,108,109', '消耗6000分，必中奖范围', '2023-12-09 10:30:43', '2023-12-31 14:51:50');
INSERT INTO `rule` VALUES (14, 100001, NULL, 1, 'rule_blacklist', '101:user001,user002,user003', '黑名单抽奖，积分兜底', '2023-12-09 12:59:45', '2024-02-14 18:16:20');
INSERT INTO `rule` VALUES (15, 100006, NULL, 1, 'rule_weight', '60:102 4000:102,103,104,105 5000:102,103,104,105,106,107 6000:102,103,104,105,106,107,108', '消耗6000分，必中奖范围', '2025-01-15 17:11:15', '2025-01-15 17:13:14');
INSERT INTO `rule` VALUES (16, 100006, NULL, 1, 'rule_blacklist', '101:user001,user002,user003', '黑名单抽奖，积分兜底', '2025-01-15 17:13:58', '2025-01-15 17:13:58');

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
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '规则表-树' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '规则表-树节点' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '规则表-树节点连线' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '抽奖策略' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of strategy
-- ----------------------------
INSERT INTO `strategy` VALUES (1, 100001, '抽奖策略', 'rule_blacklist,rule_weight', '2023-12-09 09:37:19', '2024-01-20 11:39:23');
INSERT INTO `strategy` VALUES (2, 100003, '抽奖策略-验证lock', NULL, '2024-01-13 10:34:06', '2024-04-03 16:03:21');
INSERT INTO `strategy` VALUES (3, 100002, '抽奖策略-非完整1概率', NULL, '2023-12-09 09:37:19', '2024-02-03 10:14:17');
INSERT INTO `strategy` VALUES (4, 100004, '抽奖策略-随机抽奖', NULL, '2023-12-09 09:37:19', '2024-01-20 19:21:03');
INSERT INTO `strategy` VALUES (5, 100005, '抽奖策略-测试概率计算', NULL, '2023-12-09 09:37:19', '2024-01-21 21:54:58');
INSERT INTO `strategy` VALUES (6, 100006, '抽奖策略-规则树', 'rule_blacklist,rule_weight', '2024-02-03 09:53:40', '2025-01-16 21:02:22');

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
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '抽奖策略奖品概率' ROW_FORMAT = Dynamic;

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
INSERT INTO `strategy_award` VALUES (22, 100006, 101, '随机积分', NULL, 100, 60, 0.0200, 'tree_luck_award', 1, '2023-12-09 09:38:31', '2025-01-16 21:22:45');
INSERT INTO `strategy_award` VALUES (23, 100006, 102, 'OpenAI会员卡', NULL, 100, 17, 0.0300, 'tree_luck_award', 2, '2023-12-09 09:38:31', '2025-01-16 21:22:45');
INSERT INTO `strategy_award` VALUES (24, 100006, 103, '支付优惠券', NULL, 100, 42, 0.0300, 'tree_luck_award', 3, '2023-12-09 09:38:31', '2025-01-16 19:26:43');
INSERT INTO `strategy_award` VALUES (25, 100006, 104, '小米台灯', NULL, 100, 31, 0.0300, 'tree_luck_award', 4, '2023-12-09 09:38:31', '2025-01-16 21:22:45');
INSERT INTO `strategy_award` VALUES (26, 100006, 105, '小米su7周体验', '抽奖3次后解锁', 100, 38, 0.0300, 'tree_lock_3', 5, '2023-12-09 09:38:31', '2024-04-27 13:08:16');
INSERT INTO `strategy_award` VALUES (27, 100006, 106, '轻奢办公椅', '抽奖2次后解锁', 100, 25, 0.0300, 'tree_lock_2', 6, '2023-12-09 09:38:31', '2024-04-27 13:30:05');
INSERT INTO `strategy_award` VALUES (28, 100006, 107, '小霸王游戏机', '抽奖1次后解锁', 100, 24, 0.0300, 'tree_lock_1', 7, '2023-12-09 09:38:31', '2024-04-27 13:36:35');
INSERT INTO `strategy_award` VALUES (29, 100006, 108, '暴走玩偶', NULL, 100, 31, 0.0300, 'tree_luck_award', 8, '2023-12-09 09:38:31', '2025-01-16 21:02:26');

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
) ENGINE = InnoDB AUTO_INCREMENT = 299 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '任务表，发送MQ' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of task
-- ----------------------------
INSERT INTO `task` VALUES (265, 'rem', 'send_rebate', '56028864422', '{\"eventId\":\"56028864422\",\"timestamp\":\"Jan 13, 2025 5:36:27 PM\",\"data\":{\"userId\":\"rem\",\"rebateDesc\":\"签到返利-sku额度\",\"rebateType\":\"sku\",\"rebateConfig\":\"9011\",\"bizId\":\"rem_sku_20250113\"}}', 'completed', '2025-01-13 17:36:16', '2025-01-13 17:36:16');
INSERT INTO `task` VALUES (266, 'rem', 'send_rebate', '46548599835', '{\"eventId\":\"46548599835\",\"timestamp\":\"Jan 13, 2025 5:36:27 PM\",\"data\":{\"userId\":\"rem\",\"rebateDesc\":\"签到返利-积分\",\"rebateType\":\"integral\",\"rebateConfig\":\"10\",\"bizId\":\"rem_integral_20250113\"}}', 'completed', '2025-01-13 17:36:16', '2025-01-13 17:36:17');
INSERT INTO `task` VALUES (267, 'rem', 'send_rebate', '53079489895', '{\"eventId\":\"53079489895\",\"timestamp\":\"Jan 13, 2025 5:40:11 PM\",\"data\":{\"userId\":\"rem\",\"rebateDesc\":\"签到返利-sku额度\",\"rebateType\":\"sku\",\"rebateConfig\":\"9011\",\"bizId\":\"rem_sku_20250113\"}}', 'completed', '2025-01-13 17:40:00', '2025-01-13 17:40:00');
INSERT INTO `task` VALUES (268, 'rem', 'send_rebate', '47735366441', '{\"eventId\":\"47735366441\",\"timestamp\":\"Jan 13, 2025 5:40:11 PM\",\"data\":{\"userId\":\"rem\",\"rebateDesc\":\"签到返利-积分\",\"rebateType\":\"integral\",\"rebateConfig\":\"10\",\"bizId\":\"rem_integral_20250113\"}}', 'completed', '2025-01-13 17:40:00', '2025-01-13 17:40:00');
INSERT INTO `task` VALUES (269, 'rem', 'send_rebate', '78574305381', '{\"eventId\":\"78574305381\",\"timestamp\":\"Jan 15, 2025 4:54:43 PM\",\"data\":{\"userId\":\"rem\",\"rebateDesc\":\"签到返利-sku额度\",\"rebateType\":\"sku\",\"rebateConfig\":\"9011\",\"bizId\":\"rem_sku_20250115\"}}', 'completed', '2025-01-15 16:54:29', '2025-01-15 16:54:29');
INSERT INTO `task` VALUES (270, 'rem', 'send_rebate', '89331866098', '{\"eventId\":\"89331866098\",\"timestamp\":\"Jan 15, 2025 4:54:43 PM\",\"data\":{\"userId\":\"rem\",\"rebateDesc\":\"签到返利-积分\",\"rebateType\":\"integral\",\"rebateConfig\":\"10\",\"bizId\":\"rem_integral_20250115\"}}', 'completed', '2025-01-15 16:54:29', '2025-01-15 16:54:29');
INSERT INTO `task` VALUES (274, 'user001', 'send_rebate', '05389507966', '{\"eventId\":\"05389507966\",\"timestamp\":\"Jan 16, 2025 8:12:15 PM\",\"data\":{\"userId\":\"user001\",\"rebateDesc\":\"签到返利-sku额度\",\"rebateType\":\"sku\",\"rebateConfig\":\"9011\",\"bizId\":\"user001_sku_20250116\"}}', 'completed', '2025-01-16 20:11:58', '2025-01-16 20:11:58');
INSERT INTO `task` VALUES (275, 'user001', 'send_rebate', '16330205983', '{\"eventId\":\"16330205983\",\"timestamp\":\"Jan 16, 2025 8:12:15 PM\",\"data\":{\"userId\":\"user001\",\"rebateDesc\":\"签到返利-积分\",\"rebateType\":\"integral\",\"rebateConfig\":\"10\",\"bizId\":\"user001_integral_20250116\"}}', 'completed', '2025-01-16 20:11:58', '2025-01-16 20:11:58');
INSERT INTO `task` VALUES (276, 'user001', 'send_award', 'WAFreCAAvCZ', '{\"eventId\":\"WAFreCAAvCZ\",\"timestamp\":\"Jan 16, 2025 8:54:37 PM\",\"data\":{\"awardId\":108,\"userId\":\"user001\",\"orderId\":\"784342925352\",\"awardTitle\":\"暴走玩偶\"}}', 'completed', '2025-01-16 20:54:38', '2025-01-16 20:54:20');
INSERT INTO `task` VALUES (277, 'user001', 'send_award', 'TyqVSysFYhY', '{\"eventId\":\"TyqVSysFYhY\",\"timestamp\":\"Jan 16, 2025 9:09:52 PM\",\"data\":{\"awardId\":104,\"userId\":\"user001\",\"orderId\":\"843267704343\",\"awardTitle\":\"小米台灯\"}}', 'completed', '2025-01-16 21:09:52', '2025-01-16 21:09:35');
INSERT INTO `task` VALUES (278, 'user001', 'send_award', 'dxDXdB9KQu1', '{\"eventId\":\"dxDXdB9KQu1\",\"timestamp\":\"Jan 16, 2025 9:16:16 PM\",\"data\":{\"awardId\":104,\"userId\":\"user001\",\"orderId\":\"037848280141\",\"awardTitle\":\"小米台灯\"}}', 'completed', '2025-01-16 21:16:16', '2025-01-16 21:15:59');
INSERT INTO `task` VALUES (279, 'user001', 'send_award', 'PQTodBfFA5R', '{\"eventId\":\"PQTodBfFA5R\",\"timestamp\":\"Jan 16, 2025 9:31:54 PM\",\"data\":{\"awardId\":101,\"userId\":\"user001\",\"orderId\":\"159360299516\",\"awardTitle\":\"随机积分\",\"awardConfig\":\"0.01,1\"}}', 'completed', '2025-01-16 21:31:54', '2025-01-16 21:31:37');
INSERT INTO `task` VALUES (280, 'user001', 'send_award', 'TOFA9ypg7MC', '{\"eventId\":\"TOFA9ypg7MC\",\"timestamp\":\"Jan 16, 2025 9:50:54 PM\",\"data\":{\"awardId\":101,\"userId\":\"user001\",\"orderId\":\"495379549517\",\"awardTitle\":\"随机积分\",\"awardConfig\":\"0.01,1\"}}', 'completed', '2025-01-16 21:50:54', '2025-01-16 21:50:37');
INSERT INTO `task` VALUES (281, 'user001', 'send_award', 'eSNljPuzkZz', '{\"eventId\":\"eSNljPuzkZz\",\"timestamp\":\"Jan 16, 2025 9:56:32 PM\",\"data\":{\"awardId\":101,\"userId\":\"user001\",\"orderId\":\"621416604479\",\"awardTitle\":\"随机积分\",\"awardConfig\":\"0.01,1\"}}', 'completed', '2025-01-16 21:56:33', '2025-01-16 21:56:15');
INSERT INTO `task` VALUES (282, 'user001', 'send_award', 'i7MFwwjQNoD', '{\"eventId\":\"i7MFwwjQNoD\",\"timestamp\":\"Jan 16, 2025 9:58:56 PM\",\"data\":{\"awardId\":101,\"userId\":\"user001\",\"orderId\":\"394903079977\",\"awardTitle\":\"随机积分\",\"awardConfig\":\"0.01,1\"}}', 'completed', '2025-01-16 21:58:56', '2025-01-16 21:58:39');
INSERT INTO `task` VALUES (283, 'user001', 'send_award', 'TX1GJvFxT6z', '{\"eventId\":\"TX1GJvFxT6z\",\"timestamp\":\"Jan 16, 2025 10:16:29 PM\",\"data\":{\"awardId\":101,\"userId\":\"user001\",\"orderId\":\"229483182272\",\"awardTitle\":\"随机积分\",\"awardConfig\":\"0.01,1\"}}', 'completed', '2025-01-16 22:16:29', '2025-01-16 22:16:12');
INSERT INTO `task` VALUES (284, 'rem', 'send_rebate', '42975137127', '{\"eventId\":\"42975137127\",\"timestamp\":\"Jan 18, 2025 6:19:27 PM\",\"data\":{\"userId\":\"rem\",\"orderId\":\"837768543652\",\"amount\":-1.68,\"outBusinessNo\":\"72983292644\"}}', 'completed', '2025-01-18 18:19:28', '2025-01-18 18:19:27');
INSERT INTO `task` VALUES (285, 'rem', 'send_rebate', '89110975081', '{\"eventId\":\"89110975081\",\"timestamp\":\"Jan 18, 2025 6:21:06 PM\",\"data\":{\"userId\":\"rem\",\"orderId\":\"372828302059\",\"amount\":-1.68,\"outBusinessNo\":\"700091009132\"}}', 'completed', '2025-01-18 18:21:07', '2025-01-18 18:21:06');
INSERT INTO `task` VALUES (286, 'rem', 'send_rebate', '75726955165', '{\"eventId\":\"75726955165\",\"timestamp\":\"Jan 18, 2025 6:49:36 PM\",\"data\":{\"userId\":\"rem\",\"orderId\":\"844486049108\",\"amount\":-1.68,\"outBusinessNo\":\"700091009133\"}}', 'completed', '2025-01-18 18:49:37', '2025-01-18 18:49:36');
INSERT INTO `task` VALUES (287, 'rem', 'credit_adjust_success', '14661584374', '{\"eventId\":\"14661584374\",\"timestamp\":\"Jan 18, 2025 7:01:51 PM\",\"data\":{\"userId\":\"rem\",\"orderId\":\"536905613003\",\"amount\":-1.68,\"outBusinessNo\":\"700091009134\"}}', 'completed', '2025-01-18 19:01:52', '2025-01-18 19:01:51');
INSERT INTO `task` VALUES (288, 'rem', 'credit_adjust_success', '98942872077', '{\"eventId\":\"98942872077\",\"timestamp\":\"Jan 18, 2025 7:10:57 PM\",\"data\":{\"userId\":\"rem\",\"orderId\":\"290576378971\",\"amount\":-1.68,\"outBusinessNo\":\"700091009135\"}}', 'completed', '2025-01-18 19:10:57', '2025-01-18 19:10:57');
INSERT INTO `task` VALUES (289, 'rem', 'send_rebate', '19136038362', '{\"eventId\":\"19136038362\",\"timestamp\":\"Jan 18, 2025 7:12:38 PM\",\"data\":{\"userId\":\"rem\",\"rebateDesc\":\"签到返利-sku额度\",\"rebateType\":\"sku\",\"rebateConfig\":\"9011\",\"bizId\":\"rem_sku_20250118\"}}', 'completed', '2025-01-18 19:12:37', '2025-01-18 19:12:38');
INSERT INTO `task` VALUES (290, 'rem', 'send_rebate', '62026386916', '{\"eventId\":\"62026386916\",\"timestamp\":\"Jan 18, 2025 7:12:38 PM\",\"data\":{\"userId\":\"rem\",\"rebateDesc\":\"签到返利-积分\",\"rebateType\":\"integral\",\"rebateConfig\":\"10\",\"bizId\":\"rem_integral_20250118\"}}', 'completed', '2025-01-18 19:12:37', '2025-01-18 19:12:38');
INSERT INTO `task` VALUES (291, 'rem', 'credit_adjust_success', '67653113313', '{\"eventId\":\"67653113313\",\"timestamp\":\"Jan 18, 2025 7:12:39 PM\",\"data\":{\"userId\":\"rem\",\"orderId\":\"096730416914\",\"amount\":10,\"outBusinessNo\":\"rem_integral_20250118\"}}', 'completed', '2025-01-18 19:12:40', '2025-01-18 19:12:39');
INSERT INTO `task` VALUES (292, 'user001', 'send_rebate', '71983984737', '{\"eventId\":\"71983984737\",\"timestamp\":\"Jan 18, 2025 9:38:39 PM\",\"data\":{\"userId\":\"user001\",\"rebateDesc\":\"签到返利-sku额度\",\"rebateType\":\"sku\",\"rebateConfig\":\"9011\",\"bizId\":\"user001_sku_20250118\"}}', 'completed', '2025-01-18 21:38:38', '2025-01-18 21:38:38');
INSERT INTO `task` VALUES (293, 'user001', 'send_rebate', '21580295720', '{\"eventId\":\"21580295720\",\"timestamp\":\"Jan 18, 2025 9:38:39 PM\",\"data\":{\"userId\":\"user001\",\"rebateDesc\":\"签到返利-积分\",\"rebateType\":\"integral\",\"rebateConfig\":\"10\",\"bizId\":\"user001_integral_20250118\"}}', 'completed', '2025-01-18 21:38:38', '2025-01-18 21:38:38');
INSERT INTO `task` VALUES (294, 'user001', 'credit_adjust_success', '04448433118', '{\"eventId\":\"04448433118\",\"timestamp\":\"Jan 18, 2025 9:38:40 PM\",\"data\":{\"userId\":\"user001\",\"orderId\":\"463390026993\",\"amount\":10,\"outBusinessNo\":\"user001_integral_20250118\"}}', 'completed', '2025-01-18 21:38:41', '2025-01-18 21:38:39');
INSERT INTO `task` VALUES (295, 'user002', 'send_rebate', '95688247243', '{\"eventId\":\"95688247243\",\"timestamp\":\"Jan 18, 2025 9:58:11 PM\",\"data\":{\"userId\":\"user002\",\"rebateDesc\":\"签到返利-sku额度\",\"rebateType\":\"sku\",\"rebateConfig\":\"9011\",\"bizId\":\"user002_sku_20250118\"}}', 'completed', '2025-01-18 21:58:10', '2025-01-18 21:58:10');
INSERT INTO `task` VALUES (296, 'user002', 'send_rebate', '44488499632', '{\"eventId\":\"44488499632\",\"timestamp\":\"Jan 18, 2025 9:58:11 PM\",\"data\":{\"userId\":\"user002\",\"rebateDesc\":\"签到返利-积分\",\"rebateType\":\"integral\",\"rebateConfig\":\"10\",\"bizId\":\"user002_integral_20250118\"}}', 'completed', '2025-01-18 21:58:10', '2025-01-18 21:58:38');
INSERT INTO `task` VALUES (297, 'user002', 'credit_adjust_success', '08097379271', '{\"eventId\":\"08097379271\",\"timestamp\":\"Jan 18, 2025 10:00:35 PM\",\"data\":{\"userId\":\"user002\",\"orderId\":\"745472307962\",\"amount\":10,\"outBusinessNo\":\"user002_integral_20250118\"}}', 'completed', '2025-01-18 22:00:36', '2025-01-18 22:00:35');
INSERT INTO `task` VALUES (300, 'rem', 'send_rebate', '98655417284', '{\"eventId\":\"98655417284\",\"timestamp\":\"Jan 21, 2025 4:59:44 PM\",\"data\":{\"userId\":\"rem\",\"rebateDesc\":\"签到返利-sku额度\",\"rebateType\":\"sku\",\"rebateConfig\":\"9011\",\"bizId\":\"rem_sku_20250121\"}}', 'completed', '2025-01-21 16:59:43', '2025-01-21 16:59:43');
INSERT INTO `task` VALUES (301, 'rem', 'send_rebate', '66634391894', '{\"eventId\":\"66634391894\",\"timestamp\":\"Jan 21, 2025 4:59:44 PM\",\"data\":{\"userId\":\"rem\",\"rebateDesc\":\"签到返利-积分\",\"rebateType\":\"integral\",\"rebateConfig\":\"10\",\"bizId\":\"rem_integral_20250121\"}}', 'completed', '2025-01-21 16:59:43', '2025-01-21 16:59:43');
INSERT INTO `task` VALUES (302, 'rem', 'credit_adjust_success', '01552816034', '{\"eventId\":\"01552816034\",\"timestamp\":\"Jan 21, 2025 4:59:45 PM\",\"data\":{\"userId\":\"rem\",\"orderId\":\"457214904105\",\"amount\":10,\"outBusinessNo\":\"rem_integral_20250121\"}}', 'completed', '2025-01-21 16:59:46', '2025-01-21 16:59:44');
INSERT INTO `task` VALUES (303, 'rem', 'credit_adjust_success', '94861144482', '{\"eventId\":\"94861144482\",\"timestamp\":\"Jan 21, 2025 5:00:37 PM\",\"data\":{\"userId\":\"rem\",\"orderId\":\"575631655257\",\"amount\":1.68,\"outBusinessNo\":\"843574410235\"}}', 'completed', '2025-01-21 17:00:37', '2025-01-21 17:00:36');
INSERT INTO `task` VALUES (304, 'rem', 'credit_adjust_success', '91813514099', '{\"eventId\":\"91813514099\",\"timestamp\":\"Jan 21, 2025 5:04:51 PM\",\"data\":{\"userId\":\"rem\",\"orderId\":\"017261762149\",\"amount\":1.68,\"outBusinessNo\":\"838307787957\"}}', 'completed', '2025-01-21 17:04:51', '2025-01-21 17:04:50');
INSERT INTO `task` VALUES (305, 'rem', 'credit_adjust_success', '24687690799', '{\"eventId\":\"24687690799\",\"timestamp\":\"Jan 21, 2025 6:16:55 PM\",\"data\":{\"userId\":\"rem\",\"orderId\":\"000212020813\",\"amount\":1.68,\"outBusinessNo\":\"195830752458\"}}', 'completed', '2025-01-21 18:16:55', '2025-01-21 18:16:54');
INSERT INTO `task` VALUES (306, 'rem', 'credit_adjust_success', '88902213603', '{\"eventId\":\"88902213603\",\"timestamp\":\"Jan 21, 2025 6:18:09 PM\",\"data\":{\"userId\":\"rem\",\"orderId\":\"045703978891\",\"amount\":1.68,\"outBusinessNo\":\"196154478727\"}}', 'completed', '2025-01-21 18:18:09', '2025-01-21 18:18:08');
INSERT INTO `task` VALUES (307, 'rem', 'credit_adjust_success', '52076284740', '{\"eventId\":\"52076284740\",\"timestamp\":\"Jan 21, 2025 6:18:23 PM\",\"data\":{\"userId\":\"rem\",\"orderId\":\"071216816710\",\"amount\":1.68,\"outBusinessNo\":\"601168775828\"}}', 'completed', '2025-01-21 18:18:24', '2025-01-21 18:18:22');
INSERT INTO `task` VALUES (308, 'rem', 'credit_adjust_success', '50902363437', '{\"eventId\":\"50902363437\",\"timestamp\":\"Jan 21, 2025 6:18:30 PM\",\"data\":{\"userId\":\"rem\",\"orderId\":\"822029805574\",\"amount\":1.68,\"outBusinessNo\":\"120601711567\"}}', 'completed', '2025-01-21 18:18:30', '2025-01-21 18:18:29');
INSERT INTO `task` VALUES (309, 'rem', 'credit_adjust_success', '96933670939', '{\"eventId\":\"96933670939\",\"timestamp\":\"Jan 21, 2025 6:26:09 PM\",\"data\":{\"userId\":\"rem\",\"orderId\":\"251807532639\",\"amount\":1.68,\"outBusinessNo\":\"870315337545\"}}', 'completed', '2025-01-21 18:26:09', '2025-01-21 18:26:58');
INSERT INTO `task` VALUES (310, 'rem', 'credit_adjust_success', '70661351525', '{\"eventId\":\"70661351525\",\"timestamp\":\"Jan 21, 2025 6:27:26 PM\",\"data\":{\"userId\":\"rem\",\"orderId\":\"144827650172\",\"amount\":1.68,\"outBusinessNo\":\"967330176242\"}}', 'completed', '2025-01-21 18:27:27', '2025-01-21 18:27:25');
INSERT INTO `task` VALUES (311, 'rem', 'credit_adjust_success', '70904126443', '{\"eventId\":\"70904126443\",\"timestamp\":\"Jan 21, 2025 6:32:38 PM\",\"data\":{\"userId\":\"rem\",\"orderId\":\"061384517963\",\"amount\":1.68,\"outBusinessNo\":\"733389754979\"}}', 'completed', '2025-01-21 18:32:39', '2025-01-21 18:32:37');
INSERT INTO `task` VALUES (312, 'rem', 'credit_adjust_success', '72604183258', '{\"eventId\":\"72604183258\",\"timestamp\":\"Jan 21, 2025 6:33:33 PM\",\"data\":{\"userId\":\"rem\",\"orderId\":\"694516370121\",\"amount\":1.68,\"outBusinessNo\":\"960669138565\"}}', 'completed', '2025-01-21 18:33:34', '2025-01-21 18:33:32');

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
INSERT INTO `user` VALUES (1, 'user001', 100006, 'rule_blacklist', '2025-01-16 17:18:57', '2025-01-16 17:18:57');

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
) ENGINE = InnoDB AUTO_INCREMENT = 252 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户中奖记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_award_record
-- ----------------------------
INSERT INTO `user_award_record` VALUES (247, 'user001', 100301, 100006, '495379549517', 101, '随机积分', '2025-01-16 21:50:54', 'completed', '2025-01-16 21:50:54', '2025-01-16 21:55:54');
INSERT INTO `user_award_record` VALUES (248, 'user001', 100301, 100006, '621416604479', 101, '随机积分', '2025-01-16 21:56:32', 'completed', '2025-01-16 21:56:33', '2025-01-16 21:58:23');
INSERT INTO `user_award_record` VALUES (249, 'user001', 100301, 100006, '394903079977', 101, '随机积分', '2025-01-16 21:58:56', 'completed', '2025-01-16 21:58:56', '2025-01-16 22:15:27');
INSERT INTO `user_award_record` VALUES (252, 'user001', 100301, 100006, '229483182272', 101, '随机积分', '2025-01-16 22:16:29', 'completed', '2025-01-16 22:16:29', '2025-01-16 22:17:06');

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
  `out_business_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '业务防重ID-外部透传',
  `biz_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '业务ID-  拼接的唯一值',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uq_order_id`(`order_id` ASC) USING BTREE,
  UNIQUE INDEX `uq_biz_id`(`biz_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 77 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户行为返利流水订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_behavior_rebate_order
-- ----------------------------
INSERT INTO `user_behavior_rebate_order` VALUES (63, 'rem', '889044219512', 'sign', '签到返利-sku额度', 'sku', '9011', '', 'rem_sku_20250113', '2025-01-13 17:40:00', '2025-01-13 17:40:00');
INSERT INTO `user_behavior_rebate_order` VALUES (64, 'rem', '476896637656', 'sign', '签到返利-积分', 'integral', '10', '', 'rem_integral_20250113', '2025-01-13 17:40:00', '2025-01-13 17:40:00');
INSERT INTO `user_behavior_rebate_order` VALUES (65, 'rem', '825237223304', 'sign', '签到返利-sku额度', 'sku', '9011', '20250115', 'rem_sku_20250115', '2025-01-15 16:54:28', '2025-01-15 16:54:28');
INSERT INTO `user_behavior_rebate_order` VALUES (66, 'rem', '995045360045', 'sign', '签到返利-积分', 'integral', '10', '20250115', 'rem_integral_20250115', '2025-01-15 16:54:29', '2025-01-15 16:54:29');
INSERT INTO `user_behavior_rebate_order` VALUES (67, 'user001', '412988445459', 'sign', '签到返利-sku额度', 'sku', '9011', '20250116', 'user001_sku_20250116', '2025-01-16 20:11:58', '2025-01-16 20:11:58');
INSERT INTO `user_behavior_rebate_order` VALUES (68, 'user001', '093961514479', 'sign', '签到返利-积分', 'integral', '10', '20250116', 'user001_integral_20250116', '2025-01-16 20:11:58', '2025-01-16 20:11:58');
INSERT INTO `user_behavior_rebate_order` VALUES (70, 'rem', '826921424310', 'sign', '签到返利-sku额度', 'sku', '9011', '20250118', 'rem_sku_20250118', '2025-01-18 19:12:37', '2025-01-18 19:12:37');
INSERT INTO `user_behavior_rebate_order` VALUES (71, 'rem', '768585551534', 'sign', '签到返利-积分', 'integral', '10', '20250118', 'rem_integral_20250118', '2025-01-18 19:12:37', '2025-01-18 19:12:37');
INSERT INTO `user_behavior_rebate_order` VALUES (72, 'user001', '286504039139', 'sign', '签到返利-sku额度', 'sku', '9011', '20250118', 'user001_sku_20250118', '2025-01-18 21:38:38', '2025-01-18 21:38:38');
INSERT INTO `user_behavior_rebate_order` VALUES (73, 'user001', '781011508461', 'sign', '签到返利-积分', 'integral', '10', '20250118', 'user001_integral_20250118', '2025-01-18 21:38:38', '2025-01-18 21:38:38');
INSERT INTO `user_behavior_rebate_order` VALUES (74, 'user002', '752742266082', 'sign', '签到返利-sku额度', 'sku', '9011', '20250118', 'user002_sku_20250118', '2025-01-18 21:58:10', '2025-01-18 21:58:10');
INSERT INTO `user_behavior_rebate_order` VALUES (75, 'user002', '655009711226', 'sign', '签到返利-积分', 'integral', '10', '20250118', 'user002_integral_20250118', '2025-01-18 21:58:10', '2025-01-18 21:58:10');
INSERT INTO `user_behavior_rebate_order` VALUES (78, 'rem', '014083316494', 'sign', '签到返利-sku额度', 'sku', '9011', '20250121', 'rem_sku_20250121', '2025-01-21 16:59:43', '2025-01-21 16:59:43');
INSERT INTO `user_behavior_rebate_order` VALUES (79, 'rem', '933097996021', 'sign', '签到返利-积分', 'integral', '10', '20250121', 'rem_integral_20250121', '2025-01-21 16:59:43', '2025-01-21 16:59:43');

-- ----------------------------
-- Table structure for user_credit_account
-- ----------------------------
DROP TABLE IF EXISTS `user_credit_account`;
CREATE TABLE `user_credit_account`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户ID',
  `total_amount` decimal(10, 2) NOT NULL COMMENT '总积分 显示总账户值',
  `available_amount` decimal(10, 2) NOT NULL COMMENT '可用积分-每次扣减的值',
  `account_status` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '账户状态【open-可用、close-冻结】',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uq_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户积分账户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_credit_account
-- ----------------------------
INSERT INTO `user_credit_account` VALUES (1, 'user001', 11.37, 11.37, 'open', '2025-01-16 21:58:39', '2025-01-18 21:38:39');
INSERT INTO `user_credit_account` VALUES (2, 'rem', 38.32, 38.32, 'open', '2025-01-17 18:04:05', '2025-01-21 18:33:32');
INSERT INTO `user_credit_account` VALUES (3, 'user002', 10.00, 10.00, 'open', '2025-01-18 22:00:34', '2025-01-18 22:00:34');

-- ----------------------------
-- Table structure for user_credit_order
-- ----------------------------
DROP TABLE IF EXISTS `user_credit_order`;
CREATE TABLE `user_credit_order`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户ID',
  `order_id` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单ID',
  `trade_name` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单ID',
  `trade_type` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '交易类型【forward-正向、reverse-逆向】',
  `trade_amount` decimal(10, 2) NOT NULL COMMENT '交易金额',
  `out_business_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '业务防重-外部透传【返行为等唯一标识】',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uq_out_business_no`(`out_business_no` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户积分订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_credit_order
-- ----------------------------
INSERT INTO `user_credit_order` VALUES (1, 'rem', '397875069632', '行为返利', 'forward', 1.11, '202501170321', '2025-01-17 18:04:06', '2025-01-17 18:04:06');
INSERT INTO `user_credit_order` VALUES (3, 'rem', '516206622478', '行为返利', 'forward', -1.11, '202501170322', '2025-01-17 18:06:34', '2025-01-17 18:06:34');
INSERT INTO `user_credit_order` VALUES (7, 'rem', '837768543652', '行为返利', 'forward', -1.68, '72983292644', '2025-01-18 18:19:27', '2025-01-18 18:19:27');
INSERT INTO `user_credit_order` VALUES (8, 'rem', '372828302059', '行为返利', 'forward', -1.68, '700091009132', '2025-01-18 18:21:06', '2025-01-18 18:21:06');
INSERT INTO `user_credit_order` VALUES (9, 'rem', '844486049108', '行为返利', 'forward', -1.68, '700091009133', '2025-01-18 18:49:36', '2025-01-18 18:49:36');
INSERT INTO `user_credit_order` VALUES (10, 'rem', '536905613003', '行为返利', 'forward', -1.68, '700091009134', '2025-01-18 19:01:51', '2025-01-18 19:01:51');
INSERT INTO `user_credit_order` VALUES (11, 'rem', '290576378971', '行为返利', 'forward', -1.68, '700091009135', '2025-01-18 19:10:56', '2025-01-18 19:10:56');
INSERT INTO `user_credit_order` VALUES (12, 'rem', '096730416914', '行为返利', 'forward', 10.00, 'rem_integral_20250118', '2025-01-18 19:12:38', '2025-01-18 19:12:38');
INSERT INTO `user_credit_order` VALUES (13, 'user001', '463390026993', '行为返利', 'forward', 10.00, 'user001_integral_20250118', '2025-01-18 21:38:39', '2025-01-18 21:38:39');
INSERT INTO `user_credit_order` VALUES (14, 'user002', '745472307962', '行为返利', 'forward', 10.00, 'user002_integral_20250118', '2025-01-18 22:00:35', '2025-01-18 22:00:35');
INSERT INTO `user_credit_order` VALUES (15, 'rem', '457214904105', '行为返利', 'forward', 10.00, 'rem_integral_20250121', '2025-01-21 16:59:44', '2025-01-21 16:59:44');
INSERT INTO `user_credit_order` VALUES (16, 'rem', '575631655257', '兑换抽奖', 'forward', 1.68, '843574410235', '2025-01-21 17:00:35', '2025-01-21 17:00:35');
INSERT INTO `user_credit_order` VALUES (17, 'rem', '017261762149', '兑换抽奖', 'forward', 1.68, '838307787957', '2025-01-21 17:04:50', '2025-01-21 17:04:50');
INSERT INTO `user_credit_order` VALUES (18, 'rem', '000212020813', '兑换抽奖', 'reserve', 1.68, '195830752458', '2025-01-21 18:16:54', '2025-01-21 18:16:54');
INSERT INTO `user_credit_order` VALUES (19, 'rem', '045703978891', '兑换抽奖', 'reserve', 1.68, '196154478727', '2025-01-21 18:18:08', '2025-01-21 18:18:08');
INSERT INTO `user_credit_order` VALUES (20, 'rem', '071216816710', '兑换抽奖', 'reserve', 1.68, '601168775828', '2025-01-21 18:18:22', '2025-01-21 18:18:22');
INSERT INTO `user_credit_order` VALUES (21, 'rem', '822029805574', '兑换抽奖', 'reserve', 1.68, '120601711567', '2025-01-21 18:18:29', '2025-01-21 18:18:29');
INSERT INTO `user_credit_order` VALUES (22, 'rem', '251807532639', '兑换抽奖', 'reserve', 1.68, '870315337545', '2025-01-21 18:26:45', '2025-01-21 18:26:45');
INSERT INTO `user_credit_order` VALUES (23, 'rem', '144827650172', '兑换抽奖', 'reserve', 1.68, '967330176242', '2025-01-21 18:27:25', '2025-01-21 18:27:25');
INSERT INTO `user_credit_order` VALUES (24, 'rem', '061384517963', '兑换抽奖', 'reserve', 1.68, '733389754979', '2025-01-21 18:32:37', '2025-01-21 18:32:37');
INSERT INTO `user_credit_order` VALUES (25, 'rem', '694516370121', '兑换抽奖', 'reserve', 1.68, '960669138565', '2025-01-21 18:33:32', '2025-01-21 18:33:32');

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
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户抽奖订单表' ROW_FORMAT = Dynamic;

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
INSERT INTO `user_raffle_order` VALUES (15, 'user001', 100301, '测试活动', 100006, '495379549517', '2025-01-16 21:50:53', 'used', '2025-01-16 21:50:54', '2025-01-16 21:56:08');
INSERT INTO `user_raffle_order` VALUES (16, 'user001', 100301, '测试活动', 100006, '621416604479', '2025-01-16 21:56:32', 'used', '2025-01-16 21:56:32', '2025-01-16 21:58:30');
INSERT INTO `user_raffle_order` VALUES (17, 'user001', 100301, '测试活动', 100006, '394903079977', '2025-01-16 21:58:55', 'used', '2025-01-16 21:58:56', '2025-01-16 22:14:46');
INSERT INTO `user_raffle_order` VALUES (18, 'user001', 100301, '测试活动', 100006, '229483182272', '2025-01-16 22:16:29', 'used', '2025-01-16 22:16:29', '2025-01-16 22:17:04');

SET FOREIGN_KEY_CHECKS = 1;
