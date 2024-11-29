package top.javarem.domain.strategy.model.entity;

import lombok.Data;
import top.javarem.types.common.constants.Constants;


/**
 * @Author: rem
 * @Date: 2024/11/22/09:01
 * @Description:
 */
@Data
public class StrategyEntity {

    private Long strategyId;

    private String strategyDesc;

    private String ruleModels;

    public StrategyEntity() {

    }

    public String[] getRuleModels() {
        if (ruleModels == null) return null;
        return ruleModels.split(Constants.COMMA);
    }


}
