package top.javarem.domain.activity.service;

import top.javarem.domain.activity.model.entity.SkuRechargeEntity;

/**
 * @Author: rem
 * @Date: 2024/12/01/23:24
 * @Description: 抽奖活动账户定额服务
 */
public interface IRaffleActivityAccountQuotaService {

    /**
     * 以sku创建抽奖活动订单，获得参与抽奖资格（可消耗的次数）
     *
     * @param cart 活动sku实体，通过sku领取活动。
     * @return 活动参与记录实体
     */
    String createOrder(SkuRechargeEntity cart);

    /**
     * 获取用户当日参与活动次数
     * @param userId 用户id
     * @param activityId 活动id
     * @return
     */
    Integer getActivityTodayPartakeCount(String userId, Long activityId);
}
