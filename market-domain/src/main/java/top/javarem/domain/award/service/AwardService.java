package top.javarem.domain.award.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.javarem.domain.award.event.SendAwardMessageEvent;
import top.javarem.domain.award.model.aggregate.UserAwardRecodeAggregate;
import top.javarem.domain.award.model.entity.TaskEntity;
import top.javarem.domain.award.model.entity.UserAwardRecordEntity;
import top.javarem.domain.award.model.vo.TaskStateVO;
import top.javarem.domain.award.repository.IAwardRepository;
import top.javarem.types.event.BaseEvent;

/**
 * @Author: rem
 * @Date: 2024/12/08/18:16
 * @Description:
 */
@Service
public class AwardService implements IAwardService{

    @Autowired
    private IAwardRepository awardRepository;

    @Autowired
    private SendAwardMessageEvent sendAwardMessageEvent;


    @Override
    public void saveUserAwardRecord(UserAwardRecordEntity userAwardRecordEntity) {

        SendAwardMessageEvent.SendAwardMessage sendAwardMessage = SendAwardMessageEvent.SendAwardMessage.builder()
                .userId(userAwardRecordEntity.getUserId())
                .awardId(userAwardRecordEntity.getAwardId())
                .awardTitle(userAwardRecordEntity.getAwardTitle())
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
}
