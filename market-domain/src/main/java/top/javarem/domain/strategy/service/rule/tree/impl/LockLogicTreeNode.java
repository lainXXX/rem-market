package top.javarem.domain.strategy.service.rule.tree.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.javarem.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import top.javarem.domain.strategy.repository.IStrategyRepository;
import top.javarem.domain.strategy.service.rule.tree.ILogicTreeNode;
import top.javarem.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import top.javarem.domain.strategy.service.rule.tree.factory.DefaultTreeFactory.TreeActionEntity;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: rem
 * @Date: 2024/11/26/14:43
 * @Description:
 */
@Component("rule_lock")
@Slf4j
public class LockLogicTreeNode implements ILogicTreeNode {

    @Autowired
    private IStrategyRepository repository;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 判断用户抽奖次数有没有达标 该方法只做判断 返回兜底奖品操作交给下一节点
     * @param userId 用户id
     * @param strategyId  策略id
     * @param awardId 奖品id
     * @param ruleValue 奖品解锁次数值
     * @param endTime
     * @return
     */
    @Override
    public TreeActionEntity execute(String userId, Long strategyId, Integer awardId, String ruleValue, Date endTime) {

//        定义解锁奖品次数变量
        Integer unlockAwardCount;

        try {
            unlockAwardCount = Integer.parseInt(ruleValue);
        } catch (Exception e) {
            throw new RuntimeException("规则树过滤-奖品锁异常 ruleValue: " + ruleValue + " 配置不正确");
        }
//        查询用户当日抽奖次数
        String today = dateFormat.format(new Date());
        Integer userRaffleCount = repository.queryUserTodayRaffleCount(userId, strategyId, today);
        if (userRaffleCount > unlockAwardCount ) return TreeActionEntity.builder()
                .ruleLogicCheckTypeVO(RuleLogicCheckTypeVO.PASS)
                .build();
        log.info("规则树过滤-奖品锁 用户积分未达到奖品解锁标准");
        return TreeActionEntity.builder()
                .ruleLogicCheckTypeVO(RuleLogicCheckTypeVO.BLOCK)
                .build();
    }

}
