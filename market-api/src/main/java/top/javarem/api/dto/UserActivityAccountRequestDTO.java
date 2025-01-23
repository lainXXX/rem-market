package top.javarem.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: rem
 * @Date: 2025/01/14/18:34
 * @Description:用户查询账户额度请求对象
 */
@Data
public class UserActivityAccountRequestDTO implements Serializable {

    private String userId;

    private String activityId;

}
