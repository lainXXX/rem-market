package top.javarem.domain.strategy.service.rule.tree.factory;

import lombok.*;
import org.springframework.stereotype.Service;
import top.javarem.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import top.javarem.domain.strategy.model.vo.RuleTreeVO;
import top.javarem.domain.strategy.service.rule.tree.ILogicTreeNode;
import top.javarem.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import top.javarem.domain.strategy.service.rule.tree.factory.engine.impl.DecisionTreeEngine;

import java.util.Map;

/**
 * @Author: rem
 * @Date: 2024/11/26/14:38
 * @Description:
 */
@Service
public class DefaultTreeFactory {

    private final Map<String, ILogicTreeNode> logicTreeNodeMap;

    public DefaultTreeFactory(Map<String, ILogicTreeNode> logicTreeNodeMap) {
        this.logicTreeNodeMap = logicTreeNodeMap;
    }

//    开启规则树逻辑
    public IDecisionTreeEngine openLogicTree(RuleTreeVO ruleTreeVO) {

        return new DecisionTreeEngine(logicTreeNodeMap, ruleTreeVO);

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LogicAwardVO {

        private Integer awardId;
        private String ruleModel;

    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TreeActionEntity {

        private RuleLogicCheckTypeVO ruleLogicCheckTypeVO;

        private LogicAwardVO logicAwardVO;

    }

}
