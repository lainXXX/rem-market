package top.javarem.domain.strategy.service.rule.chain.factory;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.javarem.domain.strategy.model.entity.StrategyEntity;
import top.javarem.domain.strategy.repository.IStrategyRepository;
import top.javarem.domain.strategy.service.rule.chain.IStrategyLogicChain;

import java.util.Map;

/**
 * @Author: rem
 * @Date: 2024/11/25/21:25
 * @Description:装配责任链工厂 将所有策略节点都有装配到工厂
 */
@Service
public class DefaultChainFactory {

    private final Map<String, IStrategyLogicChain> logicChainMap;

    private IStrategyRepository repository;

    @Autowired
    public DefaultChainFactory(Map<String, IStrategyLogicChain> logicChainMap, IStrategyRepository repository) {
        this.logicChainMap = logicChainMap;
        this.repository = repository;
    }

    /**
     * 开启一条责任链
     * @param strategyId 策略id
     * @return 返回一条责任链
     */
    public IStrategyLogicChain openLogicChain(Long strategyId) {

//        获取策略规则
        StrategyEntity strategyEntity = repository.getStrategyEntity(strategyId);
        String[] ruleModels = strategyEntity.getRuleModels();
//        如果没有策略规则则执行默认规则
        if (ruleModels == null || ruleModels.length == 0)  return logicChainMap.get("default");
//        责任链初始节点
        IStrategyLogicChain root = logicChainMap.get(ruleModels[0]);
//        循环前设置当前节点为初始节点 可以知道初始节点是没有设置在AbstractStrategyLogicChain中的next节点中的
        IStrategyLogicChain current = root;
        for (int i = 1; i < ruleModels.length; i++) {
            IStrategyLogicChain next = logicChainMap.get(ruleModels[i]);
            if (next != null) {
                current = current.appendNode(next);
            }
        }
//        最后执行默认规则
        current.appendNode(logicChainMap.get("default"));
        return root;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LogicAwardVO {

        private Integer awardId;

        private String ruleModel;

    }

    @Getter
    @AllArgsConstructor
    public enum LogicModel {

        BLACKLIST("rule_blacklist", "黑名单规则"),
        WEIGHT("rule_weight", "权重规则"),
        DEFAULT("default", "默认规则");

        private String code;
        private String info;

    }

}
