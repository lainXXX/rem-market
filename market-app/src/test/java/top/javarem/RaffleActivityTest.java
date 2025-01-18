package top.javarem;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.javarem.domain.activity.model.entity.ActivityPartakeEntity;
import top.javarem.domain.activity.model.entity.SkuRechargeEntity;
import top.javarem.domain.activity.model.entity.UserRaffleConsumeOrderEntity;
import top.javarem.domain.activity.model.vo.OrderTradeTypeVO;
import top.javarem.domain.activity.service.IRaffleActivityAccountQuotaService;
import top.javarem.domain.activity.service.IRaffleActivityPartakeService;
import top.javarem.domain.activity.service.armory.IActivityArmory;
import top.javarem.domain.award.model.entity.UserAwardRecordEntity;
import top.javarem.domain.award.model.vo.AwardStateVO;
import top.javarem.domain.award.service.IAwardService;
import top.javarem.infrastructure.dao.entity.RaffleActivity;
import top.javarem.infrastructure.dao.iService.RaffleActivityService;

import java.util.Date;


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

    @Autowired
    private IRaffleActivityAccountQuotaService accountQuotaService;

    @Autowired
    private IAwardService awardService;

    @Test
    public void test_createOrder() {
        ActivityPartakeEntity activityPartakeEntity = new ActivityPartakeEntity();
        activityPartakeEntity.setActivityId(100301L);
        activityPartakeEntity.setUserId("rem");
        UserRaffleConsumeOrderEntity order = partakeService.createOrder(activityPartakeEntity);
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

    @Test
    public void test_saveUserAwardRecord() throws InterruptedException {
            UserAwardRecordEntity userAwardRecordEntity = new UserAwardRecordEntity();
            userAwardRecordEntity.setUserId("rem");
            userAwardRecordEntity.setActivityId(100301L);
            userAwardRecordEntity.setStrategyId(100006L);
            userAwardRecordEntity.setOrderId(RandomStringUtils.randomNumeric(12));
            userAwardRecordEntity.setAwardId(101);
            userAwardRecordEntity.setAwardTitle("OpenAI 增加使用次数");
            userAwardRecordEntity.setAwardTime(new Date());
            userAwardRecordEntity.setStatus(AwardStateVO.create.getCode());
            awardService.saveUserAwardRecord(userAwardRecordEntity);
            Thread.sleep(5000);
    }

    @Test
    public void test_credit_pay_trade() {
        SkuRechargeEntity skuRechargeEntity = new SkuRechargeEntity();
        skuRechargeEntity.setUserId("rem");
        skuRechargeEntity.setSku(9011L);
        skuRechargeEntity.setOutBusinessNo("700091009135");
        skuRechargeEntity.setOrderTradeTypeVO(OrderTradeTypeVO.credit_pay_trade);
        String orderId = accountQuotaService.createOrder(skuRechargeEntity);
        log.info("orderId : {}", orderId);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
