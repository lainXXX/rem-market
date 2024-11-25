package top.javarem.domain.strategy.service.rule.chain;

/**
 * @Author: rem
 * @Date: 2024/11/25/21:01
 * @Description: 抽奖策略责任链
 */
public interface IStrategyLogicLogicChain extends IStrategyLogicChainArmory {

    Integer executeStrategy(String userId, Long strategyId);

}
