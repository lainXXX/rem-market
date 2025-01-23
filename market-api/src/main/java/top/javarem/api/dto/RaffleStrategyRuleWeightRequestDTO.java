package top.javarem.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: rem
 * @Date: 2025/01/15/14:40
 * @Description:
 */
@Data
public class RaffleStrategyRuleWeightRequestDTO implements Serializable {

    private String userId;

    private Long activityId;


}
