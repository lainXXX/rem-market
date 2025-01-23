package top.javarem.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: rem
 * @Date: 2025/01/15/14:40
 * @Description:
 */
@Data
public class RaffleStrategyRuleWeightResponseDTO implements Serializable {

    private Integer ruleWeightCount;

    private Integer userActivityAccountTotalCount;

    private List<StrategyAward> strategyAwards;

    @Data
    public static class StrategyAward {

        private Integer awardId;

        private String awardTitle;

    }

}
