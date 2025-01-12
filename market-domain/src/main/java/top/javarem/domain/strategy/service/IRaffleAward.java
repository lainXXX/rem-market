package top.javarem.domain.strategy.service;

import top.javarem.domain.strategy.model.entity.StrategyAwardEntity;

import java.util.List;

/**
 * @Author: rem
 * @Date: 2024/12/01/13:45
 * @Description: 抽奖奖品接口
 */
public interface IRaffleAward {

    /**
     * 展示奖品接口
     * @param strategyId 策略id
     * @return 策略奖品集合
     */
    List<StrategyAwardEntity> displayAward(Long strategyId);

    /**
     * 展示奖品接口 通过活动id
     * @param activityId 活动id
     * @return 策略奖品集合
     */
    List<StrategyAwardEntity> displayAwardByActivityId(Long activityId);
}
