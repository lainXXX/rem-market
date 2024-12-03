package top.javarem.domain.activity.model.entity;


import lombok.Data;

import java.util.Date;

/**
 * 
 * @TableName raffle_activity
 */
@Data
public class ActivityEntity {

    /**
     * 活动id
     */
    private Long activityId;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 活动描述
     */
    private String activityDesc;

    /**
     * 活动开始时间
     */
    private Date beginTime;

    /**
     * 活动结束时间
     */
    private Date endTime;

    /**
     * 活动抽奖策略id
     */
    private Long strategyId;

    /**
     * 活动状态
     */
    private String status;

}