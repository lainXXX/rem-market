package top.javarem.trigger.http;

import com.google.gson.Gson;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.javarem.api.IRaffleActivityService;
import top.javarem.api.dto.*;
import top.javarem.api.response.Response;
import top.javarem.domain.activity.model.entity.*;
import top.javarem.domain.activity.model.vo.OrderTradeTypeVO;
import top.javarem.domain.activity.service.IRaffleActivityAccountQuotaService;
import top.javarem.domain.activity.service.IRaffleActivityPartakeService;
import top.javarem.domain.activity.service.IRaffleActivitySkuProductService;
import top.javarem.domain.activity.service.armory.IActivityArmory;
import top.javarem.domain.award.model.entity.UserAwardRecordEntity;
import top.javarem.domain.award.model.vo.AwardStateVO;
import top.javarem.domain.award.service.IAwardService;
import top.javarem.domain.credit.model.entity.CreditAccountEntity;
import top.javarem.domain.credit.model.entity.TradeEntity;
import top.javarem.domain.credit.model.vo.TradeNameVO;
import top.javarem.domain.credit.model.vo.TradeTypeVO;
import top.javarem.domain.credit.service.ICreditService;
import top.javarem.domain.rebate.model.entity.BehaviorEntity;
import top.javarem.domain.rebate.model.vo.BehaviorTypeVO;
import top.javarem.domain.rebate.service.IBehaviorRebateService;
import top.javarem.domain.strategy.model.entity.RaffleAwardEntity;
import top.javarem.domain.strategy.model.entity.RaffleFactorEntity;
import top.javarem.domain.strategy.service.IRaffleStrategy;
import top.javarem.domain.strategy.service.armory.IStrategyArmory;
import top.javarem.types.annotations.DCCValue;
import top.javarem.types.annotations.RateLimiterAccessInterceptor;
import top.javarem.types.common.constants.Constants;
import top.javarem.types.enums.ResponseCode;
import top.javarem.types.exception.AppException;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
@DubboService(version = "1.0")
public class RaffleActivityController implements IRaffleActivityService {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    private final IRaffleActivityPartakeService partakeService;
    private final IActivityArmory activityArmory;
    private final IStrategyArmory strategyArmory;
    private final IRaffleStrategy strategy;
    private final IAwardService awardService;
    private final IBehaviorRebateService rebateService;
    private final IRaffleActivityAccountQuotaService raffleActivityAccountQuotaService;
    private final ICreditService creditService;
    private final IRaffleActivitySkuProductService raffleActivitySkuProductService;
    private final ICreditService creditAdjustService;

    // dcc 统一配置中心动态配置降级开关
    @DCCValue("degradeSwitch:open")
    private String degradeSwitch;

    public RaffleActivityController(IRaffleActivityPartakeService partakeService, IActivityArmory activityArmory, IStrategyArmory strategyArmory, IRaffleStrategy strategy, IAwardService awardService, IBehaviorRebateService rebateService, IRaffleActivityAccountQuotaService raffleActivityAccountQuotaService, ICreditService creditService, IRaffleActivitySkuProductService raffleActivitySkuProductService, ICreditService creditAdjustService) {
        this.partakeService = partakeService;
        this.activityArmory = activityArmory;
        this.strategyArmory = strategyArmory;
        this.strategy = strategy;
        this.awardService = awardService;
        this.rebateService = rebateService;
        this.raffleActivityAccountQuotaService = raffleActivityAccountQuotaService;
        this.creditService = creditService;
        this.raffleActivitySkuProductService = raffleActivitySkuProductService;
        this.creditAdjustService = creditAdjustService;
    }

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

