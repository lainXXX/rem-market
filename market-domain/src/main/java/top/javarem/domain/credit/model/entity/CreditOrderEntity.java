package top.javarem.domain.credit.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.javarem.domain.credit.model.vo.TradeNameVO;
import top.javarem.domain.credit.model.vo.TradeTypeVO;

import java.math.BigDecimal;

/**
 * @Author: rem
 * @Date: 2025/01/17/16:53
 * @Description:积分订单实体对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditOrderEntity {

    private String userId;

    private String orderId;

    private TradeNameVO tradeName;

    private TradeTypeVO tradeType;

    private BigDecimal tradeAmount;

    private String outBusinessNo;

}
