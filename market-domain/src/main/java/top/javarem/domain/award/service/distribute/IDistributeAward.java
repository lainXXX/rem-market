package top.javarem.domain.award.service.distribute;

import top.javarem.domain.award.model.entity.DistributeAwardEntity;

/**
 * @Author: rem
 * @Date: 2025/01/15/20:53
 * @Description:分发奖品接口
 */
public interface IDistributeAward {

    void giveOutPrizes(DistributeAwardEntity distributeAwardEntity);

}
