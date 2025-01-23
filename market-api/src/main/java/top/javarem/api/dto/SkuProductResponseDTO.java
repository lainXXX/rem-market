package top.javarem.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: rem
 * @Date: 2025/01/20/22:03
 * @Description:sku商品响应传输对象
 */
@Data
public class SkuProductResponseDTO implements Serializable {

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
