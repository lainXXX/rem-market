package top.javarem.domain.strategy.service.armory;

import java.util.Date;

public interface IStrategyArmoryDispatch {

    Integer getRandomAwardId(Long strategyId);

    Integer getRandomAwardId(Long strategyId, String ruleWeightKey);

    Boolean deductAwardCount(Long strategyId, Integer awardId, Date endTime);
}
