package top.javarem.credit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.javarem.domain.credit.model.entity.TradeEntity;
import top.javarem.domain.credit.model.vo.TradeNameVO;
import top.javarem.domain.credit.model.vo.TradeTypeVO;
import top.javarem.domain.credit.service.ICreditService;

import java.math.BigDecimal;

/**
 * @Author: rem
 * @Date: 2025/01/17/17:46
 * @Description:
 */
@SpringBootTest
public class CreditAdjustTest {

    @Autowired
    private ICreditService creditService;

    /**
     * 测试创建订单 正向增加积分
     */
    @Test
    public void test_createOrder_forward() {

        TradeEntity tradeEntity = new TradeEntity();
        tradeEntity.setUserId("rem");
        tradeEntity.setTradeName(TradeNameVO.REBATE);
        tradeEntity.setTradeType(TradeTypeVO.FORWARD);
        tradeEntity.setAmount(new BigDecimal("-1.68"));
        tradeEntity.setOutBusinessNo("700091009135");
        creditService.createOrder(tradeEntity);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
