package top.javarem.infrastructure.dao.iService;


import com.baomidou.mybatisplus.extension.service.IService;
import top.javarem.domain.activity.model.entity.SkuRechargeEntity;
import top.javarem.domain.activity.model.entity.UnpaidActivityOrderEntity;
import top.javarem.infrastructure.dao.entity.RaffleActivityOrder;

/**
* @author aaa
* @description 针对表【raffle_activity_order】的数据库操作Service
* @createDate 2024-12-01 17:15:17
*/
public interface RaffleActivityOrderService extends IService<RaffleActivityOrder> {

    RaffleActivityOrder getOrder(String userId, String outBusinessNo);

    int updateOrderStatus(RaffleActivityOrder raffleActivityOrderRes);

    RaffleActivityOrder queryUnpaidActivityOrder(SkuRechargeEntity skuRechargeEntity);
}
