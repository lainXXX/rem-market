package top.javarem.domain.strategy.model.entity;

import lombok.Data;
import top.javarem.domain.strategy.service.rule.filter.factory.DefaultFilterLogicFactory;
import top.javarem.types.common.constants.Constants;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<String> getRaffleBeforeModel() {
        return Arrays.stream(ruleModels.split(","))
                .filter(model -> DefaultFilterLogicFactory.LogicModel.isBefore(model))
                .collect(Collectors.toList());
    }

    public List<String> getRaffleExecutingModel() {
        return Arrays.stream(ruleModels.split(Constants.COMMA))
                .filter(model -> DefaultFilterLogicFactory.LogicModel.isExecuting(model))
                .collect(Collectors.toList());
    }

    public List<String> getRaffleAfterModel() {
        return Arrays.stream(ruleModels.split(Constants.COMMA))
                .filter(model -> DefaultFilterLogicFactory.LogicModel.isAfter(model))
                .collect(Collectors.toList());
    }

}
