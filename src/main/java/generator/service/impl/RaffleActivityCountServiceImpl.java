package generator.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import generator.domain.RaffleActivityCount;
import generator.service.RaffleActivityCountService;
import generator.mapper.RaffleActivityCountMapper;
import org.springframework.stereotype.Service;

/**
* @author aaa
* @description 针对表【raffle_activity_count】的数据库操作Service实现
* @createDate 2024-12-01 17:14:25
*/
@Service
public class RaffleActivityCountServiceImpl extends ServiceImpl<RaffleActivityCountMapper, RaffleActivityCount>
implements RaffleActivityCountService{

}
