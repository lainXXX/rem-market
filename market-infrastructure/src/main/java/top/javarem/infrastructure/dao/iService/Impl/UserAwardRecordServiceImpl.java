package top.javarem.infrastructure.dao.iService.Impl;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.javarem.domain.award.model.entity.UserCreditAwardEntity;
import top.javarem.infrastructure.dao.entity.UserAwardRecord;
import top.javarem.infrastructure.dao.iService.UserAwardRecordService;
import top.javarem.infrastructure.dao.mapper.UserAwardRecordMapper;

/**
* @author aaa
* @description 针对表【user_award_record(用户中奖记录表)】的数据库操作Service实现
* @createDate 2024-12-05 14:39:30
*/
@Service
public class UserAwardRecordServiceImpl extends ServiceImpl<UserAwardRecordMapper, UserAwardRecord>
implements UserAwardRecordService {

    @Override
    public boolean updateAwardRecordCompletedState(UserAwardRecord userAwardRecord) {
        // 使用 baseMapper 获取实际更新行数
        int rows = this.baseMapper.update(null,
                new LambdaUpdateWrapper<UserAwardRecord>()
                        .set(UserAwardRecord::getStatus, userAwardRecord.getStatus())
                        .eq(UserAwardRecord::getUserId, userAwardRecord.getUserId())
                        .eq(UserAwardRecord::getOrderId, userAwardRecord.getOrderId())
                        .eq(UserAwardRecord::getStatus, "create"));

        return rows > 0;  // 只有真正更新了记录才返回 true
    }
}
