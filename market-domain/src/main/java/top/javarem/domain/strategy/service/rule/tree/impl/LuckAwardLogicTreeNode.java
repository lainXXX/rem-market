package top.javarem.domain.strategy.service.rule.tree.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.javarem.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import top.javarem.domain.strategy.service.rule.tree.ILogicTreeNode;
import top.javarem.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import top.javarem.types.common.constants.Constants;

import java.util.Date;

/**
 * @Author: rem
 * @Date: 2024/11/26/14:44
 * @Description:
 */
@Component("rule_luck_award")
@Slf4j
public class LuckAwardLogicTreeNode implements ILogicTreeNode {


    @Override
    public DefaultTreeFactory.TreeActionEntity execute(String userId, Long strategyId, Integer awardId, String ruleValue, Date endTime) {

        String[] split = ruleValue.split(Constants.COLON);
        if (split.length != 2) {
            throw new RuntimeException("规则树过滤- 兜底奖品 rule_luck_award rulValue 配置错误");
        }
        Integer luckAwardId = Integer.parseInt(split[0]);
        String scoreRange = split[1];
        log.info("规则树过滤-兜底奖品 luckAwardId : {} scoreRange : {} ", luckAwardId, scoreRange);
        return DefaultTreeFactory.TreeActionEntity.builder()
                .ruleLogicCheckTypeVO(RuleLogicCheckTypeVO.BLOCK)
                .logicAwardVO(DefaultTreeFactory.LogicAwardVO.builder()
                        .awardId(luckAwardId)
                        .ruleModel(DefaultTreeFactory.LogicModel.LUCK_AWARD.getCode())
                        .value(scoreRange)
                        .build())
                .build();
    }
}
