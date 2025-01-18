package top.javarem.domain.task.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.javarem.domain.award.model.vo.TaskStateVO;

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
    private String message;

    /**
     * 状态【create、complete、fail】
     */
    private TaskStateVO status;

}
