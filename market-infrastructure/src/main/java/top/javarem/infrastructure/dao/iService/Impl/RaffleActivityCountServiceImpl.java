package top.javarem.infrastructure.dao.iService.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.javarem.infrastructure.dao.entity.RaffleActivityCount;
import top.javarem.infrastructure.dao.iService.RaffleActivityCountService;
import top.javarem.infrastructure.dao.mapper.RaffleActivityCountMapper;

/**
* @author aaa
* @description 针对表【raffle_activity_count】的数据库操作Service实现
* @createDate 2024-12-01 17:14:25
*/
@Service
public class RaffleActivityCountServiceImpl extends ServiceImpl<RaffleActivityCountMapper, RaffleActivityCount> implements RaffleActivityCountService {

    @Override
    public RaffleActivityCount getActivityCountById(Long activityCountId) {

        return this.lambdaQuery()
                .select(RaffleActivityCount::getTotalCount, RaffleActivityCount::getMonthCount, RaffleActivityCount::getDayCount)
                .eq(RaffleActivityCount::getActivityCountId, activityCountId)
                .one();

    }
}
