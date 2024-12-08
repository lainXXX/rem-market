package top.javarem.infrastructure.dao.iService;


import com.baomidou.mybatisplus.extension.service.IService;
import top.javarem.infrastructure.dao.entity.RaffleActivityAccount;

/**
* @author aaa
* @description 针对表【raffle_activity_account】的数据库操作Service
* @createDate 2024-12-01 17:15:06
*/
public interface RaffleActivityAccountService extends IService<RaffleActivityAccount> {


    int updateAccount(RaffleActivityAccount raffleActivityAccount);

    int updateAccountDeductQuota(RaffleActivityAccount build);
}
