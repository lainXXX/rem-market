package top.javarem.trigger.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.javarem.domain.strategy.model.vo.AwardStockQueueKeyVO;
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

    @Scheduled(cron = "0/5 * * * * ?")
    public void executeAwardStockJob() {
        try {
            AwardStockQueueKeyVO awardStockQueueKeyVO = raffleStock.handleQueueValue();
            if (awardStockQueueKeyVO == null) {
                log.info("执行库存扣减任务 - 没有需要处理的业务");
                return;
            }
            while (awardStockQueueKeyVO != null) {
                Boolean result = raffleStock.updateAwardStock(awardStockQueueKeyVO);
                log.info("批量扣减库存任务:{} strategyId:{} awardId:{}", result, awardStockQueueKeyVO.getStrategyId(), awardStockQueueKeyVO.getAwardId());
                awardStockQueueKeyVO = raffleStock.handleQueueValue();
            }

        } catch (Exception e) {
            log.error("扣减库存失败");
        }

    }

}
