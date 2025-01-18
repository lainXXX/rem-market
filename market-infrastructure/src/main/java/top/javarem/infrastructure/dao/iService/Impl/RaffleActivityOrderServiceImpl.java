package top.javarem.infrastructure.dao.iService.Impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.javarem.infrastructure.dao.entity.RaffleActivityOrder;
import top.javarem.infrastructure.dao.iService.RaffleActivityOrderService;
import top.javarem.infrastructure.dao.mapper.RaffleActivityOrderMapper;

import java.util.Date;


/**
 * @author aaa
 * @description 针对表【raffle_activity_order】的数据库操作Service实现
 * @createDate 2024-12-01 17:15:17
 */
@Service
public class RaffleActivityOrderServiceImpl extends ServiceImpl<RaffleActivityOrderMapper, RaffleActivityOrder>
        implements RaffleActivityOrderService {

    @Override
    public RaffleActivityOrder getOrder(String userId, String outBusinessNo) {

        return this.lambdaQuery()
                .select(RaffleActivityOrder::getUserId, RaffleActivityOrder::getSku, RaffleActivityOrder::getActivityId, RaffleActivityOrder::getActivityName, RaffleActivityOrder::getStrategyId, RaffleActivityOrder::getOrderId, RaffleActivityOrder::getOrderTime, RaffleActivityOrder::getTotalCount, RaffleActivityOrder::getMonthCount, RaffleActivityOrder::getDayCount, RaffleActivityOrder::getPayAmount, RaffleActivityOrder::getOutBusinessNo, RaffleActivityOrder::getStatus)
                .eq(RaffleActivityOrder::getUserId, userId)
                .eq(RaffleActivityOrder::getOutBusinessNo, outBusinessNo)
                .one();

    }

    @Override
    public int updateOrderStatus(RaffleActivityOrder raffleActivityOrderRes) {

        return this.baseMapper.update(null, Wrappers.<RaffleActivityOrder>lambdaUpdate()
                .set(RaffleActivityOrder::getStatus, "completed")
                .set(RaffleActivityOrder::getUpdateTime, new Date())
                .eq(RaffleActivityOrder::getUserId, raffleActivityOrderRes.getUserId())
                .eq(RaffleActivityOrder::getOutBusinessNo, raffleActivityOrderRes.getOutBusinessNo())
                .eq(RaffleActivityOrder::getStatus, "created"));

    }
}
