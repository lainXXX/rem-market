package top.javarem.domain.strategy.service.raffle.impl;

import org.springframework.beans.factory.annotation.Autowired;
import top.javarem.domain.strategy.model.entity.RuleActionEntity;
import top.javarem.domain.strategy.model.entity.RuleActionEntity.RaffleBeforeEntity;
import top.javarem.domain.strategy.model.entity.RuleMatterEntity;
import top.javarem.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import top.javarem.domain.strategy.repository.IStrategyRepository;
import top.javarem.domain.strategy.service.raffle.ILogicFilter;

/**
 * @Author: rem
 * @Date: 2024/11/21/16:41
 * @Description:
 */
public class BlacklistLogicFilter implements ILogicFilter<RaffleBeforeEntity> {

    @Autowired
    private IStrategyRepository repository;

    @Override
    public RuleActionEntity<RaffleBeforeEntity> filter(RuleMatterEntity ruleMatterEntity) {

        Integer awardId = repository.getBlacklistEntity(ruleMatterEntity.getUserId());
        if (awardId != null) {
            return RuleActionEntity.<RaffleBeforeEntity>builder()
                    .ruleModel(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode())
                    .data(RuleActionEntity.RaffleBeforeEntity.builder()
                            .strategyId(ruleMatterEntity.getStrategyId())
                            .awardId(awardId)
                            .build())
                    .code(RuleLogicCheckTypeVO.TAKE_OVER.getCode())
                    .info(RuleLogicCheckTypeVO.TAKE_OVER.getInfo())
                    .build();

        }

        return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
                .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
                .build();

    }
}
