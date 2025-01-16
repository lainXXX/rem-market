package top.javarem.domain.award.service.distribute.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.javarem.domain.award.model.aggregate.GiveOutPrizeAggregate;
import top.javarem.domain.award.model.entity.DistributeAwardEntity;
import top.javarem.domain.award.model.vo.AwardStateVO;
import top.javarem.domain.award.repository.IAwardRepository;
import top.javarem.domain.award.service.distribute.IDistributeAward;
import top.javarem.types.common.constants.Constants;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * @Author: rem
 * @Date: 2025/01/15/21:41
 * @Description:用户积分奖品 支持award_config透传 满足黑名单积分奖励
 */
@Component("user_credit_random")
public class UserCreditRandomAward implements IDistributeAward {

    @Autowired
    private IAwardRepository awardRepository;

    @Override
    public void giveOutPrizes(DistributeAwardEntity distributeAwardEntity) {

//        奖品Id
        Integer awardId = distributeAwardEntity.getAwardId();
//        奖品配置信息
        String awardConfig = distributeAwardEntity.getAwardConfig();
        if (StringUtils.isBlank(awardConfig)) {
            awardConfig = awardRepository.getAwardConfig(awardId);
        }
//        拆分配置 为积分范围
        String[] creditRange = awardConfig.split(Constants.COMMA);
        if (creditRange.length != 2) {
            throw new RuntimeException("award_config 「" + awardConfig + "」配置不是一个范围值，如 1,100");
        }
//        利用积分范围生成一个随机积分值
        BigDecimal creditAmount = generateRandomCredit(new BigDecimal(creditRange[0]), new BigDecimal(creditRange[1]));
//        创建奖品分发聚合对象
        GiveOutPrizeAggregate giveOutPrizeAggregate = new GiveOutPrizeAggregate();
        giveOutPrizeAggregate.setUserId(distributeAwardEntity.getUserId());
        giveOutPrizeAggregate.setUserAwardRecordEntity(distributeAwardEntity.getUserId(), distributeAwardEntity.getOrderId(), distributeAwardEntity.getAwardId(), AwardStateVO.completed.getCode());
        giveOutPrizeAggregate.setUserCreditAwardEntity(distributeAwardEntity.getUserId(), creditAmount);
//        存储发奖对象
        awardRepository.saveGiveOutPrizeAggregate(giveOutPrizeAggregate);
    }

    private BigDecimal generateRandomCredit(BigDecimal min, BigDecimal max) {
        if (min.equals(max)) {
            return min;
        }
        BigDecimal randomBigDecimal = min.add(new BigDecimal(Math.random()).multiply(max.subtract(min)));
        return randomBigDecimal.round(new MathContext(3));
    }

}
