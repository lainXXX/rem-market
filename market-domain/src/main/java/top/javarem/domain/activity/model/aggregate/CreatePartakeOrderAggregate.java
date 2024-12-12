package top.javarem.domain.activity.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.javarem.domain.activity.model.entity.ActivityAccountDayCountEntity;
import top.javarem.domain.activity.model.entity.ActivityAccountCountEntity;
import top.javarem.domain.activity.model.entity.ActivityAccountMonthCountEntity;
import top.javarem.domain.activity.model.entity.UserRaffleConsumeOrderEntity;

/**
 * @Author: rem
 * @Date: 2024/12/05/20:24
 * @Description:创建用户参与活动订单聚合对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePartakeOrderAggregate {

    private String userId;

    private Long activityId;

    private ActivityAccountCountEntity activityAccountCountEntity;

    /**
     * 是否存在日次数
     */
    private Boolean isExistDayCount;

    private ActivityAccountDayCountEntity activityAccountDayCountEntity;

    /**
     * 是否存在月次数
     */
    private Boolean isExistMonthCount;

    private ActivityAccountMonthCountEntity activityAccountMonthCountEntity;

    private UserRaffleConsumeOrderEntity userRaffleConsumeOrderEntity;

}
