package generator.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName raffle_activity_order
 */
@TableName(value ="raffle_activity_order")
@Data
public class RaffleActivityOrder implements Serializable {
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
     * 抽奖策略id
     */
    private Long strategyId;

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 下单时间
     */
    private Date orderTime;

    /**
     * 订单状态
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