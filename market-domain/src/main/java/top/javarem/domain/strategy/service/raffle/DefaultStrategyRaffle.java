package top.javarem.domain.strategy.service.raffle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.javarem.domain.strategy.model.entity.RaffleFactorEntity;
import top.javarem.domain.strategy.model.entity.RuleActionEntity;
import top.javarem.domain.strategy.model.entity.RuleActionEntity.RaffleExecutingEntity;
import top.javarem.domain.strategy.model.entity.RuleMatterEntity;
import top.javarem.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import top.javarem.domain.strategy.model.vo.RuleTreeVO;
import top.javarem.domain.strategy.model.vo.StrategyAwardRuleModelsVO;
import top.javarem.domain.strategy.repository.IStrategyRepository;
import top.javarem.domain.strategy.service.AbstractStrategyRaffle;
import top.javarem.domain.strategy.service.armory.IStrategyArmoryDispatch;
import top.javarem.domain.strategy.service.rule.chain.IStrategyLogicLogicChain;
import top.javarem.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import top.javarem.domain.strategy.service.rule.filter.ILogicFilter;
import top.javarem.domain.strategy.service.rule.filter.factory.DefaultFilterLogicFactory;
import top.javarem.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import top.javarem.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;

import java.util.List;
import java.util.Map;

/**
 * @Author: rem
 * @Date: 2024/11/22/09:30
 * @Description:
 */
@Slf4j
@Service
public class DefaultStrategyRaffle extends AbstractStrategyRaffle {

    private DefaultFilterLogicFactory logicFactory;
    private DefaultTreeFactory treeFactory;

    @Autowired
    public DefaultStrategyRaffle(IStrategyRepository repository, IStrategyArmoryDispatch dispatch, DefaultChainFactory chainFactory) {
        super(repository, dispatch, chainFactory);
    }

    @Override
    protected DefaultChainFactory.LogicAwardVO doRaffleLogicChain(String userId, Long strategyId) {
        //        开启抽奖策略责任链
        IStrategyLogicLogicChain logicChain = chainFactory.openLogicChain(strategyId);
//        通过责任链获取奖品ID
        return logicChain.executeStrategy(userId, strategyId);
    }

    @Override
    protected DefaultTreeFactory.LogicAwardVO  doRaffleLogicTree(Long strategyId, Integer awardId) {
        StrategyAwardRuleModelsVO ruleModelsVO = repository.getAwardRules(strategyId, awardId);
        if (ruleModelsVO == null) return DefaultTreeFactory.LogicAwardVO.builder().build();
        RuleTreeVO ruleTreeVO = repository.getRuleTreeVO(ruleModelsVO.getRuleModels());
        IDecisionTreeEngine engine = treeFactory.openLogicTree(ruleTreeVO);
        return engine.process(strategyId, awardId);
    }
}
