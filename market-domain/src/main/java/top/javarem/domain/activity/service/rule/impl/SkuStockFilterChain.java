package top.javarem.domain.activity.service.rule.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.javarem.domain.activity.model.vo.ActivityStockDecrQueueVO;
import top.javarem.domain.activity.repository.IActivityRepository;
import top.javarem.domain.activity.service.armory.IActivityDispatch;
import top.javarem.domain.activity.service.rule.AbstractActivityChain;
import top.javarem.domain.activity.service.rule.factory.DefaultActivityChainFactory;
import top.javarem.types.enums.ResponseCode;
import top.javarem.types.exception.AppException;

/**
 * @Author: rem
 * @Date: 2024/12/02/14:54
 * @Description:库存扣减
 */
@Component("activity_sku_stock_filter")
@Slf4j
public class SkuStockFilterChain extends AbstractActivityChain {

    @Autowired
    private IActivityDispatch activityDispatch;

    @Autowired
    private IActivityRepository activityRepository;

    @Override
    public Boolean execute(DefaultActivityChainFactory.ActivityChainFilterVO chainFilterVO) {
        log.info("执行活动sku库存扣减责任链");
        Boolean result = activityDispatch.deductActivityStock(chainFilterVO.getSku(), chainFilterVO.getEndTime());
        if (result) {
            activityRepository.sendActivityStockDecrQueue(ActivityStockDecrQueueVO.builder()
                    .sku(chainFilterVO.getSku())
                    .activityId(chainFilterVO.getActivityId())
                    .build()
            );
            return true;
        }
//        throw new AppException(ResponseCode.ERROR_ACTIVITY_STOCK_DECR.getInfo(), ResponseCode.ERROR_ACTIVITY_STOCK_DECR.getCode());
        return false;
    }

}
