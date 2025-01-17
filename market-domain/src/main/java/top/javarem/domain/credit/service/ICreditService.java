package top.javarem.domain.credit.service;

import top.javarem.domain.credit.model.entity.TradeEntity;

/**
 * @Author: rem
 * @Date: 2025/01/17/17:05
 * @Description:积分服务接口
 */
public interface ICreditService {

    /**
     *创建积分订单
     * @param tradeEntity 交易实体
     * @return订单号
     */
    String createOrder(TradeEntity tradeEntity);

}
