package top.javarem.domain.award.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.javarem.domain.award.event.SendAwardMessageEvent;
import top.javarem.domain.award.model.vo.TaskStateVO;
import top.javarem.types.event.BaseEvent;

/**
 * @Author: rem
 * @Date: 2024/12/08/19:56
 * @Description:
 */
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
    private BaseEvent.EventMessage<SendAwardMessageEvent.SendAwardMessage> message;

    /**
     * 状态【create、complete、fail】
     */
    private TaskStateVO status;

}
