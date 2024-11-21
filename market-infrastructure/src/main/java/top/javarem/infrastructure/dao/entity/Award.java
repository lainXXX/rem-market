package top.javarem.infrastructure.dao.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @TableName award
 */
@Data
public class Award implements Serializable {
    /**
     * 自增id
     */
    private Long id;

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
     * 规则模型 rule配置模型同步到此表
     */
    private String models;

    /**
     * 排序
     */
    private String sort;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}