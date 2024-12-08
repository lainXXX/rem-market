package top.javarem.infrastructure.adapter.repository;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.CollectionUtils;
import top.javarem.domain.activity.event.ActivitySkuStockZeroEventMessage;
import top.javarem.domain.activity.model.aggregate.CreatePartakeOrderAggregate;
import top.javarem.domain.activity.model.aggregate.CreateQuotaOrderAggregate;
import top.javarem.domain.activity.model.entity.*;
import top.javarem.domain.activity.model.vo.ActivityStockDecrQueueVO;
import top.javarem.domain.activity.model.vo.UserRaffleStatus;
import top.javarem.domain.activity.repository.IActivityRepository;
import top.javarem.infrastructure.dao.entity.*;
import top.javarem.infrastructure.dao.iService.*;
import top.javarem.infrastructure.event.EventPublisher;
import top.javarem.types.common.constants.Constants;
import top.javarem.types.enums.ResponseCode;
import top.javarem.types.exception.AppException;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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
    private UserAwardRecordService userAwardRecordService;

    @Autowired
    private RaffleActivityAccountMonthCountService accountMonthCountService;

    @Autowired
    private RaffleActivityAccountDayCountService accountDayCountService;

    @Autowired
    private UserRaffleOrderService userRaffleOrderService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private EventPublisher eventPublisher;

    @Autowired
    private ActivitySkuStockZeroEventMessage activitySkuStockZeroEventMessage;


    @Override
    public ActivitySkuEntity getActivitySku(Long sku) {
        String skuKey = Constants.RedisKey.ACTIVITY_SKU_KEY + sku;
        ActivitySkuEntity activitySkuEntity = redissonClient.<ActivitySkuEntity>getBucket(skuKey).get();
        String surplusStockKey = Constants.RedisKey.ACTIVITY_SKU_STOCK_KEY + sku;
        Long surplusStock = redissonClient.getAtomicLong(surplusStockKey).get();
//        原本时应该对surplusStock非空判断 但是redissonClient.getAtomicLong(surplusStockKey).get()在redis没有数据时会默认返回0L 所以没必要了
        if (activitySkuEntity != null) {
            activitySkuEntity.setStockCountSurplus(surplusStock.intValue());
            return activitySkuEntity;
        }
        RaffleActivitySku activitySku = skuService.lambdaQuery()
                .select(RaffleActivitySku::getSku, RaffleActivitySku::getActivityId, RaffleActivitySku::getActivityCountId, RaffleActivitySku::getStockCount, RaffleActivitySku::getStockCountSurplus)
                .eq(RaffleActivitySku::getSku, sku)
                .one();
        activitySkuEntity = new ActivitySkuEntity();
        BeanUtils.copyProperties(activitySku, activitySkuEntity);
        activitySkuEntity.setStockCountSurplus(surplusStock.intValue());
        redissonClient.getBucket(skuKey).set(activitySkuEntity);
        return activitySkuEntity;
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
    public void saveOrder(CreateQuotaOrderAggregate createQuotaOrderAggregate) {
//        创建订单对象
        ActivityOrderEntity activityOrderEntity = createQuotaOrderAggregate.getActivityOrder();
        RaffleActivityOrder raffleActivityOrder = new RaffleActivityOrder();
        BeanUtils.copyProperties(activityOrderEntity, raffleActivityOrder);
        raffleActivityOrder.setCreateTime(LocalDateTime.now());
        raffleActivityOrder.setUpdateTime(LocalDateTime.now());

//        创建账户对象
        RaffleActivityAccount raffleActivityAccount = new RaffleActivityAccount();
        raffleActivityAccount.setUserId(createQuotaOrderAggregate.getUserId());
        raffleActivityAccount.setActivityId(createQuotaOrderAggregate.getActivityId());
        raffleActivityAccount.setTotalCount(createQuotaOrderAggregate.getTotalCount());
        raffleActivityAccount.setTotalCountSurplus(createQuotaOrderAggregate.getTotalCount());
        raffleActivityAccount.setDayCount(createQuotaOrderAggregate.getDayCount());
        raffleActivityAccount.setDayCountSurplus(createQuotaOrderAggregate.getDayCount());
        raffleActivityAccount.setMonthCount(createQuotaOrderAggregate.getMonthCount());
        raffleActivityAccount.setMonthCountSurplus(createQuotaOrderAggregate.getMonthCount());
        raffleActivityAccount.setCreateTime(new Date());
        raffleActivityAccount.setUpdateTime(new Date());

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

    @Override
    public void cacheActivityStock(String key, Integer stockCount) {
        if (redissonClient.getAtomicLong(key).isExists()) return;
        redissonClient.getAtomicLong(key).set(stockCount);
    }

    @Override
    public Boolean deductActivityStock(Long sku, Date endTime) {

//        1.分段扣减库存
        String cacheKey = Constants.RedisKey.ACTIVITY_SKU_STOCK_KEY + sku;
        long surplus = redissonClient.getAtomicLong(cacheKey).decrementAndGet();
//        2.如果库存扣减为0 则发送mq消息使数据库更新
        if (surplus == 0) {
            eventPublisher.publish(activitySkuStockZeroEventMessage.topic(), activitySkuStockZeroEventMessage.buildEEventMessage(sku));
            return false;
        }
//        3.库存<0 则把库存设置为0 返回false
        if (surplus < 0) {
            redissonClient.getAtomicLong(cacheKey).set(0);
            return false;
        }
//        4.给库存加锁
        String lock = Constants.RedisKey.ACTIVITY_SKU_STOCK_KEY + sku + Constants.UNDERLINE + surplus;
//        锁过期的毫秒值
        long expireMills = endTime.getTime() - System.currentTimeMillis();
        boolean isLock = redissonClient.getBucket(lock).trySet(Constants.ACTIVITY_SURPLUS_STOCK_LOCK, expireMills, TimeUnit.MILLISECONDS);
        if (!isLock) {
            log.info("活动剩余库存加锁失败 {}", lock);
        }
        return isLock;
    }

    @Override
    public void sendActivityStockDecrQueue(ActivityStockDecrQueueVO activityStockDecrQueueVO) {
        String queueKey = Constants.RedisKey.ACTIVITY_SKU_STOCK_DECR_QUEUE_KEY;
        RBlockingQueue<ActivityStockDecrQueueVO> blockingQueue = redissonClient.getBlockingQueue(queueKey);
        redissonClient.getDelayedQueue(blockingQueue).offer(activityStockDecrQueueVO, 3, TimeUnit.SECONDS);
    }

    @Override
    public ActivityStockDecrQueueVO handleSkuStockDecrQueue() {
        String queueKey = Constants.RedisKey.ACTIVITY_SKU_STOCK_DECR_QUEUE_KEY;
//        从阻塞队列中弹出一个元素
        return redissonClient.<ActivityStockDecrQueueVO>getBlockingQueue(queueKey).poll();
    }

    @Override
    public Boolean updateSkuStock(ActivityStockDecrQueueVO activityStockDecrQueueVO) {
        return skuService.lambdaUpdate()
                .setSql("stock_count_surplus = stock_count_surplus - 1")
                .set(RaffleActivitySku::getUpdateTime, LocalDateTime.now())
                .eq(RaffleActivitySku::getSku, activityStockDecrQueueVO.getSku())
                .eq(RaffleActivitySku::getActivityId, activityStockDecrQueueVO.getActivityId())
                .update();
    }

    @Override
    public Boolean isEmptySkuStockDecrQueue() {
        String queueKey = Constants.RedisKey.ACTIVITY_SKU_STOCK_DECR_QUEUE_KEY;
        RBlockingQueue<ActivityStockDecrQueueVO> blockingQueue = redissonClient.getBlockingQueue(queueKey);
        return CollectionUtils.isEmpty(blockingQueue);
    }

    @Override
    public void clearActivitySkuStock(Long sku) {
        skuService.lambdaUpdate()
                .set(RaffleActivitySku::getStockCountSurplus, 0)
                .set(RaffleActivitySku::getUpdateTime, LocalDateTime.now())
                .eq(RaffleActivitySku::getSku, sku)
                .update();
    }

    @Override
    public void clearSkuStockDecrQueue() {
        String cacheKey = Constants.RedisKey.ACTIVITY_SKU_STOCK_DECR_QUEUE_KEY;
//        清空阻塞队列
        redissonClient.getBlockingQueue(cacheKey).clear();
    }

    @Override
    public UserConsumeOrderEntity getUserUnconsumedOrder(String userId, Long activityId) {

        UserAwardRecord userAwardRecord = userAwardRecordService.lambdaQuery()
                .select(UserAwardRecord::getUserId, UserAwardRecord::getStrategyId, UserAwardRecord::getActivityId, UserAwardRecord::getOrderId, UserAwardRecord::getCreateTime, UserAwardRecord::getStatus)
                .eq(UserAwardRecord::getUserId, userId)
                .eq(UserAwardRecord::getActivityId, activityId)
                .one();
        if (userAwardRecord == null) {
            return null;
        }
        return UserConsumeOrderEntity.builder()
                .orderId(userAwardRecord.getOrderId())
                .userId(userId)
                .activityId(activityId)
                .strategyId(userAwardRecord.getStrategyId())
                .status(UserRaffleStatus.valueOf(userAwardRecord.getStatus()))
                .orderTime(userAwardRecord.getCreateTime())
                .build();
    }

    @Override
    public ActivityAccountCountEntity getActivityAccountCount(String userId, Long activityId) {
        String cacheKey = Constants.RedisKey.ACTIVITY_ACCOUNT_KEY + userId + Constants.UNDERLINE + activityId;
        ActivityAccountCountEntity activityAccountCountEntity = redissonClient.<ActivityAccountCountEntity>getBucket(cacheKey).get();
        if (activityAccountCountEntity != null) {
            return activityAccountCountEntity;
        }
        RaffleActivityAccount raffleActivityAccount = activityAccountService.lambdaQuery()
                .select(RaffleActivityAccount::getActivityId, RaffleActivityAccount::getUserId, RaffleActivityAccount::getTotalCount, RaffleActivityAccount::getTotalCountSurplus, RaffleActivityAccount::getDayCount, RaffleActivityAccount::getDayCountSurplus, RaffleActivityAccount::getMonthCount, RaffleActivityAccount::getMonthCountSurplus)
                .eq(RaffleActivityAccount::getUserId, userId)
                .eq(RaffleActivityAccount::getActivityId, activityId)
                .one();
        if (raffleActivityAccount == null) {
            return null;
        }
        activityAccountCountEntity = new ActivityAccountCountEntity();
        BeanUtils.copyProperties(raffleActivityAccount, activityAccountCountEntity);
        redissonClient.<ActivityAccountCountEntity>getBucket(cacheKey).set(activityAccountCountEntity);
        return activityAccountCountEntity;
    }

    @Override
    public ActivityAccountMonthCountEntity getActivityAccountMonthCount(String userId, Long activityId, String month) {

        RaffleActivityAccountMonthCount raffleActivityAccountMonthCount = accountMonthCountService.lambdaQuery()
                .select(RaffleActivityAccountMonthCount::getUserId, RaffleActivityAccountMonthCount::getActivityId, RaffleActivityAccountMonthCount::getMonth, RaffleActivityAccountMonthCount::getMonthCount, RaffleActivityAccountMonthCount::getMonthCountSurplus)
                .eq(RaffleActivityAccountMonthCount::getUserId, userId)
                .eq(RaffleActivityAccountMonthCount::getActivityId, activityId)
                .eq(RaffleActivityAccountMonthCount::getMonth, month)
                .one();

        if (raffleActivityAccountMonthCount == null) {
            return null;
        }

        ActivityAccountMonthCountEntity activityAccountMonthCountEntity = new ActivityAccountMonthCountEntity();
        BeanUtils.copyProperties(raffleActivityAccountMonthCount, activityAccountMonthCountEntity);
        return activityAccountMonthCountEntity;


    }

    @Override
    public ActivityAccountDayCountEntity getActivityAccountDayCount(String userId, Long activityId, String day) {
        RaffleActivityAccountDayCount raffleActivityAccountDayCount = accountDayCountService.lambdaQuery()
                .select(RaffleActivityAccountDayCount::getUserId, RaffleActivityAccountDayCount::getActivityId, RaffleActivityAccountDayCount::getDay, RaffleActivityAccountDayCount::getDayCount, RaffleActivityAccountDayCount::getDayCountSurplus)
                .eq(RaffleActivityAccountDayCount::getUserId, userId)
                .eq(RaffleActivityAccountDayCount::getActivityId, activityId)
                .eq(RaffleActivityAccountDayCount::getDay, day)
                .one();
        if (raffleActivityAccountDayCount == null) {
            return null;
        }
        ActivityAccountDayCountEntity activityAccountDayCountEntity = new ActivityAccountDayCountEntity();
        BeanUtils.copyProperties(raffleActivityAccountDayCount, activityAccountDayCountEntity);
        return activityAccountDayCountEntity;
    }

    @Override
    public void saveCreatePartakeOrderAggregate(CreatePartakeOrderAggregate createPartakeOrderAggregate) {

        String userId = createPartakeOrderAggregate.getUserId();
        Long activityId = createPartakeOrderAggregate.getActivityId();

        transactionTemplate.execute(status -> {
            try {
                ActivityAccountCountEntity activityAccountCountEntity = createPartakeOrderAggregate.getActivityAccountCountEntity();
//            更新总账户
                int updateTotal = activityAccountService.updateAccountDeductQuota(RaffleActivityAccount.builder()
                        .userId(activityAccountCountEntity.getUserId())
                        .activityId(activityAccountCountEntity.getActivityId())
                        .build());
                if (updateTotal != 1) {
//                如果失败则回滚
                    status.setRollbackOnly();
                    log.warn("扣减账户额度失败 userId :{} activityId: {}", userId, activityId);
                    throw new AppException(ResponseCode.ERROR_ACCOUNT_TOTAL_QUOTA.getInfo(), ResponseCode.ERROR_ACCOUNT_TOTAL_QUOTA.getInfo());
                }
//            如果账户月额定存在则更新数据 不存在则插入
                ActivityAccountMonthCountEntity activityAccountMonthCountEntity = createPartakeOrderAggregate.getActivityAccountMonthCountEntity();

                if (createPartakeOrderAggregate.getIsExistMonthCount()) {
                    boolean updateMonth = accountMonthCountService.lambdaUpdate()
                            .setSql("month_count_surplus = month_count_surplus - 1, , update_time = now()")
                            .eq(RaffleActivityAccountMonthCount::getUserId, userId)
                            .eq(RaffleActivityAccountMonthCount::getActivityId, activityId)
                            .eq(RaffleActivityAccountMonthCount::getMonth, activityAccountMonthCountEntity.getMonth())
                            .update();
                    if (!updateMonth) {
                        status.setRollbackOnly();
                        log.warn("写入创建参与活动记录，更新月账户额度不足，异常 userId: {} activityId: {} month: {}", userId, activityId, activityAccountMonthCountEntity.getMonth());
                        throw new AppException(ResponseCode.ERROR_ACCOUNT_MONTH_QUOTA.getCode(), ResponseCode.ERROR_ACCOUNT_MONTH_QUOTA.getInfo());
                    }
                } else {
                    accountMonthCountService.save(RaffleActivityAccountMonthCount.builder()
                            .userId(userId)
                            .activityId(activityId)
                            .month(activityAccountMonthCountEntity.getMonth())
                            .monthCount(activityAccountMonthCountEntity.getMonthCount())
                            .monthCountSurplus(activityAccountMonthCountEntity.getMonthCountSurplus() - 1)
                            .createTime(new Date())
                            .updateTime(new Date())
                            .build());
                }
                ActivityAccountDayCountEntity activityAccountDayCountEntity = createPartakeOrderAggregate.getActivityAccountDayCountEntity();

                if (createPartakeOrderAggregate.getIsExistDayCount()) {
                    boolean updateDay = accountDayCountService.lambdaUpdate()
                            .setSql("day_count_surplus = month_count_surplus - 1, , update_time = now()")
                            .eq(RaffleActivityAccountDayCount::getUserId, userId)
                            .eq(RaffleActivityAccountDayCount::getActivityId, activityId)
                            .eq(RaffleActivityAccountDayCount::getDay, activityAccountDayCountEntity.getDay())
                            .update();
                    if (!updateDay) {
                        // 未更新成功则回滚
                        status.setRollbackOnly();
                        log.warn("写入创建参与活动记录，更新日账户额度不足，异常 userId: {} activityId: {} day: {}", userId, activityId, activityAccountDayCountEntity.getDay());
                        throw new AppException(ResponseCode.ERROR_ACCOUNT_DAY_QUOTA.getCode(), ResponseCode.ERROR_ACCOUNT_DAY_QUOTA.getInfo());
                    }
                } else {
                    accountDayCountService.save(RaffleActivityAccountDayCount.builder()
                            .userId(userId)
                            .activityId(activityId)
                            .day(activityAccountDayCountEntity.getDay())
                            .dayCount(activityAccountDayCountEntity.getDayCount())
                            .dayCountSurplus(activityAccountDayCountEntity.getDayCountSurplus() - 1)
                            .createTime(new Date())
                            .updateTime(new Date())
                            .build());
                }

//                写入抽奖消费记录单
                UserConsumeOrderEntity userConsumeOrderEntity = createPartakeOrderAggregate.getUserConsumeOrderEntity();
                userRaffleOrderService.save(UserRaffleOrder.builder()
                                .userId(userId)
                                .activityId(activityId)
                                .activityName(userConsumeOrderEntity.getActivityName())
                                .strategyId(userConsumeOrderEntity.getStrategyId())
                                .orderId(userConsumeOrderEntity.getOrderId())
                                .orderTime(userConsumeOrderEntity.getOrderTime())
                                .status(userConsumeOrderEntity.getStatus().getCode())
                                .createTime(new Date())
                                .updateTime(new Date())
                        .build());

                return 1;
            } catch (DuplicateKeyException e) {
                status.setRollbackOnly();
                log.error("写入创建参与活动记录，唯一索引冲突 userId: {} activityId: {}", userId, activityId, e);
                throw new AppException(ResponseCode.INDEX_DUP.getCode(), e);
            }
        });

    }

}
