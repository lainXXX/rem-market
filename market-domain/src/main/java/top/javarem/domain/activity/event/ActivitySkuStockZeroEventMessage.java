package top.javarem.domain.activity.event;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.javarem.types.event.BaseEvent;

import java.util.Date;

/**
 * @Author: rem
 * @Date: 2024/12/04/15:09
 * @Description:
 */
@Component
public class ActivitySkuStockZeroEventMessage extends BaseEvent<Long> {

    @Value("${spring.rabbitmq.topic.activity_sku_stock_zero}")
    private String topic;

    @Override
    public EventMessage<Long> buildEventMessage(Long sku) {
        return EventMessage.<Long>builder()
                .eventId(RandomStringUtils.randomNumeric(11))
                .data(sku)
                .timestamp(new Date())
                .build();
    }

    @Override
    public String topic() {
        return this.topic;
    }
}
