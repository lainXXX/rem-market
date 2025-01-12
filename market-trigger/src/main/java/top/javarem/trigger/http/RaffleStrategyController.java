package top.javarem.trigger.http;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.javarem.api.IRaffleStrategyService;
import top.javarem.api.dto.DisplayAwardDTO;
import top.javarem.api.dto.DisplayAwardRequestDTO;
import top.javarem.api.dto.RaffleAwardDTO;
import top.javarem.api.dto.RaffleStrategyRequestDTO;
import top.javarem.api.response.Response;
import top.javarem.domain.activity.service.IRaffleActivityAccountQuotaService;
import top.javarem.domain.strategy.model.entity.RaffleAwardEntity;
import top.javarem.domain.strategy.model.entity.RaffleFactorEntity;
import top.javarem.domain.strategy.model.entity.StrategyAwardEntity;
import top.javarem.domain.strategy.service.IRaffleAward;
import top.javarem.domain.strategy.service.IRaffleRule;
import top.javarem.domain.strategy.service.IRaffleStrategy;
import top.javarem.domain.strategy.service.armory.IStrategyArmory;
import top.javarem.types.enums.ResponseCode;
import top.javarem.types.exception.AppException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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

    private final IRaffleStrategy raffleStrategy;

    private final IStrategyArmory strategyArmory;

    private final IRaffleAward raffleAward;

    private final IRaffleRule raffleRule;

    private final IRaffleActivityAccountQuotaService accountQuotaService;

    public RaffleStrategyController(IRaffleStrategy raffleStrategy, IStrategyArmory strategyArmory, IRaffleAward raffleAward, IRaffleRule raffleRule, IRaffleActivityAccountQuotaService accountQuotaService) {
        this.raffleStrategy = raffleStrategy;
        this.strategyArmory = strategyArmory;
        this.raffleAward = raffleAward;
        this.raffleRule = raffleRule;
        this.accountQuotaService = accountQuotaService;
    }

    @GetMapping("/assemble")
    @Override
    public Response<Boolean> assembleStrategy(@RequestParam Long strategyId) {
        log.info("预热抽奖策略 strategyId={}", strategyId);
        try {
            log.info("执行抽奖策略装配");
            boolean result = strategyArmory.assembleRaffleStrategy(strategyId);
            return Response.success(result);
        } catch (Exception e) {
            return Response.error();
        }
    }

    @PostMapping("/display_award")
    @Override
    public Response<List<DisplayAwardDTO>> displayAwardList(@RequestBody DisplayAwardRequestDTO request) {
//        1.参数检验
        String userId = request.getUserId();
        Long activityId = request.getActivityId();
        if (StringUtils.isBlank(userId) || activityId == null) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        log.info("展示奖品请求 用户id:{} 活动id:{}", userId, activityId);
//        2.获取奖品集合
        List<StrategyAwardEntity> awards = raffleAward.displayAwardByActivityId(activityId);
//        3.1获取规则模型
        String[] treeIds = awards.stream()
                .map(StrategyAwardEntity::getModels)
                .filter(model -> !StringUtils.isEmpty(model))
                .toArray(String[]::new);
        log.info(treeIds.toString());
//            3.2从规则树节点查询规则配置- N次后解锁奖品
        Map<String, Integer> awardUnlockCountMap = raffleRule.getAwardUnlockCountMap(treeIds);
//            4.从用户账户中查询当日的抽奖次数
        Integer todayPartakeCount = accountQuotaService.getActivityTodayPartakeCount(userId, activityId);
        List<DisplayAwardDTO> displayAwardDTOS = new ArrayList<>(awards.size());
        for (StrategyAwardEntity award : awards) {
            Integer awardUnlockCount = awardUnlockCountMap.get(award.getModels());
            DisplayAwardDTO awardDTO = new DisplayAwardDTO();
            BeanUtils.copyProperties(award, awardDTO);
            awardDTO.setUnlockCount(awardUnlockCount);
            awardDTO.setIsUnLocked(awardUnlockCount == null || todayPartakeCount > awardUnlockCount ? true : false);
            awardDTO.setWaitUnlockCount(awardDTO.getIsUnLocked() ? 0 : awardUnlockCount - todayPartakeCount);
            displayAwardDTOS.add(awardDTO);
        }
        return Response.success(displayAwardDTOS);/*catch (Exception e) {
            return Response.error();
        }*/
    }

    @PostMapping("/random_raffle")
    @Override
    public Response<RaffleAwardDTO> executeRaffle(@RequestBody RaffleStrategyRequestDTO raffleStrategyRequestDTO) {
        try {
            Long strategyId = raffleStrategyRequestDTO.getStrategyId();
            String userId = raffleStrategyRequestDTO.getUserId();
            log.info("执行随机抽奖 策略ID: {} 用户ID: {}", strategyId, userId);
//        参数校验
            if (StringUtils.isBlank(userId) || strategyId == null) {
                throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
            }
            RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(RaffleFactorEntity.builder()
                    .userId(userId)
                    .strategyId(strategyId)
                    .build());
            Integer awardId = raffleAwardEntity.getAwardId();
            log.info("抽中的奖品ID: {}", awardId);
            return Response.success(RaffleAwardDTO.builder()
                    .awardId(awardId)
                    .build());
        } catch (Exception e) {
            return Response.error();
        }
    }
}
