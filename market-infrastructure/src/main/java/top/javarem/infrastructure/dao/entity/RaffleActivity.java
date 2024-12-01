package top.javarem.infrastructure.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName raffle_activity
 */
@TableName(value ="raffle_activity")
@Data
public class RaffleActivity implements Serializable {
    /**
     * 自增id
     */
    @TableId
    private Long id;

    /**
     * 活动id
     */
    private Long activityId;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 活动描述
     */
    private String activityDesc;

    /**
     * 活动开始时间
     */
    private Date beginTime;

    /**
     * 活动结束时间
     */
    private Date endTime;

    /**
     * 库存总量
     */
    private Integer stockCount;

    /**
     * 库存剩余量
     */
    private Integer stockCountSurplus;

    /**
     * 活动参与次数配置
     */
    private Long activityCountId;

    /**
     * 活动抽奖策略id
     */
    private Long strategyId;

    /**
     * 活动状态
     */
    private String status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}