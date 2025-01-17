package top.javarem.domain.credit.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: rem
 * @Date: 2025/01/17/16:49
 * @Description:交易类型值对象
 */
@Getter
@AllArgsConstructor
public enum TradeTypeVO {

    FORWARD("forward", "正向交易 +积分"),
    RESERVE("reserve", "逆向交易 -积分"),
    ;

    private String code;

    private String info;

}
