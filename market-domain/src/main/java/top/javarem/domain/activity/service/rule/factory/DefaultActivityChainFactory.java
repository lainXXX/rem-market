package top.javarem.domain.activity.service.rule.factory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.stereotype.Service;
import top.javarem.domain.activity.service.rule.IActivityChain;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

/**
 * @Author: rem
 * @Date: 2024/12/02/14:25
 * @Description:
 */
@Service
public class DefaultActivityChainFactory {

    private final IActivityChain chain;

    public DefaultActivityChainFactory(Map<String, IActivityChain> chainMap) {
        this.chain = chainMap.get(ActivityModel.activity_base_filter.code);
        this.chain.appendNext(chainMap.get(ActivityModel.activity_sku_stock_filter.code));
    }

    public IActivityChain openActivityChain() {
        return this.chain;
    }

    @Getter
    @AllArgsConstructor
    public enum ActivityModel {

        activity_base_filter("activity_base_filter", "活动的库存、时间校验"),
        activity_sku_stock_filter("activity_sku_stock_filter", "活动sku库存"),
        ;

        private final String code;
        private final String info;

    }

    /**
     * 活动责任链过滤参数的值对象
     */
    @Data
    @Builder
    public static class ActivityChainFilterVO {

        /**
         * 活动sku
         */
        private Long sku;

        /**
         * 活动id
         */
        private Long activityId;

        /**
         * 剩余库存
         */
        private Integer skuSurplusStock;

        /**
         * 活动状态
         */
        private String status;

        /**
         * 开启时间
         */
        private Date beginTime;

        /**
         * 结束时间
         */
        private Date endTime;

    }

}
