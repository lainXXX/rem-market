package top.javarem.domain.strategy.service.raffle;

import lombok.extern.slf4j.Slf4j;
import top.javarem.domain.strategy.model.entity.RaffleAwardEntity;
import top.javarem.domain.strategy.model.entity.RaffleFactorEntity;
import top.javarem.domain.strategy.model.entity.RuleActionEntity;
import top.javarem.domain.strategy.model.entity.StrategyEntity;
import top.javarem.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import top.javarem.domain.strategy.repository.IStrategyRepository;
import top.javarem.domain.strategy.service.IStrategyRaffle;
import top.javarem.domain.strategy.service.armory.IStrategyArmoryDispatch;
import top.javarem.domain.strategy.service.rule.factory.DefaultLogicFactory;
import top.javarem.types.common.constants.Constants;

/**
 * @Author: rem
 * @Date: 2024/11/21/17:59
 * @Description:
 */

@Slf4j
public abstract class AbstractStrategyRaffle implements IStrategyRaffle {

    protected IStrategyRepository repository;
    protected IStrategyArmoryDispatch dispatch;

    public AbstractStrategyRaffle(IStrategyRepository repository, IStrategyArmoryDispatch dispatch) {
        this.repository = repository;
        this.dispatch = dispatch;
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

        RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> rule = this.doRaffleBeforeLogic(factor, strategyEntity.getRuleModels());
        String code = rule.getCode();
        String model = rule.getRuleModel();
        if (code.equals(RuleLogicCheckTypeVO.FILTER_BLOCK.getCode()) && model.equals(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode())) {
            return RaffleAwardEntity.builder()
                    .awardId(Constants.BLACKLIST_AWARD_ID)
                    .build();
        } else if (code.equals(RuleLogicCheckTypeVO.FILTER_BLOCK.getCode()) && model.equals(DefaultLogicFactory.LogicModel.RULE_WIGHT.getCode())) {
            Integer randomAwardId = dispatch.getRandomAwardId(strategyId, rule.getData().getRuleWeightValueKey());
            return RaffleAwardEntity.builder()
                    .awardId(randomAwardId)
                    .build();
        }

        log.info("执行正常抽奖");
        Integer randomAwardId = dispatch.getRandomAwardId(strategyId);
        return RaffleAwardEntity.builder()
                .awardId(randomAwardId)
                .build();

    }

    protected abstract RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> doRaffleBeforeLogic(RaffleFactorEntity factor, String[] ruleModels);

}
