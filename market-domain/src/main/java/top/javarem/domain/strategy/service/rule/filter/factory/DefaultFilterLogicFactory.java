package top.javarem.domain.strategy.service.rule.filter.factory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;
import top.javarem.domain.strategy.annotation.StrategyLogic;
import top.javarem.domain.strategy.model.entity.RuleActionEntity;
import top.javarem.domain.strategy.service.rule.filter.ILogicFilter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: rem
 * @Date: 2024/11/21/22:57
 * @Description:
 */
@Service
public class DefaultFilterLogicFactory {

    public Map<String, ILogicFilter<?>> filterMap = new ConcurrentHashMap<>();

    //    spring在注册DefaultLogicFactory的实例时会自动寻找构造函数来进行创建 如果没有自定义则选择默认构造函数
//    在定义参数List<ILogicFilter<?>> logicFilters时 如果ILogicFilter的实现标注了@Component相关的注解 那么spring会自动把这些满足条件的bean收集到list集合中
//    在收集到list后 ILogicFilter的实现如果标准了@StrategyLogic注解 则会被收集到map集合中
    @Autowired
    public DefaultFilterLogicFactory(List<ILogicFilter<?>> logicFilters) {

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

        RULE_WEIGHT("rule_weight", "【抽奖前规则】根据抽奖权重返回可抽奖范围KEY", "before"),
        RULE_BLACKLIST("rule_blacklist", "【抽奖前规则】黑名单规则过滤，命中黑名单则直接返回", "before"),
        RULE_LOCK("rule_lock", "【抽奖中规则】，抽奖n次后，对应奖品可解锁抽奖", "executing"),
        RULE_LUCK_AWARD("rule_luck_award", "【抽奖后规则，抽奖n次后，对应奖品可解锁抽奖", "after"),

        ;

        private final String code;
        private final String info;
        private final String type;

        public static boolean isBefore(String code) {
            return "before".equals(LogicModel.valueOf(code.toUpperCase()).type);
        }

        public static boolean isExecuting(String code) {
            return "executing".equals(LogicModel.valueOf(code.toUpperCase()).type);
        }

        public static boolean isAfter(String code) {
            return "after".equals(LogicModel.valueOf(code.toUpperCase()).type);
        }

    }

}
