package top.javarem.domain.activity.model.entity;

import lombok.Data;

import java.math.BigDecimal;


/**
 * @TableName raffle_activity_sku
 */
@Data
public class ActivitySkuEntity {

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
    /** 商品金额【积分】 */
    private BigDecimal productAmount;

}