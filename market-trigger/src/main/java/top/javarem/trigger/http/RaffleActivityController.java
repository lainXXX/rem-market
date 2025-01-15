package top.javarem.trigger.http;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.javarem.api.IRaffleActivityService;
import top.javarem.api.dto.ActivityDrawRequestDTO;
import top.javarem.api.dto.ActivityDrawResponseDTO;
import top.javarem.api.dto.UserActivityAccountRequestDTO;
import top.javarem.api.dto.UserActivityAccountResponseDTO;
import top.javarem.api.response.Response;
import top.javarem.domain.activity.model.entity.ActivityAccountCountEntity;
import top.javarem.domain.activity.model.entity.ActivityPartakeEntity;
import top.javarem.domain.activity.model.entity.UserRaffleConsumeOrderEntity;
import top.javarem.domain.activity.service.IRaffleActivityPartakeService;
import top.javarem.domain.activity.service.armory.IActivityArmory;
import top.javarem.domain.activity.service.quota.RaffleActivityAccountQuotaService;
import top.javarem.domain.award.model.entity.UserAwardRecordEntity;
import top.javarem.domain.award.model.vo.AwardStateVO;
import top.javarem.domain.award.service.IAwardService;
import top.javarem.domain.rebate.model.entity.BehaviorEntity;
import top.javarem.domain.rebate.model.vo.BehaviorTypeVO;
import top.javarem.domain.rebate.service.IBehaviorRebateService;
import top.javarem.domain.strategy.model.entity.RaffleAwardEntity;
import top.javarem.domain.strategy.model.entity.RaffleFactorEntity;
import top.javarem.domain.strategy.service.IRaffleStrategy;
import top.javarem.domain.strategy.service.armory.IStrategyArmory;
import top.javarem.types.common.constants.Constants;
import top.javarem.types.enums.ResponseCode;
import top.javarem.types.exception.AppException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Author: rem
 * @Date: 2024/12/11/22:48
 * @Description:抽奖活动服务 controller
 */
@Slf4j
@RestController
@RequestMapping("/${app.config.api-version}/raffle/activity")
@CrossOrigin("*")
public class RaffleActivityController implements IRaffleActivityService {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    @Autowired
    private IRaffleActivityPartakeService partakeService;
    @Autowired
    private IActivityArmory activityArmory;
    @Autowired
    private IStrategyArmory strategyArmory;
    @Autowired
    private IRaffleStrategy strategy;
    @Autowired
    private IAwardService awardService;
    @Autowired
    private IBehaviorRebateService rebateService;
    @Autowired
    private RaffleActivityAccountQuotaService raffleActivityAccountQuotaService;

    @GetMapping("/armory")
    @Override
    public Response<Boolean> armory(Long activityId) {
        try {
            log.info("活动数据装配 activityId:{}", activityId);
            if (activityId == null) {
                throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
            }
//            装配活动
            activityArmory.assembleRaffleActivityByActivityId(activityId);
//            顺便装配策略
            strategyArmory.assembleRaffleStrategyByActivityId(activityId);
            return Response.success(true);
        } catch (Exception e) {
            log.error("活动数据装配失败 activityId:{}", activityId);
            return Response.success(false);
        }
    }

