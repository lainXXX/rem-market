package top.javarem.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 活动抽奖请求对象
 * @create 2024-04-13 09:29
 */
@Data
public class ActivityDrawRequestDTO implements Serializable {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 活动ID
     */
    private Long activityId;

}
