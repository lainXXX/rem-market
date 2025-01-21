package top.javarem.domain.activity.model.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: rem
 * @Date: 2025/01/21/15:50
 * @Description:
 */
@Data
public class SkuProductEntity {

    private Long sku;

    private Long activityId;

    private Long activityCountId;

    private Integer stockCount;

    private Integer stockCountSurplus;

    private BigDecimal productAmount;

    private  ActivityCount activityCount;

    @Data
    public static class ActivityCount {
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
    }

}
