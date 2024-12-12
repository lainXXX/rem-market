package top.javarem.trigger.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.javarem.domain.activity.model.vo.ActivityStockDecrQueueVO;
import top.javarem.domain.activity.service.IRaffleActivitySkuStockService;

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

    @Scheduled(cron = "0/30 * * * * ?")
    public void executeActivitySkuStockJob() {
        try {
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
