package top.javarem.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.javarem.domain.strategy.model.vo.RuleLogicCheckTypeVO;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleActionEntity<T extends RuleActionEntity.RaffleEntity> {

    private String code = RuleLogicCheckTypeVO.FILTER_PASS.getCode();
    private String info = RuleLogicCheckTypeVO.FILTER_PASS.getInfo();
    private String ruleModel;
    private T data;


    public static class RaffleEntity {

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RaffleBeforeEntity extends RaffleEntity {
        private Long strategyId;
        private String ruleWeightValueKey;
        private Integer awardId;
    }

    public static class RaffleExecutingEntity extends RaffleEntity {
    }

    public static class RaffleAfterEntity {

    }
}
