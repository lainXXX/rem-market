package top.javarem.domain.strategy.service;

import java.util.Map;

/**
 * @Author: rem
 * @Date: 2024/12/14/14:52
 * @Description:抽奖规则接口
 */
public interface IRaffleRule {

    Map<String, Integer> getAwardUnlockCountMap(String[] treeIds);

}
