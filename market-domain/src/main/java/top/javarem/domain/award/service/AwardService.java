package top.javarem.domain.award.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.javarem.domain.award.event.SendAwardMessageEvent;
import top.javarem.domain.award.model.aggregate.UserAwardRecodeAggregate;
import top.javarem.domain.award.model.entity.DistributeAwardEntity;
import top.javarem.domain.award.model.entity.TaskEntity;
import top.javarem.domain.award.model.entity.UserAwardRecordEntity;
import top.javarem.domain.award.model.vo.TaskStateVO;
import top.javarem.domain.award.repository.IAwardRepository;
import top.javarem.domain.award.service.distribute.IDistributeAward;
import top.javarem.types.event.BaseEvent;

import java.util.Map;

/**
 * @Author: rem
 * @Date: 2024/12/08/18:16
 * @Description:
 */
@Slf4j
@Service
public class AwardService implements IAwardService{

    private final String OTHERS_BEAN_NAME = "others";

    private final IAwardRepository awardRepository;

    private final SendAwardMessageEvent sendAwardMessageEvent;

    private final Map<String, IDistributeAward> distributeAwardMap;

    public AwardService(IAwardRepository awardRepository, SendAwardMessageEvent sendAwardMessageEvent, Map<String, IDistributeAward> distributeAwardMap) {
        this.awardRepository = awardRepository;
        this.sendAwardMessageEvent = sendAwardMessageEvent;
        this.distributeAwardMap = distributeAwardMap;
    }


    @Override
    public void saveUserAwardRecord(UserAwardRecordEntity userAwardRecordEntity) {

        SendAwardMessageEvent.SendAwardMessage sendAwardMessage = SendAwardMessageEvent.SendAwardMessage.builder()
                .userId(userAwardRecordEntity.getUserId())
                .awardId(userAwardRecordEntity.getAwardId())
                .orderId(userAwardRecordEntity.getOrderId())
                .awardTitle(userAwardRecordEntity.getAwardTitle())
                .awardConfig(userAwardRecordEntity.getAwardConfig())
                .build();

        BaseEvent.EventMessage<SendAwardMessageEvent.SendAwardMessage> sendAwardEventMessage = sendAwardMessageEvent.buildEventMessage(sendAwardMessage);

        TaskEntity taskEntity = TaskEntity.builder()
                .userId(userAwardRecordEntity.getUserId())
                .topic(sendAwardMessageEvent.topic())
                .messageId(sendAwardEventMessage.getEventId())
                .message(sendAwardEventMessage)
                .status(TaskStateVO.create)
                .build();

        UserAwardRecodeAggregate userAwardRecodeAggregate = UserAwardRecodeAggregate
                .builder()
                .userAwardRecordEntity(userAwardRecordEntity)
                .taskEntity(taskEntity)
                .build();
        awardRepository.saveAwardRecord(userAwardRecodeAggregate);

    }

    @Override
    public void distributeAward(DistributeAwardEntity distributeAwardEntity) {
        String awardKey = awardRepository.getAwardKey(distributeAwardEntity.getAwardId());
        if (null == awardKey) {
            log.error("分发奖品，奖品ID不存在。awardKey:{}", awardKey);
            return;
        }

        // 奖品服务
        IDistributeAward distributeAward = null;
        if (! OTHERS_BEAN_NAME.equals(awardKey)) {
            distributeAward = distributeAwardMap.get(OTHERS_BEAN_NAME);
        } else {
            distributeAward = distributeAwardMap.get(awardKey);
        }

        if (null == distributeAward) {
            log.error("分发奖品，对应的服务不存在。awardKey:{}", awardKey);
            throw new RuntimeException("分发奖品，奖品" + awardKey + "对应的服务不存在");
        }

        // 发放奖品
        distributeAward.giveOutPrizes(distributeAwardEntity);
    }
}
