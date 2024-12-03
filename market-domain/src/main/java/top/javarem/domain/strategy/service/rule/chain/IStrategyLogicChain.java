package top.javarem.domain.strategy.service.rule.chain;

import top.javarem.domain.strategy.service.rule.chain.factory.DefaultChainFactory;

/**
 * @Author: rem
 * @Date: 2024/11/25/21:01
 * @Description: 抽奖策略责任链
 */
public interface IStrategyLogicChain extends IStrategyLogicChainArmory {

    DefaultChainFactory.LogicAwardVO executeStrategy(String userId, Long strategyId);

}
