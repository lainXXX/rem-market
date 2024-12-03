package top.javarem.domain.activity.model.entity;

import lombok.Data;

/**
 * 
 * @TableName raffle_activity_count
 */
@Data
public class ActivityCountEntity {

    /**
     * 活动次数编号
     */
    private Long activityCountId;

    /**
     * 总次数
     */
    private Integer totalCount;

    /**
     * 日次数
     */
    private Integer dayCount;

    /**
     * 月次数
     */
    private Integer monthCount;

}