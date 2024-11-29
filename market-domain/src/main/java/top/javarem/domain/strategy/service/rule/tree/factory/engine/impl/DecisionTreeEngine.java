package top.javarem.domain.strategy.service.rule.tree.factory.engine.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import top.javarem.domain.strategy.model.vo.RuleTreeNodeLineVO;
import top.javarem.domain.strategy.model.vo.RuleTreeNodeVO;
import top.javarem.domain.strategy.model.vo.RuleTreeVO;
import top.javarem.domain.strategy.service.rule.tree.ILogicTreeNode;
import top.javarem.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import top.javarem.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;

import java.util.List;
import java.util.Map;

/**
 * @Author: rem
 * @Date: 2024/11/26/23:22
 * @Description: 规则树逻辑执行引擎
 */
public class DecisionTreeEngine implements IDecisionTreeEngine {

    private final Map<String, ILogicTreeNode> logicTreeNodeMap;

    private final RuleTreeVO ruleTree;

    public DecisionTreeEngine(Map<String, ILogicTreeNode> logicTreeNodeMap, RuleTreeVO ruleTreeVO) {
        this.logicTreeNodeMap = logicTreeNodeMap;
        this.ruleTree = ruleTreeVO;
    }

    @Override
    public DefaultTreeFactory.LogicAwardVO process(Long strategyId, Integer awardId) {
        DefaultTreeFactory.LogicAwardVO award = null;
//        获取根节点的键
        String rootNodeKey = ruleTree.getTreeRootRuleNode();
//        获取规则树的节点集合
        Map<String, RuleTreeNodeVO> treeNodeMap = ruleTree.getTreeNodeMap();
//        定义当前节点key 循环前把根节点当作第一个当前节点 开始循环
        String currentNodeKey = rootNodeKey;
        while (StringUtils.isNotBlank(currentNodeKey)) {
//            通过key获取当前节点信息
            RuleTreeNodeVO currentNode = treeNodeMap.get(currentNodeKey);
            ILogicTreeNode logicTreeNode = logicTreeNodeMap.get(currentNode.getTreeNodeKey());
            DefaultTreeFactory.TreeActionEntity actionEntity = logicTreeNode.execute(strategyId, awardId, currentNode.getRuleValue());
            award = actionEntity.getLogicAwardVO();
            String code = actionEntity.getRuleLogicCheckTypeVO().getCode();
            currentNodeKey = nextNodeKey(code, treeNodeMap.get(currentNodeKey).getTreeNodeLineVOList());
        }
        return award;
    }

    /**
     * 获取下一节点
     * @param code 节点执行逻辑后返回的情况 PASS or BLOCK
     * @param nodeLines 该节点获取下一节点的连接逻辑
     * @return
     */
    private String nextNodeKey(String code, List<RuleTreeNodeLineVO> nodeLines) {
        if (CollectionUtils.isEmpty(nodeLines)) {
            return null;
        }
         return nodeLines.stream()
                .filter(nodeLine -> matchNextNode(code, nodeLine))
                .map(RuleTreeNodeLineVO::getRuleChildNode)
                .findFirst()
                .orElseGet(() -> {
                    throw new RuntimeException("决策树引擎，nextNode 计算失败，未找到可执行节点！");
                });
    }

    private boolean matchNextNode(String matchCode, RuleTreeNodeLineVO line) {

        switch (line.getRuleLimitType()) {
            case EQUAL:
                return matchCode.equals(line.getRuleLimitValue().getCode());
            // 以下规则暂时不需要实现
            case GT:
            case LT:
            case GE:
            case LE:
            default:
                return false;
        }

    }

}
