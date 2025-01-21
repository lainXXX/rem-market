package top.javarem.domain.activity.service;

import top.javarem.domain.activity.model.entity.SkuProductEntity;

import java.util.List;

/**
 * @Author: rem
 * @Date: 2025/01/21/15:44
 * @Description:抽奖活动sku商品服务接口
 */
public interface IRaffleActivitySkuProductService {

    /**
     * 获取sku商品集合 通过活动ID
     */
    List<SkuProductEntity> getSkuProductListByActivityId(Long activityId);

}
