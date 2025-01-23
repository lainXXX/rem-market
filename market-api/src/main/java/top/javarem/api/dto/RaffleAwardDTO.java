package top.javarem.api.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: rem
 * @Date: 2024/12/01/14:03
 * @Description:抽奖奖品DTO
 */
@Data
@Builder
public class RaffleAwardDTO implements Serializable {

    private Integer awardId;

    private Integer awardIndex;

}
