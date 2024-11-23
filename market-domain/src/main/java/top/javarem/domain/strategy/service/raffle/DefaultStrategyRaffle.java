package top.javarem.domain.strategy.service.raffle;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.javarem.domain.strategy.model.entity.RaffleFactorEntity;
import top.javarem.domain.strategy.model.entity.RuleActionEntity;
import top.javarem.domain.strategy.model.entity.RuleActionEntity.RaffleExecutingEntity;
import top.javarem.domain.strategy.model.entity.RuleMatterEntity;
import top.javarem.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import top.javarem.domain.strategy.repository.IStrategyRepository;
import top.javarem.domain.strategy.service.armory.IStrategyArmoryDispatch;
import top.javarem.domain.strategy.service.rule.ILogicFilter;
import top.javarem.domain.strategy.service.rule.factory.DefaultLogicFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: rem
 * @Date: 2024/11/22/09:30
 * @Description:
 */
@Slf4j
@Service
public class DefaultStrategyRaffle extends AbstractStrategyRaffle {

    private final DefaultLogicFactory logicFactory;

    @Autowired
    public DefaultStrategyRaffle(IStrategyRepository repository, IStrategyArmoryDispatch dispatch, DefaultLogicFactory logicFactory) {
        super(repository, dispatch);
        this.logicFactory = logicFactory;
    }

    /**
     * 执行抽奖前的额逻辑过滤
     *
     * @param factor     抽奖因素
     * @param ruleModels 抽奖规则模型
     * @return 抽奖规则行为实体
     */
    @Override
    protected RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> doRaffleBeforeLogic(RaffleFactorEntity factor, List<String> ruleModels) {
//        如果该过滤器没有传入规则 则直接放行
        if (CollectionUtils.isEmpty(ruleModels))
            return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
                    .code(RuleLogicCheckTypeVO.FILTER_PASS.getCode())
                    .info(RuleLogicCheckTypeVO.FILTER_PASS.getInfo())
                    .build();
//        从逻辑工厂获取ILogicFilter的实现集合
        Map<String, ILogicFilter<RuleActionEntity.RaffleBeforeEntity>> filterMap = logicFactory.getFilterMap();
        RuleMatterEntity matter = RuleMatterEntity.builder()
                .userId(factor.getUserId())
                .strategyId(factor.getStrategyId())
                .ruleModel(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode())
                .build();
//        优先过滤黑名单规则
        String ruleBlacklist = ruleModels.stream()
                .filter(model -> model.equals(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode()))
                .findFirst()
                .orElse(null);
        if (StringUtils.isNotBlank(ruleBlacklist)) {
//            从ILogicFilter的实现集合获取黑名单过滤器
            ILogicFilter<RuleActionEntity.RaffleBeforeEntity> blacklistFilter = filterMap.get(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode());
//            装配RuleMatterEntity对象

            RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> ruleAction = blacklistFilter.filter(matter);
//            如果blacklistFilter.filter(matter)的结果为
            boolean result = ruleAction.getCode().equals(RuleLogicCheckTypeVO.FILTER_BLOCK.getCode());

            log.info("黑名单过滤结果 {}", result ? "拦截" : "放行");
            if (result) return ruleAction;
        }

//        后续的过滤
        List<String> models = ruleModels.stream()
                .filter(model -> !model.equals(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode()))
                .collect(Collectors.toList());

        RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> ruleAction = null;

        for (String model : models) {
            ILogicFilter<RuleActionEntity.RaffleBeforeEntity> filter = filterMap.getOrDefault(model, null);
            if (filter == null) continue;
            matter = RuleMatterEntity.builder()
                    .userId(factor.getUserId())
                    .strategyId(factor.getStrategyId())
                    .ruleModel(model)
                    .build();
            ruleAction = filter.filter(matter);
            boolean result = ruleAction.getCode().equals(RuleLogicCheckTypeVO.FILTER_BLOCK.getCode());
            log.info("抽奖前规则过滤 {} {}", model, result ? "拦截" : "放行");
            if (result) return ruleAction;
        }

        return ruleAction;

    }

    @Override
    protected RuleActionEntity<RaffleExecutingEntity> doRaffleExecutingLogic(RaffleFactorEntity factor, List<String> ruleModels) {
        if (CollectionUtils.isEmpty(ruleModels))
            return RuleActionEntity.<RaffleExecutingEntity>builder()
                    .code(RuleLogicCheckTypeVO.FILTER_PASS.getCode())
                    .info(RuleLogicCheckTypeVO.FILTER_PASS.getInfo())
                    .build();
        //        从逻辑工厂获取ILogicFilter的实现集合
        Map<String, ILogicFilter<RaffleExecutingEntity>> filterMap = logicFactory.getFilterMap();
        RuleMatterEntity matter;
        RuleActionEntity<RaffleExecutingEntity> ruleAction = null;
        for (String model : ruleModels) {
            ILogicFilter<RaffleExecutingEntity> filter = filterMap.getOrDefault(model, null);
            if (filter == null) continue;
            matter = RuleMatterEntity.builder()
                    .userId(factor.getUserId())
                    .strategyId(factor.getStrategyId())
                    .ruleModel(model)
                    .awardId(factor.getAwardId())
                    .build();
            ruleAction = filter.filter(matter);
            boolean result = ruleAction.getCode().equals(RuleLogicCheckTypeVO.FILTER_BLOCK.getCode());
            log.info("抽奖时规则过滤 {} {}", model, result ? "拦截" : "放行");
            if (result) return ruleAction;
        }
        return ruleAction;
    }


}
