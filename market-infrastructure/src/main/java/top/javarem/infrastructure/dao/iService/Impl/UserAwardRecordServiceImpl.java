package top.javarem.infrastructure.dao.iService.Impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
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

}
