package top.javarem.domain.activity.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: rem
 * @Date: 2024/12/04/10:47
 * @Description:
 */
@Getter
@AllArgsConstructor
public enum ActivityStatusVO {

    CREATE("create", "创建"),
    OPEN("open", "开启"),
    CLOSE("close", "关闭"),
    ;
    private String code;
    private String desc;

    }
