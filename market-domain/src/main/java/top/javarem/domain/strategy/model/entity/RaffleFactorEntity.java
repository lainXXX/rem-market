package top.javarem.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author: rem
 * @Date: 2024/11/21/17:54
 * @Description:抽奖因素
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RaffleFactorEntity {

    private String userId;

    private Long strategyId;

    /**
     * 结束时间
     */
    private Date endTime;

//    private Integer awardId;

}
