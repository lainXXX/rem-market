package top.javarem.domain.award.model.aggregate;

import lombok.Data;
import top.javarem.domain.award.model.entity.UserAwardRecordEntity;
import top.javarem.domain.award.model.entity.UserCreditAwardEntity;
import top.javarem.domain.award.model.vo.AccountStatusVO;

import java.math.BigDecimal;

/**
 * @Author: rem
 * @Date: 2025/02/25/14:59
 * @Description:其他奖品聚合对象
 */
@Data
public class OthersPrizeAggregate {

    private String userId;

    private UserAwardRecordEntity userAwardRecordEntity;

    public void setUserAwardRecordEntity(String userId, String orderId, Integer awardId, String status) {
        this.userAwardRecordEntity = UserAwardRecordEntity.builder()
                .userId(userId)
                .orderId(orderId)
                .awardId(awardId)
                .status(status)
                .build();
    }

}
