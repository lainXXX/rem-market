package top.javarem.api.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @Author: rem
 * @Date: 2024/12/01/14:03
 * @Description:抽奖奖品DTO
 */
@Data
@Builder
public class RaffleAwardDTO {

    private Integer awardId;

    private Integer awardIndex;

}
