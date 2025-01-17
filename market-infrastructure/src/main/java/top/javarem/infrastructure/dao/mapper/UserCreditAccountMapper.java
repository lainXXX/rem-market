package top.javarem.infrastructure.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.javarem.infrastructure.dao.entity.UserCreditAccount;

/**
* @author aaa
* @description 针对表【user_credit_account(用户积分账户)】的数据库操作Mapper
* @createDate 2025-01-15 20:48:56
* @Entity top.javarem.infrastructure.dao.entity.UserCreditAccount
*/
@Mapper
public interface UserCreditAccountMapper extends BaseMapper<UserCreditAccount> {


}
