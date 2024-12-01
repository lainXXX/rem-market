package generator.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName raffle_activity_account_flow
 */
@TableName(value ="raffle_activity_account_flow")
@Data
public class RaffleActivityAccountFlow implements Serializable {
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
     * 总次数
     */
    private Integer totalCount;

    /**
     * 日次数
     */
    private Integer dayCount;

    /**
     * 月次数
     */
    private Integer monthCount;

    /**
     * 流水id
     */
    private String flowId;

    /**
     * 流水渠道(activity-活动领取、sale-购买、redeem-兑换、free-免费领取)
     */
    private String flowChannel;

    /**
     * 业务id(活动id、订单id)
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