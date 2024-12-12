package top.javarem.domain.strategy.service;

import lombok.extern.slf4j.Slf4j;
import top.javarem.domain.strategy.model.entity.RaffleAwardEntity;
import top.javarem.domain.strategy.model.entity.RaffleFactorEntity;
import top.javarem.domain.strategy.model.entity.StrategyAwardEntity;
import top.javarem.domain.strategy.model.entity.StrategyEntity;
import top.javarem.domain.strategy.repository.IStrategyRepository;
import top.javarem.domain.strategy.service.armory.IStrategyArmoryDispatch;
import top.javarem.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import top.javarem.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;

import static top.javarem.domain.strategy.service.rule.chain.factory.DefaultChainFactory.LogicAwardVO;
import static top.javarem.domain.strategy.service.rule.chain.factory.DefaultChainFactory.LogicModel;

/**
 * @Author: rem
 * @Date: 2024/11/21/17:59
 * @Description: 抽奖流程逻辑
 */

@Slf4j
public abstract class AbstractRaffleLogic implements IRaffleStrategy, IRaffleStock {

    protected IStrategyRepository repository;
    protected IStrategyArmoryDispatch dispatch;
    protected DefaultChainFactory chainFactory;

    public AbstractRaffleLogic(IStrategyRepository repository, IStrategyArmoryDispatch dispatch, DefaultChainFactory chainFactory) {
        this.repository = repository;
        this.dispatch = dispatch;
        this.chainFactory = chainFactory;
    }

    @Override
    public RaffleAwardEntity performRaffle(RaffleFactorEntity factor) {

        log.info("开始执行抽奖策略核心流程 用户id : {}, 策略id : {}", factor.getUserId(), factor.getStrategyId());

        Long strategyId = factor.getStrategyId();
        String userId = factor.getUserId();
        if (strategyId == null || userId == null) {
            throw new IllegalArgumentException("Strategy id or userId is null");
        }
//        查询策略规则
        StrategyEntity strategyEntity = repository.getStrategyEntity(strategyId);
//        抽奖策略责任链获取奖品
        LogicAwardVO chainLogicAwardVO = this.doRaffleLogicChain(userId, strategyId);
        log.info("责任链执行完毕- 结果为 awardId: {} ruleModel: {}", chainLogicAwardVO.getAwardId(), chainLogicAwardVO.getRuleModel());
//        如果抽奖奖品执行规则不为默认规则 则直接返回奖品
        if (!LogicModel.DEFAULT.getCode().equals(chainLogicAwardVO.getRuleModel())) {
            return getRaffleAwardEntity(strategyId, chainLogicAwardVO.getAwardId(), null);
        }

//        获取奖品后规则树逻辑执行
        DefaultTreeFactory.LogicAwardVO treeLogicAwardVO = this.doRaffleLogicTree(userId, strategyId, chainLogicAwardVO.getAwardId());
        Integer awardId = treeLogicAwardVO.getAwardId();
        log.info("规则树逻辑执行完毕执行- 结果为 奖品id {} ", awardId);

        return getRaffleAwardEntity(strategyId, awardId, treeLogicAwardVO.getValue());
    }

    private RaffleAwardEntity getRaffleAwardEntity(Long strategyId, Integer awardId, String awardConfig) {
        StrategyAwardEntity strategyAwardEntity = repository.getStrategyAwardEntity(strategyId, awardId);
        return RaffleAwardEntity.builder()
                .awardId(awardId)
                .awardTitle(strategyAwardEntity.getAwardTitle())
                .awardConfig(awardConfig)
                .sort(strategyAwardEntity.getSort())
                .build();
    }

    protected abstract LogicAwardVO doRaffleLogicChain(String userId, Long strategyId);

    protected abstract DefaultTreeFactory.LogicAwardVO doRaffleLogicTree(String userId, Long strategyId, Integer awardId);

}
