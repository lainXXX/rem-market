package top.javarem.infrastructure.adapter.repository;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RLock;
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

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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

    /**
     * 查询活动sku并存入redis
     *
     * @param sku
     * @return
     */
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
                .select(RaffleActivitySku::getSku, RaffleActivitySku::getActivityId, RaffleActivitySku::getActivityCountId, RaffleActivitySku::getStockCount, RaffleActivitySku::getStockCountSurplus, RaffleActivitySku::getProductAmount)
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
        String lockKey = Constants.RedisKey.ACTIVITY_ACCOUNT_LOCK + createQuotaOrderAggregate.getUserId() + Constants.UNDERLINE + createQuotaOrderAggregate.getActivityId();
        RLock lock = redissonClient.getLock(lockKey);
//        为每个活动的每个用户加一个分布式锁 在同一时刻只能有一个线程进入该方法
        try {
            lock.lock(3, TimeUnit.SECONDS);
            //        创建订单对象
            ActivityOrderEntity activityOrderEntity = createQuotaOrderAggregate.getActivityOrder();
            RaffleActivityOrder raffleActivityOrder = new RaffleActivityOrder();
            BeanUtils.copyProperties(activityOrderEntity, raffleActivityOrder);
            raffleActivityOrder.setCreateTime(LocalDateTime.now());
            raffleActivityOrder.setUpdateTime(LocalDateTime.now());

//        创建账户对象 - 总额度
            RaffleActivityAccount raffleActivityAccount = new RaffleActivityAccount();
            raffleActivityAccount.setUserId(createQuotaOrderAggregate.getUserId());
            raffleActivityAccount.setActivityId(createQuotaOrderAggregate.getActivityId());
            raffleActivityAccount.setTotalCount(createQuotaOrderAggregate.getTotalCount());
            raffleActivityAccount.setTotalCountSurplus(createQuotaOrderAggregate.getTotalCount());
            raffleActivityAccount.setDayCount(createQuotaOrderAggregate.getDayCount());
            raffleActivityAccount.setDayCountSurplus(createQuotaOrderAggregate.getDayCount());
            raffleActivityAccount.setMonthCount(createQuotaOrderAggregate.getMonthCount());
            raffleActivityAccount.setMonthCountSurplus(createQuotaOrderAggregate.getMonthCount());
            raffleActivityAccount.setUpdateTime(new Date());

//        创建账户对象 - 月额度
            RaffleActivityAccountMonthCount raffleActivityAccountMonthCount = new RaffleActivityAccountMonthCount();
            raffleActivityAccountMonthCount.setUserId(createQuotaOrderAggregate.getUserId());
            raffleActivityAccountMonthCount.setActivityId(createQuotaOrderAggregate.getActivityId());
            raffleActivityAccountMonthCount.setMonth(raffleActivityAccountMonthCount.currentMonth());
            raffleActivityAccountMonthCount.setMonthCount(createQuotaOrderAggregate.getMonthCount());
            raffleActivityAccountMonthCount.setMonthCountSurplus(createQuotaOrderAggregate.getMonthCount());
            raffleActivityAccountMonthCount.setUpdateTime(new Date());

//        创建账户对象 - 日额度
            RaffleActivityAccountDayCount raffleActivityAccountDayCount = new RaffleActivityAccountDayCount();
            raffleActivityAccountDayCount.setUserId(createQuotaOrderAggregate.getUserId());
            raffleActivityAccountDayCount.setActivityId(createQuotaOrderAggregate.getActivityId());
            raffleActivityAccountDayCount.setDay(raffleActivityAccountDayCount.today());
            raffleActivityAccountDayCount.setDayCount(createQuotaOrderAggregate.getDayCount());
            raffleActivityAccountDayCount.setDayCountSurplus(createQuotaOrderAggregate.getDayCount());
            raffleActivityAccountDayCount.setUpdateTime(new Date());
//        将订单写入
            transactionTemplate.execute(status -> {
                try {
//                存入订单
                    activityOrderService.save(raffleActivityOrder);
//                更新总账户 如果账户不存在则先创建账户
                    RaffleActivityAccount raffleActivityAccountRes = activityAccountService.getAcitivtyAccountByUserId(createQuotaOrderAggregate.getUserId());
                    if (raffleActivityAccountRes == null) {
                        activityAccountService.save(raffleActivityAccount);
                    }
                    activityAccountService.updateAccount(raffleActivityAccount);
//                更新月账户
                    accountMonthCountService.addAccountQuota(raffleActivityAccountMonthCount);
//                更新日账户
                    accountDayCountService.addAccountQuota(raffleActivityAccountDayCount);
                    return 1;
                } catch (DuplicateKeyException e) {
                    status.setRollbackOnly();
                    log.error("写入订单记录，唯一索引冲突 userId: {} activityId: {} sku: {}", activityOrderEntity.getUserId(), activityOrderEntity.getActivityId(), activityOrderEntity.getSku(), e);
                    throw new AppException(ResponseCode.INDEX_DUP.getCode());
                }
            });
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void saveRebateOrder(CreateQuotaOrderAggregate createQuotaOrderAggregate) {
        String lockKey = Constants.RedisKey.ACTIVITY_ACCOUNT_LOCK + createQuotaOrderAggregate.getUserId() + Constants.UNDERLINE + createQuotaOrderAggregate.getActivityId();
        RLock lock = redissonClient.getLock(lockKey);
//        为每个活动的每个用户加一个分布式锁 在同一时刻只能有一个线程进入该方法
        try {
            lock.lock(3, TimeUnit.SECONDS);
//        创建订单对象
            ActivityOrderEntity activityOrderEntity = createQuotaOrderAggregate.getActivityOrder();
            RaffleActivityOrder raffleActivityOrder = new RaffleActivityOrder();
            BeanUtils.copyProperties(activityOrderEntity, raffleActivityOrder);
            raffleActivityOrder.setCreateTime(LocalDateTime.now());
            raffleActivityOrder.setUpdateTime(LocalDateTime.now());

//        创建账户对象 - 总额度
            RaffleActivityAccount raffleActivityAccount = new RaffleActivityAccount();
            raffleActivityAccount.setUserId(createQuotaOrderAggregate.getUserId());
            raffleActivityAccount.setActivityId(createQuotaOrderAggregate.getActivityId());
            raffleActivityAccount.setTotalCount(createQuotaOrderAggregate.getTotalCount());
            raffleActivityAccount.setTotalCountSurplus(createQuotaOrderAggregate.getTotalCount());
            raffleActivityAccount.setDayCount(createQuotaOrderAggregate.getDayCount());
            raffleActivityAccount.setDayCountSurplus(createQuotaOrderAggregate.getDayCount());
            raffleActivityAccount.setMonthCount(createQuotaOrderAggregate.getMonthCount());
            raffleActivityAccount.setMonthCountSurplus(createQuotaOrderAggregate.getMonthCount());
            raffleActivityAccount.setUpdateTime(new Date());

//        创建账户对象 - 月额度
            RaffleActivityAccountMonthCount raffleActivityAccountMonthCount = new RaffleActivityAccountMonthCount();
            raffleActivityAccountMonthCount.setUserId(createQuotaOrderAggregate.getUserId());
            raffleActivityAccountMonthCount.setActivityId(createQuotaOrderAggregate.getActivityId());
            raffleActivityAccountMonthCount.setMonth(raffleActivityAccountMonthCount.currentMonth());
            raffleActivityAccountMonthCount.setMonthCount(createQuotaOrderAggregate.getMonthCount());
            raffleActivityAccountMonthCount.setMonthCountSurplus(createQuotaOrderAggregate.getMonthCount());
            raffleActivityAccountMonthCount.setUpdateTime(new Date());

//        创建账户对象 - 日额度
            RaffleActivityAccountDayCount raffleActivityAccountDayCount = new RaffleActivityAccountDayCount();
            raffleActivityAccountDayCount.setUserId(createQuotaOrderAggregate.getUserId());
            raffleActivityAccountDayCount.setActivityId(createQuotaOrderAggregate.getActivityId());
            raffleActivityAccountDayCount.setDay(raffleActivityAccountDayCount.today());
            raffleActivityAccountDayCount.setDayCount(createQuotaOrderAggregate.getDayCount());
            raffleActivityAccountDayCount.setDayCountSurplus(createQuotaOrderAggregate.getDayCount());
            raffleActivityAccountDayCount.setUpdateTime(new Date());
//        将订单写入
            transactionTemplate.execute(status -> {
                try {
//                存入订单
                    activityOrderService.save(raffleActivityOrder);
//                更新总账户 如果账户不存在则先创建账户
                    RaffleActivityAccount raffleActivityAccountRes = activityAccountService.getAcitivtyAccountByUserId(createQuotaOrderAggregate.getUserId());
                    if (raffleActivityAccountRes == null) {
                        activityAccountService.save(raffleActivityAccount);
                    }
                    activityAccountService.updateAccount(raffleActivityAccount);
//                更新月账户
                    accountMonthCountService.addAccountQuota(raffleActivityAccountMonthCount);
//                更新日账户
                    accountDayCountService.addAccountQuota(raffleActivityAccountDayCount);
                    return 1;
                } catch (DuplicateKeyException e) {
                    status.setRollbackOnly();
                    log.error("写入订单记录，唯一索引冲突 userId: {} activityId: {} sku: {}", activityOrderEntity.getUserId(), activityOrderEntity.getActivityId(), activityOrderEntity.getSku(), e);
                    throw new AppException(ResponseCode.INDEX_DUP.getCode());
                }
            });
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void saveCreditOrder(CreateQuotaOrderAggregate createQuotaOrderAggregate) {

//        创建订单对象
        ActivityOrderEntity activityOrderEntity = createQuotaOrderAggregate.getActivityOrder();
        RaffleActivityOrder raffleActivityOrder = new RaffleActivityOrder();
        BeanUtils.copyProperties(activityOrderEntity, raffleActivityOrder);
        raffleActivityOrder.setCreateTime(LocalDateTime.now());
        raffleActivityOrder.setUpdateTime(LocalDateTime.now());
//        将订单写入
        transactionTemplate.execute(status -> {
            try {
//                存入订单
                activityOrderService.save(raffleActivityOrder);
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
            eventPublisher.publish(activitySkuStockZeroEventMessage.topic(), activitySkuStockZeroEventMessage.buildEventMessage(sku));
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
    public UserRaffleConsumeOrderEntity getUserUnconsumedOrder(String userId, Long activityId) {
//        查询未消费的用户消费单
        UserRaffleOrder userRaffleOrder = userRaffleOrderService.lambdaQuery()
                .select(UserRaffleOrder::getUserId, UserRaffleOrder::getStrategyId, UserRaffleOrder::getActivityId, UserRaffleOrder::getOrderId, UserRaffleOrder::getCreateTime, UserRaffleOrder::getStatus)
                .eq(UserRaffleOrder::getUserId, userId)
                .eq(UserRaffleOrder::getActivityId, activityId)
                .eq(UserRaffleOrder::getStatus, UserRaffleStatus.CREATE.getCode())
                .one();
        if (userRaffleOrder == null) {
            return null;
        }
        return UserRaffleConsumeOrderEntity.builder()
                .orderId(userRaffleOrder.getOrderId())
                .userId(userId)
                .activityId(activityId)
                .strategyId(userRaffleOrder.getStrategyId())
                .status(UserRaffleStatus.valueOf(userRaffleOrder.getStatus().toUpperCase()))
                .orderTime(userRaffleOrder.getCreateTime())
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

//        TODO 需要路由
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
                            .setSql("month_count_surplus = month_count_surplus - 1, update_time = now()")
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
//            如果账户日额定存在则更新数据 不存在则插入
                if (createPartakeOrderAggregate.getIsExistDayCount()) {
                    boolean updateDay = accountDayCountService.lambdaUpdate()
                            .setSql("day_count_surplus = day_count_surplus - 1, update_time = now()")
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
                UserRaffleConsumeOrderEntity userRaffleConsumeOrderEntity = createPartakeOrderAggregate.getUserRaffleConsumeOrderEntity();
                userRaffleOrderService.save(UserRaffleOrder.builder()
                        .userId(userId)
                        .activityId(activityId)
                        .activityName(userRaffleConsumeOrderEntity.getActivityName())
                        .strategyId(userRaffleConsumeOrderEntity.getStrategyId())
                        .orderId(userRaffleConsumeOrderEntity.getOrderId())
                        .orderTime(userRaffleConsumeOrderEntity.getOrderTime())
                        .status(userRaffleConsumeOrderEntity.getStatus().getCode())
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

    @Override
    public List<ActivitySkuEntity> getActivitySkuEntityList(Long activityId) {
        List<RaffleActivitySku> activitySkuList = skuService.lambdaQuery()
                .select(RaffleActivitySku::getSku, RaffleActivitySku::getActivityId, RaffleActivitySku::getActivityCountId, RaffleActivitySku::getStockCount, RaffleActivitySku::getStockCountSurplus)
                .eq(RaffleActivitySku::getActivityId, activityId)
                .list();
        return activitySkuList.stream()
                .map(sku -> {
                    ActivitySkuEntity activitySkuEntity = new ActivitySkuEntity();
                    BeanUtils.copyProperties(sku, activitySkuEntity);
                    return activitySkuEntity;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Integer getActivityTodayPartakeCount(String userId, Long activityId) {

        if (userId == null || activityId == null) {
            return 0;
        }

        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        RaffleActivityAccountDayCount dayCount = accountDayCountService.lambdaQuery()
                .select(RaffleActivityAccountDayCount::getDayCount, RaffleActivityAccountDayCount::getDayCountSurplus)
                .eq(RaffleActivityAccountDayCount::getUserId, userId)
                .eq(RaffleActivityAccountDayCount::getActivityId, activityId)
                .eq(RaffleActivityAccountDayCount::getDay, today)
                .one();
        if (dayCount.getDayCount() == null || dayCount.getDayCountSurplus() == null) {
            return 0;
        }
//        今天参与活动的次数 = 今日总次数 - 今日剩余次数
        return dayCount.getDayCount() - dayCount.getDayCountSurplus();
    }

    @Override
    public Long getActivityIdByStrategyId(Long strategyId) {
        return activityService.lambdaQuery()
                .select(RaffleActivity::getActivityId)
                .eq(RaffleActivity::getStrategyId, strategyId)
                .one()
                .getActivityId();
    }

    @Override
    public Integer queryRaffleActivityAccountDayPartakeCount(Long activityId, String userId) {

        RaffleActivityAccount raffleActivityAccount = activityAccountService.lambdaQuery()
                .select(RaffleActivityAccount::getTotalCount, RaffleActivityAccount::getTotalCountSurplus)
                .eq(RaffleActivityAccount::getActivityId, activityId)
                .eq(RaffleActivityAccount::getUserId, userId)
                .one();
        return raffleActivityAccount.getTotalCount() - raffleActivityAccount.getTotalCountSurplus();

    }

    @Override
    public void updateOrder(DeliveryOrderEntity deliveryOrderEntity) {

        String lockKey = Constants.RedisKey.ACTIVITY_ACCOUNT_UPDATE_LOCK + deliveryOrderEntity.getUserId();
        RLock lock = redissonClient.getLock(lockKey);
        try {
            lock.lock(3, TimeUnit.SECONDS);
            RaffleActivityOrder raffleActivityOrderRes = activityOrderService.getOrder(deliveryOrderEntity.getUserId(), deliveryOrderEntity.getOutBusinessNo());
            if (raffleActivityOrderRes == null) return;
            //        创建账户对象 - 总额度
            RaffleActivityAccount raffleActivityAccount = new RaffleActivityAccount();
            raffleActivityAccount.setUserId(raffleActivityOrderRes.getUserId());
            raffleActivityAccount.setActivityId(raffleActivityOrderRes.getActivityId());
            raffleActivityAccount.setTotalCount(raffleActivityOrderRes.getTotalCount());
            raffleActivityAccount.setTotalCountSurplus(raffleActivityOrderRes.getTotalCount());
            raffleActivityAccount.setDayCount(raffleActivityOrderRes.getDayCount());
            raffleActivityAccount.setDayCountSurplus(raffleActivityOrderRes.getDayCount());
            raffleActivityAccount.setMonthCount(raffleActivityOrderRes.getMonthCount());
            raffleActivityAccount.setMonthCountSurplus(raffleActivityOrderRes.getMonthCount());
            raffleActivityAccount.setUpdateTime(new Date());

//        创建账户对象 - 月额度
            RaffleActivityAccountMonthCount raffleActivityAccountMonthCount = new RaffleActivityAccountMonthCount();
            raffleActivityAccountMonthCount.setUserId(raffleActivityOrderRes.getUserId());
            raffleActivityAccountMonthCount.setActivityId(raffleActivityOrderRes.getActivityId());
            raffleActivityAccountMonthCount.setMonth(raffleActivityAccountMonthCount.currentMonth());
            raffleActivityAccountMonthCount.setMonthCount(raffleActivityOrderRes.getMonthCount());
            raffleActivityAccountMonthCount.setMonthCountSurplus(raffleActivityOrderRes.getMonthCount());
            raffleActivityAccountMonthCount.setUpdateTime(new Date());

//        创建账户对象 - 日额度
            RaffleActivityAccountDayCount raffleActivityAccountDayCount = new RaffleActivityAccountDayCount();
            raffleActivityAccountDayCount.setUserId(raffleActivityOrderRes.getUserId());
            raffleActivityAccountDayCount.setActivityId(raffleActivityOrderRes.getActivityId());
            raffleActivityAccountDayCount.setDay(raffleActivityAccountDayCount.today());
            raffleActivityAccountDayCount.setDayCount(raffleActivityOrderRes.getDayCount());
            raffleActivityAccountDayCount.setDayCountSurplus(raffleActivityOrderRes.getDayCount());
            raffleActivityAccountDayCount.setUpdateTime(new Date());
//        将订单写入
            transactionTemplate.execute(status -> {
                try {
//                更新订单状态
                    int updateCount = activityOrderService.updateOrderStatus(raffleActivityOrderRes);
                    if (updateCount != 1) {
                        status.setRollbackOnly();
                        return 1;
                    }
//                更新总账户 如果账户不存在则先创建账户
                    RaffleActivityAccount raffleActivityAccountRes = activityAccountService.getAcitivtyAccountByUserId(raffleActivityOrderRes.getUserId());

                    if (raffleActivityAccountRes == null) {
                        activityAccountService.save(raffleActivityAccount);
                    }
                    activityAccountService.updateAccount(raffleActivityAccount);
//                更新月账户
                    accountMonthCountService.addAccountQuota(raffleActivityAccountMonthCount);
//                更新日账户
                    accountDayCountService.addAccountQuota(raffleActivityAccountDayCount);
                    return 1;
                } catch (DuplicateKeyException e) {
                    status.setRollbackOnly();
                    log.error("写入订单记录，唯一索引冲突 userId: {} activityId: {} sku: {}", raffleActivityOrderRes.getUserId(), raffleActivityOrderRes.getActivityId(), raffleActivityOrderRes.getSku(), e);
                    throw new AppException(ResponseCode.INDEX_DUP.getCode());
                }
            });

        } finally {
            lock.unlock();
        }

    }

    @Override
    public UnpaidActivityOrderEntity queryUnpaidActivityOrder(SkuRechargeEntity skuRechargeEntity) {

        RaffleActivityOrder raffleActivityOrder = activityOrderService.queryUnpaidActivityOrder(skuRechargeEntity);
        if (raffleActivityOrder == null) {
            return null;
        }
        return UnpaidActivityOrderEntity.builder()
                .userId(raffleActivityOrder.getUserId())
                .sku(raffleActivityOrder.getSku())
                .outBusinessNo(raffleActivityOrder.getOutBusinessNo())
                .payAmount(raffleActivityOrder.getPayAmount())
                .build();

    }

    @Override
    public List<SkuProductEntity> getSkuProductListByActivityId(Long activityId) {

//        1.获取sku商品集合
        List<RaffleActivitySku> activitySkuList = skuService.getSkuProductListByActivityId(activityId);
//        2.遍历填充skuProductEntity对象
        List<SkuProductEntity> skuProductList = new ArrayList<SkuProductEntity>(activitySkuList.size());
        for (RaffleActivitySku activitySku : activitySkuList) {
            SkuProductEntity skuProductEntity = new SkuProductEntity();
            skuProductEntity.setSku(activitySku.getSku());
            skuProductEntity.setActivityId(activityId);
            skuProductEntity.setActivityCountId(activitySku.getActivityCountId());
            skuProductEntity.setStockCount(activitySku.getStockCount());
            skuProductEntity.setStockCountSurplus(activitySku.getStockCountSurplus());
            skuProductEntity.setProductAmount(activitySku.getProductAmount());

            RaffleActivityCount raffleActivityCount = activityCountService.getActivityCountById(activitySku.getActivityCountId());
            SkuProductEntity.ActivityCount activityCount = new SkuProductEntity.ActivityCount();
            BeanUtils.copyProperties(raffleActivityCount, activityCount);
            skuProductEntity.setActivityCount(activityCount);
            skuProductList.add(skuProductEntity);
        }

        return skuProductList;

    }

}
