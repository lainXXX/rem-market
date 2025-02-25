package top.javarem.domain.strategy.service.armory.algorithm.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.javarem.domain.strategy.model.entity.StrategyAwardEntity;
import top.javarem.domain.strategy.service.armory.algorithm.AbstractAlgorithm;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * @Author: rem
 * @Date: 2025/02/03/21:49
 * @Description:ologN复杂度算法
 */
@Slf4j
@Component("oLogNAlgorithm")
public class OLogNAlgorithm extends AbstractAlgorithm {

    private ThreadPoolExecutor threadPoolExecutor;

    @Override
    public void armoryAlgorithm(String key, List<StrategyAwardEntity> strategyAwardEntities, BigDecimal rateRange) {
        log.info("抽奖算法 OLog(n) 装配 key:{}", key);
        int from = 1;
        int to = 0;
        Map<Map<Integer, Integer>, Integer> table = new HashMap<>();
        for (StrategyAwardEntity strategyAwardEntity : strategyAwardEntities) {

            Integer awardId = strategyAwardEntity.getAwardId();
            BigDecimal rate = strategyAwardEntity.getRate();
            to += rateRange.multiply(rate).intValue();
            Map<Integer, Integer> awardRange = new HashMap<>();
            awardRange.put(from, to);
            table.put(awardRange, awardId);
            from = to + 1;
        }

        repository.storeShuffleAwardTable(key, to, table);

    }

    @Override
    public Integer dispatchAlgorithm(String key) {

        Integer awardRange = repository.getAwardRange(key);
        Map<Map<String, Integer>, Integer> table = repository.getAwardMap(key);
        // 小于等于8 for循环、小于等于16 二分查找、更多检索走多线程
        if (table.size() <= 8) {
            log.info("抽奖算法 OLog(n) 抽奖计算（循环） key:{}", key);
            return forSearch(secureRandom.nextInt(awardRange), table);
        } else if (table.size() <= 16) {
            log.info("抽奖算法 OLog(n) 抽奖计算（二分） key:{}", key);
            return binarySearch(secureRandom.nextInt(awardRange), table);
        } else {
            log.info("抽奖算法 OLog(n) 抽奖计算（多线程） key:{}", key);
            return threadSearch(secureRandom.nextInt(awardRange), table);
        }

    }

    private Integer threadSearch(int rateKey, Map<Map<String, Integer>, Integer> table) {

        List<CompletableFuture<Map.Entry<Map<String, Integer>, Integer>>> futures = table.entrySet().stream()
                .map(entry -> CompletableFuture.supplyAsync(() -> {
                    Map<String, Integer> rangeMap = entry.getKey();
                    for (Map.Entry<String, Integer> rangeEntry : rangeMap.entrySet()) {

                        int start = Integer.parseInt(rangeEntry.getKey());
                        int end = rangeEntry.getValue();
                        if (rateKey >= start && rateKey <= end) {
                            return entry;
                        }

                    }
                    return null;
                }, threadPoolExecutor))
                .collect(Collectors.toList());

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        try {
            // 等待所有异步任务完成，同时返回第一个匹配的结果
            allFutures.join();
            for (CompletableFuture<Map.Entry<Map<String, Integer>, Integer>> future : futures) {
                Map.Entry<Map<String, Integer>, Integer> result = future.getNow(null);
                if (result != null) {
                    return result.getValue();
                }
            }
        } catch (CompletionException e) {
            e.printStackTrace();
        }

        return null;

    }

    private Integer binarySearch(int rateKey, Map<Map<String, Integer>, Integer> table) {
//        1.将map集合转化为list
        List<Map.Entry<Map<String, Integer>, Integer>> entries = new ArrayList<>(table.entrySet());
//        2.按集合元素中的key的map集合中的第一个元素大小进行有序排序
        entries.sort(Comparator.comparingInt(e -> Integer.parseInt(e.getKey().keySet().iterator().next())));
//        3.1 进行二分查找 定义查找的左右初始值
        int left = 0;
        int right = entries.size() - 1;
        while (left <= right) {
//            3.2 取索引中间值进行比较
            int mid = left + (right - left) / 2;
//            3.3 获取mid对应的元素
            Map.Entry<Map<String, Integer>, Integer> entry = entries.get(mid);
//            获取索引中间值的map集合中的key的map集合的第一个元素的值 即为奖品区间范围
            Map.Entry<String, Integer> range = entry.getKey().entrySet().iterator().next();
            int start = Integer.parseInt(range.getKey());
            int end = range.getValue();
//            3.4 看rateKey(生成的随机数)是否在该奖品区间范围内
            if (rateKey < start) {
                right = mid - 1;
            } else if (rateKey > end) {
                left = mid + 1;
            } else {
                return entry.getValue();
            }

        }

        return null;

    }

    private Integer forSearch(int rateKey, Map<Map<String, Integer>, Integer> table) {
        Integer awardId = null;
        for (Map.Entry<Map<String, Integer>, Integer> entry : table.entrySet()) {

            Map<String, Integer> awardRange = entry.getKey();
            for (Map.Entry<String, Integer> range : awardRange.entrySet()) {
                int start = Integer.parseInt(range.getKey());
                int end = range.getValue();
                if (rateKey >= start && rateKey <= end) {
                    awardId = entry.getValue();
                    break;
                }
            }

            if (awardId != null) {
                break;
            }

        }
        return awardId;
    }
}
