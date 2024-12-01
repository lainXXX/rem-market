package top.javarem.domain.strategy.service;

import top.javarem.domain.strategy.model.entity.StrategyAwardEntity;

import java.util.List;

/**
 * @Author: rem
 * @Date: 2024/12/01/13:45
 * @Description: 抽奖奖品接口
 */
public interface IRaffleAward {

    List<StrategyAwardEntity> displayAward(Long strategyId);

}
