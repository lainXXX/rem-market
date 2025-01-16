package top.javarem.infrastructure.adapter.repository;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;
import top.javarem.domain.award.model.aggregate.GiveOutPrizeAggregate;
import top.javarem.domain.award.model.aggregate.UserAwardRecodeAggregate;
import top.javarem.domain.award.model.entity.TaskEntity;
import top.javarem.domain.award.model.entity.UserAwardRecordEntity;
import top.javarem.domain.award.model.entity.UserCreditAwardEntity;
import top.javarem.domain.award.model.vo.AccountStatusVO;
import top.javarem.domain.award.repository.IAwardRepository;
import top.javarem.infrastructure.dao.entity.*;
import top.javarem.infrastructure.dao.iService.*;
import top.javarem.infrastructure.dao.iService.Impl.UserRaffleOrderServiceImpl;
import top.javarem.infrastructure.event.EventPublisher;
import top.javarem.types.common.constants.Constants;
import top.javarem.types.enums.ResponseCode;
import top.javarem.types.exception.AppException;

import java.util.Date;

/**
 * @Author: rem
 * @Date: 2024/12/08/20:04
 * @Description:
 */
@Repository
@Slf4j
public class AwardRepository implements IAwardRepository {

    private final TransactionTemplate transactionTemplate;

    private final UserAwardRecordService userAwardRecordService;

    private final TaskService taskService;

    private final EventPublisher eventPublisher;

    private final IAwardService awardService;

    private final Redisson redisson;

    private final UserCreditAccountService userCreditAccountService;
    private final UserRaffleOrderService userRaffleOrderService;

    public AwardRepository(TransactionTemplate transactionTemplate, UserAwardRecordService userAwardRecordService, TaskService taskService, EventPublisher eventPublisher, IAwardService awardService, Redisson redisson, UserCreditAccountService userCreditAccountService, UserRaffleOrderService userRaffleOrderService) {
        this.transactionTemplate = transactionTemplate;
        this.userAwardRecordService = userAwardRecordService;
        this.taskService = taskService;
        this.eventPublisher = eventPublisher;
        this.awardService = awardService;
        this.redisson = redisson;
        this.userCreditAccountService = userCreditAccountService;
        this.userRaffleOrderService = userRaffleOrderService;
    }

    @Override
    public void saveAwardRecord(UserAwardRecodeAggregate userAwardRecodeAggregate) {

        UserAwardRecordEntity userAwardRecordEntity = userAwardRecodeAggregate.getUserAwardRecordEntity();
        String userId = userAwardRecordEntity.getUserId();
        Integer awardId = userAwardRecordEntity.getAwardId();
        Long activityId = userAwardRecordEntity.getActivityId();
        UserAwardRecord userAwardRecord = new UserAwardRecord();
        BeanUtils.copyProperties(userAwardRecordEntity, userAwardRecord);
        userAwardRecord.setCreateTime(new Date());
        userAwardRecord.setUpdateTime(new Date());

        TaskEntity taskEntity = userAwardRecodeAggregate.getTaskEntity();
        Task task = new Task();
        task.setUserId(userId);
        task.setTopic(taskEntity.getTopic());
        task.setMessageId(taskEntity.getMessageId());
        task.setMessage(new Gson().toJson(taskEntity.getMessage()));
        task.setStatus(taskEntity.getStatus().getCode());
        task.setCreateTime(new Date());
        task.setUpdateTime(new Date());

        transactionTemplate.execute(status -> {
                    try {
                        userAwardRecordService.save(userAwardRecord);
                        taskService.save(task);
                        return 1;
                    } catch (DuplicateKeyException e) {
                        status.setRollbackOnly();
                        log.error("写入中奖记录，唯一索引冲突 userId: {} activityId: {} awardId: {}", userId, activityId, awardId, e);
                        throw new AppException(ResponseCode.INDEX_DUP.getCode(), e);
                    }
                }
        );

        try {
            eventPublisher.publish(task.getTopic(), task.getMessage());
            taskService.lambdaUpdate()
                    .set(Task::getStatus, "completed")
                    .eq(Task::getUserId, userId)
                    .eq(Task::getMessageId, task.getMessageId())
                    .update();
            log.info("写入中奖记录，发送MQ消息完成 userId: {} orderId:{} topic: {}", userId, userAwardRecordEntity.getOrderId(), task.getTopic());
        } catch (AppException e) {
//            失败则status为failed
            log.error("写入中奖记录，发送MQ消息失败 userId: {} topic: {}", userId, task.getTopic());
            taskService.lambdaUpdate()
                    .set(Task::getStatus, "failed")
                    .eq(Task::getUserId, userId)
                    .eq(Task::getMessageId, task.getMessageId())
                    .update();
        }


    }

