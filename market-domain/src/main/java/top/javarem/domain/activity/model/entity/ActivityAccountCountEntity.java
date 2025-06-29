package top.javarem.domain.activity.model.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 
 * @TableName raffle_activity_account
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityAccountCountEntity {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 活动id
     */
    private Long activityId;

    /**
     * 总次数
     */
    private Integer totalCount;

    /**
     * 总剩余次数
     */
    private Integer totalCountSurplus;

    /**
     * 日次数
     */
    private Integer dayCount;

    /**
     * 日剩余次数
     */
    private Integer dayCountSurplus;


    /**
     * 月次数
     */
    private Integer monthCount;

    /**
     * 月剩余次数
     */
    private Integer monthCountSurplus;

}