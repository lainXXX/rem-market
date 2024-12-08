package top.javarem.infrastructure.dao.iService.Impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.javarem.infrastructure.dao.entity.Task;
import top.javarem.infrastructure.dao.iService.TaskService;
import top.javarem.infrastructure.dao.mapper.TaskMapper;

import java.util.Collections;
import java.util.List;

/**
* @author aaa
* @description 针对表【task】的数据库操作Service实现
* @createDate 2024-12-05 14:39:24
*/
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task>
implements TaskService {

    private final TaskMapper mapper;

    public TaskServiceImpl(TaskMapper taskMapper) {
        this.mapper = taskMapper;
    }

    @Override
    public List<Task> queryNoSendMessageTaskList() {
        return mapper.queryNoSendMessageTaskList();
    }
}
