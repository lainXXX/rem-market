package top.javarem.infrastructure.dao.Iservice.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.javarem.infrastructure.dao.Iservice.IUserService;
import top.javarem.infrastructure.dao.entity.User;
import top.javarem.infrastructure.dao.mapper.UserMapper;

/**
 * @Author: rem
 * @Date: 2024/11/21/17:16
 * @Description:
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
