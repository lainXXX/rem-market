package top.javarem.domain.activity.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: rem
 * @Date: 2024/12/05/15:30
 * @Description:
 */
@Getter
@AllArgsConstructor
public enum UserRaffleStatus {

    CREATE("create", "创建"),
    USED("used", "已使用"),
    CANCEL("cancel", "已取消"),
    ;

    private String code;
    private String info;

}
