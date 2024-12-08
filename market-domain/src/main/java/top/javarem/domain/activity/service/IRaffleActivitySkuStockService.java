package top.javarem.domain.activity.service;

import top.javarem.domain.activity.model.vo.ActivityStockDecrQueueVO;

/**
 * @Author: rem
 * @Date: 2024/12/03/14:35
 * @Description:
 */
public interface IRaffleActivitySkuStockService {

    ActivityStockDecrQueueVO handleSkuStockDecrQueue();

    Boolean updateSkuStock(ActivityStockDecrQueueVO activityStockDecrQueueVO);

    Boolean isEmptySkuStockDecrQueue();

    void clearActivitySkuStock(Long sku);

    void clearSkuStockDecrQueue();
}
