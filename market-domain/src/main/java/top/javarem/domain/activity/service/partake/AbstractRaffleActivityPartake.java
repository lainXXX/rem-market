package top.javarem.domain.activity.service.partake;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import top.javarem.domain.activity.model.aggregate.CreatePartakeOrderAggregate;
import top.javarem.domain.activity.model.entity.ActivityEntity;
import top.javarem.domain.activity.model.entity.ActivityPartakeEntity;
import top.javarem.domain.activity.model.entity.UserRaffleConsumeOrderEntity;
import top.javarem.domain.activity.model.vo.ActivityStatusVO;
import top.javarem.domain.activity.repository.IActivityRepository;
import top.javarem.domain.activity.service.IRaffleActivityPartakeService;
import top.javarem.types.enums.ResponseCode;
import top.javarem.types.exception.AppException;

import java.util.Date;

/**
 * @Author: rem
 * @Date: 2024/12/05/15:11
 * @Description:抽奖活动参与抽象类 定义参与的流程
 */
@Slf4j
public abstract class AbstractRaffleActivityPartake implements IRaffleActivityPartakeService {

    @Autowired
    private IActivityRepository activityRepository;

    @Override
    public UserRaffleConsumeOrderEntity createOrder(ActivityPartakeEntity activityPartakeEntity) {
        String userId = activityPartakeEntity.getUserId();
        Long activityId = activityPartakeEntity.getActivityId();
        Date currentDate = new Date();
        if (StringUtils.isBlank(userId) || activityId == null) {
            throw new NullPointerException("活动参与消息实体类信息错误");
        }
        ActivityEntity activityEntity = activityRepository.getActivityById(activityPartakeEntity.getActivityId());
//        1.判断活动是否开启
        if (!activityEntity.getStatus().equals(ActivityStatusVO.OPEN.getCode())) {
            throw new AppException(ResponseCode.ERROR_ACTIVITY_STATUS.getInfo(), ResponseCode.ERROR_ACTIVITY_STATUS.getCode());
        }
//        2.查询当前时间是否在活动时间内

        if (!currentDate.after(activityEntity.getBeginTime()) && currentDate.before(activityEntity.getEndTime())) {

        }
//        3.查询是否存在未消费的抽奖订单
        UserRaffleConsumeOrderEntity userUnconsumedOrder = activityRepository.getUserUnconsumedOrder(userId, activityId);
        if (userUnconsumedOrder != null) {
            log.info("创建参与活动订单 userId:{} activityId: {} UserConsumeOrderEntity{}", userId, activityId, userUnconsumedOrder);
            return userUnconsumedOrder;
        }

//        4.过滤额度【额度是否足够】 并返回聚合对象
        CreatePartakeOrderAggregate createPartakeOrderAggregate = this.doFilterAccount(userId, activityId, currentDate);

//        5.构建用户参与活动消费单
        UserRaffleConsumeOrderEntity userRaffleConsumeOrderEntity = buildUserConsumeOrder(userId, activityId, currentDate);

//        6.用户参与活动消费单放入聚合
        createPartakeOrderAggregate.setUserRaffleConsumeOrderEntity(userRaffleConsumeOrderEntity);

//        7.存入事务
        activityRepository.saveCreatePartakeOrderAggregate(createPartakeOrderAggregate);

        return userRaffleConsumeOrderEntity;
    }


    protected abstract UserRaffleConsumeOrderEntity buildUserConsumeOrder(String userId, Long activityId, Date currentDate);

    protected abstract CreatePartakeOrderAggregate doFilterAccount(String userId, Long activityId, Date currentDate);

}
