package top.javarem.infrastructure.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.javarem.infrastructure.dao.entity.UserCreditOrder;

/**
* @author aaa
* @description 针对表【user_credit_order(用户积分订单表)】的数据库操作Mapper
* @createDate 2025-01-17 15:39:38
* @Entity generator.domain.UserCreditOrder
*/
@Mapper
public interface UserCreditOrderMapper extends BaseMapper<UserCreditOrder> {


}
