package top.javarem.domain.strategy.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RuleLogicCheckTypeVO {

    FILTER_BLOCK("0000", "过滤拦截；直接执行过滤逻辑"),
    FILTER_PASS("0001", "直接放行；按流程执行下一步");
    private final String code;
    private final String info;

}
