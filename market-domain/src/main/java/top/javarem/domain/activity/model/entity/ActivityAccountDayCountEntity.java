package top.javarem.domain.activity.model.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityAccountDayCountEntity {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 活动id
     */
    private Long activityId;

    /**
     * 格式【yyyy-mm-dd】
     */
    private String day;

    /**
     * 日总次数
     */
    private Integer dayCount;

    /**
     * 日剩余次数
     */
    private Integer dayCountSurplus;

}