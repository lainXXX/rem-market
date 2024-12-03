package top.javarem.domain.strategy.service.rule.chain;

/**
 * @Author: rem
 * @Date: 2024/11/25/21:11
 * @Description: 策略责任链装配
 */
public interface IStrategyLogicChainArmory {

    IStrategyLogicChain next();

    IStrategyLogicChain appendNode(IStrategyLogicChain next);

}
