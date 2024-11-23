package top.javarem.domain.strategy.service.raffle;

import lombok.extern.slf4j.Slf4j;
import top.javarem.domain.strategy.model.entity.RaffleAwardEntity;
import top.javarem.domain.strategy.model.entity.RaffleFactorEntity;
import top.javarem.domain.strategy.model.entity.RuleActionEntity;
import top.javarem.domain.strategy.model.entity.RuleActionEntity.RaffleBeforeEntity;
import top.javarem.domain.strategy.model.entity.RuleActionEntity.RaffleExecutingEntity;
import top.javarem.domain.strategy.model.entity.StrategyEntity;
import top.javarem.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import top.javarem.domain.strategy.repository.IStrategyRepository;
import top.javarem.domain.strategy.service.IStrategyRaffle;
import top.javarem.domain.strategy.service.armory.IStrategyArmoryDispatch;
import top.javarem.domain.strategy.service.rule.factory.DefaultLogicFactory;
import top.javarem.types.common.constants.Constants;

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

        RuleActionEntity<RaffleBeforeEntity> beforeRule = this.doRaffleBeforeLogic(factor, strategyEntity.getRaffleBeforeModel());
        String code = beforeRule.getCode();
        String model = beforeRule.getRuleModel();
        Integer randomAwardId;
        if (code.equals(RuleLogicCheckTypeVO.FILTER_BLOCK.getCode()) && model.equals(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode())) {
            return RaffleAwardEntity.builder()
                    .awardId(Constants.BLACKLIST_AWARD_ID)
                    .build();
        } else if (code.equals(RuleLogicCheckTypeVO.FILTER_BLOCK.getCode()) && model.equals(DefaultLogicFactory.LogicModel.RULE_WEIGHT.getCode())) {
            randomAwardId = dispatch.getRandomAwardId(strategyId, beforeRule.getData().getRuleWeightValueKey());
        } else {
            log.info("执行正常抽奖");
            randomAwardId = dispatch.getRandomAwardId(strategyId);
        }

//        抽奖时规则逻辑执行
        RuleActionEntity<RaffleExecutingEntity> executingRule = this.doRaffleExecutingLogic(RaffleFactorEntity.builder().userId(userId).strategyId(strategyId).awardId(randomAwardId).build(),
                strategyEntity.getRaffleExecutingModel());
        if (executingRule.getCode().equals(RuleLogicCheckTypeVO.FILTER_BLOCK.getCode())) {
            log.info("抽奖时规则被拦截 通过rule_luck_award规则走兜底奖励");
            return RaffleAwardEntity.builder()
                    .awardDesc("中奖中规则拦截，通过抽奖后规则 rule_luck_award 走兜底奖励。")
                    .build();
        }
        return RaffleAwardEntity.builder()
                .awardId(randomAwardId)
                .build();

    }

    protected abstract RuleActionEntity<RaffleBeforeEntity> doRaffleBeforeLogic(RaffleFactorEntity factor, List<String> ruleModels);

    protected abstract RuleActionEntity<RaffleExecutingEntity> doRaffleExecutingLogic(RaffleFactorEntity factor, List<String> ruleModels);

}
