package top.javarem.infrastructure.adapter.repository;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;
import top.javarem.domain.award.model.vo.AccountStatusVO;
import top.javarem.domain.credit.model.aggregate.TradeAggregate;
import top.javarem.domain.credit.model.entity.CreditAccountEntity;
import top.javarem.domain.credit.model.entity.CreditOrderEntity;
import top.javarem.domain.credit.reposiotry.ICreditRepository;
import top.javarem.infrastructure.dao.entity.UserCreditAccount;
import top.javarem.infrastructure.dao.entity.UserCreditOrder;
import top.javarem.infrastructure.dao.iService.UserCreditAccountService;
import top.javarem.infrastructure.dao.iService.UserCreditOrderService;
import top.javarem.types.common.constants.Constants;

import java.util.concurrent.TimeUnit;

/**
 * @Author: rem
 * @Date: 2025/01/17/16:42
 * @Description: 积分服务仓储实现类
 */
@Slf4j
@Repository
public class CreditRepository implements ICreditRepository {

    private final RedissonClient redissonClient;
    private final UserCreditOrderService userCreditOrderService;
    private final UserCreditAccountService userCreditAccountService;
    private final TransactionTemplate transactionTemplate;

    public CreditRepository(RedissonClient redissonClient, UserCreditOrderService userCreditOrderService, UserCreditAccountService userCreditAccountService, TransactionTemplate transactionTemplate) {
        this.redissonClient = redissonClient;
        this.userCreditOrderService = userCreditOrderService;
        this.userCreditAccountService = userCreditAccountService;
        this.transactionTemplate = transactionTemplate;
    }


    @Override
    public void saveUserCreditTradeOrder(TradeAggregate tradeAggregate) {

        String userId = tradeAggregate.getUserId();
        CreditAccountEntity creditAccountEntity = tradeAggregate.getCreditAccountEntity();
        CreditOrderEntity creditOrderEntity = tradeAggregate.getCreditOrderEntity();

        // 积分账户
        UserCreditAccount userCreditAccountReq = new UserCreditAccount();
        userCreditAccountReq.setUserId(userId);
        userCreditAccountReq.setTotalAmount(creditAccountEntity.getAdjustAmount());
        userCreditAccountReq.setAccountStatus(AccountStatusVO.open.getCode());
        // 知识；仓储往上有业务语义，仓储往下到 dao 操作是没有业务语义的。所以不用在乎这块使用的字段名称，直接用持久化对象即可。
        userCreditAccountReq.setAvailableAmount(creditAccountEntity.getAdjustAmount());

        // 积分订单
        UserCreditOrder userCreditOrderReq = new UserCreditOrder();
        userCreditOrderReq.setUserId(creditOrderEntity.getUserId());
        userCreditOrderReq.setOrderId(creditOrderEntity.getOrderId());
        userCreditOrderReq.setTradeName(creditOrderEntity.getTradeName().getName());
        userCreditOrderReq.setTradeType(creditOrderEntity.getTradeType().getCode());
        userCreditOrderReq.setTradeAmount(creditOrderEntity.getTradeAmount());
        userCreditOrderReq.setOutBusinessNo(creditOrderEntity.getOutBusinessNo());

        String lockKey = Constants.RedisKey.USER_CREDIT_ACCOUNT_LOCK + userId + Constants.UNDERLINE + creditOrderEntity.getOutBusinessNo();
        RLock lock = redissonClient.getLock(lockKey);
        try {
            lock.lock(3, TimeUnit.SECONDS);
            transactionTemplate.execute(status -> {

                try {
//                    1.保存账户积分
                    UserCreditAccount userCreditAccount = userCreditAccountService.getAccountByUserId(userId);
                    if (userCreditAccount == null) {
                        userCreditAccountService.save(userCreditAccountReq);
                    } else {
                        userCreditAccountService.addAccount(userCreditAccountReq);
                    }
//                    2.保存积分订单
                    userCreditOrderService.save(userCreditOrderReq);
                } catch (DuplicateKeyException e) {
                    status.setRollbackOnly();
                    log.error("调整账户积分额度异常，唯一索引冲突 userId:{} orderId:{}", userId, creditOrderEntity.getOrderId(), e);
                } catch (Exception e) {
                    status.setRollbackOnly();
                    log.error("调整账户积分额度失败 userId:{} orderId:{}", userId, creditOrderEntity.getOrderId(), e);
                }
                return 1;

            });
        } finally {
            lock.unlock();
        }

    }

}
