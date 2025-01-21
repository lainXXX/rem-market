package top.javarem.infrastructure.dao.iService.Impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.javarem.infrastructure.dao.entity.RaffleActivitySku;
import top.javarem.infrastructure.dao.iService.RaffleActivitySkuService;
import top.javarem.infrastructure.dao.mapper.RaffleActivitySkuMapper;

import java.util.List;

/**
 * @author aaa
 * @description 针对表【raffle_activity_sku】的数据库操作Service实现
 * @createDate 2024-12-01 22:58:43
 */
@Service
public class RaffleActivitySkuServiceImpl extends ServiceImpl<RaffleActivitySkuMapper, RaffleActivitySku>
        implements RaffleActivitySkuService {

    @Override
    public List<RaffleActivitySku> getSkuProductListByActivityId(Long activityId) {

        return this.lambdaQuery()
                .select(RaffleActivitySku::getSku, RaffleActivitySku::getActivityId, RaffleActivitySku::getActivityCountId, RaffleActivitySku::getStockCount, RaffleActivitySku::getStockCountSurplus, RaffleActivitySku::getProductAmount)
                .eq(RaffleActivitySku::getActivityId, activityId).list();

    }
}
