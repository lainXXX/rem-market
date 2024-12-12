package top.javarem.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: rem
 * @Date: 2024/11/21/17:57
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RaffleAwardEntity {

    /** 奖品ID */
    private Integer awardId;
    /**
     * 奖品标题
     */
    private String awardTitle;
    /** 奖品配置信息 */
    private String awardConfig;
    /** 奖品顺序号 */
    private Integer sort;

}
