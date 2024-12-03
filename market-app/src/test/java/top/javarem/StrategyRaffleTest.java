package top.javarem;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import top.javarem.domain.strategy.model.entity.RaffleAwardEntity;
import top.javarem.domain.strategy.model.entity.RaffleFactorEntity;
import top.javarem.domain.strategy.service.IRaffleStrategy;
import top.javarem.domain.strategy.service.armory.IStrategyArmory;
import top.javarem.domain.strategy.service.rule.chain.IStrategyLogicChain;
import top.javarem.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import top.javarem.domain.strategy.service.rule.chain.impl.RuleWeightLogicNode;
import top.javarem.domain.strategy.service.rule.tree.impl.LockLogicTreeNode;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: rem
 * @Date: 2024/11/22/22:02
 * @Description:
 */
@SpringBootTest
@Slf4j
public class StrategyRaffleTest {

    @Autowired
    private IRaffleStrategy strategyRaffle;

    @Resource
    private IStrategyArmory strategyArmory;

    @Resource
    private RuleWeightLogicNode ruleWeightLogicNode;

    @Resource
    private DefaultChainFactory defaultChainFactory;

    @Resource
    private LockLogicTreeNode lockLogicTreeNode;

    @Test
    public void test_strategyArmory() {
        // 策略装配 100001、100002、100003
        boolean success = strategyArmory.assembleRaffleStrategy(100003L);
        log.info("策略装配测试结果：{}", success);
    }

    @BeforeEach
    public void setUp() {
        // 通过反射 mock 规则中的值
        ReflectionTestUtils.setField(ruleWeightLogicNode, "userScore", 6100L);
        ReflectionTestUtils.setField(lockLogicTreeNode, "userRaffleCount", 10L);
    }

    @Test
    public void test_raffle_center_rule_lock() {
        RaffleFactorEntity raffleFactorEntity = RaffleFactorEntity.builder()
                .userId("3")
                .strategyId(100001L)
                .build();

        RaffleAwardEntity raffleAwardEntity = strategyRaffle.performRaffle(raffleFactorEntity);

        log.info("请求参数：{}", JSON.toJSONString(raffleFactorEntity));
        log.info("测试结果：{}", JSON.toJSONString(raffleAwardEntity));
    }

    @Test
    public void test_performRaffle_blacklist() {
        RaffleFactorEntity raffleFactorEntity = RaffleFactorEntity.builder()
                .userId("3")  // 黑名单用户 user001,user002,user003
                .strategyId(100001L)
                .build();

        RaffleAwardEntity raffleAwardEntity = strategyRaffle.performRaffle(raffleFactorEntity);

        log.info("请求参数：{}", JSON.toJSONString(raffleFactorEntity));
        log.info("测试结果：{}", JSON.toJSONString(raffleAwardEntity));
    }

    @Test
    public void test_StrategyLogicChain() {
        IStrategyLogicChain logicChain = defaultChainFactory.openLogicChain(100002L);
        DefaultChainFactory.LogicAwardVO logicAwardVO = logicChain.executeStrategy("5", 100002L);
        System.out.println(logicAwardVO);
    }

    @Test
    public void test_raffle() throws InterruptedException {
        RaffleFactorEntity factor = RaffleFactorEntity.builder()
                .strategyId(100003L)
                .userId("3")
                .build();
        int iCount = 20;
        Map<Integer, Integer> recordCountMap = new HashMap<>();
        for (int count = 0; count < iCount; count++) {
            RaffleAwardEntity raffleAwardEntity = strategyRaffle.performRaffle(factor);
            Integer awardId = raffleAwardEntity.getAwardId();
            recordCountMap.put(awardId, recordCountMap.getOrDefault(awardId, 0) + 1);
        }
        for (Integer key : recordCountMap.keySet()) {
            log.info("awardId :{} count : {}", key, recordCountMap.get(key));
        }
        Thread.sleep(20 * 1000);
    }

}
