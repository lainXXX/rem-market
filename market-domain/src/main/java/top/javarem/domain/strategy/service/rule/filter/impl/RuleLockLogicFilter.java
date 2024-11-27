package top.javarem.domain.strategy.service.rule.filter.impl;

import org.springframework.stereotype.Component;
import top.javarem.domain.strategy.annotation.StrategyLogic;
import top.javarem.domain.strategy.model.entity.RuleActionEntity;
import top.javarem.domain.strategy.model.entity.RuleActionEntity.RaffleExecutingEntity;
import top.javarem.domain.strategy.model.entity.RuleEntity;
import top.javarem.domain.strategy.model.entity.RuleMatterEntity;
import top.javarem.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import top.javarem.domain.strategy.repository.IStrategyRepository;
import top.javarem.domain.strategy.service.rule.filter.ILogicFilter;
import top.javarem.domain.strategy.service.rule.filter.factory.DefaultFilterLogicFactory;

import javax.annotation.Resource;

/**
 * @Author: rem
 * @Date: 2024/11/23/15:21
 * @Description:
 */
@Component
@StrategyLogic(logicModel = DefaultFilterLogicFactory.LogicModel.RULE_LOCK)
public class RuleLockLogicFilter implements ILogicFilter<RaffleExecutingEntity> {

    @Resource
    private IStrategyRepository repository;

    private Long userRaffleCount = 0L;

    @Override
    public RuleActionEntity<RaffleExecutingEntity> filter(RuleMatterEntity ruleMatterEntity) {
        Long strategyId = ruleMatterEntity.getStrategyId();
        String userId = ruleMatterEntity.getUserId();
        Integer awardId = ruleMatterEntity.getAwardId();
        String ruleModel = ruleMatterEntity.getRuleModel();
        RuleEntity ruleEntity = repository.getRuleEntity(strategyId, ruleModel, awardId);
        if (ruleEntity == null) {
            return RuleActionEntity.<RaffleExecutingEntity>builder()
                    .code(RuleLogicCheckTypeVO.PASS.getCode())
                    .info(RuleLogicCheckTypeVO.PASS.getInfo())
                    .build();
        }
        Long awardValue = Long.parseLong(ruleEntity.getRuleValue());
        if (userRaffleCount >= awardValue) {
            return RuleActionEntity.<RaffleExecutingEntity>builder()
                    .code(RuleLogicCheckTypeVO.PASS.getCode())
                    .info(RuleLogicCheckTypeVO.PASS.getInfo())
                    .build();
        }

        return RuleActionEntity.<RaffleExecutingEntity>builder()
                .code(RuleLogicCheckTypeVO.BLOCK.getCode())
                .info(RuleLogicCheckTypeVO.BLOCK.getInfo())
                .build();
    }
}
