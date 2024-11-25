package top.javarem.domain.strategy.service.rule.chain.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.javarem.domain.strategy.service.armory.IStrategyArmoryDispatch;
import top.javarem.domain.strategy.service.rule.chain.AbstractStrategyLogicChain;

/**
 * @Author: rem
 * @Date: 2024/11/25/21:21
 * @Description:
 */
@Component("default")
@Slf4j
public class DefaultLogicNode extends AbstractStrategyLogicChain {

    @Autowired
    private IStrategyArmoryDispatch dispatch;

    @Override
    public Integer executeStrategy(String userId, Long strategyId) {
        Integer randomAwardId = dispatch.getRandomAwardId(strategyId);
        log.info("默认规则接管执行");
        if (next() != null) return next().executeStrategy(userId, strategyId);
        return randomAwardId;
    }

    @Override
    protected String getRuleModel() {
        return "default";
    }
}
