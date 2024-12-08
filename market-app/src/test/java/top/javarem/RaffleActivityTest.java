package top.javarem;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.javarem.domain.activity.model.entity.ActivityPartakeEntity;
import top.javarem.domain.activity.model.entity.SkuRechargeEntity;
import top.javarem.domain.activity.model.entity.UserConsumeOrderEntity;
import top.javarem.domain.activity.service.IRaffleActivityAccountQuotaService;
import top.javarem.domain.activity.service.IRaffleActivityPartakeService;
import top.javarem.domain.activity.service.armory.IActivityArmory;
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
    private IRaffleActivityAccountQuotaService raffleOrder;

    @Autowired
    private IActivityArmory activityArmory;

    @Autowired
    private IRaffleActivityPartakeService partakeService;

    @Test
    public void test_createOrder() {
        ActivityPartakeEntity activityPartakeEntity = new ActivityPartakeEntity();
        activityPartakeEntity.setActivityId(100301L);
        activityPartakeEntity.setUserId("rem");
        UserConsumeOrderEntity order = partakeService.createOrder(activityPartakeEntity);
        log.info(order.toString());

    }

    @Test
    public void test_raffle_activity() {
        RaffleActivity raffleActivity = raffleActivityService.lambdaQuery()
                .eq(RaffleActivity::getActivityId, 100301L)
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
        String orderId = raffleOrder.createOrder(skuRechargeEntity);
        log.info("测试结果：{}", orderId);
    }

    @Test
    public void test_assembleActivity() {
        Boolean b = activityArmory.assembleRaffleActivity(9011L);
        log.info(b.toString());
    }

    @Test
    public void test_createActivityOrder() throws InterruptedException {
        SkuRechargeEntity skuRechargeEntity = SkuRechargeEntity.builder()
                .userId("rem")
                .sku(9011L)
                .outBusinessNo("700091009128")
                .build();
        String activityOrder = raffleOrder.createOrder(skuRechargeEntity);
        log.info("orderId : {}", activityOrder);
        Thread.sleep(5000);
    }


}
