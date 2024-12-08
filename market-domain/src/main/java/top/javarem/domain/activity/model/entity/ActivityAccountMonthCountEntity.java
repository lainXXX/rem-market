package top.javarem.domain.activity.model.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityAccountMonthCountEntity {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 活动id
     */
    private Long activityId;

    /**
     * 格式【yyyy-mm】
     */
    private String month;

    /**
     * 月总次数
     */
    private Integer monthCount;

    /**
     * 月剩余次数
     */
    private Integer monthCountSurplus;

}