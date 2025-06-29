package top.javarem.infrastructure.dao.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: rem
 * @Date: 2024/11/22/13:58
 * @Description:
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
     * 奖品对接标识 - 每一个都是一个对应的发奖策略
     */
    private String awardKey;

    /**
     * 奖品配置信息
     */
    private String awardConfig;

    /**
     * 奖品描述
     */
    private String awardDesc;

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
