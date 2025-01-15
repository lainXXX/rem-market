package top.javarem.infrastructure.dao.iService;

import com.baomidou.mybatisplus.extension.service.IService;
import top.javarem.infrastructure.dao.entity.StrategyAward;

import java.util.List;

public interface IStrategyAwardService extends IService<StrategyAward> {

    List<StrategyAward> getAwardListByStrategyId(Long strategyId);
}
