package top.javarem.api.dto;

import lombok.Data;

/**
 * @Author: rem
 * @Date: 2025/01/20/21:56
 * @Description:商品购物车请求对象
 */
@Data
public class SkuProductShopCartRequestDTO {

    private String userId;

    /**
     *sku商品
     */
    private Long sku;

}
