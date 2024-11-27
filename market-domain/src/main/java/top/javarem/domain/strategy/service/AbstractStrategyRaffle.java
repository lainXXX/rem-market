package top.javarem.domain.strategy.service;

import lombok.extern.slf4j.Slf4j;
import top.javarem.domain.strategy.model.entity.RaffleAwardEntity;
import top.javarem.domain.strategy.model.entity.RaffleFactorEntity;
import top.javarem.domain.strategy.model.entity.RuleActionEntity;
import top.javarem.domain.strategy.model.entity.RuleActionEntity.RaffleExecutingEntity;
import top.javarem.domain.strategy.model.entity.StrategyEntity;
import top.javarem.domain.strategy.repository.IStrategyRepository;
import top.javarem.domain.strategy.service.armory.IStrategyArmoryDispatch;
import top.javarem.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import top.javarem.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;

import java.util.List;

/**
 * @Author: rem
 * @Date: 2024/11/21/17:59
 * @Description:
 */

@Slf4j
public abstract class AbstractStrategyRaffle implements IStrategyRaffle {

    protected IStrategyRepository repository;
    protected IStrategyArmoryDispatch dispatch;
    protected DefaultChainFactory chainFactory;

    public AbstractStrategyRaffle(IStrategyRepository repository, IStrategyArmoryDispatch dispatch, DefaultChainFactory chainFactory) {
        this.repository = repository;
        this.dispatch = dispatch;
        this.chainFactory = chainFactory;
    }

    @Override
    public RaffleAwardEntity performRaffle(RaffleFactorEntity factor) {

        log.info("开始执行抽奖流程 用户id : {}, 策略id : {}", factor.getUserId(), factor.getStrategyId());

        Long strategyId = factor.getStrategyId();
        String userId = factor.getUserId();
        if (strategyId == null || userId == null) {
            throw new IllegalArgumentException("Strategy id or userId is null");
        }
//        查询策略规则
        StrategyEntity strategyEntity = repository.getStrategyEntity(strategyId);
//        抽奖策略责任链获取奖品
        DefaultChainFactory.LogicAwardVO chainLogicAwardVO = this.doRaffleLogicChain(userId, strategyId);
        log.info("责任链获取奖品ID awardId: {} ruleModel: {}", chainLogicAwardVO.getAwardId(), chainLogicAwardVO.getRuleModel());
//        如果抽奖奖品执行规则不为默认规则 则直接返回奖品
        if (!DefaultChainFactory.LogicModel.DEFAULT.getCode().equals(chainLogicAwardVO.getRuleModel())) {
            return RaffleAwardEntity.builder()
                    .awardId(chainLogicAwardVO.getAwardId())
                    .build();
        }

//        获取奖品后规则树逻辑执行
        DefaultTreeFactory.LogicAwardVO treeLogicAwardVO = this.doRaffleLogicTree(strategyId, chainLogicAwardVO.getAwardId());
        Integer awardId = treeLogicAwardVO.getAwardId();
        log.info("规则树逻辑执行 奖品id {} ", awardId);

        return RaffleAwardEntity.builder()
                .awardId(awardId)
                .build();

    }

    protected abstract DefaultChainFactory.LogicAwardVO doRaffleLogicChain(String userId, Long strategyId);

    protected abstract DefaultTreeFactory.LogicAwardVO doRaffleLogicTree(Long strategyId, Integer awardId);

}
