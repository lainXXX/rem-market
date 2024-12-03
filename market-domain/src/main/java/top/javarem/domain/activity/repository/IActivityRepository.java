package top.javarem.domain.activity.repository;

import top.javarem.domain.activity.model.aggregate.ActivityOrderAggregate;
import top.javarem.domain.activity.model.entity.ActivityCountEntity;
import top.javarem.domain.activity.model.entity.ActivityEntity;
import top.javarem.domain.activity.model.entity.ActivitySkuEntity;

/**
 * @Author: rem
 * @Date: 2024/12/01/23:29
 * @Description: 活动仓储接口
 */
public interface IActivityRepository {


    ActivitySkuEntity getActivitySku(Long sku);

    ActivityEntity getActivityById(Long activityId);

    ActivityCountEntity getActivityCount(Long activityCountId);

    void saveOrder(ActivityOrderAggregate activityOrderAggregate);
}
