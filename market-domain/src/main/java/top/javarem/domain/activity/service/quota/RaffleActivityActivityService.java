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
public class RaffleActivityActivityService extends AbstractRaffleActivityActivityService implements IRaffleActivitySkuStockService {

    public RaffleActivityActivityService(IActivityRepository repository, DefaultActivityChainFactory factory) {
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
}
