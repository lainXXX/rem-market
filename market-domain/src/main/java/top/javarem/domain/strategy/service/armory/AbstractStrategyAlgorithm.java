package top.javarem.domain.strategy.service.armory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import top.javarem.domain.strategy.model.entity.RuleEntity;
import top.javarem.domain.strategy.model.entity.StrategyAwardEntity;
import top.javarem.domain.strategy.repository.IStrategyRepository;
import top.javarem.types.common.constants.Constants;
import top.javarem.types.enums.ResponseCode;
import top.javarem.types.exception.AppException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: rem
 * @Date: 2025/02/06/00:13
 * @Description:策略装配调度抽象类
 */
public abstract class AbstractStrategyAlgorithm implements IStrategyArmoryDispatch, IStrategyArmory {

    @Autowired
    protected IStrategyRepository repository;

    @Override
    public boolean assembleRaffleStrategyByActivityId(Long activityId) {

        Long strategyId = repository.getStrategyId(activityId);
        return assembleRaffleStrategy(strategyId);
    }

    /**
     * 装配抽奖策略
     *
     * @param strategyId 策略id
     * @return
     */
    @Override
    public boolean assembleRaffleStrategy(Long strategyId) {

//        1.查询策略配置 该策略包含的奖品信息集合
        List<StrategyAwardEntity> awardEntities = repository.getStrategyAwardList(strategyId);

        if (CollectionUtils.isEmpty(awardEntities)) return false;
//       2. 缓存每个奖品的初始总量
        for (StrategyAwardEntity awardEntity : awardEntities) {
            repository.cacheAwardCount(awardEntity.getStrategyId(), awardEntity.getAwardId(), awardEntity.getAwardCount());
        }

        armoryAlgorithm(strategyId.toString(), awardEntities);
//        获取规则权重
        RuleEntity ruleEntity = repository.getRuleEntity(strategyId, Constants.LogicModel.RULE_WEIGHT.getCode(), null);
        if (ruleEntity != null) {
//        解析权重
            Map<String, List<Integer>> ruleWeightValues = ruleEntity.getRuleWeightValues();
            if (CollectionUtils.isEmpty(ruleWeightValues)) {
                throw new AppException(ResponseCode.RULE_WEIGHT_NULL.getInfo());
            }
            double score = 1.0;
            for (String ruleWeightKey : ruleWeightValues.keySet()) {
//            复用之前通过策略id获取的awardEntities 避免了再一次从数据库中获取
                List<StrategyAwardEntity> strategyAwardEntityCloneList = new ArrayList<>(awardEntities);
                List<Integer> keys = ruleWeightValues.get(ruleWeightKey);
//            使用集合的removeIf() 如果awardEntities的awardId不包含在权重配置中则移除
                strategyAwardEntityCloneList.removeIf(strategyAwardEntity -> !keys.contains(strategyAwardEntity.getAwardId()));
                armoryAlgorithm(strategyId.toString().concat("_").concat(ruleWeightKey), strategyAwardEntityCloneList);
//            将权重的键 4000 5000 6000 存入redis
                repository.storeRuleWeightKey(score, Long.parseLong(ruleWeightKey));
                score = score + 1.0;
            }
        }


        return true;
    }


    @Override
    public Boolean deductAwardCount(Long strategyId, Integer awardId, Date endTime) {

//        扣减库存
        String key = Constants.RedisKey.STRATEGY_AWARD_COUNT_KEY + strategyId + Constants.UNDERLINE + awardId;
        return repository.decrAwardCount(key, endTime);
    }

    @Override
    public Integer getRandomAwardId(Long strategyId, String ruleWeightKey) {
//        先获取该策略下的奖品数量范围
        String key = strategyId.toString().concat("_").concat(ruleWeightKey);
        return dispatchAlgorithm(key);
    }

    /**
     * 获取最小概率
     *
     * @param awardEntities
     * @return
     */
    protected BigDecimal minAwardRate(List<StrategyAwardEntity> awardEntities) {
        return awardEntities.stream()
                .map(StrategyAwardEntity::getRate)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }

    protected abstract Integer dispatchAlgorithm(String key);

    /**
     * 随机获取奖品id
     *
     * @param strategyId 策略id
     * @return awardId
     */
    @Override
    public Integer getRandomAwardId(Long strategyId) {
//        通过奖品数量范围生成随机数从奖品随即表中随机获取
        return dispatchAlgorithm(String.valueOf(strategyId));
    }

    protected double convert(double min) {

//        1.参数校验 如果最小概率为0 则返回1
        if (min == 0) return 1d;
//        2.去除小数点后无意义的0 如0.21000 -> 0.21  0.10200 -> 0.102
        BigDecimal decimal = BigDecimal.valueOf(min).stripTrailingZeros();
//        3.获取小数点后的位数
        int scale = decimal.scale();
//        4.做10的幂运算
        return Math.pow(10, scale);

    }

    /**
     * 装配算法
     *
     * @param key                   为策略ID、权重ID
     * @param strategyAwardEntities 对应的奖品概率
     */
    protected abstract void armoryAlgorithm(String key, List<StrategyAwardEntity> strategyAwardEntities);

}
