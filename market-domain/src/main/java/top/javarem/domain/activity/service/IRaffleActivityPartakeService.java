package top.javarem.domain.activity.service;

import top.javarem.domain.activity.model.entity.ActivityPartakeEntity;
import top.javarem.domain.activity.model.entity.UserRaffleConsumeOrderEntity;

/**
 * @Author: rem
 * @Date: 2024/12/05/15:04
 * @Description: 抽奖活动参与服务
 */
public interface IRaffleActivityPartakeService {

    /**
     * 创建用户参与抽奖订单 扣减总次数、月次数、日次数
     *
     * @return用于抽奖的配置
     */
    UserRaffleConsumeOrderEntity createOrder(ActivityPartakeEntity activityPartakeEntity);


}
