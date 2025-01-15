package top.javarem.infrastructure.dao.iService.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.javarem.infrastructure.dao.iService.IStrategyAwardService;
import top.javarem.infrastructure.dao.entity.StrategyAward;
import top.javarem.infrastructure.dao.mapper.StrategyAwardMapper;

import java.util.Collections;
import java.util.List;

@Service
public class StrategyAwardServiceImpl extends ServiceImpl<StrategyAwardMapper, StrategyAward> implements IStrategyAwardService {

    @Override
    public List<StrategyAward> getAwardListByStrategyId(Long strategyId) {

        return this.lambdaQuery()
                .select(StrategyAward::getStrategyId, StrategyAward::getAwardId, StrategyAward::getAwardTitle, StrategyAward::getAwardSubtitle, StrategyAward::getAwardSubtitle, StrategyAward::getAwardCount, StrategyAward::getAwardCountSurplus, StrategyAward::getSort, StrategyAward::getRate, StrategyAward::getModels, StrategyAward::getSort)
                .eq(StrategyAward::getStrategyId, strategyId)
                .list();
    }
}
