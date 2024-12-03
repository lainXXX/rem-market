package top.javarem.domain.activity.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 活动状态值对象
 * @create 2024-03-16 11:16
 */
@Getter
@AllArgsConstructor
public enum ActivityStateVO {

    create("create", "创建");

    private final String code;
    private final String desc;

}
