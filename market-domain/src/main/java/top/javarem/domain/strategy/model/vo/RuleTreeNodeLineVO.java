package top.javarem.domain.strategy.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author rem
 * @description 规则树节点指向线对象。用于衔接 from->to 节点链路关系
 * @create 2024-01-27 10:49
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleTreeNodeLineVO {

    /** 规则树ID */
    private String treeId;
    /** 规则Key节点 From */
    private String ruleNodeKey;
    /** 规则Key节点 To */
    private String ruleChildNode;
    /** 限定类型；1:=;2:>;3:<;4:>=;5<=;6:enum[枚举范围] */
    private RuleLimitTypeVO ruleLimitType;
    /** 限定值（到下个节点） */
    private RuleLogicCheckTypeVO ruleLimitValue;

}
