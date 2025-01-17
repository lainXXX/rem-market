package top.javarem.domain.credit.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Author: rem
 * @Date: 2025/01/17/16:50
 * @Description:积分账户实体对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditAccountEntity {

    private String userId;

    /**
     * 可用积分，每次扣减的值
     */
    private BigDecimal adjustAmount;

}
