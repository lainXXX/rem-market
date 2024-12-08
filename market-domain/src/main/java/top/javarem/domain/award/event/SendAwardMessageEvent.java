package top.javarem.domain.award.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.javarem.types.event.BaseEvent;

import java.util.Date;

/**
 * @Author: rem
 * @Date: 2024/12/08/19:59
 * @Description:奖品记录时间
 */
@Component
public class SendAwardMessageEvent extends BaseEvent<SendAwardMessageEvent.SendAwardMessage> {

    @Value("${spring.rabbitmq.topic.send_award}")
    private String topic;

    @Override
    public EventMessage<SendAwardMessage> buildEventMessage(SendAwardMessage data) {

        return EventMessage.<SendAwardMessage>builder()
                .eventId(RandomStringUtils.randomAlphanumeric(11))
                .data(data)
                .timestamp(new Date())
                .build();

    }

    @Override
    public String topic() {
        return topic;
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SendAwardMessage {

        private Integer awardId;

        private String userId;

        private String awardTitle;

    }

}
