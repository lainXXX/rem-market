package top.javarem.infrastructure.dao.iService;


import com.baomidou.mybatisplus.extension.service.IService;
import top.javarem.infrastructure.dao.entity.RaffleActivityAccountMonthCount;

/**
* @author aaa
* @description 针对表【raffle_activity_account_month_count】的数据库操作Service
* @createDate 2024-12-05 14:39:13
*/
public interface RaffleActivityAccountMonthCountService extends IService<RaffleActivityAccountMonthCount> {

    void addAccountQuota(RaffleActivityAccountMonthCount raffleActivityAccountMonthCount);

}
