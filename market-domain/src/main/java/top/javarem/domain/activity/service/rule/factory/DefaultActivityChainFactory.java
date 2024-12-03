package top.javarem.domain.activity.service.rule.factory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;
import top.javarem.domain.activity.service.rule.IActivityChain;

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

}
