package top.javarem.domain.strategy.service.rule.filter;

import top.javarem.domain.strategy.model.entity.RuleActionEntity;
import top.javarem.domain.strategy.model.entity.RuleMatterEntity;

public interface ILogicFilter<T extends RuleActionEntity.RaffleEntity> {

    RuleActionEntity<T> filter(RuleMatterEntity ruleMatterEntity);

}
