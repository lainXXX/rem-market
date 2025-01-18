package top.javarem.domain.activity.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: rem
 * @Date: 2025/01/18/14:40
 * @Description:订单交易类型
 */
@Getter
@AllArgsConstructor
public enum OrderTradeTypeVO {

    credit_pay_trade("credit_pay_trade", "积分兑换，需要支付类交易"),
    rebate("rebate_trade", "返利类型， 无需交易"),
    ;

    private final String code;

    private final String desc;

}
