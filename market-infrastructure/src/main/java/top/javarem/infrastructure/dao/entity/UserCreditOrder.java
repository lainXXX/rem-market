package top.javarem.infrastructure.dao.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户积分订单表
 * @TableName user_credit_order
 */
@TableName(value ="user_credit_order")
@Data
public class UserCreditOrder implements Serializable {
    /**
     * 自增ID
     */
    @TableId
    private Long id;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 订单ID
     */
    private String tradeName;

    /**
     * 交易类型【forward-正向、reverse-逆向】
     */
    private String tradeType;

    /**
     * 交易金额
     */
    private BigDecimal tradeAmount;

    /**
     * 业务防重-外部透传【返行为等唯一标识】
     */
    private String outBusinessNo;

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