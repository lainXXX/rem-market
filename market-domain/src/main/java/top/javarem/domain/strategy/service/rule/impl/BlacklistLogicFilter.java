package top.javarem.domain.strategy.service.rule.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.javarem.domain.strategy.annotation.StrategyLogic;
import top.javarem.domain.strategy.model.entity.RuleActionEntity;
import top.javarem.domain.strategy.model.entity.RuleActionEntity.RaffleBeforeEntity;
import top.javarem.domain.strategy.model.entity.RuleMatterEntity;
import top.javarem.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import top.javarem.domain.strategy.repository.IStrategyRepository;
import top.javarem.domain.strategy.service.rule.ILogicFilter;
import top.javarem.domain.strategy.service.rule.factory.DefaultLogicFactory;
import top.javarem.types.common.constants.Constants;

/**
 * @Author: rem
 * @Date: 2024/11/21/16:41
 * @Description:
 */
@Component
@Slf4j
@StrategyLogic(logicModel = DefaultLogicFactory.LogicModel.RULE_BLACKLIST)
public class BlacklistLogicFilter implements ILogicFilter<RaffleBeforeEntity> {

    private final IStrategyRepository repository;

    public BlacklistLogicFilter(IStrategyRepository repository) {
        this.repository = repository;
    }

    @Override
    public RuleActionEntity<RaffleBeforeEntity> filter(RuleMatterEntity ruleMatterEntity) {
        String ruleModel = repository.getBlacklistEntity(ruleMatterEntity.getUserId());
//        如果用户规则模型为黑名单 则返回黑名单奖品
        if (DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode().equals(ruleModel)) {
            log.info("黑名单过滤结果 拦截");
            return RuleActionEntity.<RaffleBeforeEntity>builder()
                    .ruleModel(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode())
                    .data(RuleActionEntity.RaffleBeforeEntity.builder()
                            .strategyId(ruleMatterEntity.getStrategyId())
                            .awardId(Constants.BLACKLIST_AWARD_ID)
                            .build())
                    .code(RuleLogicCheckTypeVO.FILTER_BLOCK.getCode())
                    .info(RuleLogicCheckTypeVO.FILTER_BLOCK.getInfo())
                    .build();
        }

        return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
                .code(RuleLogicCheckTypeVO.FILTER_PASS.getCode())
                .info(RuleLogicCheckTypeVO.FILTER_PASS.getInfo())
                .build();

    }
}
