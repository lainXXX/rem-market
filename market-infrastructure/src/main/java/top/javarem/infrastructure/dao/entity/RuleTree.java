package top.javarem.infrastructure.dao.entity;


import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName rule_tree
 */
@TableName(value ="rule_tree")
@Data
public class RuleTree implements Serializable {
    /**
     * 自增id
     */
    @TableId
    private Integer id;

    /**
     * 规则树id
     */
    private String treeId;

    /**
     * 规则树名称
     */
    private String treeName;

    /**
     * 规则树描述
     */
    private String treeDesc;

    /**
     * 根节点
     */
    private String treeRootNodeKey;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}