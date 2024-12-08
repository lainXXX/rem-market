package top.javarem.infrastructure.dao.entity;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName raffle_activity_sku
 */
@TableName(value ="raffle_activity_sku")
@Data
public class RaffleActivitySku implements Serializable {
    /**
     * 自增id
     */
    @TableId
    private Integer id;

    /**
     * 商品sku - 把每一个组合当作一个商品
     */
    private Long sku;

    /**
     * 活动id
     */
    private Long activityId;

    /**
     * 活动个人参与次数id
     */
    private Long activityCountId;

    /**
     * 商品库存
     */
    private Integer stockCount;

    /**
     * 剩余库存
     */
    private Integer stockCountSurplus;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}