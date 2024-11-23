package top.javarem.infrastructure.dao.Iservice.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.javarem.infrastructure.dao.Iservice.IStrategyService;
import top.javarem.infrastructure.dao.entity.Strategy;
import top.javarem.infrastructure.dao.mapper.StrategyMapper;

/**
 * @Author: rem
 * @Date: 2024/11/22/14:10
 * @Description:
 */
@Service
public class StrategyServiceImpl extends ServiceImpl<StrategyMapper, Strategy> implements IStrategyService {
}
