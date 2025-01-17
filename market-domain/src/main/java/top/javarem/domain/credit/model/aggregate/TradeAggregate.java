package top.javarem.domain.credit.model.aggregate;

import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;
import top.javarem.domain.credit.model.entity.CreditAccountEntity;
import top.javarem.domain.credit.model.entity.CreditOrderEntity;
import top.javarem.domain.credit.model.vo.TradeNameVO;
import top.javarem.domain.credit.model.vo.TradeTypeVO;

import java.math.BigDecimal;

/**
 * @Author: rem
 * @Date: 2025/01/17/16:59
 * @Description:交易聚合对象
 */
@Data
public class TradeAggregate {

    private String userId;

    /**
     * 积分账户实体
     */
    private CreditAccountEntity creditAccountEntity;
    /**
     * 积分对象实体
     */
    private CreditOrderEntity creditOrderEntity;

    /**
     * 设置积分账户实体
     * @param userId
     * @param adjustAmount
     */
    public void setCreditAccountEntity(String userId, BigDecimal adjustAmount) {

        this.creditAccountEntity = CreditAccountEntity.builder()
                .userId(userId)
                .adjustAmount(adjustAmount)
                .build();

    }

    /**
     * 设置积分订单实体对象
     * @param userId
     * @param tradeName
     * @param tradeType
     * @param tradeAmount
     * @param outBusinessNo
     */
    public void setCreditOrderEntity(String userId, TradeNameVO tradeName, TradeTypeVO tradeType, BigDecimal tradeAmount, String outBusinessNo) {

        this.creditOrderEntity = CreditOrderEntity.builder()
                .userId(userId)
                .orderId(RandomStringUtils.randomNumeric(12))
                .tradeName(tradeName)
                .tradeType(tradeType)
                .tradeAmount(tradeAmount)
                .outBusinessNo(outBusinessNo)
                .build();

    }

}
