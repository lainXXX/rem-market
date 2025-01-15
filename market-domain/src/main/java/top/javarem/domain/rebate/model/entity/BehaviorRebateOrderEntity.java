package top.javarem.domain.rebate.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: rem
 * @Date: 2025/01/12/16:10
 * @Description:行为返利订单实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BehaviorRebateOrderEntity {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 行为类型（sign 签到、pay 支付）
     */
    private String behaviorType;

    /**
     * 返利描述
     */
    private String rebateDesc;

    /**
     * 返利类型（sku 活动库存充值商品、integral 用户活动积分）
     */
    private String rebateType;

    /**
     * 返利配置【sku值、 积分值】
     */
    private String rebateConfig;

    /**
     * 业务放防重ID-外部透传
     */
    private String outBusinessNo;

    /**
     * 业务ID-  拼接的唯一值
     */
    private String bizId;

}
