package top.javarem.domain.strategy.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: rem
 * @Date: 2024/11/28/21:35
 * @Description:奖品库存队列需要传入的值对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AwardStockQueueKeyVO {

    private Long strategyId;

    private Integer awardId;

}
