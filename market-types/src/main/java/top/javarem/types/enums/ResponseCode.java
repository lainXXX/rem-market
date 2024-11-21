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
