package top.javarem.trigger.listener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.javarem.domain.activity.model.entity.SkuRechargeEntity;
import top.javarem.domain.activity.service.IRaffleActivityAccountQuotaService;
import top.javarem.domain.rebate.event.SendRebateMessageEvent;
import top.javarem.domain.rebate.event.SendRebateMessageEvent.RebateMessage;
import top.javarem.domain.rebate.model.vo.RebateTypeVO;
import top.javarem.types.enums.ResponseCode;
import top.javarem.types.event.BaseEvent;
import top.javarem.types.event.BaseEvent.EventMessage;
import top.javarem.types.exception.AppException;

/**
 * @Author: rem
 * @Date: 2025/01/13/13:46
 * @Description:
 */
@Component
@Slf4j
public class RebateMessageCustomer {

    @Value("${spring.rabbitmq.topic.send_rebate}")
    private String topic;

    @Autowired
    private IRaffleActivityAccountQuotaService raffleActivityAccountQuotaService;

    @RabbitListener(queuesToDeclare = @Queue(value = "${spring.rabbitmq.topic.send_rebate}"))
    public void listener(String message) {
        try {
            log.info("监听用户返利消息 topic:{} message:{}", topic, message);
            EventMessage<RebateMessage> eventMessage = new Gson().fromJson(message, new TypeToken<EventMessage<RebateMessage>>() {
            }.getType());
            RebateMessage rebateMessage = eventMessage.getData();
//        TODO 非sku奖励暂时不处理
            if (!rebateMessage.getRebateType().equals(RebateTypeVO.SKU.getCode())) {
                log.info("监听用户行为返利消息 - 非sku奖励暂时不处理 topic: {} message: {}", topic, message);
                return;
            }
            SkuRechargeEntity skuRechargeEntity = SkuRechargeEntity.builder()
                    .userId(rebateMessage.getUserId())
                    .sku(Long.valueOf(rebateMessage.getRebateConfig()))
                    .outBusinessNo(rebateMessage.getBizId())
                    .build();
            raffleActivityAccountQuotaService.createOrder(skuRechargeEntity);
        } catch (AppException e) {
            if (ResponseCode.INDEX_DUP.getCode().equals(e.getCode())) {
                log.warn("监听用户行为返利消息，消费重复 topic: {} message: {}", topic, message, e);
                return;
            }
            throw e;
        } catch (Exception e) {
            log.error("监听用户行为返利消息，消费失败 topic: {} message: {}", topic, message, e);
            throw e;
        }
    }

}
