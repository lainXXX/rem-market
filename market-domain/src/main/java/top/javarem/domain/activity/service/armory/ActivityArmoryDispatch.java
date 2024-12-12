package top.javarem.domain.activity.service.armory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.javarem.domain.activity.model.entity.ActivitySkuEntity;
import top.javarem.domain.activity.repository.IActivityRepository;
import top.javarem.types.common.constants.Constants;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

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
    public Boolean assembleRaffleActivityByActivityId(Long activityId) {
//        查询活动sku sku为活动的最小配置单元 一般有多个
        List<ActivitySkuEntity> activitySkuEntityList = activityRepository.getActivitySkuEntityList(activityId);
        for (ActivitySkuEntity skuEntity : activitySkuEntityList) {
//            缓存活动sku库存
            cacheActivitySku(skuEntity.getSku(), skuEntity.getStockCount());
//            预热活动参与次数
            activityRepository.getActivityCount(skuEntity.getActivityCountId());
        }
//        查询并预热活动
        activityRepository.getActivityById(activityId);
        return true;
    }

    @Override
    public Boolean assembleRaffleActivity(Long sku) {

//        1.查询活动sku 并预热
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
