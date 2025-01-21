package top.javarem.infrastructure.dao.iService.Impl;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.javarem.infrastructure.dao.entity.UserCreditAccount;
import top.javarem.infrastructure.dao.iService.UserCreditAccountService;
import top.javarem.infrastructure.dao.mapper.UserCreditAccountMapper;

/**
* @author aaa
* @description 针对表【user_credit_account(用户积分账户)】的数据库操作Service实现
* @createDate 2025-01-15 20:48:56
*/
@Service
public class UserCreditAccountServiceImpl extends ServiceImpl<UserCreditAccountMapper, UserCreditAccount>
implements UserCreditAccountService {

    @Override
    public boolean addAccount(UserCreditAccount userCreditAccount) {
        // 方案1：使用 update() 返回的记录数
        int rows = this.baseMapper.update(null,
                new LambdaUpdateWrapper<UserCreditAccount>()
                        .setSql("total_amount = total_amount + " + userCreditAccount.getTotalAmount()
                                + ", available_amount = available_amount + " + userCreditAccount.getAvailableAmount())
                        .eq(UserCreditAccount::getUserId, userCreditAccount.getUserId()));
        return rows > 0;
    }

    @Override
    public UserCreditAccount getAccountByUserId(String userId) {

        return this.lambdaQuery().eq(UserCreditAccount::getUserId, userId).one();

    }

    @Override
    public UserCreditAccount queryUserCredit(String userId) {

        return this.lambdaQuery()
                .select(UserCreditAccount::getUserId, UserCreditAccount::getAvailableAmount)
                .eq(UserCreditAccount::getUserId, userId)
                .eq(UserCreditAccount::getAccountStatus, "open")
                .one();

    }

}
