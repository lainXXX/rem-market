package top.javarem.api;

import top.javarem.api.dto.ActivityDrawRequestDTO;
import top.javarem.api.dto.ActivityDrawResponseDTO;
import top.javarem.api.response.Response;

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

}
