package top.javarem.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: rem
 * @Date: 2024/11/21/16:38
 * @Description: 规则问题实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RuleMatterEntity {

    private String userId;

    private Long strategyId;

    private Integer awardId;

    private String ruleModel;

}
