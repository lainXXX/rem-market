package top.javarem.infrastructure.dao.iService;


import com.baomidou.mybatisplus.extension.service.IService;
import top.javarem.domain.award.model.entity.UserCreditAwardEntity;
import top.javarem.infrastructure.dao.entity.UserAwardRecord;

/**
* @author aaa
* @description 针对表【user_award_record(用户中奖记录表)】的数据库操作Service
* @createDate 2024-12-05 14:39:30
*/
public interface UserAwardRecordService extends IService<UserAwardRecord> {


    boolean updateAwardRecordCompletedState(UserAwardRecord userAwardRecord);
}
