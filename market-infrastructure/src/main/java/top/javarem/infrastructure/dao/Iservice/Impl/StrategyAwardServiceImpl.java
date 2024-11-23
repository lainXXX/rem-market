package top.javarem.infrastructure.dao.Iservice.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.javarem.infrastructure.dao.Iservice.IStrategyAwardService;
import top.javarem.infrastructure.dao.entity.StrategyAward;
import top.javarem.infrastructure.dao.mapper.StrategyAwardMapper;

@Service
public class StrategyAwardServiceImpl extends ServiceImpl<StrategyAwardMapper, StrategyAward> implements IStrategyAwardService {

}
