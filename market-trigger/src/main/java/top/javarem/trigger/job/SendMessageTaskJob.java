package top.javarem.trigger.job;

import com.xxl.job.core.handler.annotation.XxlJob;
import io.jsonwebtoken.lang.Collections;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.javarem.domain.task.model.entity.TaskEntity;
import top.javarem.domain.task.service.ITaskService;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: rem
 * @Date: 2024/12/08/22:37
 * @Description:发送MQ消息任务
 */
@Component
@Slf4j
public class SendMessageTaskJob {

    @Autowired
    private ITaskService taskService;

    @Autowired
    private ThreadPoolExecutor executor;

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 本地化任务注解；@Scheduled(cron = "0/5 * * * * ?")
     * 分布式任务注解；@XxlJob("SendMessageTaskJob")
     */
    @XxlJob("SendMessageTaskJob_DB1")
    public void execute() {
        log.info("发送MQ消息任务开始执行");
        RLock lock = redissonClient.getLock("rem-market-SendMessageTaskJob_DB1");
        boolean isLocked = false;
        try {
            isLocked =lock.tryLock(3, 0, TimeUnit.SECONDS);
            if (!isLocked) return;
            List<TaskEntity> taskEntityList = taskService.queryNoSendMessageTaskList();
            if (Collections.isEmpty(taskEntityList)) {
                return;
            }
            log.info("");
            for (TaskEntity taskEntity : taskEntityList) {
                executor.execute(() -> {
                    try {
                        taskService.sendMessage(taskEntity.getTopic(), taskEntity.getMessage());
                        taskService.updateTaskCompleted(taskEntity.getMessageId());
                    } catch (Exception e) {
                        log.error("定时任务，发送MQ消息失败 userId: {} topic: {}", taskEntity.getUserId(), taskEntity.getTopic());
                        taskService.updateTaskFailed(taskEntity.getMessageId());
                    }
                });
            }
        } catch (Exception e) {
            log.error("定时任务，扫描MQ任务表发送消息失败。", e);
        }
    }

}
