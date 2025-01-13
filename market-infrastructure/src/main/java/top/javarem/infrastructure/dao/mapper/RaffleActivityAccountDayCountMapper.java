package top.javarem.infrastructure.dao.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.javarem.infrastructure.dao.entity.RaffleActivityAccountDayCount;

/**
* @author aaa
* @description 针对表【raffle_activity_account_day_count】的数据库操作Mapper
* @createDate 2024-12-05 14:40:08
* @Entity generator.domain.RaffleActivityAccountDayCount
*/
@Mapper
public interface RaffleActivityAccountDayCountMapper extends BaseMapper<RaffleActivityAccountDayCount> {


    void addAccountQuota(RaffleActivityAccountDayCount raffleActivityAccountDayCount);
}
