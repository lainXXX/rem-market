package top.javarem.infrastructure.dao.iService;


import com.baomidou.mybatisplus.extension.service.IService;
import top.javarem.domain.award.model.entity.UserCreditAwardEntity;
import top.javarem.infrastructure.dao.entity.UserCreditAccount;

/**
* @author aaa
* @description 针对表【user_credit_account(用户积分账户)】的数据库操作Service
* @createDate 2025-01-15 20:48:56
*/
public interface UserCreditAccountService extends IService<UserCreditAccount> {

    boolean addAccount(UserCreditAccount userCreditAccount);

    UserCreditAccount getAccountByUserId(String userId);
}
