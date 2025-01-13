package top.javarem.infrastructure.dao.iService.Impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.javarem.infrastructure.dao.entity.RaffleActivityAccountDayCount;
import top.javarem.infrastructure.dao.iService.RaffleActivityAccountDayCountService;
import top.javarem.infrastructure.dao.mapper.RaffleActivityAccountDayCountMapper;

/**
* @author aaa
* @description 针对表【raffle_activity_account_day_count】的数据库操作Service实现
* @createDate 2024-12-05 14:40:09
*/
@Service
public class RaffleActivityAccountDayCountServiceImpl extends ServiceImpl<RaffleActivityAccountDayCountMapper, RaffleActivityAccountDayCount>
implements RaffleActivityAccountDayCountService {

    @Autowired
    private RaffleActivityAccountDayCountMapper mapper;

    @Override
    public void addAccountQuota(RaffleActivityAccountDayCount raffleActivityAccountDayCount) {
        mapper.addAccountQuota(raffleActivityAccountDayCount);
    }
}
