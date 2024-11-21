package top.javarem.domain.strategy.service.armory;

public interface IStrategyArmoryDispatch {

    Integer getRandomAwardId(Long strategyId);

    Integer getRandomAwardId(Long strategyId, String ruleWeightValue);
}
