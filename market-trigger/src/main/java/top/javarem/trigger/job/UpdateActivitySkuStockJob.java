package top.javarem.trigger.job;

import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.javarem.domain.activity.model.vo.ActivityStockDecrQueueVO;
import top.javarem.domain.activity.service.IRaffleActivitySkuStockService;

import java.util.concurrent.TimeUnit;

/**
 * @Author: rem
 * @Date: 2024/12/03/14:37
 * @Description:活动库存扣减定时任务
 */
@Component
@Slf4j
public class UpdateActivitySkuStockJob {

    @Autowired
    private IRaffleActivitySkuStockService skuStock;

    @Autowired
    private RedissonClient redissonClient;

//    @Scheduled(cron = "0/30 * * * * ?")
    @XxlJob("UpdateActivitySkuStockJob")
    public void executeActivitySkuStockJob() {
        RLock lock = redissonClient.getLock("rem-market-UpdateActivitySkuStockJob");
        boolean isLocked = false;
        try {
            isLocked =lock.tryLock(3, 0, TimeUnit.SECONDS);
            if (!isLocked) return;
            log.info("活动库存扣减定时任务");
            while (!skuStock.isEmptySkuStockDecrQueue()) {
                ActivityStockDecrQueueVO activityStockDecrQueueVO = skuStock.handleSkuStockDecrQueue();
                skuStock.updateSkuStock(activityStockDecrQueueVO);
            }
        } catch (Exception e) {
            log.info("活动库存扣减定时任务失败", e);
        }
    }

}
