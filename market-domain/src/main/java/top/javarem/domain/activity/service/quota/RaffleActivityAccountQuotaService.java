package top.javarem.domain.activity.service.quota;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import top.javarem.domain.activity.model.aggregate.CreateQuotaOrderAggregate;
import top.javarem.domain.activity.model.entity.*;
import top.javarem.domain.activity.model.vo.ActivityStockDecrQueueVO;
import top.javarem.domain.activity.model.vo.OrderStateVO;
import top.javarem.domain.activity.repository.IActivityRepository;
import top.javarem.domain.activity.service.IRaffleActivitySkuStockService;
import top.javarem.domain.activity.service.quota.rule.factory.DefaultActivityChainFactory;

import java.time.LocalDateTime;

/**
 * @Author: rem
 * @Date: 2024/12/02/16:19
 * @Description:抽奖活动服务
 */
@Service
public class RaffleActivityAccountQuotaService extends AbstractRaffleActivityAccountQuotaService implements IRaffleActivitySkuStockService {

    public RaffleActivityAccountQuotaService(IActivityRepository repository, DefaultActivityChainFactory factory) {
        super(repository, factory);
    }

    @Override
    protected CreateQuotaOrderAggregate buildOrderAggregate(SkuRechargeEntity skuRechargeEntity, ActivitySkuEntity skuEntity, ActivityEntity activityEntity, ActivityCountEntity countEntity) {

        ActivityOrderEntity activityOrderEntity = ActivityOrderEntity.builder()
                .userId(skuRechargeEntity.getUserId())
                .sku(skuRechargeEntity.getSku())
                .activityId(activityEntity.getActivityId())
                .activityName(activityEntity.getActivityName())
                .strategyId(activityEntity.getStrategyId())
                .orderId(RandomStringUtils.randomNumeric(11))
                .orderTime(LocalDateTime.now())
                .totalCount(countEntity.getTotalCount())
                .monthCount(countEntity.getMonthCount())
                .dayCount(countEntity.getDayCount())
                .status(OrderStateVO.completed.getCode())
                .outBusinessNo(skuRechargeEntity.getOutBusinessNo())
                .build();

        return CreateQuotaOrderAggregate.builder()
                .activityId(skuEntity.getActivityId())
                .userId(skuRechargeEntity.getUserId())
                .totalCount(countEntity.getTotalCount())
                .monthCount(countEntity.getMonthCount())
                .dayCount(countEntity.getDayCount())
                .activityOrder(activityOrderEntity)
                .build();
    }

    @Override
    protected void saveOrder(CreateQuotaOrderAggregate createQuotaOrderAggregate) {
        repository.saveOrder(createQuotaOrderAggregate);
    }

    @Override
    public ActivityStockDecrQueueVO handleSkuStockDecrQueue() {
        return repository.handleSkuStockDecrQueue();
    }

    @Override
    public Boolean updateSkuStock(ActivityStockDecrQueueVO activityStockDecrQueueVO) {
        return repository.updateSkuStock(activityStockDecrQueueVO);
    }

    @Override
    public Boolean isEmptySkuStockDecrQueue() {
        return repository.isEmptySkuStockDecrQueue();
    }

    @Override
    public void clearActivitySkuStock(Long sku) {
        repository.clearActivitySkuStock(sku);
    }

    @Override
    public void clearSkuStockDecrQueue() {
        repository.clearSkuStockDecrQueue();
    }

    @Override
    public Integer getActivityTodayPartakeCount(String userId, Long activityId) {

        return repository.getActivityTodayPartakeCount(userId, activityId);

    }

    @Override
    public ActivityAccountCountEntity getActivityAccountCount(String userId, Long activityId) {

        ActivityAccountCountEntity activityAccountCount = repository.getActivityAccountCount(userId, activityId);
        if (activityAccountCount != null) {
            return activityAccountCount;
        }
        return ActivityAccountCountEntity.builder()
                .activityId(activityId)
                .userId(userId)
                .totalCount(0)
                .totalCountSurplus(0)
                .monthCount(0)
                .monthCountSurplus(0)
                .dayCount(0)
                .dayCountSurplus(0)
                .build();
    }

    /**
     * 查询活动账户 - 总，参与次数
     *
     * @param activityId 活动ID
     * @param userId     用户ID
     * @return 参与次数
     */
    public Integer queryRaffleActivityAccountPartakeCount(Long activityId, String userId) {

        return repository.queryRaffleActivityAccountDayPartakeCount(activityId, userId);

    }
}
