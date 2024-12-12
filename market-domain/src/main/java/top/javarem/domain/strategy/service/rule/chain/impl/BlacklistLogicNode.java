package top.javarem.domain.strategy.service.rule.chain.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.javarem.domain.strategy.repository.IStrategyRepository;
import top.javarem.domain.strategy.service.rule.chain.AbstractStrategyLogicChain;
import top.javarem.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import top.javarem.types.common.constants.Constants;

/**
 * @Author: rem
 * @Date: 2024/11/25/21:20
 * @Description:策略责任链节点 黑名单处理
 */
@Component("rule_blacklist")
@Slf4j
public class BlacklistLogicNode extends AbstractStrategyLogicChain {

    @Autowired
    private IStrategyRepository repository;

    @Override
    public DefaultChainFactory.LogicAwardVO executeStrategy(String userId, Long strategyId) {
        String ruleModel = repository.getUserRuleModel(userId);
//        如果用户规则模型为黑名单 则返回黑名单奖品
        if (Constants.LogicModel.RULE_BLACKLIST.getCode().equals(ruleModel)) {
            Integer awardId = Constants.BLACKLIST_AWARD_ID;
            log.info("抽奖责任链-黑名单规则接管执行");
            return DefaultChainFactory.LogicAwardVO.builder()
                    .awardId(awardId)
                    .ruleModel(getRuleModel())
                    .build();
        }

        log.info("抽奖责任链-黑名单规则放行");
        return this.next().executeStrategy(userId, strategyId);
    }

    @Override
    protected String getRuleModel () {
        return "rule_blacklist";
    }

}
