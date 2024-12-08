package top.javarem.infrastructure.adapter.repository;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;
import top.javarem.domain.award.model.aggregate.UserAwardRecodeAggregate;
import top.javarem.domain.award.model.entity.TaskEntity;
import top.javarem.domain.award.model.entity.UserAwardRecordEntity;
import top.javarem.domain.award.repository.IAwardRepository;
import top.javarem.infrastructure.dao.entity.Task;
import top.javarem.infrastructure.dao.entity.UserAwardRecord;
import top.javarem.infrastructure.dao.iService.TaskService;
import top.javarem.infrastructure.dao.iService.UserAwardRecordService;
import top.javarem.infrastructure.event.EventPublisher;
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

    public AwardRepository(TransactionTemplate transactionTemplate, UserAwardRecordService userAwardRecordService, TaskService taskService, EventPublisher eventPublisher) {
        this.transactionTemplate = transactionTemplate;
        this.userAwardRecordService = userAwardRecordService;
        this.taskService = taskService;
        this.eventPublisher = eventPublisher;
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

}
