package top.javarem.domain.activity.service.quota.rule;

/**
 * @Author: rem
 * @Date: 2024/12/02/14:24
 * @Description: 装配活动责任链
 */
public interface IActivityChainArmory {

    IActivityChain next();

    void appendNext(IActivityChain next);

}
