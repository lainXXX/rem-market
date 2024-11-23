package top.javarem;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.javarem.domain.strategy.service.armory.IStrategyArmory;
import top.javarem.domain.strategy.service.armory.IStrategyArmoryDispatch;

import java.util.HashMap;
import java.util.Map;


@SpringBootTest(classes = Application.class)
@Slf4j
public class StrategyTest {

    @Autowired
    private IStrategyArmory strategyArmory;

    @Autowired
    private IStrategyArmoryDispatch strategyArmoryDispatch;

    @Test
    public void test_strategyArmory() {
        boolean success = strategyArmory.assembleLotteryStrategy(100001L);
        log.info("测试结果：{}", success);
    }

    @Test
    public void test_getAssembleRandomVal() {
        // 创建一个 HashMap 用来记录每个 randomAwardId 出现的次数
        Map<Integer, Integer> awardCountMap = new HashMap<>();

        for (int i = 0; i < 706; i++) {
            Integer randomAwardId = strategyArmoryDispatch.getRandomAwardId(100001L);

            // 将 randomAwardId 的出现次数计入 Map
            awardCountMap.put(randomAwardId, awardCountMap.getOrDefault(randomAwardId, 0) + 1);
        }

        // 打印每个 randomAwardId 的出现次数
        for (Map.Entry<Integer, Integer> entry : awardCountMap.entrySet()) {
            System.out.println("Award ID: " + entry.getKey() + ", Count: " + entry.getValue());
        }
    }

    @Test
    public void test_weightValue_getAssembleRandomVal() {
        // 创建一个 HashMap 用来记录每个 randomAwardId 出现的次数
        Map<Integer, Integer> awardCountMap = new HashMap<>();

        for (int i = 0; i < 100; i++) {
            Integer randomAwardId = strategyArmoryDispatch.getRandomAwardId(100001L, "6000");

            // 将 randomAwardId 的出现次数计入 Map
            awardCountMap.put(randomAwardId, awardCountMap.getOrDefault(randomAwardId, 0) + 1);
        }

        // 打印每个 randomAwardId 的出现次数
        for (Map.Entry<Integer, Integer> entry : awardCountMap.entrySet()) {
            System.out.println("Award ID: " + entry.getKey() + ", Count: " + entry.getValue());
        }
    }


}
