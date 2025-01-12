package top.javarem.domain.rebate.model.entity;


import lombok.Data;
import top.javarem.domain.rebate.model.vo.BehaviorTypeVO;


/**
 * 用户返利产生的行为
 *
 * @TableName user_behavior_rebate_order
 */
@Data
public class BehaviorEntity {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 行为类型（sign 签到、pay 支付）
     */
    private BehaviorTypeVO behaviorType;

    /**
     * 业务ID-  拼接的唯一值
     */
    private String outBusinessNo;
}