    @PostMapping("/draw")
    @Override
    public Response<ActivityDrawResponseDTO> draw(@RequestBody ActivityDrawRequestDTO request) {
        try {
            log.info("抽奖活动- 用户id:{} 活动id:{}", request.getActivityId(), request.getActivityId());
//        1.参数校验
            if (request.getActivityId() == null || StringUtils.isBlank(request.getUserId())) {
                throw new AppException(Constants.ResponseCode.ILLEGAL_PARAMETER.getCode(), Constants.ResponseCode.ILLEGAL_PARAMETER.getInfo());
            }
//        2.参与活动- 创建参与记录订单
            UserRaffleConsumeOrderEntity order = partakeService.createOrder(ActivityPartakeEntity.builder()
                    .userId(request.getUserId())
                    .activityId(request.getActivityId())
                    .build());
            log.info("活动抽奖- 创建参与记录订单 orderId:{}", order.getOrderId());
//        3.抽奖策略- 执行抽奖
            RaffleAwardEntity raffleAwardEntity = strategy.performRaffle(RaffleFactorEntity.builder()
                    .strategyId(order.getStrategyId())
                    .userId(order.getUserId())
                    .endTime(order.getEndTime())
                    .build());
//        4.存放结果- 写入中奖记录
            UserAwardRecordEntity userAwardRecordEntity = UserAwardRecordEntity.builder()
                    .userId(order.getUserId())
                    .activityId(order.getActivityId())
                    .strategyId(order.getStrategyId())
                    .awardId(raffleAwardEntity.getAwardId())
                    .orderId(order.getOrderId())
                    .status(AwardStateVO.create.getCode())
                    .awardTitle(raffleAwardEntity.getAwardTitle())
                    .awardTime(new Date())
                    .build();

            awardService.saveUserAwardRecord(userAwardRecordEntity);
            return Response.success(ActivityDrawResponseDTO.builder()
                    .awardId(raffleAwardEntity.getAwardId())
                    .awardTitle(raffleAwardEntity.getAwardTitle())
                    .awardIndex(raffleAwardEntity.getSort())
                    .build());
        } catch (AppException e) {
            log.error("活动抽奖失败 userId:{} activityId:{}", request.getUserId(), request.getActivityId(), e);
            return Response.<ActivityDrawResponseDTO>builder()
                    .code(e.getCode())
                    .info(e.getInfo())
                    .build();
        } catch (Exception e) {
            log.error("活动抽奖失败 userId:{} activityId:{}", request.getUserId(), request.getActivityId(), e);
            return Response.error();
        }
    }

    @PostMapping("/calendar_sign_rebate")
    @Override
    public Response<Boolean> calenderSignRebate(String userId) {

        try {
            log.info("日历签到返利 用户ID: {}", userId);
            BehaviorEntity behaviorEntity = new BehaviorEntity();
            behaviorEntity.setUserId(userId);
            behaviorEntity.setBehaviorType(BehaviorTypeVO.SIGN);
            behaviorEntity.setOutBusinessNo(dateFormat.format(new Date()));
            List<String> orderIds = rebateService.createOrder(behaviorEntity);
            log.info("签到返利完成 userId:{} orderIds:{}", userId, orderIds);
            return Response.success(true);
        } catch (AppException e) {
            log.error("日历签到返利异常 userId:{} ", userId, e);
            return Response.<Boolean>builder()
                    .code(e.getCode())
                    .info(e.getInfo())
                    .build();
        } catch (Exception e) {
            log.error("日历签到返利失败 userId:{}", userId);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(false)
                    .build();
        }

    }

    @PostMapping("/is_calendar_sign_rebate")
    @Override
    public Response<Boolean> isCalenderSignRebate(String userId) {

        try {
            log.info("查询用户是否完成签到 用户Id: {}", userId);
            String outBusinessNo = dateFormat.format(new Date());
            Boolean isSign = rebateService.isCalenderSignRebate(userId, outBusinessNo);
            return Response.success(isSign);
        } catch (Exception e) {
            log.error("查询用户是否完成日历签到返利失败 userId:{}", userId, e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(false)
                    .build();
        }

    }

    @PostMapping("/query_user_activity_account")
    @Override
    public Response<UserActivityAccountResponseDTO> queryUserActivityAccount(@RequestBody UserActivityAccountRequestDTO request) {

        try {
            log.info("查询用户活动的参与次数 userId:{} activityId:{}", request.getUserId(), request.getActivityId());
            if (StringUtils.isAnyBlank(request.getUserId(), request.getActivityId())) {
                return Response.error();
            }
            ActivityAccountCountEntity activityAccountCount = raffleActivityAccountQuotaService.getActivityAccountCount(request.getUserId(), Long.valueOf(request.getActivityId()));
            UserActivityAccountResponseDTO userActivityAccountResponseDTO = UserActivityAccountResponseDTO.builder()
                    .totalCount(activityAccountCount.getTotalCount())
                    .totalCountSurplus(activityAccountCount.getTotalCountSurplus())
                    .dayCount(activityAccountCount.getDayCount())
                    .dayCountSurplus(activityAccountCount.getDayCountSurplus())
                    .monthCount(activityAccountCount.getMonthCount())
                    .monthCountSurplus(activityAccountCount.getMonthCountSurplus())
                    .build();
            log.info("查询用户活动账户完成 userId:{} activityId:{} dto:{}", request.getUserId(), request.getActivityId(), new Gson().toJson(userActivityAccountResponseDTO));
            return Response.success(userActivityAccountResponseDTO);
        } catch (Exception e) {
            log.error("查询用户活动账户失败 userId:{} activityId:{}", request.getUserId(), request.getActivityId(), e);
            return Response.<UserActivityAccountResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

}
