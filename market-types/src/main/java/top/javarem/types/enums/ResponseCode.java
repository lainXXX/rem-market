package top.javarem.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ResponseCode {

    RULE_WEIGHT_NULL("WEIGHT_001", "rule weight 为null"),
    RULE_WEIGHT_ERROR("WEIGHT_002", "数据库中rule weight配置错误"),
    INDEX_DUP("0003", "唯一索引冲突"),
    UN_ASSEMBLED_STRATEGY_ARMORY("ERR_BIZ_002", "抽奖策略配置未装配，请通过IStrategyArmory完成装配"),
    ERROR_ACTIVITY_STATUS("ERR_BIZ_003", "活动未开启或已结束"),
    ERROR_ACTIVITY_TIMEOUT("ERR_BIZ_004", "不在活动时间"),
    ERROR_ACTIVITY_STOCK_EMPTY("ERROR_BIZ_005", "活动库存为空"),
    ERROR_ACTIVITY_STOCK_DECR("ERROR_BIZ_006", "活动库存扣减错误"),
    ERROR_ACCOUNT_TOTAL_QUOTA("ERROR_BIZ_007", "账户总定额不足"),
    ERROR_ACCOUNT_MONTH_QUOTA("ERROR_BIZ_008", "账户月定额不足"),
    ERROR_ACCOUNT_DAY_QUOTA("ERROR_BIZ_009", "账户日定额不足"),
    ;

    private String code;
    private String info;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public enum Login {
        TOKEN_ERROR("0003", "登录权限拦截"),

        ;

        private String code;
        private String info;
    }

}
