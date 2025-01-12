package top.javarem.domain.task.repository;

import top.javarem.domain.task.model.entity.TaskEntity;
import top.javarem.types.event.BaseEvent;

import java.util.List;

/**
 * @Author: rem
 * @Date: 2024/12/08/21:49
 * @Description:任务仓储接口
 */
public interface ITaskRepository {

    List<TaskEntity> queryNoSendMessageTaskList();

    void sendMessage(String topic, String message);

    void sendMessage(String topic, BaseEvent.EventMessage<?> message);

    void updateTaskCompleted(String messageId);

    void updateTaskFailed(String messageId);
}
