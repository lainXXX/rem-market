package top.javarem.domain.award.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.javarem.domain.award.model.entity.DistributeAwardEntity;
import top.javarem.domain.award.model.entity.UserAwardRecordEntity;
import top.javarem.domain.award.model.entity.UserCreditAwardEntity;
import top.javarem.domain.award.model.vo.AccountStatusVO;

import java.math.BigDecimal;

/**
 * @Author: rem
 * @Date: 2025/01/16/16:02
 * @Description:奖品分发聚合对象
 */
@Data
public class GiveOutPrizeAggregate {

    private String userId;

    private UserAwardRecordEntity userAwardRecordEntity;

    private UserCreditAwardEntity userCreditAwardEntity;

    public void setUserAwardRecordEntity(String userId, String orderId, Integer awardId, String status) {
        this.userAwardRecordEntity = UserAwardRecordEntity.builder()
                .userId(userId)
                .orderId(orderId)
                .awardId(awardId)
                .status(status)
                .build();
    }

    public void setUserCreditAwardEntity(String userId, BigDecimal creditAmount) {
        this.userCreditAwardEntity = UserCreditAwardEntity.builder()
                .userId(userId)
                .creditAmount(creditAmount)
                .accountStatus(AccountStatusVO.open.getCode())
                .build();
    }

}
