package top.javarem.infrastructure.dao.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName rule_tree_node
 */
@Data
public class RuleTreeNode implements Serializable {
    /**
     * 自增id
     */
    private Integer id;

    /**
     * 规则树id
     */
    private String treeId;

    /**
     * 规则树key
     */
    private String treeNodeKey;

    /**
     * 规则描述
     */
    private String ruleDesc;

    /**
     * 规则赋值
     */
    private String ruleValue;

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