    @Override
    public String getAwardConfig(Integer awardId) {
//        1.先从缓存中查询是否有奖品配置信息
        String cacheKey = Constants.RedisKey.AWARD_CONFIG_KEY + awardId;
        RBucket<String> bucket = redisson.<String>getBucket(cacheKey);
        String awardConfig = bucket.get();
        if (!StringUtils.isBlank(awardConfig)) {
            return awardConfig;
        }
//        2.如果缓存中没有配置 则从数据库中获取 再存入缓存
        awardConfig = awardService.lambdaQuery()
                .select(Award::getAwardConfig)
                .eq(Award::getAwardId, awardId)
                .one().getAwardConfig();
        bucket.set(awardConfig);
        return awardConfig;
    }

    @Override
    public void saveGiveOutPrizeAggregate(GiveOutPrizeAggregate giveOutPrizeAggregate) {
//        创建数据库映射对应 并把对应数据传入
        String userId = giveOutPrizeAggregate.getUserId();
        UserAwardRecordEntity userAwardRecordEntity = giveOutPrizeAggregate.getUserAwardRecordEntity();
        UserCreditAwardEntity userCreditAwardEntity = giveOutPrizeAggregate.getUserCreditAwardEntity();
        UserAwardRecord userAwardRecord = new UserAwardRecord();
//        拷贝数据
        BeanUtils.copyProperties(userAwardRecordEntity, userAwardRecord);
//        拷贝数据
        UserCreditAccount userCreditAccount = new UserCreditAccount();
        userCreditAccount.setUserId(userCreditAwardEntity.getUserId());
        userCreditAccount.setTotalAmount(userCreditAwardEntity.getCreditAmount());
        userCreditAccount.setAvailableAmount(userCreditAwardEntity.getCreditAmount());
        userCreditAccount.setAccountStatus(AccountStatusVO.open.getCode());
//        TODO 路由配置
        transactionTemplate.execute(status -> {
            try {
//            更新积分 || 创建积分账户
                boolean addResult = userCreditAccountService.addAccount(userCreditAccount);
                if (!addResult) {
                    userCreditAccountService.save(userCreditAccount);
                }
//            更新奖品记录
                boolean updateResult = userAwardRecordService.updateAwardRecordCompletedState(userAwardRecord);
                if (!updateResult) {
                    log.warn("更新中奖记录，重复更新拦截 userId:{} giveOutPrizesAggregate:{}", userId, new Gson().toJson(giveOutPrizeAggregate));
                    status.setRollbackOnly();
                }
//            标记订单为完成
                userRaffleOrderService.lambdaUpdate()
                        .set(UserRaffleOrder::getStatus, "used")
                        .eq(UserRaffleOrder::getUserId, userId)
                        .eq(UserRaffleOrder::getOrderId, userAwardRecord.getOrderId())
                        .update();
                return 1;
            } catch (DuplicateKeyException e) {
                status.setRollbackOnly();
                log.error("更新中奖记录，唯一索引冲突 userId: {} ", userId, e);
                throw new AppException(ResponseCode.INDEX_DUP.getCode(), e);
            }
        });
    }

    @Override
    public String getAwardKey(Integer awardId) {
        String cacheKey = Constants.RedisKey.AWARD_KEY + awardId;
        RBucket<String> bucket = redisson.<String>getBucket(cacheKey);
        String awardKey = bucket.get();
        if (!StringUtils.isBlank(awardKey)) {
            return awardKey;
        }
//        2.如果缓存中没有配置 则从数据库中获取 再存入缓存
                awardKey = awardService.lambdaQuery()
                        .select(Award::getAwardKey)
                        .eq(Award::getAwardId, awardId)
                        .one().getAwardKey();
        bucket.set(awardKey);
        return awardKey;
    }

}
