package top.javarem.domain.strategy.repository;


import top.javarem.domain.strategy.model.entity.StrategyAwardEntity;
import top.javarem.domain.strategy.model.entity.RuleEntity;
import top.javarem.domain.strategy.model.entity.StrategyEntity;
import top.javarem.domain.strategy.model.vo.AwardStockDecrQueueVO;
import top.javarem.domain.strategy.model.vo.RuleTreeVO;
import top.javarem.domain.strategy.model.vo.StrategyAwardRuleModelsVO;

import java.util.List;
import java.util.Map;

public interface IStrategyRepository {

    List<StrategyAwardEntity> getStrategyAwardList(Long strategyId);

    void storeShuffleAwardTable(String key, int range, Map<Integer, Integer> ShuffleAwardTable);

    int getAwardRange(Long strategyId);

    Integer getAwardRange(String key);

    Integer getRandomAwardId(String key, int rangeKey);

    String getUserRuleModel(String userId);

    void storeRuleWeightKey(double score, Long ruleWeightKey);

    String getMinMatchScore(Long strategyId, String ruleModel, Long userScore);

    RuleEntity getRuleEntity(Long strategyId, String ruleModel, Integer awardId);

    StrategyEntity getStrategyEntity(Long strategyId);

    StrategyAwardRuleModelsVO getAwardRules(Long strategyId, Integer awardId);

    RuleTreeVO getRuleTreeVO(String treeId);

    void cacheAwardCount(Long strategyId, Integer awardId, Integer awardCount);

    boolean decrAwardCount(String cacheKey);

    void sendAwardStockDecrQueue(AwardStockDecrQueueVO queueKeyVO);

    AwardStockDecrQueueVO handleQueueValue();

    Boolean updateAwardStock(AwardStockDecrQueueVO queueKeyVO);

    Boolean isEmptyStockDecrQueue();

}
