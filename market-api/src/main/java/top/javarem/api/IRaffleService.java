package top.javarem.api;

import org.springframework.web.bind.annotation.RequestBody;
import top.javarem.api.dto.DisplayAwardDTO;
import top.javarem.api.dto.RaffleAwardDTO;
import top.javarem.api.dto.RaffleRequestDTO;
import top.javarem.api.response.Response;

import java.util.List;

/**
 * @Author: rem
 * @Date: 2024/12/01/13:31
 * @Description: 抽奖接口
 */
public interface IRaffleService {

    Response<Boolean> assembleStrategy(Long strategyId);

    Response<List<DisplayAwardDTO>> displayAwardList(Long strategyId);


    Response<RaffleAwardDTO> executeRaffle(@RequestBody RaffleRequestDTO raffleRequestDTO);
}
