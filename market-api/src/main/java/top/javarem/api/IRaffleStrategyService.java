package top.javarem.api;

import org.springframework.web.bind.annotation.RequestBody;
import top.javarem.api.dto.DisplayAwardDTO;
import top.javarem.api.dto.DisplayAwardRequestDTO;
import top.javarem.api.dto.RaffleAwardDTO;
import top.javarem.api.dto.RaffleStrategyRequestDTO;
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
