package top.javarem.domain.task.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.javarem.domain.task.model.entity.TaskEntity;
import top.javarem.domain.task.repository.ITaskRepository;

import java.util.List;

/**
 * @Author: rem
 * @Date: 2024/12/08/21:47
 * @Description:
 */
@Service
public class TaskService implements ITaskService {

    @Autowired
    private ITaskRepository taskRepository;

    @Override
    public List<TaskEntity> queryNoSendMessageTaskList() {
        return taskRepository.queryNoSendMessageTaskList();
    }

    @Override
    public void sendMessage(String topic, String message) {
        taskRepository.sendMessage(topic, message);
    }

    @Override
    public void updateTaskCompleted(String messageId) {
        taskRepository.updateTaskCompleted(messageId);
    }

    @Override
    public void updateTaskFailed(String messageId) {
        taskRepository.updateTaskFailed(messageId);
    }

}
