package top.javarem.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.javarem.domain.activity.model.vo.UserRaffleStatus;

import java.util.Date;

/**
 * @Author: rem
 * @Date: 2024/12/05/15:20
 * @Description:用户抽奖消费单
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRaffleConsumeOrderEntity {

    private String userId;

    private Long strategyId;

    private Long activityId;

    private String activityName;

    private String orderId;

    private Date orderTime;

    private UserRaffleStatus status;

}
