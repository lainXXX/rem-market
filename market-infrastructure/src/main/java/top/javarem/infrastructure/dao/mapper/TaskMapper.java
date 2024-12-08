package top.javarem.infrastructure.dao.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.javarem.infrastructure.dao.entity.Task;

import java.util.List;

/**
* @author aaa
* @description 针对表【task】的数据库操作Mapper
* @createDate 2024-12-05 14:39:24
* @Entity generator.domain.Task
*/
@Mapper
public interface TaskMapper extends BaseMapper<Task> {


    List<Task> queryNoSendMessageTaskList();
}
