package generator.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import generator.domain.RaffleActivity;
import generator.service.RaffleActivityService;
import generator.mapper.RaffleActivityMapper;
import org.springframework.stereotype.Service;

/**
* @author aaa
* @description 针对表【raffle_activity】的数据库操作Service实现
* @createDate 2024-12-01 17:14:17
*/
@Service
public class RaffleActivityServiceImpl extends ServiceImpl<RaffleActivityMapper, RaffleActivity>
implements RaffleActivityService{

}
