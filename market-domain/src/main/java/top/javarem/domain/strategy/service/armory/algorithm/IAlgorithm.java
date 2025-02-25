package top.javarem.domain.strategy.service.armory.algorithm;

import top.javarem.domain.strategy.model.entity.StrategyAwardEntity;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: rem
 * @Date: 2025/02/03/20:11
 * @Description:抽奖策略算法接口
 */
public interface IAlgorithm {

    void armoryAlgorithm(String key, List<StrategyAwardEntity> strategyAwardEntities, BigDecimal rateRange);

    Integer dispatchAlgorithm(String key);

}
