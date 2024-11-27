package top.javarem.domain.strategy.service.rule.tree.impl;

import org.springframework.stereotype.Component;
import top.javarem.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import top.javarem.domain.strategy.service.rule.tree.ILogicTreeNode;
import top.javarem.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;

/**
 * @Author: rem
 * @Date: 2024/11/26/14:44
 * @Description:
 */
@Component("rule_stock")
public class StockLogicTreeNode implements ILogicTreeNode {
    @Override
    public DefaultTreeFactory.TreeActionEntity execute(Long strategyId, Integer awardId, String ruleValue) {
        return DefaultTreeFactory.TreeActionEntity.builder()
                .ruleLogicCheckTypeVO(RuleLogicCheckTypeVO.PASS)
                .logicAwardVO(DefaultTreeFactory.LogicAwardVO.builder()
                        .awardId(awardId)
                        .ruleModel("rule_stock")
                        .build())
                .build();
    }
}
