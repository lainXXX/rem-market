package top.javarem.types.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class Constants {

    public final static String COMMA = ",";
    public final static String COLON = ":";
    public final static String SPACE = " ";
    public final static String UNDERLINE = "_";
    public final static Integer AWARD_MIN_RANGE = 100;
    public final static String RULE_WEIGHT_KEY = "ruleWeightKey";
    public final static Integer BLACKLIST_AWARD_ID = 100;
    public final static String AWARD_COUNT_LOCK = "lock";

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
        public static String STRATEGY_KEY = "rem_market_strategy_key_";
        public static String STRATEGY_AWARD_COUNT_KEY = "rem_market_strategy_award_count_key_";
        public static String STRATEGY_AWARD_KEY = "rem_market_strategy_award_key_";
        public static String AWARD_TABLE_KEY = "rem_market_award_table_key_";
        public static String AWARD_RANGE_KEY = "rem_market_award_range_key_";
        public final static String AWARD_BLOCK_QUEUE_KEY = "award_block_queue_key_";

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
