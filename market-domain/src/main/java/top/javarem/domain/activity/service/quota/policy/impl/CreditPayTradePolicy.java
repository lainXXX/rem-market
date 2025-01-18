package top.javarem.domain.activity.service.quota.policy.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.javarem.domain.activity.model.aggregate.CreateQuotaOrderAggregate;
import top.javarem.domain.activity.model.vo.OrderStateVO;
import top.javarem.domain.activity.repository.IActivityRepository;
import top.javarem.domain.activity.service.quota.policy.ITradePolicy;

/**
 * @Author: rem
 * @Date: 2025/01/18/15:02
 * @Description:积分兑换交易策略
 */
@Service("credit_pay_trade")
public class CreditPayTradePolicy implements ITradePolicy {

    private final IActivityRepository activityRepository;

    public CreditPayTradePolicy(IActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public void trade(CreateQuotaOrderAggregate createQuotaOrderAggregate) {

        createQuotaOrderAggregate.setOrderStatus(OrderStateVO.created.getCode());
        activityRepository.saveCreditOrder(createQuotaOrderAggregate);

    }
}
