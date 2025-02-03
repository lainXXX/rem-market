package top.javarem.trigger.job;

import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.javarem.domain.strategy.model.vo.AwardStockDecrQueueVO;
import top.javarem.domain.strategy.service.IRaffleStock;

import java.util.concurrent.TimeUnit;

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

    @Autowired
    private RedissonClient redissonClient;

//    @Scheduled(cron = "0 0/1 * * * ?")
    @XxlJob("UpdateAwardStockJob")
    public void executeAwardStockJob() {
        RLock lock = redissonClient.getLock("rem-market-UpdateAwardStockJob");
        boolean isLocked = false;
        try {
            isLocked =lock.tryLock(3, 0, TimeUnit.SECONDS);
            if (!isLocked) return;
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
