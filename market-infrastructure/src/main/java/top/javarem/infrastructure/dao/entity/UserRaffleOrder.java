package top.javarem.infrastructure.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author: rem
 * @Date: 2024/12/06/13:27
 * @Description:用户抽奖记录单
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRaffleOrder {

    private Long id;

    private String userId;

    private Long activityId;

    private String activityName;

    private Long strategyId;

    private String orderId;

    private String status;

    private Date orderTime;

    private Date createTime;

    private Date updateTime;

}
