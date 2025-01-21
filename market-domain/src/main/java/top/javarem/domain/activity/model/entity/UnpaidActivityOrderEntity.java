package top.javarem.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author: rem
 * @Date: 2025/01/21/14:58
 * @Description:未支付活动订单实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnpaidActivityOrderEntity {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 商品sku
     */
    private Long sku;

    /**
     * 支付金额
     */
    private BigDecimal payAmount;

    /**
     * 业务仿重ID - 外部透传的，确保幂等
     */
    private String outBusinessNo;

}
