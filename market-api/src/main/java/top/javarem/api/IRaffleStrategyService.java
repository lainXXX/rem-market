package top.javarem.api;

import org.springframework.web.bind.annotation.RequestBody;
import top.javarem.api.dto.*;
import top.javarem.api.response.Response;

import java.util.List;

/**
 * @Author: rem
 * @Date: 2024/12/01/13:31
 * @Description: 抽奖策略服务
 */
public interface IRaffleStrategyService {

    /**
     * 装配策略
     * @param strategyId
     * @return
     */
    Response<Boolean> assembleStrategy(Long strategyId);

    /**
     * 查询抽奖策略权重规则配置（抽奖次数）
     *
     * @param request
     * @return
     */
    Response<List<RaffleStrategyRuleWeightResponseDTO>> queryRaffleStrategyRuleWeight(RaffleStrategyRuleWeightRequestDTO request);

    /**
     * 展示奖品
     * @param request
     * @return
     */
    Response<List<DisplayAwardDTO>> displayAwardList(DisplayAwardRequestDTO request);

    

    /**
     * 执行抽奖
     * @param raffleStrategyRequestDTO
     * @return
     */
    Response<RaffleAwardDTO> executeRaffle(@RequestBody RaffleStrategyRequestDTO raffleStrategyRequestDTO);
}
