package top.javarem.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: rem
 * @Date: 2024/12/01/14:08
 * @Description:用于展示的奖品信息
 */
@Data
public class DisplayAwardDTO implements Serializable {

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
     * 奖品数量
     */
    private Integer awardCount;
    /**
     * 奖品剩余数量
     */
    private Integer awardCountSurplus;

    private BigDecimal rate;

    private Integer sort;

    /**
     * 奖品次数规则 - 抽奖N次后解锁，未配置则为空
     */
    private Integer unlockCount;

    /**
     * 奖品是否解锁 - true 已解锁、false 未解锁
     */
    private Boolean isUnLocked;

    /**
     * 等待解锁次数 - 规定的抽奖N次解锁减去用户已经抽奖次数
     */
    private Integer waitUnlockCount;

}
