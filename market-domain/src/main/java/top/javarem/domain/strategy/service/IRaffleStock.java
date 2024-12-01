package top.javarem.domain.strategy.service;

import top.javarem.domain.strategy.model.vo.AwardStockQueueKeyVO;

/**
 * @Author: rem
 * @Date: 2024/11/28/16:26
 * @Description: 抽奖库存接口
 */
public interface IRaffleStock {

    AwardStockQueueKeyVO handleQueueValue();

    Boolean updateAwardStock(AwardStockQueueKeyVO queueKeyVO);
}
