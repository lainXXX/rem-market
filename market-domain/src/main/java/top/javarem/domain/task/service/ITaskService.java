package top.javarem.domain.task.service;

import top.javarem.domain.task.model.entity.TaskEntity;

import java.util.List;

/**
 * @Author: rem
 * @Date: 2024/12/08/21:46
 * @Description:任务服务接口
 */
public interface ITaskService {

    List<TaskEntity> queryNoSendMessageTaskList();

    void sendMessage(String topic, String message);

    void updateTaskCompleted(String messageId);

    void updateTaskFailed(String messageId);
}
