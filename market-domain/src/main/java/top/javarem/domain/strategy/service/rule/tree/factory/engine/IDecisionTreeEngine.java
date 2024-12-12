package top.javarem.domain.strategy.service.rule.tree.factory.engine;

import top.javarem.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;

/**
 * @Author: rem
 * @Date: 2024/11/26/23:21
 * @Description:
 */
public interface IDecisionTreeEngine {

    DefaultTreeFactory.LogicAwardVO process(String userId, Long strategyId, Integer awardId);

}
