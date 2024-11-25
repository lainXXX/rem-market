package top.javarem.domain.strategy.service.rule.chain.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.javarem.domain.strategy.model.entity.RuleActionEntity;
import top.javarem.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import top.javarem.domain.strategy.repository.IStrategyRepository;
import top.javarem.domain.strategy.service.armory.IStrategyArmoryDispatch;
import top.javarem.domain.strategy.service.rule.chain.AbstractStrategyLogicChain;
import top.javarem.domain.strategy.service.rule.filter.factory.DefaultFilterLogicFactory;

/**
 * @Author: rem
 * @Date: 2024/11/25/21:21
 * @Description:
 */
@Slf4j
@Component("rule_weight")
public class RuleWeightLogicNode extends AbstractStrategyLogicChain {

    @Autowired
    private IStrategyRepository repository;

    @Autowired
    private IStrategyArmoryDispatch dispatch;

    public Long userScore = 4500L;

    @Override
    public Integer executeStrategy(String userId, Long strategyId) {

        String minMatchWeightKey = repository.getMinMatchScore(strategyId, this.getRuleModel(), userScore);
        if (minMatchWeightKey != null) {
            Integer randomAwardId = dispatch.getRandomAwardId(strategyId, minMatchWeightKey);
            log.info("权重规则接管执行");
            return randomAwardId;
        }

        log.info("权重规则放行");
        return this.next().executeStrategy(userId, strategyId);
    }

    @Override
    protected String getRuleModel() {
        return "rule_weight";
    }
}
