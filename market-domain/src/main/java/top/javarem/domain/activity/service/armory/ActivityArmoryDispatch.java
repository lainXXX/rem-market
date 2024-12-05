package top.javarem.domain.activity.service.armory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.javarem.domain.activity.model.entity.ActivitySkuEntity;
import top.javarem.domain.activity.repository.IActivityRepository;
import top.javarem.types.common.constants.Constants;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author: rem
 * @Date: 2024/12/03/18:35
 * @Description:
 */
@Service
public class ActivityArmoryDispatch implements IActivityArmory, IActivityDispatch {

    @Autowired
    private IActivityRepository activityRepository;

    @Override
    public Boolean assembleRaffleActivity(Long sku) {

//        1.查询活动sku
        ActivitySkuEntity activitySkuEntity = activityRepository.getActivitySku(sku);
        cacheActivitySku(sku, activitySkuEntity.getStockCount());
//        2.预热活动
        activityRepository.getActivityById(activitySkuEntity.getActivityId());
//        3.预热活动参与次数
        activityRepository.getActivityCount(activitySkuEntity.getActivityCountId());

        return true;
    }

    private void cacheActivitySku(Long sku, Integer stockCount) {
        String cacheKey = Constants.RedisKey.ACTIVITY_SKU_STOCK_KEY + sku;
        activityRepository.cacheActivityStock(cacheKey, stockCount);
    }

    @Override
    public Boolean deductActivityStock(Long sku, Date endTime) {
        return activityRepository.deductActivityStock(sku, endTime);
    }
}
