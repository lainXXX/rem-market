package top.javarem.domain.rebate.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: rem
 * @Date: 2025/01/13/14:03
 * @Description:返利类型值对象
 */
@Getter
@AllArgsConstructor
public enum RebateTypeVO {


    SKU("sku", "活动库存充值商品"),
    INTEGRAL("integral", "用户活动积分"),
    ;

    private String code;

    private String info;

}
