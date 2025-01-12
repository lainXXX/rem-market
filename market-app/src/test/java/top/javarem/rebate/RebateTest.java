package top.javarem.rebate;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.javarem.domain.rebate.model.entity.BehaviorEntity;
import top.javarem.domain.rebate.model.vo.BehaviorTypeVO;
import top.javarem.domain.rebate.service.IBehaviorRebateService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: rem
 * @Date: 2025/01/12/18:13
 * @Description:
 */
@SpringBootTest
@Slf4j
public class RebateTest {
    @Resource
    private IBehaviorRebateService behaviorRebateService;

    @Test
    public void test_createOrder() {
        BehaviorEntity behaviorEntity = new BehaviorEntity();
        behaviorEntity.setUserId("rem");
        behaviorEntity.setBehaviorType(BehaviorTypeVO.SIGN);
        // 重复的 OutBusinessNo 会报错唯一索引冲突，这也是保证幂等的手段，确保不会多记账
        behaviorEntity.setOutBusinessNo("20250112");

        List<String> orderIds = behaviorRebateService.createOrder(behaviorEntity);
        log.info("请求参数：{}", JSON.toJSONString(behaviorEntity));
        log.info("测试结果：{}", JSON.toJSONString(orderIds));
    }

}
