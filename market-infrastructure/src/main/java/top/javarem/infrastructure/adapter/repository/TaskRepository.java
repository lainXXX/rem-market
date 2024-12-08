package top.javarem.infrastructure.adapter.repository;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import top.javarem.domain.task.model.entity.TaskEntity;
import top.javarem.domain.task.repository.ITaskRepository;
import top.javarem.infrastructure.dao.entity.Task;
import top.javarem.infrastructure.dao.iService.TaskService;
import top.javarem.infrastructure.event.EventPublisher;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: rem
 * @Date: 2024/12/08/22:09
 * @Description:
 */
@Repository
public class TaskRepository implements ITaskRepository {

    @Autowired
    private TaskService taskService;

    @Autowired
    private EventPublisher eventPublisher;

    @Override
    public List<TaskEntity> queryNoSendMessageTaskList() {
        List<Task> taskList = taskService.queryNoSendMessageTaskList();
        return taskList.stream()
                .map(task -> {
                    TaskEntity taskEntity = new TaskEntity();
                    BeanUtils.copyProperties(task, taskEntity);
                    return taskEntity;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void sendMessage(String topic, String message) {
        eventPublisher.publish(topic, message);
    }

    @Override
    public void updateTaskCompleted(String messageId) {
        taskService.lambdaUpdate()
                .set(Task::getStatus, "completed")
                .eq(Task::getMessageId, messageId)
                .update();
    }

    @Override
    public void updateTaskFailed(String messageId) {
        taskService.lambdaUpdate()
                .set(Task::getStatus, "failed")
                .eq(Task::getMessageId, messageId)
                .update();
    }
}
