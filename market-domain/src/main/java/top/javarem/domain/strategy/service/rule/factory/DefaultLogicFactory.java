package top.javarem.domain.strategy.service.rule.factory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;
import top.javarem.domain.strategy.annotation.StrategyLogic;
import top.javarem.domain.strategy.model.entity.RuleActionEntity;
import top.javarem.domain.strategy.service.rule.ILogicFilter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: rem
 * @Date: 2024/11/21/22:57
 * @Description:
 */
@Service
public class DefaultLogicFactory {

    public Map<String, ILogicFilter<?>> filterMap = new ConcurrentHashMap<>();

    //    spring在注册DefaultLogicFactory的实例时会自动寻找构造函数来进行创建 如果没有自定义则选择默认构造函数
//    在定义参数List<ILogicFilter<?>> logicFilters时 如果ILogicFilter的实现标注了@Component相关的注解 那么spring会自动把这些满足条件的bean收集到list集合中
//    在收集到list后 ILogicFilter的实现如果标准了@StrategyLogic注解 则会被收集到map集合中
    @Autowired
    public DefaultLogicFactory(List<ILogicFilter<?>> logicFilters) {

        logicFilters.forEach(logicFilter -> {
            StrategyLogic strategyLogic = AnnotationUtils.findAnnotation(logicFilter.getClass(), StrategyLogic.class);
            if (strategyLogic != null)
                filterMap.put(strategyLogic.logicModel().code, logicFilter);
        });

    }

    public <T extends RuleActionEntity.RaffleEntity> Map<String, ILogicFilter<T>> getFilterMap() {
        return (Map<String, ILogicFilter<T>>) (Map<?, ?>) filterMap;
    }

    @Getter
    @AllArgsConstructor
    public enum LogicModel {

        RULE_WIGHT("rule_weight", "【抽奖前规则】根据抽奖权重返回可抽奖范围KEY"),
        RULE_BLACKLIST("rule_blacklist", "【抽奖前规则】黑名单规则过滤，命中黑名单则直接返回"),

        ;

        private final String code;
        private final String info;

    }

}
