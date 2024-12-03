package top.javarem.infrastructure.adapter.repository;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;
import top.javarem.domain.activity.model.aggregate.ActivityOrderAggregate;
import top.javarem.domain.activity.model.entity.*;
import top.javarem.domain.activity.repository.IActivityRepository;
import top.javarem.infrastructure.dao.entity.*;
import top.javarem.infrastructure.dao.iService.*;
import top.javarem.infrastructure.dao.iService.Impl.RaffleActivityAccountServiceImpl;
import top.javarem.infrastructure.dao.iService.Impl.RaffleActivityOrderServiceImpl;
import top.javarem.infrastructure.dao.mapper.RaffleActivityAccountMapper;
import top.javarem.infrastructure.dao.mapper.RaffleActivityOrderMapper;
import top.javarem.types.common.constants.Constants;
import top.javarem.types.enums.ResponseCode;
import top.javarem.types.exception.AppException;

import java.time.LocalDateTime;

/**
 * @Author: rem
 * @Date: 2024/12/01/23:39
 * @Description:
 */
@Repository
@Slf4j
public class ActivityRepository implements IActivityRepository {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RaffleActivityService activityService;

    @Autowired
    private RaffleActivityCountService activityCountService;

    @Autowired
    private RaffleActivitySkuService skuService;

    @Autowired
    private RaffleActivityOrderService activityOrderService;

    @Autowired
    private RaffleActivityAccountService activityAccountService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Override
    public ActivitySkuEntity getActivitySku(Long sku) {
        RaffleActivitySku activitySku = skuService.lambdaQuery()
                .select(RaffleActivitySku::getSku, RaffleActivitySku::getActivityId, RaffleActivitySku::getActivityCountId, RaffleActivitySku::getStockCount, RaffleActivitySku::getStockCountSurplus)
                .eq(RaffleActivitySku::getSku, sku)
                .one();
        ActivitySkuEntity skuEntity = new ActivitySkuEntity();
        BeanUtils.copyProperties(activitySku, skuEntity);
        return skuEntity;
    }

    @Override
    public ActivityEntity getActivityById(Long activityId) {
        String cacheKey = Constants.RedisKey.ACTIVITY_KEY + activityId;
        ActivityEntity activityEntity = redissonClient.<ActivityEntity>getBucket(cacheKey).get();
        if (activityEntity != null) {
            return activityEntity;
        }
        RaffleActivity raffleActivity = activityService.lambdaQuery()
                .select(RaffleActivity::getActivityId, RaffleActivity::getActivityName, RaffleActivity::getActivityDesc, RaffleActivity::getBeginTime, RaffleActivity::getEndTime, RaffleActivity::getStrategyId, RaffleActivity::getStatus)
                .eq(RaffleActivity::getActivityId, activityId)
                .one();
        activityEntity = new ActivityEntity();
        BeanUtils.copyProperties(raffleActivity, activityEntity);
        redissonClient.<ActivityEntity>getBucket(cacheKey).set(activityEntity);
        return activityEntity;
    }

    @Override
    public ActivityCountEntity getActivityCount(Long activityCountId) {
        String cacheKey = Constants.RedisKey.ACTIVITY_COUNT_KEY + activityCountId;
        ActivityCountEntity activityCountEntity = redissonClient.<ActivityCountEntity>getBucket(cacheKey).get();
        if (activityCountEntity != null) {
            return activityCountEntity;
        }
        RaffleActivityCount raffleActivityCount = activityCountService.lambdaQuery()
                .select(RaffleActivityCount::getActivityCountId, RaffleActivityCount::getTotalCount, RaffleActivityCount::getDayCount, RaffleActivityCount::getMonthCount)
                .eq(RaffleActivityCount::getActivityCountId, activityCountId)
                .one();
        activityCountEntity = new ActivityCountEntity();
        BeanUtils.copyProperties(raffleActivityCount, activityCountEntity);
        redissonClient.<ActivityCountEntity>getBucket(cacheKey).set(activityCountEntity);
        return activityCountEntity;
    }

    @Override
    public void saveOrder(ActivityOrderAggregate activityOrderAggregate) {
//        创建订单对象
        ActivityOrderEntity activityOrderEntity = activityOrderAggregate.getActivityOrder();
        RaffleActivityOrder raffleActivityOrder = new RaffleActivityOrder();
        BeanUtils.copyProperties(activityOrderEntity, raffleActivityOrder);
        raffleActivityOrder.setCreateTime(LocalDateTime.now());
        raffleActivityOrder.setUpdateTime(LocalDateTime.now());

//        创建账户对象
        RaffleActivityAccount raffleActivityAccount = new RaffleActivityAccount();
        raffleActivityAccount.setUserId(activityOrderAggregate.getUserId());
        raffleActivityAccount.setActivityId(activityOrderAggregate.getActivityId());
        raffleActivityAccount.setTotalCount(activityOrderAggregate.getTotalCount());
        raffleActivityAccount.setTotalCountSurplus(activityOrderAggregate.getTotalCount());
        raffleActivityAccount.setDayCount(activityOrderAggregate.getDayCount());
        raffleActivityAccount.setDayCountSurplus(activityOrderAggregate.getDayCount());
        raffleActivityAccount.setMonthCount(activityOrderAggregate.getMonthCount());
        raffleActivityAccount.setMonthCountSurplus(activityOrderAggregate.getMonthCount());
        raffleActivityAccount.setCreateTime(LocalDateTime.now());
        raffleActivityAccount.setUpdateTime(LocalDateTime.now());

//        将订单写入
        transactionTemplate.execute(status -> {
            try {
                activityOrderService.save(raffleActivityOrder);
                int result = activityAccountService.updateAccount(raffleActivityAccount);
                if (result == 0) {
                    activityAccountService.save(raffleActivityAccount);
                }
                return 1;
            } catch (DuplicateKeyException e) {
                status.setRollbackOnly();
                log.error("写入订单记录，唯一索引冲突 userId: {} activityId: {} sku: {}", activityOrderEntity.getUserId(), activityOrderEntity.getActivityId(), activityOrderEntity.getSku(), e);
                throw new AppException(ResponseCode.INDEX_DUP.getCode());
            }
        });

    }

}
