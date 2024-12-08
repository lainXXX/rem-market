package top.javarem.types.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author: rem
 * @Date: 2024/12/04/15:03
 * @Description:基础事件构建抽象类
 */
@Data
public abstract class BaseEvent<T> {

    /**
     * 构建事件消息
     * @param data
     * @return
     */
    public abstract EventMessage<T> buildEEventMessage(T data);

    /**
     * 消息的通道 key
     * @return
     */
    public abstract String topic();

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EventMessage<T> {
        private String eventId;
        private Date timestamp;
        private T data;
    }



}
