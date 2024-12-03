package top.javarem.domain.activity.service.rule.impl;

import org.springframework.stereotype.Component;
import top.javarem.domain.activity.service.rule.AbstractActivityChain;

/**
 * @Author: rem
 * @Date: 2024/12/02/14:54
 * @Description:
 */
@Component("activity_sku_stock_filter")
public class SkuStockFilterChain extends AbstractActivityChain {

    @Override
    public void execute() {
        System.out.println("DefaultActivityChain");
    }

}
