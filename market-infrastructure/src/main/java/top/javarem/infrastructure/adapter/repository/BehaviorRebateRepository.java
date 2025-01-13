package top.javarem.infrastructure.adapter.repository;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import top.javarem.domain.rebate.model.aggregate.BehaviorRebateAggregate;
import top.javarem.domain.rebate.model.entity.BehaviorRebateOrderEntity;
import top.javarem.domain.rebate.model.entity.TaskEntity;
import top.javarem.domain.rebate.model.vo.BehaviorTypeVO;
import top.javarem.domain.rebate.model.vo.DailyBehaviorRebateVO;
import top.javarem.domain.rebate.repository.IBehaviorRebateRepository;
import top.javarem.infrastructure.dao.entity.DailyBehaviorRebate;
import top.javarem.infrastructure.dao.entity.Task;
import top.javarem.infrastructure.dao.entity.UserBehaviorRebateOrder;
import top.javarem.infrastructure.dao.iService.DailyBehaviorRebateService;
import top.javarem.infrastructure.dao.iService.TaskService;
import top.javarem.infrastructure.dao.iService.UserBehaviorRebateOrderService;
import top.javarem.infrastructure.dao.mapper.DailyBehaviorRebateMapper;
import top.javarem.infrastructure.event.EventPublisher;
import top.javarem.types.enums.ResponseCode;
import top.javarem.types.exception.AppException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author: rem
 * @Date: 2025/01/12/17:31
 * @Description:
 */
@Repository
@Slf4j
public class BehaviorRebateRepository implements IBehaviorRebateRepository {

    @Autowired
    private DailyBehaviorRebateService dailyBehaviorRebateService;

    @Autowired
    private UserBehaviorRebateOrderService userBehaviorRebateOrderService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public List<DailyBehaviorRebateVO> queryDailyBehaviorRebateConfig(String behaviorType) {
        List<DailyBehaviorRebate> dailyBehaviorRebates = dailyBehaviorRebateService.lambdaQuery()
                .select(DailyBehaviorRebate::getBehaviorType, DailyBehaviorRebate::getRebateDesc, DailyBehaviorRebate::getRebateType, DailyBehaviorRebate::getRebateConfig)
                .eq(DailyBehaviorRebate::getStatus, "open")
                .eq(DailyBehaviorRebate::getBehaviorType, behaviorType)
                .list();
        List<DailyBehaviorRebateVO> dailyBehaviorRebateVOS = new ArrayList<>(dailyBehaviorRebates.size());
        for (DailyBehaviorRebate dailyBehaviorRebate : dailyBehaviorRebates) {
            dailyBehaviorRebateVOS.add(DailyBehaviorRebateVO.builder()
                    .behaviorType(dailyBehaviorRebate.getBehaviorType())
                    .rebateDesc(dailyBehaviorRebate.getRebateDesc())
                    .rebateType(dailyBehaviorRebate.getRebateType())
                    .rebateConfig(dailyBehaviorRebate.getRebateConfig())
                    .build());
        }
        return dailyBehaviorRebateVOS;
    }

    @Override
    public void saveUserRebateRecord(String userId, List<BehaviorRebateAggregate> behaviorRebateAggregates) {
//        TODO 分库分表 按照userId
        transactionTemplate.execute(status -> {
            try {
                    for (BehaviorRebateAggregate behaviorRebateAggregate : behaviorRebateAggregates) {
                        BehaviorRebateOrderEntity behaviorRebateOrderEntity = behaviorRebateAggregate.getBehaviorRebateOrderEntity();
                        // 用户行为返利订单对象
                        UserBehaviorRebateOrder userBehaviorRebateOrder = new UserBehaviorRebateOrder();
                        userBehaviorRebateOrder.setUserId(behaviorRebateOrderEntity.getUserId());
                        userBehaviorRebateOrder.setOrderId(behaviorRebateOrderEntity.getOrderId());
                        userBehaviorRebateOrder.setBehaviorType(behaviorRebateOrderEntity.getBehaviorType());
                        userBehaviorRebateOrder.setRebateDesc(behaviorRebateOrderEntity.getRebateDesc());
                        userBehaviorRebateOrder.setRebateType(behaviorRebateOrderEntity.getRebateType());
                        userBehaviorRebateOrder.setRebateConfig(behaviorRebateOrderEntity.getRebateConfig());
                        userBehaviorRebateOrder.setBizId(behaviorRebateOrderEntity.getBizId());
                        userBehaviorRebateOrderService.save(userBehaviorRebateOrder);

                        // 任务对象
                        TaskEntity taskEntity = behaviorRebateAggregate.getTaskEntity();
                        Task task = new Task();
                        task.setUserId(taskEntity.getUserId());
                        task.setTopic(taskEntity.getTopic());
                        task.setMessageId(taskEntity.getMessageId());
                        task.setMessage(new Gson().toJson(taskEntity.getMessage()));
                        task.setStatus(taskEntity.getStatus().getCode());
                        taskRepository.saveTask(task);
                    }

                //        同步消息到MQ
                for (BehaviorRebateAggregate behaviorRebateAggregate : behaviorRebateAggregates) {
                    TaskEntity taskEntity = behaviorRebateAggregate.getTaskEntity();
                    try {
                        // 发送消息【在事务外执行，如果失败还有任务补偿】
                        taskRepository.sendMessage(taskEntity.getTopic(), taskEntity.getMessage());
                        // 更新数据库记录，task 任务表
                        taskRepository.updateTaskCompleted(taskEntity.getMessageId());
                    } catch (Exception e) {
                        log.error("写入返利记录，发送MQ消息失败 userId: {} topic: {}", userId, taskEntity.getTopic());
                        taskRepository.updateTaskFailed(taskEntity.getMessageId());
                    }
                }

                return 1;
            } catch (DuplicateKeyException e) {
                status.setRollbackOnly();
                log.error("写入返利记录，唯一索引冲突 userId: {}", userId, e);
                throw new AppException(ResponseCode.INDEX_DUP.getCode(), e);
            }
        });

    }

}
