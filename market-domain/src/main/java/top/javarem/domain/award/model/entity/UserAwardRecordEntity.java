package top.javarem.domain.award.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author: rem
 * @Date: 2024/12/08/19:42
 * @Description:用户抽奖奖品记录
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAwardRecordEntity {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 活动id
     */
    private Long activityId;

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 策略id
     */
    private Long strategyId;

    /**
     * 奖品id
     */
    private Integer awardId;

    /**
     * 奖品标题
     */
    private String awardTitle;

    /**
     * 奖品副标题
     */
    private String awardSubtitle;

    /**
     * 中奖时间
     */
    private Date awardTime;

    /**
     * 奖品状态【create、complete】
     */
    private String status;

}
