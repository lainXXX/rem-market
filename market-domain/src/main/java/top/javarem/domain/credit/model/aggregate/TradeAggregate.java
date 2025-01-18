package top.javarem.domain.credit.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import top.javarem.domain.award.model.vo.TaskStateVO;
import top.javarem.domain.credit.event.CreditAdjustSuccessMessageEvent;
import top.javarem.domain.credit.model.entity.CreditAccountEntity;
import top.javarem.domain.credit.model.entity.CreditOrderEntity;
import top.javarem.domain.credit.model.entity.TaskEntity;
import top.javarem.domain.credit.model.vo.TradeNameVO;
import top.javarem.domain.credit.model.vo.TradeTypeVO;
import top.javarem.types.event.BaseEvent;

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

    private TaskEntity taskEntity;

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

    public void setTaskEntity(String userId, String topic, String messageId, BaseEvent.EventMessage<CreditAdjustSuccessMessageEvent.CreditAdjustSuccessMessage> message) {
        this.taskEntity = TaskEntity.builder()
                .userId(userId)
                .topic(topic)
                .messageId(messageId)
                .message(message)
                .status(TaskStateVO.create)
                .build();
    }

}
