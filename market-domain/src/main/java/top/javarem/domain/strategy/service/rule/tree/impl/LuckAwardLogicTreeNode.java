package top.javarem.domain.strategy.service.rule.tree.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.javarem.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import top.javarem.domain.strategy.service.rule.tree.ILogicTreeNode;
import top.javarem.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import top.javarem.types.common.constants.Constants;

/**
 * @Author: rem
 * @Date: 2024/11/26/14:44
 * @Description:
 */
@Component("rule_luck_award")
@Slf4j
public class LuckAwardLogicTreeNode implements ILogicTreeNode {


    @Override
    public DefaultTreeFactory.TreeActionEntity execute(Long strategyId, Integer awardId, String ruleValue) {

        String[] split = ruleValue.split(Constants.COLON);
        if (split.length != 2) {
            throw new RuntimeException("rule_luck_award rulValue 配置错误");
        }
        Integer luckAwardId = Integer.parseInt(split[0]);
        String scoreRange = split[1];
        log.info("规则过滤-兜底奖品 luckAwardId : {} scoreRange : {} ", luckAwardId, scoreRange);
        return DefaultTreeFactory.TreeActionEntity.builder()
                .ruleLogicCheckTypeVO(RuleLogicCheckTypeVO.BLOCK)
                .logicAwardVO(DefaultTreeFactory.LogicAwardVO.builder()
                        .awardId(luckAwardId)
                        .ruleModel(scoreRange)
                        .build())
                .build();
    }
}
