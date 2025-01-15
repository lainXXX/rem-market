package top.javarem.domain.rebate.service;

import io.jsonwebtoken.lang.Collections;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.javarem.domain.award.model.vo.TaskStateVO;
import top.javarem.domain.rebate.event.SendRebateMessageEvent;
import top.javarem.domain.rebate.model.aggregate.BehaviorRebateAggregate;
import top.javarem.domain.rebate.model.entity.BehaviorEntity;
import top.javarem.domain.rebate.model.entity.BehaviorRebateOrderEntity;
import top.javarem.domain.rebate.model.entity.TaskEntity;
import top.javarem.domain.rebate.model.vo.DailyBehaviorRebateVO;
import top.javarem.domain.rebate.repository.IBehaviorRebateRepository;
import top.javarem.types.common.constants.Constants;
import top.javarem.types.event.BaseEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: rem
 * @Date: 2025/01/12/15:58
 * @Description:
 */
@Service
public class BehaviorRebateService implements IBehaviorRebateService {

    @Autowired
    private IBehaviorRebateRepository rebateRepository;

    @Autowired
    private SendRebateMessageEvent sendRebateMessageEvent;


    @Override
    public List<String> createOrder(BehaviorEntity behavior) {
//        1.查询返利配置
        List<DailyBehaviorRebateVO> dailyBehaviorRebateVOS =  rebateRepository.queryDailyBehaviorRebateConfig(behavior.getBehaviorType().getCode());
        if (Collections.isEmpty(dailyBehaviorRebateVOS)) {
            return new ArrayList<>();
        }
//        2.构建聚合对象
        List<String> orderIds = new ArrayList<>();
        List<BehaviorRebateAggregate> behaviorRebateAggregates = new ArrayList<>();
        for (DailyBehaviorRebateVO dailyBehaviorRebateVO : dailyBehaviorRebateVOS) {
//            拼装业务ID 用户ID_返利类型_外部透传业务ID
            String bizId = behavior.getUserId() + Constants.UNDERLINE + dailyBehaviorRebateVO.getRebateType() + Constants.UNDERLINE + behavior.getOutBusinessNo();
            BehaviorRebateOrderEntity behaviorRebateOrderEntity = BehaviorRebateOrderEntity.builder()
                    .userId(behavior.getUserId())
                    .orderId(RandomStringUtils.randomNumeric(12))
                    .behaviorType(dailyBehaviorRebateVO.getBehaviorType())
                    .rebateDesc(dailyBehaviorRebateVO.getRebateDesc())
                    .rebateType(dailyBehaviorRebateVO.getRebateType())
                    .rebateConfig(dailyBehaviorRebateVO.getRebateConfig())
                    .outBusinessNo(behavior.getOutBusinessNo())
                    .bizId(bizId)
                    .build();
            orderIds.add(bizId);

//            构建MQ消息
            SendRebateMessageEvent.RebateMessage rebateMessage = SendRebateMessageEvent.RebateMessage.builder()
                    .userId(behavior.getUserId())
                    .rebateType(dailyBehaviorRebateVO.getRebateType())
                    .rebateDesc(dailyBehaviorRebateVO.getRebateDesc())
                    .rebateConfig(dailyBehaviorRebateVO.getRebateConfig())
                    .bizId(bizId)
                    .build();

//            构建事件消息
            BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage> rebateMessageEventMessage = sendRebateMessageEvent.buildEventMessage(rebateMessage);
            TaskEntity taskEntity = new TaskEntity();
            taskEntity.setUserId(behavior.getUserId());
            taskEntity.setTopic(sendRebateMessageEvent.topic());
            taskEntity.setMessage(rebateMessageEventMessage);
            taskEntity.setMessageId(rebateMessageEventMessage.getEventId());
            taskEntity.setStatus(TaskStateVO.create);
            BehaviorRebateAggregate behaviorRebateAggregate = BehaviorRebateAggregate.builder()
                    .userId(behavior.getUserId())
                    .behaviorRebateOrderEntity(behaviorRebateOrderEntity)
                    .taskEntity(taskEntity)
                    .build();
            behaviorRebateAggregates.add(behaviorRebateAggregate);
        }

        rebateRepository.saveUserRebateRecord(behavior.getUserId(), behaviorRebateAggregates);
        return orderIds;
    }

    @Override
    public Boolean isCalenderSignRebate(String userId, String outBusinessNo) {

        return rebateRepository.isCalenderSignRebate(userId, outBusinessNo);
    }

}
