package top.javarem.domain.activity.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import top.javarem.domain.activity.model.aggregate.ActivityOrderAggregate;
import top.javarem.domain.activity.model.entity.*;
import top.javarem.domain.activity.repository.IActivityRepository;
import top.javarem.domain.activity.service.rule.IActivityChain;
import top.javarem.domain.activity.service.rule.factory.DefaultActivityChainFactory;
import top.javarem.types.common.constants.Constants;
import top.javarem.types.exception.AppException;

/**
 * @Author: rem
 * @Date: 2024/12/01/23:27
 * @Description:
 */
@Slf4j
public abstract class AbstractRaffleActivity implements IRaffleOrder{

    protected IActivityRepository repository;

    protected DefaultActivityChainFactory factory;

    public AbstractRaffleActivity(IActivityRepository repository, DefaultActivityChainFactory factory) {
        this.repository = repository;
        this.factory = factory;
    }

    @Override
    public String createActivityOrder(SkuRechargeEntity skuRechargeEntity) {
// 1. 参数校验
        String userId = skuRechargeEntity.getUserId();
        Long sku = skuRechargeEntity.getSku();
        String outBusinessNo = skuRechargeEntity.getOutBusinessNo();
        if (null == sku || StringUtils.isBlank(userId) || StringUtils.isBlank(outBusinessNo)) {
            throw new AppException(Constants.ResponseCode.ILLEGAL_PARAMETER.getCode(), Constants.ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
//        2.1.通过sku查询活动信息
        ActivitySkuEntity skuEntity = repository.getActivitySku(sku);
//        2.2.查询活动信息
        ActivityEntity activityEntity = repository.getActivityById(skuEntity.getActivityId());
//        2.3.查询次数信息(用户可参与该活动的次数)
        ActivityCountEntity countEntity = repository.getActivityCount(skuEntity.getActivityCountId());
        log.info("查询结果：{} {} {}", skuEntity, activityEntity, countEntity);

//        3.开启活动规则过滤责任链
        IActivityChain chain = factory.openActivityChain();
        chain.execute();

//        4.构建活动下单聚合对象
        ActivityOrderAggregate activityOrderAggregate = buildOrderAggregate(skuRechargeEntity, skuEntity, activityEntity, countEntity);

//        5.保存订单
        saveOrder(activityOrderAggregate);

        return null;
    }

    protected abstract ActivityOrderAggregate buildOrderAggregate(SkuRechargeEntity skuRechargeEntity, ActivitySkuEntity skuEntity, ActivityEntity activityEntity, ActivityCountEntity countEntity);

    protected abstract void saveOrder(ActivityOrderAggregate activityOrderAggregate);
}
