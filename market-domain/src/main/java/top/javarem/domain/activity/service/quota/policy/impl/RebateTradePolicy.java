package top.javarem.domain.activity.service.quota.policy.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.javarem.domain.activity.model.aggregate.CreateQuotaOrderAggregate;
import top.javarem.domain.activity.model.vo.OrderStateVO;
import top.javarem.domain.activity.repository.IActivityRepository;
import top.javarem.domain.activity.service.quota.policy.ITradePolicy;

import java.math.BigDecimal;

/**
 * @Author: rem
 * @Date: 2025/01/18/15:03
 * @Description:返利交易策略
 */
@Service("rebate_trade")
public class RebateTradePolicy implements ITradePolicy {

    private final IActivityRepository activityRepository;

    public RebateTradePolicy(IActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public void trade(CreateQuotaOrderAggregate createQuotaOrderAggregate) {

        createQuotaOrderAggregate.setOrderStatus(OrderStateVO.completed.getCode());
        createQuotaOrderAggregate.getActivityOrder().setPayAmount(BigDecimal.ZERO);
        activityRepository.saveRebateOrder(createQuotaOrderAggregate);

    }
}
