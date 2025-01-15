package top.javarem.domain.strategy.service;

import top.javarem.domain.strategy.model.vo.RuleWeightVO;

import java.util.List;
import java.util.Map;

/**
 * @Author: rem
 * @Date: 2024/12/14/14:52
 * @Description:抽奖规则接口
 */
public interface IRaffleRule {

    Map<String, Integer> getAwardUnlockCountMap(String[] treeIds);

    /**
     * 通过活动ID查询规则权重
     * @param activityId
     * @return
     */
    List<RuleWeightVO> queryRuleWeightByActivityId(Long activityId);

    /**
     * 通过策略ID查询权重
     * @param strategyId
     * @return
     */
    List<RuleWeightVO> queryRuleWeight(Long strategyId);

}
