package top.javarem.infrastructure.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.javarem.infrastructure.dao.entity.User;

/**
 * @Author: rem
 * @Date: 2024/11/21/17:16
 * @Description:
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
