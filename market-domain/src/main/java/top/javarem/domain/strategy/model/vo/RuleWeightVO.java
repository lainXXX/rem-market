package top.javarem.domain.strategy.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: rem
 * @Date: 2025/01/15/14:56
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RuleWeightVO {

    private String ruleValue;

    private Integer weight;

    private List<Integer> awardIds;

    private List<Award> awardList;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Award {

        private Integer awardId;

        private String awardTitle;
    }

}
