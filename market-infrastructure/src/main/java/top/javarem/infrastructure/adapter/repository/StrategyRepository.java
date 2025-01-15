package top.javarem.infrastructure.adapter.repository;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import top.javarem.domain.activity.repository.IActivityRepository;
import top.javarem.domain.activity.service.IRaffleActivityAccountQuotaService;
import top.javarem.domain.strategy.model.entity.StrategyAwardEntity;
import top.javarem.domain.strategy.model.entity.RuleEntity;
import top.javarem.domain.strategy.model.entity.StrategyEntity;
import top.javarem.domain.strategy.model.vo.*;
import top.javarem.domain.strategy.repository.IStrategyRepository;
import top.javarem.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import top.javarem.infrastructure.dao.iService.*;
import top.javarem.infrastructure.dao.entity.*;
import top.javarem.types.common.constants.Constants;
import top.javarem.types.exception.AppException;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static top.javarem.types.enums.ResponseCode.UN_ASSEMBLED_STRATEGY_ARMORY;

@Repository
@Slf4j
public class StrategyRepository implements IStrategyRepository {

    private final RedissonClient redissonClient;

    private final IStrategyAwardService strategyAwardService;

    private final IActivityRepository activityRepository;

    private final RaffleActivityAccountService raffleActivityAccountService;

    private final IRuleService ruleService;

    private final IUserService userService;

    private final IStrategyService strategyService;

    private final IRuleTreeService ruleTreeService;

    private final IRuleTreeNodeService ruleTreeNodeService;

    private final IRuleTreeNodeLineService ruleTreeNodeLineService;

    private final RaffleActivityService raffleActivityService;

    private final RaffleActivityAccountDayCountService raffleActivityAccountDayCountService;

