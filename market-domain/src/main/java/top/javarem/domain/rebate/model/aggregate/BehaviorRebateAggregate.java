package top.javarem.domain.rebate.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.javarem.domain.rebate.model.entity.BehaviorRebateOrderEntity;
import top.javarem.domain.rebate.model.entity.TaskEntity;

/**
 * @Author: rem
 * @Date: 2025/01/12/16:44
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BehaviorRebateAggregate {

    private String userId;

    private BehaviorRebateOrderEntity behaviorRebateOrderEntity;

    private TaskEntity taskEntity;

}
