package top.javarem.domain.activity.service.quota.policy;

import top.javarem.domain.activity.model.aggregate.CreateQuotaOrderAggregate;

/**
 * @Author: rem
 * @Date: 2025/01/18/14:46
 * @Description:交易策略接口
 */
public interface ITradePolicy {

    void trade(CreateQuotaOrderAggregate createQuotaOrderAggregate);

}
