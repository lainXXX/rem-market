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
