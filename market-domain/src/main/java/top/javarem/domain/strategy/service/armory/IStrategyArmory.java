package top.javarem.domain.strategy.service.armory;

/**
 * 策略工厂 装配抽奖策略
 */
public interface IStrategyArmory {

    boolean assembleRaffleStrategy(Long StrategyId);

    boolean assembleRaffleStrategyByActivityId(Long activityId);
}
