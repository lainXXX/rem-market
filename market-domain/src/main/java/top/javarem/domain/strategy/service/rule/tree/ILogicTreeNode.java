package top.javarem.domain.strategy.service.rule.tree;

import top.javarem.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;

import java.util.Date;

/**
 * @Author: rem
 * @Date: 2024/11/26/14:02
 * @Description:
 */
public interface ILogicTreeNode {

    DefaultTreeFactory.TreeActionEntity execute(String userId, Long strategyId, Integer awardId, String ruleValue, Date endTime);

}
