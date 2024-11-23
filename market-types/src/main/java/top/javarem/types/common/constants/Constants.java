package top.javarem.types.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class Constants {

    public final static String COMMA = ",";
    public final static String COLON = ":";
    public final static String SPACE = " ";
    public final static Integer AWARD_MIN_RANGE = 100;
    public final static String RULE_WEIGHT_KEY = "ruleWeightKey";
    public final static Integer BLACKLIST_AWARD_ID = 100;

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
        public static String STRATEGY_KEY = "rem_market_strategy_key_";
        public static String STRATEGY_AWARD_KEY = "rem_market_strategy_award_key_";
        public static String AWARD_TABLE_KEY = "rem_market_award_table_key_";
        public static String AWARD_RANGE_KEY = "rem_market_award_range_key_";

    }

}
