package top.javarem.types.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class Constants {

    public static final String COMMA = ",";
    public static final String COLON = ":";
    public static final String SPACE = " ";
    public static final String UNDERLINE = "_";
    public static final Integer AWARD_MIN_RANGE = 100;
    public static final String RULE_WEIGHT_KEY = "ruleWeightKey";
    public static final Integer BLACKLIST_AWARD_ID = 101;
    public static final String AWARD_SURPLUS_LOCK = "award_surplus_lock";
    public static final String ACTIVITY_SURPLUS_STOCK_LOCK = "activity_surplus_stock_lock";

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public enum ResponseCode {
        SUCCESS("0000", "调用成功"),
        UN_ERROR("0001", "调用失败"),
        ILLEGAL_PARAMETER("0002", "非法参数"),
        NO_LOGIN("0003", "未登录"),
        ;

        private String code;
        private String info;

    }

    public static class RedisKey {
        public static final String RULE_TREE_KEY = "rem_market_rule_tree_key_";
        public static final String STRATEGY_KEY = "rem_market_strategy_key_";
        public static final String STRATEGY_AWARD_COUNT_KEY = "rem_market_strategy_award_count_key_";
        public static final String STRATEGY_AWARD_KEY = "rem_market_strategy_award_key_";
        public static final String AWARD_TABLE_KEY = "rem_market_award_table_key_";
        public static final String AWARD_RANGE_KEY = "rem_market_award_range_key_";
        public static final String AWARD_BLOCK_QUEUE_KEY = "rem_market_award_block_queue_key_";
        public static final String ACTIVITY_COUNT_KEY = "rem_market_activity_count_key_";
        public static final String ACTIVITY_KEY = "rem_market_activity_key_";
        public static final String ACTIVITY_SKU_KEY = "rem_market_activity_sku_key_";
        public static final String ACTIVITY_SKU_STOCK_KEY = "rem_market_activity_sku_stock_key_";
        public static final String ACTIVITY_SKU_STOCK_DECR_QUEUE_KEY = "rem_market_activity_sku_stock_decr_queue_key_";
        public static final String ACTIVITY_ACCOUNT_KEY = "rem_market_activity_account_key_";
        public static final String STRATEGY_RULE_WEIGHT_KEY = "rem_market_strategy_rule_weight_key_";
        public static final String AWARD_CONFIG_KEY = "rem_market_award_config_key_";
        public static final String AWARD_KEY = "rem_market_award_key_";
        public static final String ACTIVITY_ACCOUNT_LOCK = "rem_market_activity_account_lock_key_";
        public static final String USER_CREDIT_ACCOUNT_LOCK = "user_credit_account_lock_key_";
        public static final String ACTIVITY_ACCOUNT_UPDATE_LOCK = "activity_account_update_lock_key_";
        public static final String RATE_LIMITER_KEY = "rate_limiter_key_";
        public static final String RATE_LIMITER_COUNT_KEY = "rate_limiter_count_key_";
        public static final String STRATEGY_ARMORY_ALGORITHM_KEY = "strategy_armory_algorithm_key_";
    }

    @Getter
    @AllArgsConstructor
    public enum LogicModel {

        RULE_WEIGHT("rule_weight", "【抽奖前规则】根据抽奖权重返回可抽奖范围KEY"),
        RULE_BLACKLIST("rule_blacklist", "【抽奖前规则】黑名单规则过滤，命中黑名单则直接返回"),
        RULE_LOCK("rule_lock", "【抽奖中规则】，抽奖n次后，对应奖品可解锁抽奖"),
        RULE_LUCK_AWARD("rule_luck_award", "【抽奖后规则，抽奖n次后，对应奖品可解锁抽奖"),
        RULE_STOCK("rule_stock", "判断库存是否足够")
        ;

        private final String code;
        private final String info;

    }

}
