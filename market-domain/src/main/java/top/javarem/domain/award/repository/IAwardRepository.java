package top.javarem.domain.award.repository;

import top.javarem.domain.award.model.aggregate.GiveOutPrizeAggregate;
import top.javarem.domain.award.model.aggregate.UserAwardRecodeAggregate;

/**
 * @Author: rem
 * @Date: 2024/12/08/20:03
 * @Description:奖品仓储接口
 */
public interface IAwardRepository {
    void saveAwardRecord(UserAwardRecodeAggregate userAwardRecodeAggregate);

    String getAwardConfig(Integer awardId);

    void saveGiveOutPrizeAggregate(GiveOutPrizeAggregate giveOutPrizeAggregate);

    String getAwardKey(Integer awardId);
}
