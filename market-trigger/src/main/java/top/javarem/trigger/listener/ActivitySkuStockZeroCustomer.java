package top.javarem.trigger.listener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.javarem.domain.activity.service.IRaffleActivitySkuStockService;
import top.javarem.types.event.BaseEvent;

/**
 * @Author: rem
 * @Date: 2024/12/03/14:37
 * @Description:活动库存sku耗尽
 */
@Component
@Slf4j
public class ActivitySkuStockZeroCustomer {

    @Value("${spring.rabbitmq.topic.activity_sku_stock_zero}")
    private String topic;

    @Autowired
    private IRaffleActivitySkuStockService skuStock;

    @RabbitListener(queuesToDeclare = @Queue(value = "activity_sku_stock_zero"))
    public void listener(String message) {
        try {
            log.info("活动sku库存为0消息消费 topic: {} message:{}", topic, message);
            BaseEvent.EventMessage<Long> eventMessage = new Gson().fromJson(message, new TypeToken<BaseEvent.EventMessage<Long>>() {}.getType());
            Long sku = (Long) eventMessage.getData();
//        清空数据库中的sku库存 设置为0
            skuStock.clearActivitySkuStock(sku);
//        清空扣减库存阻塞队列的全部元素
            skuStock.clearSkuStockDecrQueue();
        } catch (Exception e) {
            log.error("监听活动sku库存消耗为0消息，消费失败 topic: {} message: {}", topic, message);
            throw e;
        }
    }

}
