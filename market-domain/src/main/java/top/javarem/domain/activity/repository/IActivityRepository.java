package top.javarem.domain.activity.repository;

import top.javarem.domain.activity.model.aggregate.CreatePartakeOrderAggregate;
import top.javarem.domain.activity.model.aggregate.CreateQuotaOrderAggregate;
import top.javarem.domain.activity.model.entity.*;
import top.javarem.domain.activity.model.vo.ActivityStockDecrQueueVO;

import java.util.Date;
import java.util.List;

/**
 * @Author: rem
 * @Date: 2024/12/01/23:29
 * @Description: 活动仓储接口
 */
public interface IActivityRepository {


    ActivitySkuEntity getActivitySku(Long sku);

    ActivityEntity getActivityById(Long activityId);

    ActivityCountEntity getActivityCount(Long activityCountId);

    void saveOrder(CreateQuotaOrderAggregate createQuotaOrderAggregate);

    void saveCreditOrder(CreateQuotaOrderAggregate createQuotaOrderAggregate);

    void saveRebateOrder(CreateQuotaOrderAggregate createQuotaOrderAggregate);

    void cacheActivityStock(String key, Integer stockCount);

    Boolean deductActivityStock(Long sku, Date endTime);

    void sendActivityStockDecrQueue(ActivityStockDecrQueueVO activityStockDecrQueueVO);

    ActivityStockDecrQueueVO handleSkuStockDecrQueue();

    Boolean updateSkuStock(ActivityStockDecrQueueVO activityStockDecrQueueVO);

    Boolean isEmptySkuStockDecrQueue();

    void clearActivitySkuStock(Long sku);

    void clearSkuStockDecrQueue();

    UserRaffleConsumeOrderEntity getUserUnconsumedOrder(String userId, Long activityId);

    ActivityAccountCountEntity getActivityAccountCount(String userId, Long activityId);

    ActivityAccountMonthCountEntity getActivityAccountMonthCount(String userId, Long activityId, String month);

    ActivityAccountDayCountEntity getActivityAccountDayCount(String userId, Long activityId, String day);

    void saveCreatePartakeOrderAggregate(CreatePartakeOrderAggregate createPartakeOrderAggregate);

    List<ActivitySkuEntity> getActivitySkuEntityList(Long activityId);

    Integer getActivityTodayPartakeCount(String userId, Long activityId);

    Long getActivityIdByStrategyId(Long strategyId);

    Integer queryRaffleActivityAccountDayPartakeCount(Long activityId, String userId);

    void updateOrder(DeliveryOrderEntity deliveryOrderEntity);
}
