package top.javarem.domain.rebate.model.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 日常返利行为活动配置
 * @TableName daily_behavior_rebate
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyBehaviorRebateVO {

    /**
     * 行为类型(sign_in- 签到 、 pay-支付)
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
     * 配置
     */
    private String rebateConfig;

    /**
     * 状态（open-开启、 close-关闭）
     */
    private String status;

}