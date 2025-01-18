package top.javarem.domain.activity.service;

import top.javarem.domain.activity.model.entity.ActivityAccountCountEntity;
import top.javarem.domain.activity.model.entity.DeliveryOrderEntity;
import top.javarem.domain.activity.model.entity.SkuRechargeEntity;

/**
 * @Author: rem
 * @Date: 2024/12/01/23:24
 * @Description: 抽奖活动账户定额服务
 */
public interface IRaffleActivityAccountQuotaService {

    /**
     * 以sku创建抽奖活动订单，获得参与抽奖资格（可消耗的次数）
     *【打卡、签到、分享、对话、积分兑换】等动作下，创建活动订单，给用户的活动账户【日、月】充值可用的抽奖次数
     * @param cart 活动sku实体，通过sku领取活动。
     * @return 活动参与记录实体
     */
    String createOrder(SkuRechargeEntity cart);

    /**
     * 订单出货 - 积分充值
     * @param deliveryOrderEntity 出货单实体对象
     */
    void updateOrder(DeliveryOrderEntity deliveryOrderEntity);

    /**
     * 获取用户当日参与活动次数
     * @param userId 用户id
     * @param activityId 活动id
     * @return
     */
    Integer getActivityTodayPartakeCount(String userId, Long activityId);

    ActivityAccountCountEntity getActivityAccountCount(String userId, Long activityId);
}
