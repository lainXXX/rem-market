package top.javarem.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * @TableName strategy_award
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StrategyAwardEntity {


    /**
     * 抽奖策略id
     */
    private Long strategyId;

    /**
     * 奖品id-内部使用
     */
    private Integer awardId;

    /**
     * 奖品标题
     */
    private String awardTitle;

    /**
     * 奖品副标题
     */
    private String awardSubtitle;

    /**
     * 奖品库存总量
     */
    private Integer awardCount;

    /**
     * 库存剩余
     */
    private Integer awardCountSurplus;

    /**
     * 中奖概率
     */
    private BigDecimal rate;
    /**
     * 排序
     */
    private Integer sort;
    /** 规则模型，rule配置的模型同步到此表，便于使用 */
    private String models;

}