package top.javarem.infrastructure.dao.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.javarem.infrastructure.dao.entity.UserAwardRecord;

/**
* @author aaa
* @description 针对表【user_award_record(用户中奖记录表)】的数据库操作Mapper
* @createDate 2024-12-05 14:39:30
* @Entity generator.domain.UserAwardRecord
*/
@Mapper
public interface UserAwardRecordMapper extends BaseMapper<UserAwardRecord> {


}