    public StrategyRepository(RedissonClient redissonClient, IStrategyAwardService strategyAwardService, IActivityRepository activityRepository, IRaffleActivityAccountQuotaService affleActivityAccountQuotaService, IRaffleActivityAccountQuotaService raffleActivityAccountQuotaService, RaffleActivityAccountService raffleActivityAccountService, IRuleService ruleService, IUserService userService, IStrategyService strategyService, IRuleTreeService ruleTreeService, IRuleTreeNodeService ruleTreeNodeService, IRuleTreeNodeLineService ruleTreeNodeLineService, RaffleActivityService raffleActivityService, RaffleActivityAccountDayCountService raffleActivityAccountDayCountService) {
        this.redissonClient = redissonClient;
        this.strategyAwardService = strategyAwardService;
        this.activityRepository = activityRepository;
        this.raffleActivityAccountService = raffleActivityAccountService;
        this.ruleService = ruleService;
        this.userService = userService;
        this.strategyService = strategyService;
        this.ruleTreeService = ruleTreeService;
        this.ruleTreeNodeService = ruleTreeNodeService;
        this.ruleTreeNodeLineService = ruleTreeNodeLineService;
        this.raffleActivityService = raffleActivityService;
        this.raffleActivityAccountDayCountService = raffleActivityAccountDayCountService;
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
        List<StrategyAwardEntity> awardEntities = redissonClient.<List<StrategyAwardEntity>>getBucket(cacheKey).get();
        if (!CollectionUtils.isEmpty(awardEntities)) return awardEntities;
//        2.如果不存在缓存 从数据库中取出Award 再通过属性复制转为AwardEntity
        awardEntities = strategyAwardService.lambdaQuery().select(StrategyAward::getAwardId, StrategyAward::getStrategyId, StrategyAward::getAwardTitle, StrategyAward::getAwardSubtitle, StrategyAward::getAwardCount, StrategyAward::getAwardCountSurplus, StrategyAward::getRate, StrategyAward::getModels, StrategyAward::getSort)
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
    public Integer getAwardRange(String key) {
        String cacheKey = Constants.RedisKey.AWARD_RANGE_KEY + key;
        if (!redissonClient.<Integer>getBucket(cacheKey).isExists())
            throw new AppException(UN_ASSEMBLED_STRATEGY_ARMORY.getCode(), cacheKey + Constants.COLON + UN_ASSEMBLED_STRATEGY_ARMORY.getInfo());
        Integer awardRange = redissonClient.<Integer>getBucket(cacheKey).get();
        return awardRange == null ? 0 : awardRange;
    }

    @Override
    public Integer getRandomAwardId(String key, int rangeKey) {

        return (Integer) redissonClient.getMap(Constants.RedisKey.AWARD_TABLE_KEY + key).get(rangeKey);
    }

    @Override
    public RuleEntity getRuleEntity(Long strategyId, String ruleModel, Integer awardId) {
        Rule rule = ruleService.lambdaQuery()
                .select(Rule::getRuleValue)
                .eq(Rule::getStrategyId, strategyId)
                .eq(Rule::getRuleModel, ruleModel)
                .eq(awardId != null, Rule::getAwardId, awardId)
                .one();
        if (rule == null) return null;
        RuleEntity ruleEntity = new RuleEntity();
        BeanUtils.copyProperties(rule, ruleEntity);
        return ruleEntity;
    }

    @Override
    public String getUserRuleModel(String userId) {
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
     *
     * @param score         排序分数
     * @param ruleWeightKey 权重值
     */
    @Override
    public void storeRuleWeightKey(double score, Long ruleWeightKey) {
//        将权重键存入有序集合中
        redissonClient.<Long>getScoredSortedSet(Constants.RULE_WEIGHT_KEY).add(score, ruleWeightKey);
    }

    /**
     * 获取满足条件的最小的权重
     *
     * @param strategyId
     * @param ruleModel
     * @param userScore
     * @return 最小权重
     */
    public String getMinMatchScore(Long strategyId, String ruleModel, Long userScore) {
//        redis的ZSet默认是按分数升序排序 所以第一个元素是最小的 使用filter来找出满足条件的第一个元素返回 如果没有则返回null
        RScoredSortedSet<Long> ZSet = redissonClient.getScoredSortedSet(Constants.RULE_WEIGHT_KEY);
//        如果集合不为空 则直接返回
        if (ZSet != null && !ZSet.isEmpty()) {
            return ZSet
                    .stream()
                    .filter(key -> key >= userScore)
                    .findFirst()
                    .map(String::valueOf)
                    .orElse(null);
        }
//        集合为空 从数据库中获取 并重新存入redis
        String ruleValue = this.getRuleEntity(strategyId, ruleModel, null).getRuleValue();
        double scoreKey = 1.0;
        List<Long> weightKeys = this.getWeightKeys(ruleValue);
        for (Long weightKey : weightKeys) {
            this.storeRuleWeightKey(scoreKey, weightKey);
            scoreKey += 1.0;
        }
        return weightKeys
                .stream()
                .filter(key -> key >= userScore)
                .findFirst()
                .map(String::valueOf)
                .orElse(null);
    }

    /**
     * 获取策略实体
     *
     * @param strategyId 策略id
     * @return 策略实体
     */
    @Override
    public StrategyEntity getStrategyEntity(Long strategyId) {

        StrategyEntity strategyEntity = redissonClient.<StrategyEntity>getBucket(Constants.RedisKey.STRATEGY_KEY + strategyId).get();
        if (strategyEntity != null) return strategyEntity;
        try {
            Strategy strategy = strategyService.lambdaQuery()
                    .select(Strategy::getStrategyId, Strategy::getRuleModels)
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

    @Override
    public StrategyAwardRuleModelsVO getAwardRules(Long strategyId, Integer awardId) {
        try {
            String models = strategyAwardService.lambdaQuery()
                    .select(StrategyAward::getModels)
                    .eq(StrategyAward::getStrategyId, strategyId)
                    .eq(StrategyAward::getAwardId, awardId)
                    .one()
                    .getModels();
            return StrategyAwardRuleModelsVO.builder()
                    .ruleModels(models)
                    .build();
        } catch (NullPointerException e) {
            return null;
        }

    }

    /**
     * 获取规则树值对象
     *
     * @param treeId 规则树id
     * @return
     */
    @Override
    public RuleTreeVO getRuleTreeVO(String treeId) {
        String cacheKey = Constants.RedisKey.RULE_TREE_KEY + treeId;
        RuleTreeVO tree = redissonClient.<RuleTreeVO>getBucket(cacheKey).get();
        if (tree != null) return tree;
//        先获取节点连接逻辑
        List<RuleTreeNodeLine> treeNodeLines = getNodeLines(treeId);
//        创建节点连接逻辑集合 格式： rule_lock {RuleTreeNodeLineVO1,RuleTreeNodeLineVO2...}
        Map<String, List<RuleTreeNodeLineVO>> lineVOMap = new HashMap<>();
        treeNodeLines.stream().forEach(line -> {
//            创建一个节点连接对象
            RuleTreeNodeLineVO nodeLineVO = RuleTreeNodeLineVO.builder()
                    .treeId(line.getTreeId())
                    .ruleNodeKey(line.getRuleNodeKey())
                    .ruleChildNode(line.getRuleChildNode())
                    .ruleLimitType(RuleLimitTypeVO.valueOf(line.getRuleLimitType()))
                    .ruleLimitValue(RuleLogicCheckTypeVO.valueOf(line.getRuleLimitValue()))
                    .build();
//            如果节点连接逻辑集合中不存在某个父节点的名称 则在map存入一个元素 格式为 父节点名称 value 如果父节点存在 不在map中新增元素 仅在list 集合中新增一个元素
            lineVOMap.computeIfAbsent(line.getRuleNodeKey(), k -> new ArrayList<>()).add(nodeLineVO);
        });
//        获取规则树节点集合
        List<RuleTreeNode> nodes = getNodes(treeId);
//        将lineVOMap对应的节点连接逻辑放入对应的节点
        Map<String, RuleTreeNodeVO> nodeVOMap = nodes.stream().collect(Collectors.toMap(node -> node.getTreeNodeKey(), node ->
                RuleTreeNodeVO.builder()
                        .treeId(node.getTreeId())
                        .treeNodeKey(node.getTreeNodeKey())
                        .ruleDesc(node.getRuleDesc())
                        .ruleValue(node.getRuleValue())
                        .treeNodeLineVOList(lineVOMap.get(node.getTreeNodeKey()))
                        .build()
        ));
//        创建规则树
        RuleTree ruleTree = getRuleTree(treeId);
//        将每个节点和节点逻辑存入规则树
        tree = RuleTreeVO.builder()
                .treeId(treeId)
                .treeName(ruleTree.getTreeName())
                .treeDesc(ruleTree.getTreeDesc())
                .treeRootRuleNode(ruleTree.getTreeRootNodeKey())
                .treeNodeMap(nodeVOMap)
                .build();
//        存入redis
        redissonClient.<RuleTreeVO>getBucket(cacheKey).set(tree);
        return tree;
    }

    @Override
    public void cacheAwardCount(Long strategyId, Integer awardId, Integer awardCount) {
//        拼接策略奖品初始总量缓存的key
        String awardCountKey = Constants.RedisKey.STRATEGY_AWARD_COUNT_KEY + strategyId + Constants.UNDERLINE + awardId;
//        如果缓存存在 则直接返回
        if (redissonClient.getBucket(awardCountKey).isExists()) return;
        redissonClient.getAtomicLong(awardCountKey).set(awardCount);
    }

    @Override
    public boolean decrAwardCount(String cacheKey, Date endTime) {
//        获取缓存
        long surplus = redissonClient.getAtomicLong(cacheKey).get();
//        判断库存是否足够
        if (surplus <= 0) {
            redissonClient.getAtomicLong(cacheKey).set(0);
            log.info("该奖品库存已耗尽");
            return false;
        }
        surplus = redissonClient.getAtomicLong(cacheKey).decrementAndGet();
        String lockKey = cacheKey + Constants.UNDERLINE + surplus;
        boolean lock = false;
        if (endTime != null) {
            long expireMillis = endTime.getTime() - System.currentTimeMillis();
            lock = redissonClient.getBucket(lockKey).trySet(Constants.AWARD_SURPLUS_LOCK, expireMillis, TimeUnit.MILLISECONDS);
        } else {
            lock = redissonClient.getBucket(lockKey).trySet(Constants.AWARD_SURPLUS_LOCK);
        }
        if (!lock) {
            log.info("策略奖品库存加锁失败 {}", lockKey);
        }
        return lock;
    }

    @Override
    public void sendAwardStockDecrQueue(AwardStockDecrQueueVO queueKeyVO) {
        String queueKey = Constants.RedisKey.AWARD_BLOCK_QUEUE_KEY;
//        RBlockingQueue是线程安全的 每一次不同线程使用同一个queueKey的调用返回的都是同一个RBlockingQueue
        RBlockingQueue<AwardStockDecrQueueVO> blockingQueue = redissonClient.getBlockingQueue(queueKey);
//        将阻塞队列加入延迟队列
        RDelayedQueue<AwardStockDecrQueueVO> delayedQueue = redissonClient.getDelayedQueue(blockingQueue);
//        3秒后自动将queueKeyVO添加到blockingQueue
        delayedQueue.offer(queueKeyVO, 3, TimeUnit.SECONDS);
    }

    @Override
    public AwardStockDecrQueueVO handleQueueValue() {
        String queueKey = Constants.RedisKey.AWARD_BLOCK_QUEUE_KEY;
        RBlockingQueue<AwardStockDecrQueueVO> blockingQueue = redissonClient.getBlockingQueue(queueKey);
        return blockingQueue.poll();
    }

    @Override
    public Boolean updateAwardStock(AwardStockDecrQueueVO queueKeyVO) {
        return strategyAwardService.lambdaUpdate()
                .setSql("award_count_surplus = award_count_surplus - 1")
                .eq(StrategyAward::getStrategyId, queueKeyVO.getStrategyId())
                .eq(StrategyAward::getAwardId, queueKeyVO.getAwardId())
                .update();

    }

    @Override
    public Boolean isEmptyStockDecrQueue() {
        String queueKey = Constants.RedisKey.AWARD_BLOCK_QUEUE_KEY;
        RBlockingQueue<AwardStockDecrQueueVO> blockingQueue = redissonClient.getBlockingQueue(queueKey);
        return CollectionUtils.isEmpty(blockingQueue);
    }

    /**
     * 通过活动id获取策略id
     *
     * @param activityId
     * @return
     */
    @Override
    public Long getStrategyId(Long activityId) {
        return raffleActivityService.lambdaQuery()
                .select(RaffleActivity::getStrategyId)
                .eq(RaffleActivity::getActivityId, activityId)
                .one()
                .getStrategyId();
    }

    @Override
    public StrategyAwardEntity getStrategyAwardEntity(Long strategyId, Integer awardId) {
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_KEY + strategyId + Constants.UNDERLINE + awardId;
        StrategyAwardEntity strategyAwardEntity = redissonClient.<StrategyAwardEntity>getBucket(cacheKey).get();
        if (strategyAwardEntity != null) {
            return strategyAwardEntity;
        }
        StrategyAward strategyAward = strategyAwardService.lambdaQuery()
                .select(StrategyAward::getStrategyId, StrategyAward::getAwardId, StrategyAward::getAwardTitle, StrategyAward::getAwardSubtitle, StrategyAward::getAwardCount, StrategyAward::getAwardCountSurplus, StrategyAward::getSort, StrategyAward::getRate, StrategyAward::getModels)
                .eq(StrategyAward::getStrategyId, strategyId)
                .eq(StrategyAward::getAwardId, awardId)
                .one();
        strategyAwardEntity = new StrategyAwardEntity();
        BeanUtils.copyProperties(strategyAward, strategyAwardEntity);
        redissonClient.<StrategyAwardEntity>getBucket(cacheKey).set(strategyAwardEntity);
        return strategyAwardEntity;
    }

    @Override
    public Integer queryUserTodayRaffleCount(String userId, Long strategyId, String today) {
        Long activityId = raffleActivityService.lambdaQuery()
                .select(RaffleActivity::getActivityId)
                .eq(RaffleActivity::getStrategyId, strategyId)
                .one().getActivityId();
        RaffleActivityAccountDayCount todayCount = raffleActivityAccountDayCountService.lambdaQuery()
                .select(RaffleActivityAccountDayCount::getDay, RaffleActivityAccountDayCount::getDayCount, RaffleActivityAccountDayCount::getDayCountSurplus)
                .eq(RaffleActivityAccountDayCount::getActivityId, activityId)
                .eq(RaffleActivityAccountDayCount::getDay, today)
                .one();
        if (todayCount == null) return 0;
        // 总次数 - 剩余的，等于今日参与的
        return todayCount.getDayCount() - todayCount.getDayCountSurplus();

    }

    @Override
    public Map<String, Integer> getAwardUnlockCountMap(String[] treeIds) {

        if (treeIds == null || treeIds.length == 0) return null;
        List<RuleTreeNode> ruleTreeNodes = ruleTreeNodeService.lambdaQuery()
                .select(RuleTreeNode::getTreeId, RuleTreeNode::getRuleValue)
                .eq(RuleTreeNode::getTreeNodeKey, "rule_lock")
                .in(RuleTreeNode::getTreeId, treeIds)
                .list();
        Map<String, Integer> map = new HashMap<>();
        for (RuleTreeNode node : ruleTreeNodes) {
            String treeId = node.getTreeId();
            Integer unlockCount = Integer.valueOf(node.getRuleValue());
            map.put(treeId, unlockCount);
        }
        return map;
    }

    @Override
    public Integer getUserScore(String userId, Long strategyId) {
        Long activityId = activityRepository.getActivityIdByStrategyId(strategyId);
        RaffleActivityAccount raffleActivityAccount = raffleActivityAccountService.lambdaQuery()
                .select(RaffleActivityAccount::getTotalCount, RaffleActivityAccount::getTotalCountSurplus)
                .eq(RaffleActivityAccount::getActivityId, activityId)
                .eq(RaffleActivityAccount::getUserId, userId)
                .one();
        return (raffleActivityAccount.getTotalCount() - raffleActivityAccount.getTotalCountSurplus());

    }

    @Override
    public List<RuleWeightVO> queryRuleWeight(Long strategyId) {

//        优先从缓存获取
        String cacheKey = Constants.RedisKey.STRATEGY_RULE_WEIGHT_KEY + strategyId;
        RBucket<List<RuleWeightVO>> bucket = redissonClient.<List<RuleWeightVO>>getBucket(cacheKey);
        List<RuleWeightVO> ruleWeightVOS = bucket.get();
        if (!CollectionUtils.isEmpty(ruleWeightVOS)) return ruleWeightVOS;
        ruleWeightVOS = new ArrayList<>();
//        1.查询权重规则配置
        String ruleValue = ruleService.lambdaQuery()
                .select(Rule::getRuleValue)
                .eq(Rule::getStrategyId, strategyId)
                .eq(Rule::getRuleModel, DefaultChainFactory.LogicModel.WEIGHT.getCode())
                .one().getRuleValue();
//        2.借助实体对象转换规则
        RuleEntity ruleEntity = new RuleEntity();
        ruleEntity.setRuleValue(ruleValue);
        Map<String, List<Integer>> ruleWeightValues = ruleEntity.getRuleWeightValues();
//        3.查询出策略的全部奖品 再分别装入对应的权重规则中
        List<StrategyAward> strategyAwards = strategyAwardService.getAwardListByStrategyId(strategyId);
//        转化为Map集合
        Map<Integer, StrategyAward> strategyAwardMap = strategyAwards
                .stream()
                .collect(Collectors.toMap(StrategyAward::getAwardId, award -> award));
//        便利valueKey 每个权重对应的奖品集合分别装入
        Set<String> ruleValueKeys = ruleWeightValues.keySet();
        for (String weight : ruleValueKeys) {
            List<Integer> ruleValues = ruleWeightValues.get(weight);
//            每个权重都对应多个奖品 所以需要把多个奖品装入权重
            List<RuleWeightVO.Award> awards = new ArrayList<>();
            for (Integer key : ruleValues) {
                StrategyAward strategyAward = strategyAwardMap.get(key);
                RuleWeightVO.Award award = RuleWeightVO.Award.builder()
                        .awardTitle(strategyAward.getAwardTitle())
                        .awardId(strategyAward.getAwardId())
                        .build();
                awards.add(award);
            }
            ruleWeightVOS.add(RuleWeightVO.builder()
                    .ruleValue(ruleValue)
                    .weight(Integer.valueOf(weight))
                    .awardIds(ruleValues)
                    .awardList(awards)
                    .build()
                    );
        }
        // 设置缓存 - 实际场景中，这类数据，可以在活动下架的时候统一清空缓存。
        bucket.set(ruleWeightVOS);
        return ruleWeightVOS;
    }

    private List<RuleTreeNode> getNodes(String treeId) {
        return ruleTreeNodeService.lambdaQuery()
                .select(RuleTreeNode::getTreeId, RuleTreeNode::getTreeNodeKey, RuleTreeNode::getRuleDesc, RuleTreeNode::getRuleValue)
                .eq(RuleTreeNode::getTreeId, treeId)
                .list();
    }

    private RuleTree getRuleTree(String treeId) {
        return ruleTreeService.lambdaQuery()
                .select(RuleTree::getTreeId, RuleTree::getTreeName, RuleTree::getTreeDesc, RuleTree::getTreeRootNodeKey)
                .eq(RuleTree::getTreeId, treeId)
                .one();
    }

    private List<RuleTreeNodeLine> getNodeLines(String treeId) {
        return ruleTreeNodeLineService.lambdaQuery()
                .select(RuleTreeNodeLine::getTreeId, RuleTreeNodeLine::getRuleNodeKey, RuleTreeNodeLine::getRuleChildNode, RuleTreeNodeLine::getRuleLimitType, RuleTreeNodeLine::getRuleLimitValue)
                .eq(RuleTreeNodeLine::getTreeId, treeId)
                .list();
    }

    private List<Long> getWeightKeys(String ruleValue) {
        List<Long> weightKeys = new ArrayList<>();
        for (String ruleWeights : ruleValue.split(Constants.SPACE)) {
            weightKeys.add(Long.parseLong(ruleWeights.split(Constants.COLON)[0]));
        }
        return weightKeys;
    }

}
