package top.javarem.trigger.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.javarem.domain.strategy.model.vo.AwardStockDecrQueueVO;
import top.javarem.domain.strategy.service.IRaffleStock;

/**
 * @Author: rem
 * @Date: 2024/11/28/22:35
 * @Description: 更新奖品库存任务
 */
@Component
@Slf4j
public class UpdateAwardStockJob {

    @Autowired
    private IRaffleStock raffleStock;

    @Scheduled(cron = "0 0/30 * * * ?")
    public void executeAwardStockJob() {
        try {
            log.info("更新奖品库存任务");
            while (!raffleStock.isEmptyStockDecrQueue()) {
                AwardStockDecrQueueVO awardStockDecrQueueVO = raffleStock.handleQueueValue();
                Boolean result = raffleStock.updateAwardStock(awardStockDecrQueueVO);
                log.info("{} strategyId:{} awardId:{}", result, awardStockDecrQueueVO.getStrategyId(), awardStockDecrQueueVO.getAwardId());
            }

        } catch (Exception e) {
            log.error("扣减库存失败");
        }

    }

}
