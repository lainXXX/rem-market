package top.javarem.domain.strategy.service;

import top.javarem.domain.strategy.model.vo.AwardStockDecrQueueVO;

/**
 * @Author: rem
 * @Date: 2024/11/28/16:26
 * @Description: 抽奖库存接口
 */
public interface IRaffleStock {

    AwardStockDecrQueueVO handleQueueValue();

    Boolean updateAwardStock(AwardStockDecrQueueVO queueKeyVO);

    Boolean isEmptyStockDecrQueue();
}
