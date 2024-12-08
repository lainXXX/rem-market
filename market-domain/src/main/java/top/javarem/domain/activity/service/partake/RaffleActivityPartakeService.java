package top.javarem.domain.activity.service.partake;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.javarem.domain.activity.model.aggregate.CreatePartakeOrderAggregate;
import top.javarem.domain.activity.model.entity.ActivityAccountCountEntity;
import top.javarem.domain.activity.model.entity.ActivityAccountDayCountEntity;
import top.javarem.domain.activity.model.entity.ActivityAccountMonthCountEntity;
import top.javarem.domain.activity.model.entity.UserConsumeOrderEntity;
import top.javarem.domain.activity.model.vo.UserRaffleStatus;
import top.javarem.domain.activity.repository.IActivityRepository;
import top.javarem.types.enums.ResponseCode;
import top.javarem.types.exception.AppException;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: rem
 * @Date: 2024/12/05/15:12
 * @Description:抽奖活动参与功能具体实现服务
 */
@Service
public class RaffleActivityPartakeService extends AbstractRaffleActivityPartake {

    private final SimpleDateFormat dateFormatDay = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat dateFormatMonth = new SimpleDateFormat("yyyy-MM");

    @Autowired
    private IActivityRepository activityRepository;

    @Override
    protected UserConsumeOrderEntity buildUserConsumeOrder(String userId, Long activityId, Long strategyId, String activityName) {
        return UserConsumeOrderEntity.builder()
                .userId(userId)
                .activityId(activityId)
                .strategyId(strategyId)
                .activityName(activityName)
                .orderId(RandomStringUtils.randomNumeric(12))
                .orderTime(new Date())
                .status(UserRaffleStatus.CREATE)
                .build();
    }

    @Override
    protected CreatePartakeOrderAggregate doFilterAccount(String userId, Long activityId, Date currentDate) {

//        1.过滤总账户
        ActivityAccountCountEntity activityAccountCountEntity = activityRepository.getActivityAccountCount(userId, activityId);
        if (activityAccountCountEntity == null || activityAccountCountEntity.getTotalCountSurplus() <= 0) {
            throw new AppException(ResponseCode.ERROR_ACCOUNT_TOTAL_QUOTA.getInfo(), ResponseCode.ERROR_ACCOUNT_TOTAL_QUOTA.getCode());
        }
        String month = dateFormatMonth.format(currentDate);
        String day = dateFormatDay.format(currentDate);

        ActivityAccountMonthCountEntity activityAccountMonthCountEntity = activityRepository.getActivityAccountMonthCount(userId, activityId, month);
        if (activityAccountMonthCountEntity != null && activityAccountCountEntity.getTotalCountSurplus() <= 0) {
            throw new AppException(ResponseCode.ERROR_ACCOUNT_MONTH_QUOTA.getInfo(), ResponseCode.ERROR_ACCOUNT_MONTH_QUOTA.getCode());
        }
//        如果不存在后续在save中插入一条月次数记录
        boolean isExistMonthCount = activityAccountMonthCountEntity != null;
        if (!isExistMonthCount) {
            activityAccountMonthCountEntity = ActivityAccountMonthCountEntity.builder()
                    .userId(userId)
                    .activityId(activityId)
                    .month(month)
                    .monthCount(activityAccountCountEntity.getMonthCount())
                    .monthCountSurplus(activityAccountCountEntity.getMonthCountSurplus())
                    .build();
        }

        ActivityAccountDayCountEntity activityAccountDayCountEntity = activityRepository.getActivityAccountDayCount(userId, activityId, day);
        if (activityAccountDayCountEntity != null && activityAccountCountEntity.getTotalCountSurplus() <= 0) {
            throw new AppException(ResponseCode.ERROR_ACCOUNT_DAY_QUOTA.getInfo(), ResponseCode.ERROR_ACCOUNT_DAY_QUOTA.getCode());
        }
        boolean isExistDayCount = activityAccountDayCountEntity != null;
        if (!isExistDayCount) {
            activityAccountDayCountEntity = ActivityAccountDayCountEntity.builder()
                    .userId(userId)
                    .activityId(activityId)
                    .day(day)
                    .dayCount(activityAccountCountEntity.getDayCount())
                    .dayCountSurplus(activityAccountCountEntity.getDayCountSurplus())
                    .build();
        }

        return CreatePartakeOrderAggregate.builder()
                .userId(userId)
                .activityId(activityId)
                .activityAccountCountEntity(activityAccountCountEntity)
                .isExistDayCount(isExistDayCount)
                .activityAccountMonthCountEntity(activityAccountMonthCountEntity)
                .isExistMonthCount(isExistMonthCount)
                .activityAccountDayCountEntity(activityAccountDayCountEntity)
                .build();


    }
}
