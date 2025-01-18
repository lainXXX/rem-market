package top.javarem.domain.credit.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.javarem.domain.award.model.vo.TaskStateVO;
import top.javarem.domain.credit.event.CreditAdjustSuccessMessageEvent;
import top.javarem.types.event.BaseEvent;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskEntity {

    private String userId;

    /**
     * 消息主题
     */
    private String topic;

    private String messageId;

    /**
     * 消息内容
     */
    private BaseEvent.EventMessage<CreditAdjustSuccessMessageEvent.CreditAdjustSuccessMessage> message;

    /**
     * 状态【create、complete、fail】
     */
    private TaskStateVO status;

}