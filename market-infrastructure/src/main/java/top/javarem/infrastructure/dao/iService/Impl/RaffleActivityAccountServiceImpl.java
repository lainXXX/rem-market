package top.javarem.infrastructure.dao.iService.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.javarem.infrastructure.dao.entity.RaffleActivityAccount;
import top.javarem.infrastructure.dao.iService.RaffleActivityAccountService;
import top.javarem.infrastructure.dao.mapper.RaffleActivityAccountMapper;

/**
* @author aaa
* @description 针对表【raffle_activity_account】的数据库操作Service实现
* @createDate 2024-12-01 17:15:06
*/
@Service
public class RaffleActivityAccountServiceImpl extends ServiceImpl<RaffleActivityAccountMapper, RaffleActivityAccount> implements RaffleActivityAccountService {

    @Autowired
    protected RaffleActivityAccountMapper mapper;

    @Override
    public RaffleActivityAccountMapper getBaseMapper() {
        return super.getBaseMapper();
    }

    @Override
    public int updateAccount(RaffleActivityAccount raffleActivityAccount) {
        return mapper.updateAccount(raffleActivityAccount);
    }
}
