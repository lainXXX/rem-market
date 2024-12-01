package top.javarem.api.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: rem
 * @Date: 2024/12/01/14:08
 * @Description:用于展示的奖品信息
 */
@Data
public class DisplayAwardDTO {

    private Integer awardId;

    private String Title;

    private String awardSubtitle;

    private Integer awardCount;

    private Integer awardCountSurplus;

    private BigDecimal rate;

    private Integer sort;


}
