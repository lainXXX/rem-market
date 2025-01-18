package top.javarem.domain.credit.reposiotry;

import top.javarem.domain.credit.model.aggregate.TradeAggregate;

/**
 * @Author: rem
 * @Date: 2025/01/17/16:44
 * @Description:积分服务仓储接口
 */
public interface ICreditRepository {

    void saveUserCreditTradeOrder(TradeAggregate tradeAggregate);

}
