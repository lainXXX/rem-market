package top.javarem.infrastructure.dao.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName rule
 */
@Data
public class Rule implements Serializable {
    /**
     * 自增id
     */
    private Long id;

    /**
     * 策略id
     */
    private Long strategyId;

    /**
     * 奖品id【规则为策略 则不需要奖品id】
     */
    private Integer awardId;

    /**
     * 【rule_random - 随机值计算、 rule_lock - 抽奖几次后解锁、 rule_luck_award- 幸运奖(兜底奖品)】
     */
    private String ruleModel;

    /**
     * 1-策略规则、 2-奖品规则
     */
    private Integer ruleType;

    /**
     * 抽奖规则比值
     */
    private String ruleValue;

    /**
     * 抽奖规则描述
     */
    private String ruleDesc;

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