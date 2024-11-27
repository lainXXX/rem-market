package top.javarem.domain.strategy.service.rule.tree.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.javarem.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import top.javarem.domain.strategy.service.rule.tree.ILogicTreeNode;
import top.javarem.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import top.javarem.domain.strategy.service.rule.tree.factory.DefaultTreeFactory.TreeActionEntity;

import javax.annotation.Resource;

/**
 * @Author: rem
 * @Date: 2024/11/26/14:43
 * @Description:
 */
@Component("rule_lock")
@Slf4j
public class LockLogicTreeNode implements ILogicTreeNode {

//    @Resource
//    private IStrategyRepository repository;

    private Long userRaffleCount = 0L;

    /**
     * 判断用户抽奖次数有没有达标 该方法只做判断 返回兜底奖品操作交给下一节点
     * @param strategyId
     * @param awardId
     * @param ruleValue
     * @return
     */
    @Override
    public TreeActionEntity execute(Long strategyId, Integer awardId, String ruleValue) {

//        定义解锁奖品次数变量
        Long unlockAwardCount = 0L;

        try {
            unlockAwardCount = Long.parseLong(ruleValue);
        } catch (Exception e) {
            throw new RuntimeException("规则过滤-次数锁异常 ruleValue: " + ruleValue + " 配置不正确");
        }
        if (userRaffleCount > unlockAwardCount ) return TreeActionEntity.builder()
                .ruleLogicCheckTypeVO(RuleLogicCheckTypeVO.PASS)
                .build();
        log.info("规则过滤-奖品锁 用户积分未达到奖品解锁标准");
        return TreeActionEntity.builder()
                .ruleLogicCheckTypeVO(RuleLogicCheckTypeVO.BLOCK)
                .build();
    }
}
