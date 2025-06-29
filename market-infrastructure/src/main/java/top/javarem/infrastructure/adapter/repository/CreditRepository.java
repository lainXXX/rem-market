package top.javarem.infrastructure.adapter.repository;

import com.google.gson.Gson;
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
import top.javarem.domain.credit.model.entity.TaskEntity;
import top.javarem.domain.credit.model.vo.TradeTypeVO;
import top.javarem.domain.credit.reposiotry.ICreditRepository;
import top.javarem.infrastructure.dao.entity.Task;
import top.javarem.infrastructure.dao.entity.UserCreditAccount;
import top.javarem.infrastructure.dao.entity.UserCreditOrder;
import top.javarem.infrastructure.dao.iService.TaskService;
import top.javarem.infrastructure.dao.iService.UserCreditAccountService;
import top.javarem.infrastructure.dao.iService.UserCreditOrderService;
import top.javarem.types.common.constants.Constants;
import top.javarem.types.enums.ResponseCode;
import top.javarem.types.exception.AppException;

import java.util.Date;
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
    private final TaskRepository taskRepository;

    public CreditRepository(RedissonClient redissonClient, UserCreditOrderService userCreditOrderService, UserCreditAccountService userCreditAccountService, TransactionTemplate transactionTemplate, TaskRepository taskRepository) {
        this.redissonClient = redissonClient;
        this.userCreditOrderService = userCreditOrderService;
        this.userCreditAccountService = userCreditAccountService;
        this.transactionTemplate = transactionTemplate;
        this.taskRepository = taskRepository;
    }


    @Override
    public void saveUserCreditTradeOrder(TradeAggregate tradeAggregate) {

        String userId = tradeAggregate.getUserId();
        CreditAccountEntity creditAccountEntity = tradeAggregate.getCreditAccountEntity();
        CreditOrderEntity creditOrderEntity = tradeAggregate.getCreditOrderEntity();
        TaskEntity taskEntity = tradeAggregate.getTaskEntity();

        // 积分账户
        UserCreditAccount userCreditAccountReq = new UserCreditAccount();
        userCreditAccountReq.setUserId(userId);
        userCreditAccountReq.setAccountStatus(AccountStatusVO.open.getCode());
//        账户积分再判断时再设置

        // 积分订单
        UserCreditOrder userCreditOrderReq = new UserCreditOrder();
        userCreditOrderReq.setUserId(creditOrderEntity.getUserId());
        userCreditOrderReq.setOrderId(creditOrderEntity.getOrderId());
        userCreditOrderReq.setTradeName(creditOrderEntity.getTradeName().getName());
        userCreditOrderReq.setTradeType(creditOrderEntity.getTradeType().getCode());
        userCreditOrderReq.setTradeAmount(creditOrderEntity.getTradeAmount());
        userCreditOrderReq.setOutBusinessNo(creditOrderEntity.getOutBusinessNo());

//        任务对象
        Task task = new Task();
        task.setUserId(taskEntity.getUserId());
        task.setTopic(taskEntity.getTopic());
        task.setMessageId(taskEntity.getMessageId());
        task.setMessage(new Gson().toJson(taskEntity.getMessage()));
        task.setStatus(taskEntity.getStatus().getCode());
        task.setCreateTime(new Date());
        task.setUpdateTime(new Date());

        String lockKey = Constants.RedisKey.USER_CREDIT_ACCOUNT_LOCK + userId + Constants.UNDERLINE + creditOrderEntity.getOutBusinessNo();
        RLock lock = redissonClient.getLock(lockKey);
        Integer result;
        try {
            lock.lock(3, TimeUnit.SECONDS);
            result = transactionTemplate.execute(status -> {

                try {
//                    1.保存账户积分
                    UserCreditAccount userCreditAccount = userCreditAccountService.getAccountByUserId(userId);
//                    正向交易和反向交易分别判断
//                    2.反向交易判断
                    if (userCreditOrderReq.getTradeType().equals(TradeTypeVO.RESERVE.getCode())) {
//                        2.1反向交易 积分账户请求对象的积分值设置为负数 扣减操作
                        userCreditAccountReq.setTotalAmount(creditAccountEntity.getAdjustAmount().negate());
                        // 知识；仓储往上有业务语义，仓储往下到 dao 操作是没有业务语义的。所以不用在乎这块使用的字段名称，直接用持久化对象即可。
                        userCreditAccountReq.setAvailableAmount(creditAccountEntity.getAdjustAmount().negate());
//                        2.2扣减积分时 如果账户不存在或账户积分少于商品积分 则交易失败
                        if (userCreditAccount == null || userCreditAccountReq.getAvailableAmount().compareTo(userCreditAccount.getAvailableAmount()) == 1) {
                            log.error("{} {}", ResponseCode.ERROR_CREDIT_ACCOUNT.getCode(), ResponseCode.ERROR_CREDIT_ACCOUNT.getInfo());
                            status.setRollbackOnly();
                            return -1;
                        }
//                        2.3 扣减账户积分
                        boolean addAccount = userCreditAccountService.addAccount(userCreditAccountReq);
                        if (!addAccount) {
                            status.setRollbackOnly();
                            return 1;
                        }
                    }
//                    3.正向交易
                    else {
//                        3.1 设置账户积分值
                        userCreditAccountReq.setTotalAmount(creditAccountEntity.getAdjustAmount());
                        // 知识；仓储往上有业务语义，仓储往下到 dao 操作是没有业务语义的。所以不用在乎这块使用的字段名称，直接用持久化对象即可。
                        userCreditAccountReq.setAvailableAmount(creditAccountEntity.getAdjustAmount());
//                        3.2 判断账户是否存在 不存在则创建
                        if (userCreditAccount == null) {
                            userCreditAccountService.save(userCreditAccountReq);
                        } else {
                            boolean addAccount = userCreditAccountService.addAccount(userCreditAccountReq);
                            if (!addAccount) {
                                status.setRollbackOnly();
                                return 1;
                            }
                        }
                    }
//                    2.保存积分订单
                    userCreditOrderService.save(userCreditOrderReq);
//                    3.保存任务
                    taskRepository.saveTask(task);
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

        if (result == 1) {
            try {
//            发送任务信息到MQ (签到时的积分返利不需要发送)
                if ( !creditOrderEntity.getOutBusinessNo().contains("integral")){
                    taskRepository.sendMessage(task.getTopic(), task.getMessage());
                }
//            更新任务状态为完成
                taskRepository.updateTaskCompleted(task.getMessageId());
                log.info("调整账户积分记录，发送MQ消息完成 userId: {} orderId:{} topic: {}", userId, creditOrderEntity.getOrderId(), task.getTopic());
            } catch (Exception e) {
                log.error("调整账户积分记录，发送MQ消息失败 userId: {} topic: {}", userId, task.getTopic());
                taskRepository.updateTaskFailed(task.getMessageId());
            }
        }

    }

    @Override
    public CreditAccountEntity queryUserCredit(String userId) {

        UserCreditAccount userCreditAccount = userCreditAccountService.queryUserCredit(userId);
        return CreditAccountEntity.builder()
                .userId(userCreditAccount.getUserId())
                .adjustAmount(userCreditAccount.getAvailableAmount())
                .build();

    }

}
