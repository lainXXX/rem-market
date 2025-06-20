package top.javarem.infrastructure.dao.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户行为返利流水订单表
 * @TableName user_behavior_rebate_order
 */
@TableName(value ="user_behavior_rebate_order")
@Data
public class UserBehaviorRebateOrder implements Serializable {
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
     * 订单id
     */
    private String orderId;

    /**
     * 行为类型（sign 签到、pay 支付）
     */
    private String behaviorType;

    /**
     * 返利描述
     */
    private String rebateDesc;

    /**
     * 返利类型（sku 活动库存充值商品、integral 用户活动积分）
     */
    private String rebateType;

    /**
     * 返利配置【sku值、 积分值】
     */
    private String rebateConfig;

    /**
     * 业务防重ID-外部透传
     */
    private String outBusinessNo;

    /**
     * 业务ID-  拼接的唯一值
     */
    private String bizId;

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