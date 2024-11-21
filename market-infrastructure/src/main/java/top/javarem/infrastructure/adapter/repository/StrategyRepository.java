package top.javarem.infrastructure.adapter.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import top.javarem.domain.strategy.model.entity.StrategyAwardEntity;
import top.javarem.domain.strategy.model.entity.RuleEntity;
import top.javarem.domain.strategy.repository.IStrategyRepository;
import top.javarem.infrastructure.dao.Iservice.IBlacklistService;
import top.javarem.infrastructure.dao.Iservice.IRuleService;
import top.javarem.infrastructure.dao.entity.Blacklist;
import top.javarem.infrastructure.dao.entity.Rule;
import top.javarem.infrastructure.dao.mapper.AwardMapper;
import top.javarem.infrastructure.dao.Iservice.IAwardService;
import top.javarem.infrastructure.dao.entity.Award;
import top.javarem.types.common.constants.Constants;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class StrategyRepository extends ServiceImpl<AwardMapper, Award> implements IAwardService, IStrategyRepository {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private IRuleService ruleService;

    @Autowired
    private IBlacklistService blacklistService;

    /**
     * 获取策略下的奖品集合
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
        awardEntities = this.lambdaQuery().select(Award::getAwardId, Award::getStrategyId, Award::getAwardCount, Award::getAwardCountSurplus, Award::getRate)
                .eq(Award::getStrategyId, strategyId)
                .list()
                .stream()
                .map(award -> {
                    StrategyAwardEntity strategyAwardEntity = new StrategyAwardEntity();
                    BeanUtils.copyProperties(award, strategyAwardEntity);
                    return strategyAwardEntity;
                }).collect(Collectors.toList());
//        3.存入缓存
        redissonClient.getBucket(cacheKey).set(awardEntities);
        return awardEntities;
    }

    /**
     * 储存奖品随机表
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
     *
     * @param strategyId
     * @return
     */
    @Override
    public int getAwardRange(Long strategyId) {
        return getAwardRange(strategyId.toString());
    }

    @Override
    public int getAwardRange(String key) {
        return (int) redissonClient.getBucket(Constants.RedisKey.AWARD_RANGE_KEY + key).get();
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
    public Integer getBlacklistEntity(String userId) {
//        因为使用.one()如果没有查询到会报错空指针异常
         try {
            return blacklistService.lambdaQuery()
                    .select(Blacklist::getAwardId)
                    .eq(Blacklist::getUserId, userId)
                    .one()
                    .getAwardId();
        } catch (NullPointerException e) {
             return null;
         }
    }
}
