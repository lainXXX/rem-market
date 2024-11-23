package top.javarem.infrastructure.adapter.repository;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import top.javarem.domain.strategy.model.entity.StrategyAwardEntity;
import top.javarem.domain.strategy.model.entity.RuleEntity;
import top.javarem.domain.strategy.model.entity.StrategyEntity;
import top.javarem.domain.strategy.repository.IStrategyRepository;
import top.javarem.infrastructure.dao.Iservice.*;
import top.javarem.infrastructure.dao.entity.User;
import top.javarem.infrastructure.dao.entity.Rule;
import top.javarem.infrastructure.dao.entity.Strategy;
import top.javarem.infrastructure.dao.entity.StrategyAward;
import top.javarem.types.common.constants.Constants;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class StrategyRepository implements IStrategyRepository {

    private final RedissonClient redissonClient;

    private final IStrategyAwardService strategyAwardService;

    private final IRuleService ruleService;

    private final IUserService userService;

    private final IStrategyService strategyService;

    public StrategyRepository(RedissonClient redissonClient, IStrategyAwardService strategyAwardService, IRuleService ruleService, IUserService userService, IStrategyService strategyService) {
        this.redissonClient = redissonClient;
        this.strategyAwardService = strategyAwardService;
        this.ruleService = ruleService;
        this.userService = userService;
        this.strategyService = strategyService;
    }

    /**
     * 获取策略下的奖品集合
     *
     * @param strategyId
     * @return
     */
    @Override
    public List<StrategyAwardEntity> getStrategyAwardList(Long strategyId) {
//        1.先在redis中查询是否存在awardEntities 存在则直接返回
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_KEY + strategyId;
        List<StrategyAwardEntity> awardEntities = (List<StrategyAwardEntity>) redissonClient.getBucket(cacheKey).get();
        if (!CollectionUtils.isEmpty(awardEntities)) return awardEntities;
//        2.如果不存在缓存 从数据库中取出Award 再通过属性复制转为AwardEntity
        awardEntities = strategyAwardService.lambdaQuery().select(StrategyAward::getAwardId, StrategyAward::getStrategyId, StrategyAward::getAwardCount, StrategyAward::getAwardCountSurplus, StrategyAward::getRate)
                .eq(StrategyAward::getStrategyId, strategyId)
                .list()
                .stream()
                .map(strategyAward -> {
                    StrategyAwardEntity strategyAwardEntity = new StrategyAwardEntity();
                    BeanUtils.copyProperties(strategyAward, strategyAwardEntity);
                    return strategyAwardEntity;
                }).collect(Collectors.toList());
//        3.存入缓存
        redissonClient.getBucket(cacheKey).set(awardEntities);
        return awardEntities;
    }

    /**
     * 储存奖品随机表
     *
     * @param key
     * @param range
     * @param ShuffleAwardTable
     */
    @Override
    public void storeShuffleAwardTable(String key, int range, Map<Integer, Integer> ShuffleAwardTable) {
//        将策略id储存 还有该策略的抽奖奖品数量范围
        redissonClient.getBucket(Constants.RedisKey.AWARD_RANGE_KEY + key).set(range);
//        储存该策略下的奖品搜索表
        redissonClient.getMap(Constants.RedisKey.AWARD_TABLE_KEY + key).putAll(ShuffleAwardTable);
    }

    /**
     * @param strategyId
     * @return
     */
    @Override
    public int getAwardRange(Long strategyId) {
        return getAwardRange(strategyId.toString());
    }

    @Override
    public int getAwardRange(String key) {
        return redissonClient.<Integer>getBucket(Constants.RedisKey.AWARD_RANGE_KEY + key).get();
    }

    @Override
    public Integer getRandomAwardId(String key, int rangeKey) {

        return (Integer) redissonClient.getMap(Constants.RedisKey.AWARD_TABLE_KEY + key).get(rangeKey);
    }

    @Override
    public RuleEntity getRuleEntity(Long strategyId) {

        RuleEntity ruleEntity = new RuleEntity();
        Rule rule = ruleService.lambdaQuery()
                .select(Rule::getRuleModel, Rule::getRuleValue)
                .eq(Rule::getStrategyId, strategyId)
                .eq(Rule::getRuleModel, "rule_weight")
                .one();
        BeanUtils.copyProperties(rule, ruleEntity);
        return ruleEntity;

    }

    @Override
    public String getBlacklistEntity(String userId) {
//        因为使用.one()如果没有查询到会报错空指针异常
        try {
            return userService.lambdaQuery()
                    .select(User::getRuleModel)
                    .eq(User::getUserId, userId)
                    .one()
                    .getRuleModel();
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * 储存权重值
     * @param score 排序分数
     * @param ruleWeightKey 权重值
     */
    @Override
    public void storeRuleWeightKey(double score, String ruleWeightKey) {
//        将权重键存入有序集合中
        redissonClient.<Long>getScoredSortedSet(Constants.RULE_WEIGHT_KEY).add(score, Long.parseLong(ruleWeightKey));
    }

    /**
     * 获取满足条件的最小的权重
     *
     * @param userScore
     * @return 最小权重
     */
    public String getMinMatchScore(Long userScore) {
//        redis的ZSet默认是按分数升序排序 所以第一个元素是最小的 使用filter来找出满足条件的第一个元素返回 如果没有则返回null
        RScoredSortedSet<Long> ZSet = redissonClient.getScoredSortedSet(Constants.RULE_WEIGHT_KEY);
        if (ZSet == null || ZSet.isEmpty()) return null;
        Long MinMatchScore = ZSet
                .stream()
                .filter(key -> key >= userScore)
                .findFirst()
                .orElse(null);
        return MinMatchScore == null ? null : String.valueOf(MinMatchScore);
    }

    /**
     * 获取策略实体
     * @param strategyId 策略id
     * @return 策略实体
     */
    @Override
    public StrategyEntity getStrategyEntity(Long strategyId) {

        StrategyEntity strategyEntity = redissonClient.<StrategyEntity>getBucket(Constants.RedisKey.STRATEGY_KEY + strategyId).get();
        if (strategyEntity != null) return strategyEntity;
        try {
            Strategy strategy = strategyService.lambdaQuery()
                    .eq(Strategy::getStrategyId, strategyId)
                    .one();
            strategyEntity = new StrategyEntity();
            BeanUtils.copyProperties(strategy, strategyEntity);
            redissonClient.<StrategyEntity>getBucket(Constants.RedisKey.STRATEGY_KEY + strategyId).set(strategyEntity);
            return strategyEntity;
        } catch (NullPointerException e) {
            log.info(e.getMessage());
            return null;
        }
    }

}
