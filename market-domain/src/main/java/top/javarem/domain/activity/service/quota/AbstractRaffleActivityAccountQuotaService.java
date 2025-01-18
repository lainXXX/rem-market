package top.javarem.domain.activity.service.quota;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import top.javarem.domain.activity.model.aggregate.CreateQuotaOrderAggregate;
import top.javarem.domain.activity.model.entity.*;
import top.javarem.domain.activity.repository.IActivityRepository;
import top.javarem.domain.activity.service.IRaffleActivityAccountQuotaService;
import top.javarem.domain.activity.service.quota.policy.ITradePolicy;
import top.javarem.domain.activity.service.quota.rule.IActivityChain;
import top.javarem.domain.activity.service.quota.rule.factory.DefaultActivityChainFactory;
import top.javarem.types.common.constants.Constants;
import top.javarem.types.exception.AppException;

import java.util.Map;

/**
 * @Author: rem
 * @Date: 2024/12/01/23:27
 * @Description: 抽奖活动抽象类 定义活动流程的标准
 */
@Slf4j
public abstract class AbstractRaffleActivityAccountQuotaService implements IRaffleActivityAccountQuotaService {

    private final Map<String, ITradePolicy> tradePolicyGroup;

    protected IActivityRepository repository;

    protected DefaultActivityChainFactory factory;

    public AbstractRaffleActivityAccountQuotaService(IActivityRepository repository, DefaultActivityChainFactory factory, Map<String, ITradePolicy> tradePolicyGroup) {
        this.tradePolicyGroup = tradePolicyGroup;
        this.repository = repository;
        this.factory = factory;
    }

    @Override
    public String createOrder(SkuRechargeEntity skuRechargeEntity) {
//        1. 参数校验
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
        Boolean executeResult = chain.execute(DefaultActivityChainFactory.ActivityChainFilterVO.builder()
                .sku(skuEntity.getSku())
                .activityId(skuEntity.getActivityId())
                .skuSurplusStock(skuEntity.getStockCountSurplus())
                .status(activityEntity.getStatus())
                .beginTime(activityEntity.getBeginTime())
                .endTime(activityEntity.getEndTime())
                .build());

//        4.构建活动下单聚合对象
        CreateQuotaOrderAggregate createQuotaOrderAggregate = buildOrderAggregate(skuRechargeEntity, skuEntity, activityEntity, countEntity);
        log.info("{}", createQuotaOrderAggregate);
//        5. 交易策略 - 【积分兑换，支付类订单】【返利无支付交易订单，直接充值到账】【订单状态变更交易类型策略】
        ITradePolicy tradePolicy = tradePolicyGroup.get(skuRechargeEntity.getOrderTradeTypeVO().getCode());
        tradePolicy.trade(createQuotaOrderAggregate);
        return createQuotaOrderAggregate.getActivityOrder().getOrderId();
    }

    protected abstract CreateQuotaOrderAggregate buildOrderAggregate(SkuRechargeEntity skuRechargeEntity, ActivitySkuEntity skuEntity, ActivityEntity activityEntity, ActivityCountEntity countEntity);

    protected abstract void saveOrder(CreateQuotaOrderAggregate createQuotaOrderAggregate);
}
