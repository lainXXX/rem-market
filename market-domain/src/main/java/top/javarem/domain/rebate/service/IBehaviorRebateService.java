package top.javarem.domain.rebate.service;

import top.javarem.domain.rebate.model.entity.BehaviorEntity;

import java.util.List;

/**
 * @Author: rem
 * @Date: 2025/01/12/15:16
 * @Description:返利行为服务接口
 */
public interface IBehaviorRebateService {

    List<String> createOrder(BehaviorEntity behavior);

    Boolean isCalenderSignRebate(String userId, String outBusinessNo);
}
