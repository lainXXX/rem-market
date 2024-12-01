package top.javarem.api.dto;

import lombok.Data;

/**
 * @Author: rem
 * @Date: 2024/12/01/14:01
 * @Description:抽奖请求DTO
 */
@Data
public class RaffleRequestDTO {

    private Long strategyId;

    private String UserId;

}
