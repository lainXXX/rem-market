package top.javarem.api;

import top.javarem.api.dto.*;
import top.javarem.api.response.Response;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: rem
 * @Date: 2024/12/11/21:56
 * @Description:抽奖活动服务
 */
public interface IRaffleActivityService {

    /**
     * 装配活动 数据预热
     * @param activityId
     * @return
     */
    Response<Boolean> armory(Long activityId);

    Response<ActivityDrawResponseDTO> draw(ActivityDrawRequestDTO request);

    /**
     * 日历签到返利接口
     * @param userId 用户ID
     * @return
     */
    Response<Boolean> calenderSignRebate(String userId);

    /**
     * 查询用户是否签到
     * @param userId 用户Id
     * @return true or false
     */
    Response<Boolean> isCalenderSignRebate(String userId);

    /**
     * 查询用户账户某活动的参与次数
     * @param request
     * @return
     */
    Response<UserActivityAccountResponseDTO> queryUserActivityAccount(UserActivityAccountRequestDTO request);

    /**
     * 积分兑换商品
     * @param request 商品购物车请求对象
     * @return
     */
    Response<Boolean> creditExchangeProduct(SkuProductShopCartRequestDTO request);

    /**
     * 获取用户账户积分值
     * @param userId 用户ID
     * @return 积分值
     */
    Response<BigDecimal> queryUserCredit(String userId);

    /**
     * 通过活动ID获取sku商品集合
     * @param activityId 活动ID
     * @return sku商品集合
     */
    Response<List<SkuProductResponseDTO>> getSkuProductListByActivityId(Long activityId);

}
