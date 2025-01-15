package top.javarem.domain.strategy.service.rule.chain.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.javarem.domain.strategy.repository.IStrategyRepository;
import top.javarem.domain.strategy.service.armory.IStrategyArmoryDispatch;
import top.javarem.domain.strategy.service.rule.chain.AbstractStrategyLogicChain;
import top.javarem.domain.strategy.service.rule.chain.factory.DefaultChainFactory;

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

//    public Long userScore = 4500L;

    @Override
    public DefaultChainFactory.LogicAwardVO executeStrategy(String userId, Long strategyId) {
        Integer userScore = repository.getUserScore(userId, strategyId);
        String minMatchWeightKey = repository.getMinMatchScore(strategyId, this.getRuleModel(), Long.valueOf(userScore));
        if (minMatchWeightKey != null) {
            Integer randomAwardId = dispatch.getRandomAwardId(strategyId, minMatchWeightKey);
            log.info("抽奖责任链-权重规则接管执行");
            return DefaultChainFactory.LogicAwardVO.builder()
                    .awardId(randomAwardId)
                    .ruleModel(getRuleModel())
                    .build();
        }

        log.info("抽奖责任链-权重规则放行");
        return this.next().executeStrategy(userId, strategyId);
    }

    @Override
    protected String getRuleModel() {
        return "rule_weight";
    }
}
