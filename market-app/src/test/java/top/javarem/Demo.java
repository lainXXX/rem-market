package top.javarem;

import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.javarem.infrastructure.adapter.repository.StrategyRepository;
import top.javarem.infrastructure.dao.iService.IStrategyAwardService;
import top.javarem.infrastructure.dao.entity.StrategyAward;
import top.javarem.types.common.constants.Constants;

/**
 * @Author: rem
 * @Date: 2024/11/24/22:01
 * @Description:
 */
@SpringBootTest
public class Demo {

    @Autowired
    private StrategyRepository repository;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private IStrategyAwardService strategyAwardService;

    @Test
    public void test() {
        boolean update = strategyAwardService.lambdaUpdate()
                .setSql("award_count_surplus = award_count_surplus - 1")
                .eq(StrategyAward::getStrategyId, 100003L)
                .eq(StrategyAward::getAwardId, 102)
                .update();
        System.out.println(update);
    }

    @Test
    public void testAtomicLong() {
        String key = Constants.RedisKey.STRATEGY_AWARD_COUNT_KEY + 100001 + Constants.UNDERLINE + 101;
        boolean b = repository.decrAwardCount(key);
        System.out.println(b);
    }

    @Test
    public void testRedisson() {
        String key = Constants.RedisKey.STRATEGY_AWARD_COUNT_KEY + 100001 + Constants.UNDERLINE + 102;
        redissonClient.getAtomicLong(key).set(10000);
//        redissonClient.getBucket();
    }

    @Test
    public void testRedisson02() {
        String key = Constants.RedisKey.STRATEGY_AWARD_COUNT_KEY + 100001 + Constants.UNDERLINE + 101;
        String lockKey = key + Constants.UNDERLINE + 100;
        boolean b = redissonClient.getBucket(key).trySet(lockKey);
        System.out.println(b);
//        redissonClient.getBucket();
    }

    @Test
    public void testRedisson03() {
        String key = Constants.RedisKey.STRATEGY_AWARD_COUNT_KEY + 100001 + Constants.UNDERLINE + 101;
//        redissonClient.getAtomicLong(key).
//        redissonClient.getBucket();
    }

}