    /**
     *
     * @param request
     * @return
     */
    @RateLimiterAccessInterceptor(key = "userId", fallbackMethod = "drawRateLimiterError", permitsPerSecond = 1L, blacklistCount = 1L)
    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "150")
    }, fallbackMethod = "drawHystrixError"
    )
    @PostMapping("/draw")
    @Override
    public Response<ActivityDrawResponseDTO> draw(@RequestBody ActivityDrawRequestDTO request) {

        try {
            log.info("抽奖活动- 用户id:{} 活动id:{}", request.getUserId(), request.getActivityId());
//            degradeSwitch【降级开关 open-开启、 close-关闭】
            if (StringUtils.isBlank(degradeSwitch) || !"open".equals(degradeSwitch)) {
                log.info("活动已降级 抽奖失败");
                return Response.<ActivityDrawResponseDTO>builder()
                        .code(ResponseCode.DEGRADE_SWITCH.getCode())
                        .info(ResponseCode.DEGRADE_SWITCH.getInfo())
                        .build();
            }

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
                    .awardConfig(raffleAwardEntity.getAwardConfig())
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

    public Response<ActivityDrawResponseDTO> drawRateLimiterError(@RequestBody ActivityDrawRequestDTO request) {
        log.info("活动抽奖限流 userId:{} activityId:{}", request.getUserId(), request.getActivityId());
        return Response.<ActivityDrawResponseDTO>builder()
                .code(ResponseCode.RATE_LIMITER.getCode())
                .info(ResponseCode.RATE_LIMITER.getInfo())
                .build();
    }

    public Response<ActivityDrawResponseDTO> drawHystrixError(@RequestBody ActivityDrawRequestDTO request) {
        log.info("活动抽奖熔断 userId:{} activityId:{}", request.getUserId(), request.getActivityId());
        return Response.<ActivityDrawResponseDTO>builder()
                .code(ResponseCode.HYSTRIX.getCode())
                .info(ResponseCode.HYSTRIX.getInfo())
                .build();
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

    @PostMapping("/credit_exchange")
    @Override
    public Response<Boolean> creditExchangeProduct(@RequestBody SkuProductShopCartRequestDTO request) {
        try {
            log.info("积分兑换商品 检验参数开始 userId:{} sku:{}", request.getUserId(), request.getSku());
            if (StringUtils.isBlank(request.getUserId()) || request.getSku() == null) {
                throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
            }
            UnpaidActivityOrderEntity unpaidActivityOrderEntity = raffleActivityAccountQuotaService.createOrder(SkuRechargeEntity.builder()
                    .userId(request.getUserId())
                    .sku(request.getSku())
                    .outBusinessNo(RandomStringUtils.randomNumeric(12))
                    .orderTradeTypeVO(OrderTradeTypeVO.credit_pay_trade)
                    .build());
            String orderId = creditService.createOrder(TradeEntity.builder()
                    .userId(unpaidActivityOrderEntity.getUserId())
                    .tradeName(TradeNameVO.CONVERT_SKU)
                    .tradeType(TradeTypeVO.RESERVE)
                    .amount(unpaidActivityOrderEntity.getPayAmount())
                    .outBusinessNo(unpaidActivityOrderEntity.getOutBusinessNo())
                    .build());
            log.info("积分兑换商品，支付订单完成  userId:{} sku:{} orderId:{}", request.getUserId(), request.getSku(), orderId);

            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(true)
                    .build();

        } catch (Exception e) {
            log.error("积分兑换商品失败 userId:{} sku:{} {}", request.getUserId(), request.getSku(), e);
            return Response.error(false);
        }
    }

    @PostMapping("/query_user_credit")
    @Override
    public Response<BigDecimal> queryUserCredit(String userId) {
        try {
            log.info("查询用户积分值开始 userId:{}", userId);
            CreditAccountEntity creditAccountEntity = creditAdjustService.queryUserCredit(userId);
            log.info("查询用户积分值完成 userId:{} adjustAmount:{}", userId, creditAccountEntity.getAdjustAmount());
            return Response.<BigDecimal>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(creditAccountEntity.getAdjustAmount())
                    .build();
        } catch (Exception e) {

            log.error("查询用户积分值失败 userId:{}", userId, e);
            return Response.<BigDecimal>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    @PostMapping("/query_sku_product_list_by_activity_id")
    @Override
    public Response<List<SkuProductResponseDTO>> getSkuProductListByActivityId(Long activityId) {

        try {
            log.info("通过活动ID获取sku商品集合 参数检验 activityId:{}", activityId);
            if (activityId == null) {
                throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
            }
            List<SkuProductEntity> skuProductEntityList = raffleActivitySkuProductService.getSkuProductListByActivityId(activityId);
            log.info("通过活动ID获取sku商品集合成功 activityId:{} skuProductList:{}", activityId, skuProductEntityList);
//            转化对象实体
            List<SkuProductResponseDTO> skuProductResponseDTOList = new ArrayList<>(skuProductEntityList.size());
            for (SkuProductEntity skuProductEntity : skuProductEntityList) {
                SkuProductResponseDTO.ActivityCount activityCount = new SkuProductResponseDTO.ActivityCount();
                activityCount.setTotalCount(skuProductEntity.getActivityCount().getTotalCount());
                activityCount.setMonthCount(skuProductEntity.getActivityCount().getMonthCount());
                activityCount.setDayCount(skuProductEntity.getActivityCount().getDayCount());

                SkuProductResponseDTO skuProductResponseDTO = new SkuProductResponseDTO();
                skuProductResponseDTO.setSku(skuProductEntity.getSku());
                skuProductResponseDTO.setActivityId(skuProductEntity.getActivityId());
                skuProductResponseDTO.setActivityCountId(skuProductEntity.getActivityCountId());
                skuProductResponseDTO.setStockCount(skuProductEntity.getStockCount());
                skuProductResponseDTO.setStockCountSurplus(skuProductEntity.getStockCountSurplus());
                skuProductResponseDTO.setProductAmount(skuProductEntity.getProductAmount());
                skuProductResponseDTO.setActivityCount(activityCount);
                skuProductResponseDTOList.add(skuProductResponseDTO);
            }
            return Response.success(skuProductResponseDTOList);
        } catch (Exception e) {
            log.error("通过活动ID获取sku商品集合失败 activityId:{}", activityId);
            return Response.error();
        }

    }
}
