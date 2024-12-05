package top.javarem.domain.activity.model.vo;

import lombok.*;

/**
 * @Author: rem
 * @Date: 2024/12/04/11:27
 * @Description:活动库存扣减队列值对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityStockDecrQueueVO {

    private Long sku;

    private Long activityId;

}
