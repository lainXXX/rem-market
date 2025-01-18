package top.javarem.trigger.listener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.javarem.domain.activity.model.entity.DeliveryOrderEntity;
import top.javarem.domain.activity.service.IRaffleActivityAccountQuotaService;
import top.javarem.domain.credit.event.CreditAdjustSuccessMessageEvent;
import top.javarem.types.enums.ResponseCode;
import top.javarem.types.event.BaseEvent;
import top.javarem.types.exception.AppException;

/**
 * @Author: rem
 * @Date: 2025/01/18/17:28
 * @Description:积分调节成功信息消费者
 */
@Slf4j
@Component
public class CreditAdjustSuccessCustomer {

    @Value("${spring.rabbitmq.topic.credit_adjust_success}")
    private String topic;

    private final IRaffleActivityAccountQuotaService raffleActivityAccountQuotaService;

    public CreditAdjustSuccessCustomer(IRaffleActivityAccountQuotaService raffleActivityAccountQuotaService) {
        this.raffleActivityAccountQuotaService = raffleActivityAccountQuotaService;
    }

    @RabbitListener(queuesToDeclare = @Queue(value = "${spring.rabbitmq.topic.credit_adjust_success}"))
    public void listener(String message) {

        try {
            log.info("积分调节成功信息消费开始 message:{}", message);
//        从Json解析出对象
            BaseEvent.EventMessage<CreditAdjustSuccessMessageEvent.CreditAdjustSuccessMessage> eventMessage = new Gson().fromJson(message, new TypeToken<BaseEvent.EventMessage<CreditAdjustSuccessMessageEvent.CreditAdjustSuccessMessage>>() {
            }.getType());
            CreditAdjustSuccessMessageEvent.CreditAdjustSuccessMessage creditAdjustSuccessMessage = eventMessage.getData();
//        构建对象并执行
            DeliveryOrderEntity deliveryOrderEntity = DeliveryOrderEntity.builder()
                    .userId(creditAdjustSuccessMessage.getUserId())
                    .outBusinessNo(creditAdjustSuccessMessage.getOutBusinessNo())
                    .build();
            raffleActivityAccountQuotaService.updateOrder(deliveryOrderEntity);
        } catch (AppException e) {
            if (ResponseCode.INDEX_DUP.getCode().equals(e.getCode())) {
                log.warn("监听积分账户调整成功消息，进行交易商品发货，消费重复 topic: {} message: {}", topic, message, e);
                return;
            }
            throw e;
        } catch (Exception e) {
            log.error("监听积分账户调整成功消息，进行交易商品发货失败 topic: {} message: {}", topic, message, e);
            throw e;
        }
    }

}
