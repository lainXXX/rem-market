package top.javarem.domain.rebate.repository;

import top.javarem.domain.rebate.model.aggregate.BehaviorRebateAggregate;
import top.javarem.domain.rebate.model.vo.DailyBehaviorRebateVO;

import java.util.List;

/**
 * @Author: rem
 * @Date: 2025/01/12/15:59
 * @Description:
 */
public interface IBehaviorRebateRepository {


    List<DailyBehaviorRebateVO> queryDailyBehaviorRebateConfig(String behaviorType);

    void saveUserRebateRecord(String userId, List<BehaviorRebateAggregate> behaviorRebateAggregates);

}
