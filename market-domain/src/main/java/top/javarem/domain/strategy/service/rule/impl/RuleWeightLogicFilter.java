package top.javarem.domain.strategy.service.rule.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.javarem.domain.strategy.annotation.StrategyLogic;
import top.javarem.domain.strategy.model.entity.RuleActionEntity;
import top.javarem.domain.strategy.model.entity.RuleMatterEntity;
import top.javarem.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import top.javarem.domain.strategy.repository.IStrategyRepository;
import top.javarem.domain.strategy.service.rule.ILogicFilter;
import top.javarem.domain.strategy.service.rule.factory.DefaultLogicFactory;

import static top.javarem.domain.strategy.model.entity.RuleActionEntity.*;

/**
 * @Author: rem
 * @Date: 2024/11/21/17:41
 * @Description:
 */
@Component
@Slf4j
@StrategyLogic(logicModel = DefaultLogicFactory.LogicModel.RULE_WIGHT)
public class RuleWeightLogicFilter implements ILogicFilter<RaffleBeforeEntity> {

    @Autowired
    private IStrategyRepository repository;
    public Long userScore = 4500L;

    @Override
    public RuleActionEntity<RaffleBeforeEntity> filter(RuleMatterEntity ruleMatterEntity) {
        Long strategyId = ruleMatterEntity.getStrategyId();
        Integer awardId = ruleMatterEntity.getAwardId();
        String userId = ruleMatterEntity.getUserId();

        String minMatchWeightKey = repository.getMinMatchScore(userScore);
        if (minMatchWeightKey == null) {
            return RuleActionEntity.<RaffleBeforeEntity>builder()
                    .code(RuleLogicCheckTypeVO.FILTER_PASS.getCode())
                    .info(RuleLogicCheckTypeVO.FILTER_PASS.getInfo())
                    .build();

        }
        return RuleActionEntity.<RaffleBeforeEntity>builder()
                .code(RuleLogicCheckTypeVO.FILTER_BLOCK.getCode())
                .info(RuleLogicCheckTypeVO.FILTER_BLOCK.getInfo())
                .ruleModel(DefaultLogicFactory.LogicModel.RULE_WIGHT.getCode())
                .data(RaffleBeforeEntity.builder()
                        .strategyId(strategyId)
                        .awardId(awardId)
                        .ruleWeightValueKey(minMatchWeightKey)
                        .build()
                ).build();
    }
}
