package top.javarem.infrastructure.dao.entity;


import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户中奖记录表
 * @TableName user_award_record
 */
@TableName(value ="user_raffle_order")
@Data
public class UserAwardRecord implements Serializable {
    /**
     * 自增id
     */
    @TableId
    private Long id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 活动id
     */
    private Long activityId;

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 策略id
     */
    private Long strategyId;

    /**
     * 奖品id
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
     * 中奖时间
     */
    private Date awardTime;

    /**
     * 奖品状态【create、complete】
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