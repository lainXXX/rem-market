package top.javarem;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.javarem.infrastructure.adapter.repository.StrategyRepository;
import top.javarem.infrastructure.dao.iService.IStrategyAwardService;
import top.javarem.infrastructure.dao.entity.StrategyAward;
import top.javarem.types.common.constants.Constants;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

/**
 * @Author: rem
 * @Date: 2024/11/24/22:01
 * @Description:
 */
@SpringBootTest
@Slf4j
public class Demo {

    @Autowired
    private StrategyRepository repository;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private IStrategyAwardService strategyAwardService;

    public void test00() {
        // 使用try-with-resources语句
        try (BufferedReader reader = new BufferedReader(new FileReader("example.txt"))) {
            String line;
            // 循环读取文件中的每一行
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            // 如果在读取文件过程中发生IOException，则捕获异常
            e.printStackTrace();
        }
        // 资源自动关闭，不需要显式调用close()
    }

    @Test
    public void test02() {
        StrategyAward one = strategyAwardService.lambdaQuery()
                .eq(StrategyAward::getAwardId, 101)
                .eq(StrategyAward::getStrategyId, 100001L)
                .list().get(0);
        log.info(one.toString());
    }

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
        boolean b = repository.decrAwardCount(key, new Date());
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

    @Test
    public void test_redis() {
        redissonClient.getAtomicLong("123456").set(1L);
    }

}
