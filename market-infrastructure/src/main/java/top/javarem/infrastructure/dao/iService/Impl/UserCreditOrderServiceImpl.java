package top.javarem.infrastructure.dao.iService.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.javarem.infrastructure.dao.entity.UserCreditOrder;
import top.javarem.infrastructure.dao.iService.UserCreditOrderService;
import top.javarem.infrastructure.dao.mapper.UserCreditOrderMapper;

/**
* @author aaa
* @description 针对表【user_credit_order(用户积分订单表)】的数据库操作Service实现
* @createDate 2025-01-17 15:39:38
*/
@Service
public class UserCreditOrderServiceImpl extends ServiceImpl<UserCreditOrderMapper, UserCreditOrder>
implements UserCreditOrderService {

}
