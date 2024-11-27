package top.javarem.domain.strategy.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: rem
 * @Date: 2024/11/26/15:07
 * @Description: 策略奖品规则值对象 只能从数据库中获取
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StrategyAwardRuleModelsVO {

    private String ruleModels;

}
