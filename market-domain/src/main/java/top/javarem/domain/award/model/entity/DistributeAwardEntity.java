package top.javarem.domain.award.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: rem
 * @Date: 2025/01/15/20:54
 * @Description:分发奖品实体对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistributeAwardEntity {

    private String userId;

    private String orderId;

    private Integer awardId;

    private String awardConfig;

}
