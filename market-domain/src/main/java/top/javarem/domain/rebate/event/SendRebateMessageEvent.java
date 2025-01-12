package top.javarem.domain.rebate.event;

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
 * @Date: 2025/01/12/16:27
 * @Description:发送返利的消息事件
 */
@Component
public class SendRebateMessageEvent extends BaseEvent<SendRebateMessageEvent.RebateMessage> {

    @Value("${spring.rabbitmq.topic.send_rebate}")
    private String topic;

    @Override
    public EventMessage<RebateMessage> buildEventMessage(RebateMessage data) {
        return EventMessage.<SendRebateMessageEvent.RebateMessage>builder()
                .eventId(RandomStringUtils.randomNumeric(11))
                .timestamp(new Date())
                .data(data)
                .build();
    }

    @Override
    public String topic() {
        return topic;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RebateMessage {
        /**
         * 用户Id
         */
        private String userId;
        /**
         * 返利描述
         */
        private String rebateDesc;
        /**
         * 返利类型
         */
        private String rebateType;
        /**
         * 返利配置
         */
        private String rebateConfig;
        /**
         * 业务Id 唯一Id 确保幂等性
         */
        private String bizId;

    }

}
