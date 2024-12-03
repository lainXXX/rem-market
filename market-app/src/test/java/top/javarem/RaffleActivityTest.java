package top.javarem;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.javarem.domain.activity.model.entity.SkuRechargeEntity;
import top.javarem.domain.activity.service.IRaffleOrder;
import top.javarem.infrastructure.dao.entity.RaffleActivity;
import top.javarem.infrastructure.dao.iService.RaffleActivityService;


/**
 * @Author: rem
 * @Date: 2024/12/02/21:17
 * @Description:
 */
@SpringBootTest
@Slf4j
public class RaffleActivityTest {

    @Autowired
    private RaffleActivityService raffleActivityService;

    @Autowired
    private IRaffleOrder raffleOrder;

    @Test
    public void test_raffle_activity() {
        RaffleActivity raffleActivity = raffleActivityService.lambdaQuery()
                .eq(RaffleActivity::getActivityId, 100003L)
                .one();
        log.info(raffleActivity.toString());
    }

    @Test
    public void test_createSkuRechargeOrder() {
        SkuRechargeEntity skuRechargeEntity = new SkuRechargeEntity();
        skuRechargeEntity.setUserId("rem");
        skuRechargeEntity.setSku(1111L);
        // outBusinessNo 作为幂等仿重使用，同一个业务单号2次使用会抛出索引冲突 Duplicate entry '700091009111' for key 'uq_out_business_no' 确保唯一性。
        skuRechargeEntity.setOutBusinessNo("700091009111");
        String orderId = raffleOrder.createActivityOrder(skuRechargeEntity);
        log.info("测试结果：{}", orderId);
    }


}
