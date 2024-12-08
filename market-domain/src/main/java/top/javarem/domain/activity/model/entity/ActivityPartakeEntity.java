package top.javarem.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: rem
 * @Date: 2024/12/05/15:19
 * @Description: 活动参与消息实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityPartakeEntity {

    private String userId;

    private Long activityId;

}
