package top.javarem.infrastructure.dao.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: rem
 * @Date: 2024/11/21/16:47
 * @Description:用户
 */

@Data
public class User implements Serializable {

    /**
     * 自增id
     */
    private Integer id;

    /**
     * 用户id
     */
    private String userId;

//    /**
//     * 奖品id
//     */
//    private Integer awardId;

    /**
     * 策略id
     */
    private Long strategyId;

    /**
     * 规则模型
     */
    private String ruleModel;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;

}
