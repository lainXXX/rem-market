package top.javarem.trigger.job;

import io.jsonwebtoken.lang.Collections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.javarem.domain.task.model.entity.TaskEntity;
import top.javarem.domain.task.service.ITaskService;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

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

    @Scheduled(cron = "0/30 * * * * ?")
    public void execute() {
        log.info("发送MQ消息任务开始执行");
        try {
            List<TaskEntity> taskEntityList = taskService.queryNoSendMessageTaskList();
            if (Collections.isEmpty(taskEntityList)) {
                return;
            }
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
