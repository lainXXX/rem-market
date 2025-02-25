package top.javarem.domain.award.service.distribute.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.javarem.domain.award.model.aggregate.GiveOutPrizeAggregate;
import top.javarem.domain.award.model.aggregate.OthersPrizeAggregate;
import top.javarem.domain.award.model.entity.DistributeAwardEntity;
import top.javarem.domain.award.model.vo.AwardStateVO;
import top.javarem.domain.award.repository.IAwardRepository;
import top.javarem.domain.award.service.distribute.IDistributeAward;

/**
 * @Author: rem
 * @Date: 2025/02/25/13:58
 * @Description:其他的奖品分发处理，该实现类为临时处理 标准实现为实物奖品应该与外部对接
 */
@Component("others")
public class OthersDistributeAward implements IDistributeAward {

    @Autowired
    private IAwardRepository awardRepository;

    @Override
    public void giveOutPrizes(DistributeAwardEntity distributeAwardEntity) {

        //        创建奖品分发聚合对象
        OthersPrizeAggregate othersPrizeAggregate = new OthersPrizeAggregate();
        othersPrizeAggregate.setUserId(distributeAwardEntity.getUserId());
        othersPrizeAggregate.setUserAwardRecordEntity(distributeAwardEntity.getUserId(), distributeAwardEntity.getOrderId(), distributeAwardEntity.getAwardId(), AwardStateVO.completed.getCode());
//        存储发奖对象
        awardRepository.saveOthersPrizeAggregate(othersPrizeAggregate);

    }
}
