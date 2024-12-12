package top.javarem.domain.strategy.service.armory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import top.javarem.domain.strategy.model.entity.StrategyAwardEntity;
import top.javarem.domain.strategy.model.entity.RuleEntity;
import top.javarem.domain.strategy.repository.IStrategyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.javarem.types.common.constants.Constants;
import top.javarem.types.enums.ResponseCode;
import top.javarem.types.exception.AppException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.*;

@Service
@Slf4j
public class StrategyArmoryImpl implements IStrategyArmory, IStrategyArmoryDispatch {

    @Autowired
    private IStrategyRepository repository;

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
//        缓存每个奖品的初始总量
        awardEntities.stream()
                .forEach(award -> {
                    repository.cacheAwardCount(award.getStrategyId(), award.getAwardId(), award.getAwardCount());
                });
        assembleLotteryStrategy(strategyId.toString(), awardEntities);
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
                assembleLotteryStrategy(strategyId.toString().concat("_").concat(ruleWeightKey), strategyAwardEntityCloneList);
//            将权重的键 4000 5000 6000 存入redis
                repository.storeRuleWeightKey(score, Long.parseLong(ruleWeightKey));
                score = score + 1.0;
            }
        }


        return true;
    }

    @Override
    public boolean assembleRaffleStrategyByActivityId(Long activityId) {

        Long strategyId = repository.getStrategyId(activityId);
        return assembleRaffleStrategy(strategyId);
    }

    /**
     * 随机获取奖品id
     *
     * @param strategyId 策略id
     * @return awardId
     */
    @Override
    public Integer getRandomAwardId(Long strategyId) {
//        先获取该策略下的奖品数量范围
        int awardRange = repository.getAwardRange(strategyId);
//        通过奖品数量范围生成随机数从奖品随即表中随机获取
        return repository.getRandomAwardId(strategyId.toString(), new SecureRandom().nextInt(awardRange));
    }

    @Override
    public Integer getRandomAwardId(Long strategyId, String ruleWeightKey) {
//        先获取该策略下的奖品数量范围
        String key = strategyId.toString().concat("_").concat(ruleWeightKey);
        Integer awardRange = repository.getAwardRange(key);
//        如果策略没有装配 则直接返回 由方法调用者输出错误信息
        if (awardRange == null) return null;
//        通过奖品数量范围生成随机数从奖品随即表中随机获取
        return repository.getRandomAwardId(key, new SecureRandom().nextInt(awardRange));
    }

    @Override
    public Boolean deductAwardCount(Long strategyId, Integer awardId) {
//        扣减库存
        String key = Constants.RedisKey.STRATEGY_AWARD_COUNT_KEY + strategyId + Constants.UNDERLINE + awardId;
        return repository.decrAwardCount(key);
    }

    private boolean assembleLotteryStrategy(String key, List<StrategyAwardEntity> awardEntities) {
        //        2.获取奖品中最小概率
        BigDecimal minAwardRate = awardEntities.stream()
                .map(StrategyAwardEntity::getRate)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

//        3.获取该策略下的概率总和
        BigDecimal totalAwardRate = awardEntities.stream()
                .map(StrategyAwardEntity::getRate)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

//        4.获取奖品范围
        int awardRange = totalAwardRate.divide(minAwardRate, 0, RoundingMode.CEILING).intValue();
//        确保最小范围为100 防止概率不稳定
        if (awardRange < Constants.AWARD_MIN_RANGE) awardRange = Constants.AWARD_MIN_RANGE;
//        5.生成概率查找表 和 每个奖品的占位数量表
        List<Integer> awardRateSearchTable = new ArrayList<>(awardRange + 20);
        ArrayList<Integer> awardsCount = new ArrayList<>(awardEntities.size());
        BigDecimal maxRate = BigDecimal.ZERO; // 初始化最大概率
        Integer maxRateAwardId = null; // 用来存储最大 rate 对应的 awardId
        for (StrategyAwardEntity strategyAwardEntity : awardEntities) {
            Integer awardId = strategyAwardEntity.getAwardId();
            BigDecimal rate = strategyAwardEntity.getRate();
//            计算每个奖品的占位数量 并存入占位表 奖品概率/策略奖品总概率 * 奖品范围 由于是向上取整(保证某些概率极小的奖品最少能有一个) 所以必然有awardRateSearchTable.size() - awardRange.intValue() >= 0
            int count = rate.divide(totalAwardRate, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(awardRange))
                    .setScale(0, RoundingMode.CEILING)
                    .intValue();
            awardsCount.add(count);
            for (int i = 0; i < count; i++) {
                awardRateSearchTable.add(awardId);
            }
            // 更新最大 rate 和对应的 awardId 获取最大rate对应的id 方便后续修正查找表
            if (rate.compareTo(maxRate) > 0) {
                maxRate = rate;
                maxRateAwardId = awardId;
            }
        }
//        计算查找表和概率范围的误差 并修正保持两者一致 在概率最大的奖品上修正查找表的误差
        int deviation = awardRateSearchTable.size() - awardRange;
        for (int i = 0; i < deviation; i++) {
            awardRateSearchTable.remove(maxRateAwardId);
        }
//        6.对概率查找表中的数据进行乱序
        Collections.shuffle(awardRateSearchTable);
        // 7. 生成出Map集合，key值，对应的就是后续的概率值。通过概率来获得对应的奖品ID
        Map<Integer, Integer> shuffleAwardSearchRateTable = new LinkedHashMap<>();
        for (int i = 0; i < awardRateSearchTable.size(); i++) {
            shuffleAwardSearchRateTable.put(i, awardRateSearchTable.get(i));
        }
        boolean b = shuffleAwardSearchRateTable.containsValue(107);
        // 8. 存放到 Redis
        repository.storeShuffleAwardTable(key, shuffleAwardSearchRateTable.size(), shuffleAwardSearchRateTable);

        return true;
    }
}
