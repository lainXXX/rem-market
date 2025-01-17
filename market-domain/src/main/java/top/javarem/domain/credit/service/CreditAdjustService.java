package top.javarem.domain.credit.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.javarem.domain.credit.model.aggregate.TradeAggregate;
import top.javarem.domain.credit.model.entity.TradeEntity;
import top.javarem.domain.credit.reposiotry.ICreditRepository;

/**
 * @Author: rem
 * @Date: 2025/01/17/17:05
 * @Description:积分服务实现类
 */
@Service
@Slf4j
public class CreditAdjustService implements ICreditService {

    @Autowired
    private ICreditRepository repository;

    @Override
    public String createOrder(TradeEntity tradeEntity) {

        log.info("增加账户积分额度开始 userId:{} tradeName:{} amount:{}", tradeEntity.getUserId(), tradeEntity.getTradeName(), tradeEntity.getAmount());
//        1.构建交易聚合对象
        TradeAggregate tradeAggregate = new TradeAggregate();
        tradeAggregate.setUserId(tradeEntity.getUserId());

//        2.设置账户积分实体
        tradeAggregate.setCreditAccountEntity(
                tradeEntity.getUserId(),
                tradeEntity.getAmount());

//        3. 设置账户订单实体
        tradeAggregate.setCreditOrderEntity(
                tradeEntity.getUserId(),
                tradeEntity.getTradeName(),
                tradeEntity.getTradeType(),
                tradeEntity.getAmount(),
                tradeEntity.getOutBusinessNo());

        // 4. 保存积分交易订单
        repository.saveUserCreditTradeOrder(tradeAggregate);
        String orderId = tradeAggregate.getCreditOrderEntity().getOrderId();
        log.info("增加账户积分额度完成 userId:{} orderId:{}", tradeEntity.getUserId(), orderId);

        return orderId;

    }

}
