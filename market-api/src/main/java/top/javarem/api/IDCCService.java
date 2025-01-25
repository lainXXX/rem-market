package top.javarem.api;

import top.javarem.api.response.Response;

/**
 * @Author: rem
 * @Date: 2025/01/24/19:02
 * @Description:
 */
public interface IDCCService {

    Response<Boolean> updateConfig(String key, String value);

}
