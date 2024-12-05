package top.javarem.domain.activity.service.armory;

import java.util.Date;

/**
 * @Author: rem
 * @Date: 2024/12/03/18:45
 * @Description:活动调度接口
 */
public interface IActivityDispatch {

    /**
     * 扣减活动库存
     * @param sku
     * @return
     */
    Boolean deductActivityStock(Long sku, Date endTime);

}
