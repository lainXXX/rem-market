package top.javarem.domain.strategy.service.raffle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.javarem.domain.strategy.model.vo.AwardStockQueueKeyVO;
import top.javarem.domain.strategy.model.vo.RuleTreeVO;
import top.javarem.domain.strategy.model.vo.StrategyAwardRuleModelsVO;
import top.javarem.domain.strategy.repository.IStrategyRepository;
import top.javarem.domain.strategy.service.AbstractRaffleLogic;
import top.javarem.domain.strategy.service.armory.IStrategyArmoryDispatch;
import top.javarem.domain.strategy.service.rule.chain.IStrategyLogicLogicChain;
import top.javarem.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import top.javarem.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import top.javarem.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;

/**
 * @Author: rem
 * @Date: 2024/11/22/09:30
 * @Description:
 */
@Slf4j
@Service
public class DefaultRaffleLogic extends AbstractRaffleLogic{

    private final DefaultTreeFactory treeFactory;

    @Autowired
    public DefaultRaffleLogic(IStrategyRepository repository, IStrategyArmoryDispatch dispatch, DefaultChainFactory chainFactory, DefaultTreeFactory treeFactory) {
        super(repository, dispatch, chainFactory);
        this.treeFactory = treeFactory;
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
        if (ruleModelsVO == null) return DefaultTreeFactory.LogicAwardVO.builder()
                .awardId(awardId)
                .build();
        RuleTreeVO ruleTreeVO = repository.getRuleTreeVO(ruleModelsVO.getRuleModels());
        IDecisionTreeEngine engine = treeFactory.openLogicTree(ruleTreeVO);
        return engine.process(strategyId, awardId);
    }

    @Override
    public AwardStockQueueKeyVO handleQueueValue() {
        return repository.handleQueueValue();
    }

    @Override
    public Boolean updateAwardStock(AwardStockQueueKeyVO queueKeyVO) {
        return repository.updateAwardStock(queueKeyVO);
    }

}
