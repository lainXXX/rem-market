package top.javarem.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: rem
 * @Date: 2024/12/14/13:59
 * @Description:展示策略奖品请求DTO
 */
@Data
public class DisplayAwardRequestDTO implements Serializable {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 活动id
     */
    private Long activityId;

}
