package top.javarem.infrastructure.dao.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName rule_tree_node_line
 */
@Data
public class RuleTreeNodeLine implements Serializable {
    /**
     * 自增id
     */
    private Integer id;

    /**
     * 规则树id
     */
    private String treeId;

    /**
     * 节点
     */
    private String ruleNodeKey;

    /**
     * 下一节点
     */
    private String ruleChildNode;

    /**
     * 连接下一节点条件类型
     */
    private String ruleLimitType;

    /**
     * 连接下一节点的值
     */
    private String ruleLimitValue;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}