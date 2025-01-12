package top.javarem.domain.rebate.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: rem
 * @Date: 2025/01/12/15:08
 * @Description:返利行为类型枚举值
 */
@Getter
@AllArgsConstructor
public enum BehaviorTypeVO {

    SIGN("sign", "签到(日历)"),
    PAY("pay", "外部支付完成"),
    ;

    private final String code;

    private final String info;

}
