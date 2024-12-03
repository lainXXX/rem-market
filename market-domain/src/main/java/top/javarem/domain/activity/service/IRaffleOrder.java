package top.javarem.domain.activity.service;

import top.javarem.domain.activity.model.entity.SkuRechargeEntity;

/**
 * @Author: rem
 * @Date: 2024/12/01/23:24
 * @Description: 活动商品充值实体对象
 */
public interface IRaffleOrder {

    /**
     * 以sku创建抽奖活动订单，获得参与抽奖资格（可消耗的次数）
     *
     * @param cart 活动sku实体，通过sku领取活动。
     * @return 活动参与记录实体
     */
    String createActivityOrder(SkuRechargeEntity cart);

}
