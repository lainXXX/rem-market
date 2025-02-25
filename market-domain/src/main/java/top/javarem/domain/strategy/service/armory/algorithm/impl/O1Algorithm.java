package top.javarem.domain.strategy.service.armory.algorithm.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.javarem.domain.strategy.model.entity.StrategyAwardEntity;
import top.javarem.domain.strategy.service.armory.algorithm.AbstractAlgorithm;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.*;

/**
 * @Author: rem
 * @Date: 2025/02/03/21:35
 * @Description:o1复杂度算法
 */
@Slf4j
@Component("o1Algorithm")
public class O1Algorithm extends AbstractAlgorithm {

    @Override
    public void armoryAlgorithm(String key, List<StrategyAwardEntity> strategyEntities, BigDecimal rateRange) {
//        1.生成概率查找表 和 每个奖品的占位数量表
        List<Integer> awardRateSearchTable = new ArrayList<>(rateRange.intValue());
        ArrayList<Integer> awardsCount = new ArrayList<>(strategyEntities.size());
        for (StrategyAwardEntity strategyAwardEntity : strategyEntities) {
            Integer awardId = strategyAwardEntity.getAwardId();
            BigDecimal rate = strategyAwardEntity.getRate();
//            2.计算每个奖品的占位数量 并存入占位表 奖品概率 * 奖品总数量
            int count = rate.multiply(rateRange).intValue();
            awardsCount.add(count);
            for (int i = 0; i < count; i++) {
                awardRateSearchTable.add(awardId);
            }
//        3.对概率查找表中的数据进行乱序
            Collections.shuffle(awardRateSearchTable);
//      4. 生成出Map集合，key值，对应的就是后续的概率值。通过概率来获得对应的奖品ID
            Map<Integer, Integer> shuffleAwardSearchRateTable = new LinkedHashMap<>();
            for (int i = 0; i < awardRateSearchTable.size(); i++) {
                shuffleAwardSearchRateTable.put(i, awardRateSearchTable.get(i));
            }
//     5. 存放到 Redis
            repository.storeShuffleAwardTable(key, shuffleAwardSearchRateTable.size(), shuffleAwardSearchRateTable);

        }
    }

    @Override
    public Integer dispatchAlgorithm(String key) {
        log.info("抽奖算法 O(1) 抽奖计算 key:{}", key);
//        1.先获取该策略下的奖品数量范围
        int awardRange = repository.getAwardRange(key);
//        2.通过奖品数量范围生成随机数从奖品随即表中随机获取
        Map<Integer, Integer> table = repository.getAwardMap(key);
        return table.get(secureRandom.nextInt(awardRange));
    }
}
