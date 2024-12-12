package top.javarem.domain.activity.service.armory;

/**
 * @Author: rem
 * @Date: 2024/12/03/18:35
 * @Description: 活动预热
 */
public interface IActivityArmory {

    Boolean assembleRaffleActivityByActivityId(Long activityId);

    /**
     * 装配活动信息
     * @param sku 活动sku
     */
    Boolean assembleRaffleActivity(Long sku);

}
