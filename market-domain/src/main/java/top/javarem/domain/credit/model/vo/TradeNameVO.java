package top.javarem.domain.credit.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author rem
 * @description 交易名称枚举值
 */
@Getter
@AllArgsConstructor
public enum TradeNameVO {

    REBATE("行为返利"),
    CONVERT_SKU("兑换抽奖"),

    ;

    private final String name;

}
