package top.javarem.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.javarem.domain.activity.model.vo.OrderStateVO;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 
 * @TableName raffle_activity_order
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityOrderEntity {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 商品sku
     */
    private Long sku;

    /**
     * 活动id
     */
    private Long activityId;

    /**
     * 抽奖策略id
     */
    private Long strategyId;

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 下单时间
     */
    private LocalDateTime orderTime;

    /**
     * 库存总量
     */
    private Integer totalCount;

    /**
     * 日次数
     */
    private Integer dayCount;

    /**
     * 月次数
     */
    private Integer monthCount;

    /**
     * 订单状态
     */
    private String status;

    /**
     * 业务仿重ID - 外部透传的，确保幂等
     */
    private String outBusinessNo;

}