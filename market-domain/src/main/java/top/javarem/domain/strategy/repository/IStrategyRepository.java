package top.javarem.domain.strategy.repository;


import top.javarem.domain.strategy.model.entity.StrategyAwardEntity;
import top.javarem.domain.strategy.model.entity.RuleEntity;

import java.util.List;
import java.util.Map;

public interface IStrategyRepository {

    List<StrategyAwardEntity> getStrategyAwardList(Long strategyId);

    void storeShuffleAwardTable(String key, int range, Map<Integer, Integer> ShuffleAwardTable);

    int getAwardRange(Long strategyId);

    int getAwardRange(String key);

    Integer getRandomAwardId(String key, int rangeKey);

    RuleEntity getRuleEntity(Long strategyId);

    Integer getBlacklistEntity(String userId);
}
