package top.javarem.infrastructure.event;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.javarem.types.event.BaseEvent;

/**
 * @Author: rem
 * @Date: 2024/12/04/15:12
 * @Description:
 */
@Component
@Slf4j
public class EventPublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void publish(String topic, BaseEvent.EventMessage<?> message) {
        try {
//            BaseEvent.EventMessage<?>为静态类 但是序列化是对象 所以必须转json
            String messageJson = new Gson().toJson(message, BaseEvent.EventMessage.class);
            rabbitTemplate.convertAndSend(topic, messageJson);
            log.info("发送MQ消息 topic: {} message: {}", topic, message);
        } catch (Exception e) {
            log.error("发送MQ消息失败 topic: {} message: {}", topic, message);
            throw e;
        }
    }

}
