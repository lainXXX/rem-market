package top.javarem.domain.strategy.service.raffle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.javarem.domain.strategy.model.entity.StrategyAwardEntity;
import top.javarem.domain.strategy.model.vo.AwardStockDecrQueueVO;
import top.javarem.domain.strategy.model.vo.RuleTreeVO;
import top.javarem.domain.strategy.model.vo.StrategyAwardRuleModelsVO;
import top.javarem.domain.strategy.repository.IStrategyRepository;
import top.javarem.domain.strategy.service.AbstractRaffleLogic;
import top.javarem.domain.strategy.service.IRaffleAward;
import top.javarem.domain.strategy.service.IRaffleStock;
import top.javarem.domain.strategy.service.armory.IStrategyArmoryDispatch;
import top.javarem.domain.strategy.service.rule.chain.IStrategyLogicChain;
import top.javarem.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import top.javarem.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import top.javarem.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;

import java.util.List;

/**
 * @Author: rem
 * @Date: 2024/11/22/09:30
 * @Description: 默认抽奖策略实现
 */
@Slf4j
@Service
public class DefaultRaffleStrategy extends AbstractRaffleLogic implements IRaffleStock, IRaffleAward {

    private final DefaultTreeFactory treeFactory;

    @Autowired
    public DefaultRaffleStrategy(IStrategyRepository repository, IStrategyArmoryDispatch dispatch, DefaultChainFactory chainFactory, DefaultTreeFactory treeFactory) {
        super(repository, dispatch, chainFactory);
        this.treeFactory = treeFactory;
    }

    @Override
    protected DefaultChainFactory.LogicAwardVO doRaffleLogicChain(String userId, Long strategyId) {
        //        开启抽奖策略责任链
        IStrategyLogicChain logicChain = chainFactory.openLogicChain(strategyId);
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
    public AwardStockDecrQueueVO handleQueueValue() {
        return repository.handleQueueValue();
    }

    @Override
    public Boolean updateAwardStock(AwardStockDecrQueueVO queueKeyVO) {
        return repository.updateAwardStock(queueKeyVO);
    }

    @Override
    public Boolean isEmptyStockDecrQueue() {
        return repository.isEmptyStockDecrQueue();
    }

    @Override
    public List<StrategyAwardEntity> displayAward(Long strategyId) {
        return repository.getStrategyAwardList(strategyId);
    }
}
