/*
 Navicat Premium Data Transfer

 Source Server         : 华为云
 Source Server Type    : MySQL
 Source Server Version : 90100
 Source Host           : 110.41.180.185:3306
 Source Schema         : nacos-config

 Target Server Type    : MySQL
 Target Server Version : 90100
 File Encoding         : 65001

 Date: 23/01/2025 21:03:29
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config_info
-- ----------------------------
DROP TABLE IF EXISTS `config_info`;
CREATE TABLE `config_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'group_id',
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'source ip',
  `app_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'app_name',
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '租户字段',
  `c_desc` varchar(256) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'configuration description',
  `c_use` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'configuration usage',
  `effect` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '配置生效的描述',
  `type` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '配置的类型',
  `c_schema` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL COMMENT '配置的模式',
  `encrypted_data_key` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '密钥',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfo_datagrouptenant`(`data_id` ASC, `group_id` ASC, `tenant_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = 'config_info' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info
-- ----------------------------
INSERT INTO `config_info` VALUES (2, 'top.javarem.api.IRaffleStrategyService:1.0::provider:rem-market', 'dubbo', '{\"annotations\":[],\"canonicalName\":\"top.javarem.api.IRaffleStrategyService\",\"codeSource\":\"file:/D:/Develop/project/rem-market/market-api/target/classes/\",\"methods\":[{\"annotations\":[],\"name\":\"queryRaffleStrategyRuleWeight\",\"parameterTypes\":[\"top.javarem.api.dto.RaffleStrategyRuleWeightRequestDTO\"],\"parameters\":[],\"returnType\":\"top.javarem.api.response.Response\"},{\"annotations\":[],\"name\":\"assembleStrategy\",\"parameterTypes\":[\"java.lang.Long\"],\"parameters\":[],\"returnType\":\"top.javarem.api.response.Response\"},{\"annotations\":[],\"name\":\"executeRaffle\",\"parameterTypes\":[\"top.javarem.api.dto.RaffleStrategyRequestDTO\"],\"parameters\":[],\"returnType\":\"top.javarem.api.response.Response\"},{\"annotations\":[],\"name\":\"displayAwardList\",\"parameterTypes\":[\"top.javarem.api.dto.DisplayAwardRequestDTO\"],\"parameters\":[],\"returnType\":\"top.javarem.api.response.Response\"}],\"parameters\":{\"anyhost\":\"true\",\"application\":\"rem-market\",\"interface\":\"top.javarem.api.IRaffleStrategyService\",\"version\":\"1.0\",\"release\":\"3.0.9\",\"side\":\"provider\",\"dubbo\":\"2.0.2\",\"pid\":\"8196\",\"application.version\":\"1.0\",\"methods\":\"queryRaffleStrategyRuleWeight,displayAwardList,assembleStrategy,executeRaffle\",\"deprecated\":\"false\",\"service-name-mapping\":\"true\",\"qos.enable\":\"false\",\"generic\":\"false\",\"bind.port\":\"20880\",\"revision\":\"1.0\",\"bind.ip\":\"169.254.105.229\",\"background\":\"false\",\"dynamic\":\"true\",\"timestamp\":\"1737637071912\"},\"types\":[{\"enum\":[],\"items\":[],\"properties\":{\"activityId\":\"java.lang.Long\",\"userId\":\"java.lang.String\"},\"type\":\"top.javarem.api.dto.RaffleStrategyRuleWeightRequestDTO\"},{\"enum\":[],\"items\":[],\"properties\":{},\"type\":\"java.lang.Long\"},{\"enum\":[],\"items\":[],\"properties\":{\"code\":\"java.lang.String\",\"data\":\"java.lang.Object\",\"info\":\"java.lang.String\"},\"type\":\"top.javarem.api.response.Response\"},{\"enum\":[],\"items\":[],\"properties\":{\"activityId\":\"java.lang.Long\",\"userId\":\"java.lang.String\"},\"type\":\"top.javarem.api.dto.DisplayAwardRequestDTO\"},{\"enum\":[],\"items\":[],\"properties\":{\"UserId\":\"java.lang.String\",\"strategyId\":\"java.lang.Long\"},\"type\":\"top.javarem.api.dto.RaffleStrategyRequestDTO\"},{\"enum\":[],\"items\":[],\"properties\":{},\"type\":\"java.lang.Object\"},{\"enum\":[],\"items\":[],\"properties\":{},\"type\":\"java.lang.String\"}],\"uniqueId\":\"top.javarem.api.IRaffleStrategyService@file:/D:/Develop/project/rem-market/market-api/target/classes/\"}', 'ed74f397f01999d19b713485fa5e6461', '2025-01-23 19:04:19', '2025-01-23 20:57:48', NULL, '169.254.105.229', '', '', NULL, NULL, NULL, 'text', NULL, '');
INSERT INTO `config_info` VALUES (3, 'top.javarem.api.IRaffleStrategyService', 'mapping', 'rem-market', '2d04a7ac8ae135c17e01dedab7192a1e', '2025-01-23 19:04:19', '2025-01-23 19:04:19', NULL, '169.254.105.229', '', '', NULL, NULL, NULL, 'text', NULL, '');
INSERT INTO `config_info` VALUES (4, 'top.javarem.api.IRaffleActivityService:1.0::provider:rem-market', 'dubbo', '{\"annotations\":[],\"canonicalName\":\"top.javarem.api.IRaffleActivityService\",\"codeSource\":\"file:/D:/Develop/project/rem-market/market-api/target/classes/\",\"methods\":[{\"annotations\":[],\"name\":\"getSkuProductListByActivityId\",\"parameterTypes\":[\"java.lang.Long\"],\"parameters\":[],\"returnType\":\"top.javarem.api.response.Response\"},{\"annotations\":[],\"name\":\"isCalenderSignRebate\",\"parameterTypes\":[\"java.lang.String\"],\"parameters\":[],\"returnType\":\"top.javarem.api.response.Response\"},{\"annotations\":[],\"name\":\"creditExchangeProduct\",\"parameterTypes\":[\"top.javarem.api.dto.SkuProductShopCartRequestDTO\"],\"parameters\":[],\"returnType\":\"top.javarem.api.response.Response\"},{\"annotations\":[],\"name\":\"calenderSignRebate\",\"parameterTypes\":[\"java.lang.String\"],\"parameters\":[],\"returnType\":\"top.javarem.api.response.Response\"},{\"annotations\":[],\"name\":\"queryUserActivityAccount\",\"parameterTypes\":[\"top.javarem.api.dto.UserActivityAccountRequestDTO\"],\"parameters\":[],\"returnType\":\"top.javarem.api.response.Response\"},{\"annotations\":[],\"name\":\"queryUserCredit\",\"parameterTypes\":[\"java.lang.String\"],\"parameters\":[],\"returnType\":\"top.javarem.api.response.Response\"},{\"annotations\":[],\"name\":\"armory\",\"parameterTypes\":[\"java.lang.Long\"],\"parameters\":[],\"returnType\":\"top.javarem.api.response.Response\"},{\"annotations\":[],\"name\":\"draw\",\"parameterTypes\":[\"top.javarem.api.dto.ActivityDrawRequestDTO\"],\"parameters\":[],\"returnType\":\"top.javarem.api.response.Response\"}],\"parameters\":{\"anyhost\":\"true\",\"application\":\"rem-market\",\"interface\":\"top.javarem.api.IRaffleActivityService\",\"version\":\"1.0\",\"release\":\"3.0.9\",\"side\":\"provider\",\"dubbo\":\"2.0.2\",\"pid\":\"8196\",\"application.version\":\"1.0\",\"methods\":\"queryUserActivityAccount,armory,calenderSignRebate,isCalenderSignRebate,queryUserCredit,getSkuProductListByActivityId,draw,creditExchangeProduct\",\"deprecated\":\"false\",\"service-name-mapping\":\"true\",\"qos.enable\":\"false\",\"generic\":\"false\",\"bind.port\":\"20880\",\"revision\":\"1.0\",\"bind.ip\":\"169.254.105.229\",\"background\":\"false\",\"dynamic\":\"true\",\"timestamp\":\"1737637073069\"},\"types\":[{\"enum\":[],\"items\":[],\"properties\":{\"activityId\":\"java.lang.Long\",\"userId\":\"java.lang.String\"},\"type\":\"top.javarem.api.dto.ActivityDrawRequestDTO\"},{\"enum\":[],\"items\":[],\"properties\":{},\"type\":\"java.lang.Long\"},{\"enum\":[],\"items\":[],\"properties\":{\"code\":\"java.lang.String\",\"data\":\"java.lang.Object\",\"info\":\"java.lang.String\"},\"type\":\"top.javarem.api.response.Response\"},{\"enum\":[],\"items\":[],\"properties\":{},\"type\":\"java.lang.Object\"},{\"enum\":[],\"items\":[],\"properties\":{\"sku\":\"java.lang.Long\",\"userId\":\"java.lang.String\"},\"type\":\"top.javarem.api.dto.SkuProductShopCartRequestDTO\"},{\"enum\":[],\"items\":[],\"properties\":{},\"type\":\"java.lang.String\"},{\"enum\":[],\"items\":[],\"properties\":{\"activityId\":\"java.lang.String\",\"userId\":\"java.lang.String\"},\"type\":\"top.javarem.api.dto.UserActivityAccountRequestDTO\"}],\"uniqueId\":\"top.javarem.api.IRaffleActivityService@file:/D:/Develop/project/rem-market/market-api/target/classes/\"}', 'e73c031b474e9ec5bc0925d3be5979d0', '2025-01-23 19:04:19', '2025-01-23 20:57:48', NULL, '169.254.105.229', '', '', NULL, NULL, NULL, 'text', NULL, '');
INSERT INTO `config_info` VALUES (5, 'top.javarem.api.IRaffleActivityService', 'mapping', 'rem-market', '2d04a7ac8ae135c17e01dedab7192a1e', '2025-01-23 19:04:19', '2025-01-23 19:04:19', NULL, '169.254.105.229', '', '', NULL, NULL, NULL, 'text', NULL, '');

-- ----------------------------
-- Table structure for config_info_aggr
-- ----------------------------
DROP TABLE IF EXISTS `config_info_aggr`;
CREATE TABLE `config_info_aggr`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'group_id',
  `datum_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'datum_id',
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '内容',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `app_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'app_name',
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfoaggr_datagrouptenantdatum`(`data_id` ASC, `group_id` ASC, `tenant_id` ASC, `datum_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '增加租户字段' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info_aggr
-- ----------------------------

-- ----------------------------
-- Table structure for config_info_beta
-- ----------------------------
DROP TABLE IF EXISTS `config_info_beta`;
CREATE TABLE `config_info_beta`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'group_id',
  `app_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'content',
  `beta_ips` varchar(1024) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'betaIps',
  `md5` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'source ip',
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '租户字段',
  `encrypted_data_key` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '密钥',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfobeta_datagrouptenant`(`data_id` ASC, `group_id` ASC, `tenant_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = 'config_info_beta' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info_beta
-- ----------------------------

-- ----------------------------
-- Table structure for config_info_tag
-- ----------------------------
DROP TABLE IF EXISTS `config_info_tag`;
CREATE TABLE `config_info_tag`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `tag_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'tag_id',
  `app_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'source ip',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfotag_datagrouptenanttag`(`data_id` ASC, `group_id` ASC, `tenant_id` ASC, `tag_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = 'config_info_tag' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info_tag
-- ----------------------------

-- ----------------------------
-- Table structure for config_tags_relation
-- ----------------------------
DROP TABLE IF EXISTS `config_tags_relation`;
CREATE TABLE `config_tags_relation`  (
  `id` bigint NOT NULL COMMENT 'id',
  `tag_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'tag_name',
  `tag_type` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'tag_type',
  `data_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `nid` bigint NOT NULL AUTO_INCREMENT COMMENT 'nid, 自增长标识',
  PRIMARY KEY (`nid`) USING BTREE,
  UNIQUE INDEX `uk_configtagrelation_configidtag`(`id` ASC, `tag_name` ASC, `tag_type` ASC) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = 'config_tag_relation' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_tags_relation
-- ----------------------------

-- ----------------------------
-- Table structure for group_capacity
-- ----------------------------
DROP TABLE IF EXISTS `group_capacity`;
CREATE TABLE `group_capacity`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
  `quota` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '配额，0表示使用默认值',
  `usage` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '使用量',
  `max_size` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '聚合子配置最大个数，，0表示使用默认值',
  `max_aggr_size` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_group_id`(`group_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '集群、各Group容量信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of group_capacity
-- ----------------------------

-- ----------------------------
-- Table structure for his_config_info
-- ----------------------------
DROP TABLE IF EXISTS `his_config_info`;
CREATE TABLE `his_config_info`  (
  `id` bigint UNSIGNED NOT NULL COMMENT 'id',
  `nid` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'nid, 自增标识',
  `data_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'group_id',
  `app_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'source ip',
  `op_type` char(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'operation type',
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '租户字段',
  `encrypted_data_key` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '密钥',
  PRIMARY KEY (`nid`) USING BTREE,
  INDEX `idx_gmt_create`(`gmt_create` ASC) USING BTREE,
  INDEX `idx_gmt_modified`(`gmt_modified` ASC) USING BTREE,
  INDEX `idx_did`(`data_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '多租户改造' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of his_config_info
-- ----------------------------
INSERT INTO `his_config_info` VALUES (0, 1, '123', 'DEFAULT_GROUP', '', '123', '202cb962ac59075b964b07152d234b70', '2025-01-23 16:29:14', '2025-01-23 16:29:14', NULL, '223.157.97.242', 'I', '', '');
INSERT INTO `his_config_info` VALUES (0, 2, 'top.javarem.api.IRaffleStrategyService:1.0::provider:rem-market', 'dubbo', '', '{\"annotations\":[],\"canonicalName\":\"top.javarem.api.IRaffleStrategyService\",\"codeSource\":\"file:/D:/Develop/project/rem-market/market-api/target/classes/\",\"methods\":[{\"annotations\":[],\"name\":\"assembleStrategy\",\"parameterTypes\":[\"java.lang.Long\"],\"parameters\":[],\"returnType\":\"top.javarem.api.response.Response\"},{\"annotations\":[],\"name\":\"displayAwardList\",\"parameterTypes\":[\"top.javarem.api.dto.DisplayAwardRequestDTO\"],\"parameters\":[],\"returnType\":\"top.javarem.api.response.Response\"},{\"annotations\":[],\"name\":\"executeRaffle\",\"parameterTypes\":[\"top.javarem.api.dto.RaffleStrategyRequestDTO\"],\"parameters\":[],\"returnType\":\"top.javarem.api.response.Response\"},{\"annotations\":[],\"name\":\"queryRaffleStrategyRuleWeight\",\"parameterTypes\":[\"top.javarem.api.dto.RaffleStrategyRuleWeightRequestDTO\"],\"parameters\":[],\"returnType\":\"top.javarem.api.response.Response\"}],\"parameters\":{\"version\":\"1.0\",\"application\":\"rem-market\",\"interface\":\"top.javarem.api.IRaffleStrategyService\",\"side\":\"provider\",\"pid\":\"22884\",\"anyhost\":\"true\",\"release\":\"3.0.9\",\"dubbo\":\"2.0.2\",\"application.version\":\"1.0\",\"methods\":\"queryRaffleStrategyRuleWeight,displayAwardList,assembleStrategy,executeRaffle\",\"deprecated\":\"false\",\"service-name-mapping\":\"true\",\"qos.enable\":\"false\",\"generic\":\"false\",\"bind.port\":\"20880\",\"revision\":\"1.0\",\"bind.ip\":\"169.254.105.229\",\"background\":\"false\",\"dynamic\":\"true\",\"timestamp\":\"1737630262056\"},\"types\":[{\"enum\":[],\"items\":[],\"properties\":{\"activityId\":\"java.lang.Long\",\"userId\":\"java.lang.String\"},\"type\":\"top.javarem.api.dto.RaffleStrategyRuleWeightRequestDTO\"},{\"enum\":[],\"items\":[],\"properties\":{},\"type\":\"java.lang.Long\"},{\"enum\":[],\"items\":[],\"properties\":{\"code\":\"java.lang.String\",\"data\":\"java.lang.Object\",\"info\":\"java.lang.String\"},\"type\":\"top.javarem.api.response.Response\"},{\"enum\":[],\"items\":[],\"properties\":{\"activityId\":\"java.lang.Long\",\"userId\":\"java.lang.String\"},\"type\":\"top.javarem.api.dto.DisplayAwardRequestDTO\"},{\"enum\":[],\"items\":[],\"properties\":{\"UserId\":\"java.lang.String\",\"strategyId\":\"java.lang.Long\"},\"type\":\"top.javarem.api.dto.RaffleStrategyRequestDTO\"},{\"enum\":[],\"items\":[],\"properties\":{},\"type\":\"java.lang.Object\"},{\"enum\":[],\"items\":[],\"properties\":{},\"type\":\"java.lang.String\"}],\"uniqueId\":\"top.javarem.api.IRaffleStrategyService@file:/D:/Develop/project/rem-market/market-api/target/classes/\"}', 'a719c0e29fa0968a7a5c9e95ab2bb558', '2025-01-23 19:04:18', '2025-01-23 19:04:19', NULL, '169.254.105.229', 'I', '', '');
INSERT INTO `his_config_info` VALUES (0, 3, 'top.javarem.api.IRaffleStrategyService', 'mapping', '', 'rem-market', '2d04a7ac8ae135c17e01dedab7192a1e', '2025-01-23 19:04:18', '2025-01-23 19:04:19', NULL, '169.254.105.229', 'I', '', '');
INSERT INTO `his_config_info` VALUES (0, 4, 'top.javarem.api.IRaffleActivityService:1.0::provider:rem-market', 'dubbo', '', '{\"annotations\":[],\"canonicalName\":\"top.javarem.api.IRaffleActivityService\",\"codeSource\":\"file:/D:/Develop/project/rem-market/market-api/target/classes/\",\"methods\":[{\"annotations\":[],\"name\":\"getSkuProductListByActivityId\",\"parameterTypes\":[\"java.lang.Long\"],\"parameters\":[],\"returnType\":\"top.javarem.api.response.Response\"},{\"annotations\":[],\"name\":\"isCalenderSignRebate\",\"parameterTypes\":[\"java.lang.String\"],\"parameters\":[],\"returnType\":\"top.javarem.api.response.Response\"},{\"annotations\":[],\"name\":\"queryUserCredit\",\"parameterTypes\":[\"java.lang.String\"],\"parameters\":[],\"returnType\":\"top.javarem.api.response.Response\"},{\"annotations\":[],\"name\":\"armory\",\"parameterTypes\":[\"java.lang.Long\"],\"parameters\":[],\"returnType\":\"top.javarem.api.response.Response\"},{\"annotations\":[],\"name\":\"draw\",\"parameterTypes\":[\"top.javarem.api.dto.ActivityDrawRequestDTO\"],\"parameters\":[],\"returnType\":\"top.javarem.api.response.Response\"},{\"annotations\":[],\"name\":\"creditExchangeProduct\",\"parameterTypes\":[\"top.javarem.api.dto.SkuProductShopCartRequestDTO\"],\"parameters\":[],\"returnType\":\"top.javarem.api.response.Response\"},{\"annotations\":[],\"name\":\"queryUserActivityAccount\",\"parameterTypes\":[\"top.javarem.api.dto.UserActivityAccountRequestDTO\"],\"parameters\":[],\"returnType\":\"top.javarem.api.response.Response\"},{\"annotations\":[],\"name\":\"calenderSignRebate\",\"parameterTypes\":[\"java.lang.String\"],\"parameters\":[],\"returnType\":\"top.javarem.api.response.Response\"}],\"parameters\":{\"version\":\"1.0\",\"application\":\"rem-market\",\"interface\":\"top.javarem.api.IRaffleActivityService\",\"side\":\"provider\",\"pid\":\"22884\",\"anyhost\":\"true\",\"release\":\"3.0.9\",\"dubbo\":\"2.0.2\",\"application.version\":\"1.0\",\"methods\":\"queryUserActivityAccount,armory,calenderSignRebate,isCalenderSignRebate,queryUserCredit,getSkuProductListByActivityId,draw,creditExchangeProduct\",\"deprecated\":\"false\",\"service-name-mapping\":\"true\",\"qos.enable\":\"false\",\"generic\":\"false\",\"bind.port\":\"20880\",\"revision\":\"1.0\",\"bind.ip\":\"169.254.105.229\",\"background\":\"false\",\"dynamic\":\"true\",\"timestamp\":\"1737630263451\"},\"types\":[{\"enum\":[],\"items\":[],\"properties\":{\"activityId\":\"java.lang.Long\",\"userId\":\"java.lang.String\"},\"type\":\"top.javarem.api.dto.ActivityDrawRequestDTO\"},{\"enum\":[],\"items\":[],\"properties\":{},\"type\":\"java.lang.Long\"},{\"enum\":[],\"items\":[],\"properties\":{\"code\":\"java.lang.String\",\"data\":\"java.lang.Object\",\"info\":\"java.lang.String\"},\"type\":\"top.javarem.api.response.Response\"},{\"enum\":[],\"items\":[],\"properties\":{},\"type\":\"java.lang.Object\"},{\"enum\":[],\"items\":[],\"properties\":{\"sku\":\"java.lang.Long\",\"userId\":\"java.lang.String\"},\"type\":\"top.javarem.api.dto.SkuProductShopCartRequestDTO\"},{\"enum\":[],\"items\":[],\"properties\":{},\"type\":\"java.lang.String\"},{\"enum\":[],\"items\":[],\"properties\":{\"activityId\":\"java.lang.String\",\"userId\":\"java.lang.String\"},\"type\":\"top.javarem.api.dto.UserActivityAccountRequestDTO\"}],\"uniqueId\":\"top.javarem.api.IRaffleActivityService@file:/D:/Develop/project/rem-market/market-api/target/classes/\"}', 'ab4d8945fd370c69c876b1d8aa025306', '2025-01-23 19:04:19', '2025-01-23 19:04:19', NULL, '169.254.105.229', 'I', '', '');
INSERT INTO `his_config_info` VALUES (0, 5, 'top.javarem.api.IRaffleActivityService', 'mapping', '', 'rem-market', '2d04a7ac8ae135c17e01dedab7192a1e', '2025-01-23 19:04:19', '2025-01-23 19:04:19', NULL, '169.254.105.229', 'I', '', '');
INSERT INTO `his_config_info` VALUES (2, 6, 'top.javarem.api.IRaffleStrategyService:1.0::provider:rem-market', 'dubbo', '', '{\"annotations\":[],\"canonicalName\":\"top.javarem.api.IRaffleStrategyService\",\"codeSource\":\"file:/D:/Develop/project/rem-market/market-api/target/classes/\",\"methods\":[{\"annotations\":[],\"name\":\"assembleStrategy\",\"parameterTypes\":[\"java.lang.Long\"],\"parameters\":[],\"returnType\":\"top.javarem.api.response.Response\"},{\"annotations\":[],\"name\":\"displayAwardList\",\"parameterTypes\":[\"top.javarem.api.dto.DisplayAwardRequestDTO\"],\"parameters\":[],\"returnType\":\"top.javarem.api.response.Response\"},{\"annotations\":[],\"name\":\"executeRaffle\",\"parameterTypes\":[\"top.javarem.api.dto.RaffleStrategyRequestDTO\"],\"parameters\":[],\"returnType\":\"top.javarem.api.response.Response\"},{\"annotations\":[],\"name\":\"queryRaffleStrategyRuleWeight\",\"parameterTypes\":[\"top.javarem.api.dto.RaffleStrategyRuleWeightRequestDTO\"],\"parameters\":[],\"returnType\":\"top.javarem.api.response.Response\"}],\"parameters\":{\"version\":\"1.0\",\"application\":\"rem-market\",\"interface\":\"top.javarem.api.IRaffleStrategyService\",\"side\":\"provider\",\"pid\":\"22884\",\"anyhost\":\"true\",\"release\":\"3.0.9\",\"dubbo\":\"2.0.2\",\"application.version\":\"1.0\",\"methods\":\"queryRaffleStrategyRuleWeight,displayAwardList,assembleStrategy,executeRaffle\",\"deprecated\":\"false\",\"service-name-mapping\":\"true\",\"qos.enable\":\"false\",\"generic\":\"false\",\"bind.port\":\"20880\",\"revision\":\"1.0\",\"bind.ip\":\"169.254.105.229\",\"background\":\"false\",\"dynamic\":\"true\",\"timestamp\":\"1737630262056\"},\"types\":[{\"enum\":[],\"items\":[],\"properties\":{\"activityId\":\"java.lang.Long\",\"userId\":\"java.lang.String\"},\"type\":\"top.javarem.api.dto.RaffleStrategyRuleWeightRequestDTO\"},{\"enum\":[],\"items\":[],\"properties\":{},\"type\":\"java.lang.Long\"},{\"enum\":[],\"items\":[],\"properties\":{\"code\":\"java.lang.String\",\"data\":\"java.lang.Object\",\"info\":\"java.lang.String\"},\"type\":\"top.javarem.api.response.Response\"},{\"enum\":[],\"items\":[],\"properties\":{\"activityId\":\"java.lang.Long\",\"userId\":\"java.lang.String\"},\"type\":\"top.javarem.api.dto.DisplayAwardRequestDTO\"},{\"enum\":[],\"items\":[],\"properties\":{\"UserId\":\"java.lang.String\",\"strategyId\":\"java.lang.Long\"},\"type\":\"top.javarem.api.dto.RaffleStrategyRequestDTO\"},{\"enum\":[],\"items\":[],\"properties\":{},\"type\":\"java.lang.Object\"},{\"enum\":[],\"items\":[],\"properties\":{},\"type\":\"java.lang.String\"}],\"uniqueId\":\"top.javarem.api.IRaffleStrategyService@file:/D:/Develop/project/rem-market/market-api/target/classes/\"}', 'a719c0e29fa0968a7a5c9e95ab2bb558', '2025-01-23 20:54:35', '2025-01-23 20:54:36', NULL, '169.254.105.229', 'U', '', '');
INSERT INTO `his_config_info` VALUES (4, 7, 'top.javarem.api.IRaffleActivityService:1.0::provider:rem-market', 'dubbo', '', '{\"annotations\":[],\"canonicalName\":\"top.javarem.api.IRaffleActivityService\",\"codeSource\":\"file:/D:/Develop/project/rem-market/market-api/target/classes/\",\"methods\":[{\"annotations\":[],\"name\":\"getSkuProductListByActivityId\",\"parameterTypes\":[\"java.lang.Long\"],\"parameters\":[],\"returnType\":\"top.javarem.api.response.Response\"},{\"annotations\":[],\"name\":\"isCalenderSignRebate\",\"parameterTypes\":[\"java.lang.String\"],\"parameters\":[],\"returnType\":\"top.javarem.api.response.Response\"},{\"annotations\":[],\"name\":\"queryUserCredit\",\"parameterTypes\":[\"java.lang.String\"],\"parameters\":[],\"returnType\":\"top.javarem.api.response.Response\"},{\"annotations\":[],\"name\":\"armory\",\"parameterTypes\":[\"java.lang.Long\"],\"parameters\":[],\"returnType\":\"top.javarem.api.response.Response\"},{\"annotations\":[],\"name\":\"draw\",\"parameterTypes\":[\"top.javarem.api.dto.ActivityDrawRequestDTO\"],\"parameters\":[],\"returnType\":\"top.javarem.api.response.Response\"},{\"annotations\":[],\"name\":\"creditExchangeProduct\",\"parameterTypes\":[\"top.javarem.api.dto.SkuProductShopCartRequestDTO\"],\"parameters\":[],\"returnType\":\"top.javarem.api.response.Response\"},{\"annotations\":[],\"name\":\"queryUserActivityAccount\",\"parameterTypes\":[\"top.javarem.api.dto.UserActivityAccountRequestDTO\"],\"parameters\":[],\"returnType\":\"top.javarem.api.response.Response\"},{\"annotations\":[],\"name\":\"calenderSignRebate\",\"parameterTypes\":[\"java.lang.String\"],\"parameters\":[],\"returnType\":\"top.javarem.api.response.Response\"}],\"parameters\":{\"version\":\"1.0\",\"application\":\"rem-market\",\"interface\":\"top.javarem.api.IRaffleActivityService\",\"side\":\"provider\",\"pid\":\"22884\",\"anyhost\":\"true\",\"release\":\"3.0.9\",\"dubbo\":\"2.0.2\",\"application.version\":\"1.0\",\"methods\":\"queryUserActivityAccount,armory,calenderSignRebate,isCalenderSignRebate,queryUserCredit,getSkuProductListByActivityId,draw,creditExchangeProduct\",\"deprecated\":\"false\",\"service-name-mapping\":\"true\",\"qos.enable\":\"false\",\"generic\":\"false\",\"bind.port\":\"20880\",\"revision\":\"1.0\",\"bind.ip\":\"169.254.105.229\",\"background\":\"false\",\"dynamic\":\"true\",\"timestamp\":\"1737630263451\"},\"types\":[{\"enum\":[],\"items\":[],\"properties\":{\"activityId\":\"java.lang.Long\",\"userId\":\"java.lang.String\"},\"type\":\"top.javarem.api.dto.ActivityDrawRequestDTO\"},{\"enum\":[],\"items\":[],\"properties\":{},\"type\":\"java.lang.Long\"},{\"enum\":[],\"items\":[],\"properties\":{\"code\":\"java.lang.String\",\"data\":\"java.lang.Object\",\"info\":\"java.lang.String\"},\"type\":\"top.javarem.api.response.Response\"},{\"enum\":[],\"items\":[],\"properties\":{},\"type\":\"java.lang.Object\"},{\"enum\":[],\"items\":[],\"properties\":{\"sku\":\"java.lang.Long\",\"userId\":\"java.lang.String\"},\"type\":\"top.javarem.api.dto.SkuProductShopCartRequestDTO\"},{\"enum\":[],\"items\":[],\"properties\":{},\"type\":\"java.lang.String\"},{\"enum\":[],\"items\":[],\"properties\":{\"activityId\":\"java.lang.String\",\"userId\":\"java.lang.String\"},\"type\":\"top.javarem.api.dto.UserActivityAccountRequestDTO\"}],\"uniqueId\":\"top.javarem.api.IRaffleActivityService@file:/D:/Develop/project/rem-market/market-api/target/classes/\"}', 'ab4d8945fd370c69c876b1d8aa025306', '2025-01-23 20:54:36', '2025-01-23 20:54:36', NULL, '169.254.105.229', 'U', '', '');
INSERT INTO `his_config_info` VALUES (2, 8, 'top.javarem.api.IRaffleStrategyService:1.0::provider:rem-market', 'dubbo', '', '{\"annotations\":[],\"canonicalName\":\"top.javarem.api.IRaffleStrategyService\",\"codeSource\":\"file:/D:/Develop/project/rem-market/market-api/target/classes/\",\"methods\":[{\"annotations\":[],\"name\":\"displayAwardList\",\"parameterTypes\":[\"top.javarem.api.dto.DisplayAwardRequestDTO\"],\"parameters\":[],\"returnType\":\"top.javarem.api.response.Response\"},{\"annotations\":[],\"name\":\"assembleStrategy\",\"parameterTypes\":[\"java.lang.Long\"],\"parameters\":[],\"returnType\":\"top.javarem.api.response.Response\"},{\"annotations\":[],\"name\":\"executeRaffle\",\"parameterTypes\":[\"top.javarem.api.dto.RaffleStrategyRequestDTO\"],\"parameters\":[],\"returnType\":\"top.javarem.api.response.Response\"},{\"annotations\":[],\"name\":\"queryRaffleStrategyRuleWeight\",\"parameterTypes\":[\"top.javarem.api.dto.RaffleStrategyRuleWeightRequestDTO\"],\"parameters\":[],\"returnType\":\"top.javarem.api.response.Response\"}],\"parameters\":{\"version\":\"1.0\",\"application\":\"rem-market\",\"interface\":\"top.javarem.api.IRaffleStrategyService\",\"side\":\"provider\",\"pid\":\"19820\",\"anyhost\":\"true\",\"release\":\"3.0.9\",\"dubbo\":\"2.0.2\",\"application.version\":\"1.0\",\"methods\":\"queryRaffleStrategyRuleWeight,displayAwardList,assembleStrategy,executeRaffle\",\"deprecated\":\"false\",\"service-name-mapping\":\"true\",\"qos.enable\":\"false\",\"generic\":\"false\",\"bind.port\":\"20880\",\"revision\":\"1.0\",\"bind.ip\":\"169.254.105.229\",\"background\":\"false\",\"dynamic\":\"true\",\"timestamp\":\"1737636879803\"},\"types\":[{\"enum\":[],\"items\":[],\"properties\":{\"activityId\":\"java.lang.Long\",\"userId\":\"java.lang.String\"},\"type\":\"top.javarem.api.dto.RaffleStrategyRuleWeightRequestDTO\"},{\"enum\":[],\"items\":[],\"properties\":{},\"type\":\"java.lang.Long\"},{\"enum\":[],\"items\":[],\"properties\":{\"code\":\"java.lang.String\",\"data\":\"java.lang.Object\",\"info\":\"java.lang.String\"},\"type\":\"top.javarem.api.response.Response\"},{\"enum\":[],\"items\":[],\"properties\":{\"activityId\":\"java.lang.Long\",\"userId\":\"java.lang.String\"},\"type\":\"top.javarem.api.dto.DisplayAwardRequestDTO\"},{\"enum\":[],\"items\":[],\"properties\":{\"UserId\":\"java.lang.String\",\"strategyId\":\"java.lang.Long\"},\"type\":\"top.javarem.api.dto.RaffleStrategyRequestDTO\"},{\"enum\":[],\"items\":[],\"properties\":{},\"type\":\"java.lang.Object\"},{\"enum\":[],\"items\":[],\"properties\":{},\"type\":\"java.lang.String\"}],\"uniqueId\":\"top.javarem.api.IRaffleStrategyService@file:/D:/Develop/project/rem-market/market-api/target/classes/\"}', '4f5e1621689be053f52a462df00f48b8', '2025-01-23 20:57:47', '2025-01-23 20:57:48', NULL, '169.254.105.229', 'U', '', '');
INSERT INTO `his_config_info` VALUES (4, 9, 'top.javarem.api.IRaffleActivityService:1.0::provider:rem-market', 'dubbo', '', '{\"annotations\":[],\"canonicalName\":\"top.javarem.api.IRaffleActivityService\",\"codeSource\":\"file:/D:/Develop/project/rem-market/market-api/target/classes/\",\"methods\":[{\"annotations\":[],\"name\":\"draw\",\"parameterTypes\":[\"top.javarem.api.dto.ActivityDrawRequestDTO\"],\"parameters\":[],\"returnType\":\"top.javarem.api.response.Response\"},{\"annotations\":[],\"name\":\"armory\",\"parameterTypes\":[\"java.lang.Long\"],\"parameters\":[],\"returnType\":\"top.javarem.api.response.Response\"},{\"annotations\":[],\"name\":\"queryUserCredit\",\"parameterTypes\":[\"java.lang.String\"],\"parameters\":[],\"returnType\":\"top.javarem.api.response.Response\"},{\"annotations\":[],\"name\":\"getSkuProductListByActivityId\",\"parameterTypes\":[\"java.lang.Long\"],\"parameters\":[],\"returnType\":\"top.javarem.api.response.Response\"},{\"annotations\":[],\"name\":\"isCalenderSignRebate\",\"parameterTypes\":[\"java.lang.String\"],\"parameters\":[],\"returnType\":\"top.javarem.api.response.Response\"},{\"annotations\":[],\"name\":\"calenderSignRebate\",\"parameterTypes\":[\"java.lang.String\"],\"parameters\":[],\"returnType\":\"top.javarem.api.response.Response\"},{\"annotations\":[],\"name\":\"queryUserActivityAccount\",\"parameterTypes\":[\"top.javarem.api.dto.UserActivityAccountRequestDTO\"],\"parameters\":[],\"returnType\":\"top.javarem.api.response.Response\"},{\"annotations\":[],\"name\":\"creditExchangeProduct\",\"parameterTypes\":[\"top.javarem.api.dto.SkuProductShopCartRequestDTO\"],\"parameters\":[],\"returnType\":\"top.javarem.api.response.Response\"}],\"parameters\":{\"version\":\"1.0\",\"application\":\"rem-market\",\"interface\":\"top.javarem.api.IRaffleActivityService\",\"side\":\"provider\",\"pid\":\"19820\",\"anyhost\":\"true\",\"release\":\"3.0.9\",\"dubbo\":\"2.0.2\",\"application.version\":\"1.0\",\"methods\":\"queryUserActivityAccount,armory,calenderSignRebate,queryUserCredit,isCalenderSignRebate,getSkuProductListByActivityId,draw,creditExchangeProduct\",\"deprecated\":\"false\",\"service-name-mapping\":\"true\",\"qos.enable\":\"false\",\"generic\":\"false\",\"bind.port\":\"20880\",\"revision\":\"1.0\",\"bind.ip\":\"169.254.105.229\",\"background\":\"false\",\"dynamic\":\"true\",\"timestamp\":\"1737636880957\"},\"types\":[{\"enum\":[],\"items\":[],\"properties\":{\"activityId\":\"java.lang.Long\",\"userId\":\"java.lang.String\"},\"type\":\"top.javarem.api.dto.ActivityDrawRequestDTO\"},{\"enum\":[],\"items\":[],\"properties\":{},\"type\":\"java.lang.Long\"},{\"enum\":[],\"items\":[],\"properties\":{\"code\":\"java.lang.String\",\"data\":\"java.lang.Object\",\"info\":\"java.lang.String\"},\"type\":\"top.javarem.api.response.Response\"},{\"enum\":[],\"items\":[],\"properties\":{},\"type\":\"java.lang.Object\"},{\"enum\":[],\"items\":[],\"properties\":{\"sku\":\"java.lang.Long\",\"userId\":\"java.lang.String\"},\"type\":\"top.javarem.api.dto.SkuProductShopCartRequestDTO\"},{\"enum\":[],\"items\":[],\"properties\":{},\"type\":\"java.lang.String\"},{\"enum\":[],\"items\":[],\"properties\":{\"activityId\":\"java.lang.String\",\"userId\":\"java.lang.String\"},\"type\":\"top.javarem.api.dto.UserActivityAccountRequestDTO\"}],\"uniqueId\":\"top.javarem.api.IRaffleActivityService@file:/D:/Develop/project/rem-market/market-api/target/classes/\"}', '2bb73885bf47acb13c760ce039398629', '2025-01-23 20:57:48', '2025-01-23 20:57:48', NULL, '169.254.105.229', 'U', '', '');

-- ----------------------------
-- Table structure for permissions
-- ----------------------------
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions`  (
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'role',
  `resource` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'resource',
  `action` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'action',
  UNIQUE INDEX `uk_role_permission`(`role` ASC, `resource` ASC, `action` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of permissions
-- ----------------------------

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles`  (
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'username',
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'role',
  UNIQUE INDEX `idx_user_role`(`username` ASC, `role` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of roles
-- ----------------------------
INSERT INTO `roles` VALUES ('nacos', 'ROLE_ADMIN');

-- ----------------------------
-- Table structure for tenant_capacity
-- ----------------------------
DROP TABLE IF EXISTS `tenant_capacity`;
CREATE TABLE `tenant_capacity`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL DEFAULT '' COMMENT 'Tenant ID',
  `quota` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '配额，0表示使用默认值',
  `usage` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '使用量',
  `max_size` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '聚合子配置最大个数',
  `max_aggr_size` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_id`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '租户容量信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tenant_capacity
-- ----------------------------

-- ----------------------------
-- Table structure for tenant_info
-- ----------------------------
DROP TABLE IF EXISTS `tenant_info`;
CREATE TABLE `tenant_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `kp` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'kp',
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `tenant_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT 'tenant_name',
  `tenant_desc` varchar(256) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'tenant_desc',
  `create_source` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'create_source',
  `gmt_create` bigint NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_info_kptenantid`(`kp` ASC, `tenant_id` ASC) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = 'tenant_info' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tenant_info
-- ----------------------------

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'username',
  `password` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'password',
  `enabled` tinyint(1) NOT NULL COMMENT 'enabled',
  PRIMARY KEY (`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('nacos', '$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu', 1);

SET FOREIGN_KEY_CHECKS = 1;
