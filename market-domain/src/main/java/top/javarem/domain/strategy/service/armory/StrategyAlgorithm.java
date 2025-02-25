package top.javarem.domain.strategy.service.armory;

import lombok.extern.slf4j.Slf4j;
import top.javarem.domain.strategy.model.entity.StrategyAwardEntity;
import org.springframework.stereotype.Service;
import top.javarem.domain.strategy.service.armory.algorithm.AbstractAlgorithm;
import top.javarem.domain.strategy.service.armory.algorithm.IAlgorithm;

import java.math.BigDecimal;
import java.util.*;

@Service("strategyArmoryDispatch")
@Slf4j
public class StrategyAlgorithm extends AbstractStrategyAlgorithm {

    private final Map<String, IAlgorithm> algorithmGroup;
    // 抽奖算法阈值，在多少范围内开始选择不同选择
    private final Integer ALGORITHM_THRESHOLD = 10000;

    public StrategyAlgorithm(Map<String, IAlgorithm> algorithmGroup) {
        this.algorithmGroup = algorithmGroup;
    }

    @Override
    protected Integer dispatchAlgorithm(String key) {

        String beanName = repository.queryStrategyArmoryAlgorithmFromCache(key);
        if (beanName == null) throw new RuntimeException("key" + key + " not exist");
        IAlgorithm algorithm = algorithmGroup.get(beanName);
        return algorithm.dispatchAlgorithm(key);
    }

    protected void armoryAlgorithm(String key, List<StrategyAwardEntity> awardEntities) {
//        1.获取奖品中最小概率

        BigDecimal minAwardRate = minAwardRate(awardEntities);

//        2.获取奖品范围
        double awardRange = convert(minAwardRate.doubleValue());
//        3.根据奖品范围来选择算法
        if (awardRange < ALGORITHM_THRESHOLD) {
            IAlgorithm o1Algorithm = algorithmGroup.get(AbstractAlgorithm.Algorithm.O1.getKey());
            o1Algorithm.armoryAlgorithm(key, awardEntities, new BigDecimal(awardRange));
            repository.cacheStrategyArmoryAlgorithm(key, AbstractAlgorithm.Algorithm.O1.getKey());
        } else {
            IAlgorithm o1Algorithm = algorithmGroup.get(AbstractAlgorithm.Algorithm.OLogN.getKey());
            o1Algorithm.armoryAlgorithm(key, awardEntities, new BigDecimal(awardRange));
            repository.cacheStrategyArmoryAlgorithm(key, AbstractAlgorithm.Algorithm.OLogN.getKey());
        }

    }

}
