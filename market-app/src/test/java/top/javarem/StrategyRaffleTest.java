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
import top.javarem.domain.strategy.service.IStrategyRaffle;
import top.javarem.domain.strategy.service.armory.IStrategyArmory;
import top.javarem.domain.strategy.service.rule.impl.RuleWeightLogicFilter;

import javax.annotation.Resource;

/**
 * @Author: rem
 * @Date: 2024/11/22/22:02
 * @Description:
 */
@SpringBootTest
@Slf4j
public class StrategyRaffleTest {

    @Autowired
    private IStrategyRaffle strategyRaffle;

    @Resource
    private IStrategyArmory strategyArmory;

    @Resource
    private RuleWeightLogicFilter ruleWeightLogicFilter;

    @BeforeEach
    public void setUp() {
        // 策略装配 100001、100002、100003
        log.info("测试结果：{}", strategyArmory.assembleLotteryStrategy(100001L));

        // 通过反射 mock 规则中的值
        ReflectionTestUtils.setField(ruleWeightLogicFilter, "userScore", 4050L);
    }

/*    @Test
    public void test_raffle_center_rule_lock(){
        RaffleFactorEntity raffleFactorEntity = RaffleFactorEntity.builder()
                .userId("1")
                .StrategyId(100001L)
                .build();

        RaffleAwardEntity raffleAwardEntity = strategyRaffle.performRaffle(raffleFactorEntity);

        log.info("请求参数：{}", JSON.toJSONString(raffleFactorEntity));
        log.info("测试结果：{}", JSON.toJSONString(raffleAwardEntity));
    }*/

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

}
