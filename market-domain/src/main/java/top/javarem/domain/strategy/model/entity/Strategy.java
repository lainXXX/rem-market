package top.javarem.domain.strategy.model.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName strategy
 */
@Data
public class Strategy implements Serializable {

    /**
     * 抽奖策略id
     */
    private Long strategyId;

    /**
     * 抽奖策略描述
     */
    private String strategyDesc;

    private static final long serialVersionUID = 1L;
}