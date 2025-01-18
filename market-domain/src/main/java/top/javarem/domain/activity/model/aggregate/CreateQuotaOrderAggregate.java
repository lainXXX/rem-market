package top.javarem.domain.activity.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.javarem.domain.activity.model.entity.ActivityOrderEntity;

/**
 * @Author: rem
 * @Date: 2024/12/02/15:15
 * @Description: 下单聚合对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateQuotaOrderAggregate {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 活动ID
     */
    private Long activityId;

    private String activityName;

    /**
     * 增加的总次数
     */
    private Integer totalCount;

    /**
     * 增加的月次数
     */
    private Integer monthCount;

    /**
     * 增加的日次数
     */
    private Integer dayCount;


    /**
     * 活动订单实体
     */
    private ActivityOrderEntity activityOrder;

    public void setOrderStatus(String orderStatus) {

        this.activityOrder.setStatus(orderStatus);

    }

}
