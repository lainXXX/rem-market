package top.javarem.trigger.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.javarem.api.IRaffleStrategyService;
import top.javarem.api.dto.DisplayAwardDTO;
import top.javarem.api.dto.RaffleAwardDTO;
import top.javarem.api.dto.RaffleStrategyRequestDTO;
import top.javarem.api.response.Response;
import top.javarem.domain.strategy.model.entity.RaffleAwardEntity;
import top.javarem.domain.strategy.model.entity.RaffleFactorEntity;
import top.javarem.domain.strategy.model.entity.StrategyAwardEntity;
import top.javarem.domain.strategy.service.IRaffleAward;
import top.javarem.domain.strategy.service.IRaffleStrategy;
import top.javarem.domain.strategy.service.armory.IStrategyArmory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: rem
 * @Date: 2024/12/01/13:40
 * @Description:抽奖策略服务 controller
 */
@RestController
@RequestMapping("/${app.config.api-version}/raffle/strategy")
@Slf4j
@CrossOrigin("*")
public class RaffleStrategyController implements IRaffleStrategyService {

    @Autowired
    private IRaffleStrategy raffleStrategy;

    @Autowired
    private IStrategyArmory strategyArmory;

    @Autowired
    private IRaffleAward raffleAward;

    @GetMapping("/assemble")
    @Override
    public Response<Boolean> assembleStrategy(@RequestParam Long strategyId) {
        log.info("执行抽奖策略装配");
        boolean result = strategyArmory.assembleRaffleStrategy(strategyId);
        return Response.success(result);
    }

    @GetMapping("/display_award")
    @Override
    public Response<List<DisplayAwardDTO>> displayAwardList(@RequestParam Long strategyId) {
        log.info("执行展示奖品操作 策略ID: {}", strategyId);
        List<StrategyAwardEntity> awards = raffleAward.displayAward(strategyId);
        List<DisplayAwardDTO> displayAwardDTOS = awards.stream().map(award -> {
            DisplayAwardDTO displayAwardDTO = new DisplayAwardDTO();
            BeanUtils.copyProperties(award, displayAwardDTO);
            return displayAwardDTO;
        }).collect(Collectors.toList());
        return Response.success(displayAwardDTOS);
    }

    @PostMapping("/random_raffle")
    @Override
    public Response<RaffleAwardDTO> executeRaffle(@RequestBody RaffleStrategyRequestDTO raffleStrategyRequestDTO) {
        log.info("执行随机抽奖 策略ID: {} 用户ID: {}", raffleStrategyRequestDTO.getStrategyId(), raffleStrategyRequestDTO.getUserId());
        RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(RaffleFactorEntity.builder()
                .userId(raffleStrategyRequestDTO.getUserId())
                .strategyId(raffleStrategyRequestDTO.getStrategyId())
                .build());
        log.info("抽中的奖品ID: {}", raffleAwardEntity.getAwardId());
        return Response.success(RaffleAwardDTO.builder()
                .awardId(raffleAwardEntity.getAwardId())
                .build());
    }
}
