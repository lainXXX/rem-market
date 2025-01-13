package top.javarem.infrastructure.dao.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName raffle_activity_account_day_count
 */
@TableName(value ="raffle_activity_account_day_count")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RaffleActivityAccountDayCount implements Serializable {

    private final SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");

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
     * 格式【yyyy-mm-dd】
     */
    private String day;

    /**
     * 日总次数
     */
    private Integer dayCount;

    /**
     * 日剩余次数
     */
    private Integer dayCountSurplus;

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

    public String today() {
        return dayFormat.format(new Date());
    }

}