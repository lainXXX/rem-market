package top.javarem.infrastructure.dao.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 日常返利行为活动配置
 * @TableName daily_behavior_rebate
 */
@TableName(value ="daily_behavior_rebate")
@Data
public class DailyBehaviorRebate implements Serializable {
    /**
     * 自增id
     */
    @TableId
    private Long id;

    /**
     * 行为类型(sign_in- 签到 、 pay-支付)
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
     * 配置
     */
    private String rebateConfig;

    /**
     * 状态（open-开启、 close-关闭）
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