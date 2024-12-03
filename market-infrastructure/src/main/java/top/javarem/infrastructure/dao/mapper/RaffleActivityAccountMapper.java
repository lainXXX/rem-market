package top.javarem.infrastructure.dao.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.javarem.infrastructure.dao.entity.RaffleActivityAccount;

/**
* @author aaa
* @description 针对表【raffle_activity_account】的数据库操作Mapper
* @createDate 2024-12-01 17:15:06
* @Entity generator.domain.RaffleActivityAccount
*/
@Mapper
public interface RaffleActivityAccountMapper extends BaseMapper<RaffleActivityAccount> {

    int updateAccount(RaffleActivityAccount raffleActivityAccount);

}
