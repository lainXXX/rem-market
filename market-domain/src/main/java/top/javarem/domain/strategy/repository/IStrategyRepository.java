package top.javarem.domain.strategy.repository;


import top.javarem.domain.strategy.model.entity.StrategyAwardEntity;
import top.javarem.domain.strategy.model.entity.RuleEntity;
import top.javarem.domain.strategy.model.entity.StrategyEntity;
import top.javarem.domain.strategy.model.vo.AwardStockDecrQueueVO;
import top.javarem.domain.strategy.model.vo.RuleTreeVO;
import top.javarem.domain.strategy.model.vo.RuleWeightVO;
import top.javarem.domain.strategy.model.vo.StrategyAwardRuleModelsVO;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IStrategyRepository {

    List<StrategyAwardEntity> getStrategyAwardList(Long strategyId);

    <K, V> void storeShuffleAwardTable(String key, int range, Map<K, V> ShuffleAwardTable);

    int getAwardRange(Long strategyId);

    Integer getAwardRange(String key);

    <K, V> Map<K, V> getAwardMap(String key);

    String getUserRuleModel(String userId);

    void storeRuleWeightKey(double score, Long ruleWeightKey);

    String getMinMatchScore(Long strategyId, String ruleModel, Long userScore);

    RuleEntity getRuleEntity(Long strategyId, String ruleModel, Integer awardId);

    StrategyEntity getStrategyEntity(Long strategyId);

    StrategyAwardRuleModelsVO getAwardRules(Long strategyId, Integer awardId);

    RuleTreeVO getRuleTreeVO(String treeId);

    void cacheAwardCount(Long strategyId, Integer awardId, Integer awardCount);

    boolean decrAwardCount(String cacheKey, Date endTime);

    void sendAwardStockDecrQueue(AwardStockDecrQueueVO queueKeyVO);

    AwardStockDecrQueueVO handleQueueValue();

    Boolean updateAwardStock(AwardStockDecrQueueVO queueKeyVO);

    Boolean isEmptyStockDecrQueue();

    Long getStrategyId(Long activityId);

    StrategyAwardEntity getStrategyAwardEntity(Long strategyId, Integer awardId);

    Integer queryUserTodayRaffleCount(String userId, Long strategyId, String today);

    Map<String, Integer> getAwardUnlockCountMap(String[] treeIds);

    Integer getUserScore(String userId, Long strategyId);

    List<RuleWeightVO> queryRuleWeight(Long strategyId);

    /**
     * 存储抽奖策略对应的Bean算法
     *
     * @param key      策略ID
     * @param beanName 策略对象名称
     */
    void cacheStrategyArmoryAlgorithm(String key, String beanName);

    /**
     * 从缓存中查询策略装配算法名称
     * @param key cacheKey后缀
     * @return 策略装配算法名称
     */
    String queryStrategyArmoryAlgorithmFromCache(String key);
}
