package top.javarem.infrastructure.dao.iService.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.javarem.infrastructure.dao.entity.UserRaffleOrder;
import top.javarem.infrastructure.dao.iService.UserRaffleOrderService;
import top.javarem.infrastructure.dao.mapper.UserRaffleOrderMapper;

/**
 * @Author: rem
 * @Date: 2024/12/06/13:30
 * @Description:
 */
@Service
public class UserRaffleOrderServiceImpl extends ServiceImpl<UserRaffleOrderMapper, UserRaffleOrder> implements UserRaffleOrderService {
}
