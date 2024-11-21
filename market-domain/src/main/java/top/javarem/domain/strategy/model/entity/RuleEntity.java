package top.javarem.domain.strategy.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;
import top.javarem.types.enums.ResponseCode;
import top.javarem.types.exception.AppException;

/**
 * 
 * @TableName rule
 */
@Data
public class RuleEntity implements Serializable {

    /**
     * 策略id
     */
    private Long strategyId;

    /**
     * 奖品id【规则为策略 则不需要奖品id】
     */
    private Integer awardId;

    /**
     * 【rule_random - 随机值计算、 rule_lock - 抽奖几次后解锁、 rule_luck_award- 幸运奖(兜底奖品)】
     */
    private String ruleModel;

    /**
     * 1-策略规则、 2-奖品规则
     */
    private Integer ruleType;

    /**
     * 抽奖规则比值
     */
    private String ruleValue;

    /**
     * 抽奖规则描述
     */
    private String ruleDesc;

//    4000:101,102,103,104,105,106 6000:102,103,104,105,106,107,108,109
//    拆分规则权重 装入map集合 如4000 {101,102,103} 6000 {102,103}
    public Map<String, List<Integer>> getRuleWeightValues() {
        if (!"rule_weight".equals(ruleModel)) return null;
        Map<String, List<Integer>> ruleWeightValues = new HashMap<String, List<Integer>>();
        for (String ruleWeights : ruleValue.split(" ")) {
            String[] ruleWeight = ruleWeights.split(":");
            if (ruleWeight.length != 2) {
                throw new AppException(ResponseCode.RULE_WEIGHT_ERROR.getInfo());
            }
            List<Integer> awardIds = new ArrayList<>();
            for (String s : ruleWeight[1].split(",")) {
                awardIds.add(Integer.parseInt(s));
            }
            ruleWeightValues.put(ruleWeight[0], awardIds);
        }
        return ruleWeightValues;
    }

    private static final long serialVersionUID = 1L;
}