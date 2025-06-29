package top.javarem.domain.award.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author
 * @description 奖品状态枚举值对象 【值对象，用于描述对象属性的值，一个对象中，一个属性，有多个状态值。】
 * @create 2024-04-06 09:13
 */
@Getter
@AllArgsConstructor
public enum AwardStateVO {

    create("create", "创建"),
    completed("completed", "发奖完成"),
    fail("fail", "发奖失败"),
    ;

    private final String code;
    private final String desc;

}
