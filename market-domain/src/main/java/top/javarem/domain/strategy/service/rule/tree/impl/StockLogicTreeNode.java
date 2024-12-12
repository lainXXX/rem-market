package top.javarem.domain.strategy.service.rule.tree.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.javarem.domain.strategy.model.vo.AwardStockDecrQueueVO;
import top.javarem.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import top.javarem.domain.strategy.repository.IStrategyRepository;
import top.javarem.domain.strategy.service.armory.IStrategyArmoryDispatch;
import top.javarem.domain.strategy.service.rule.tree.ILogicTreeNode;

import static top.javarem.domain.strategy.service.rule.tree.factory.DefaultTreeFactory.*;

/**
 * @Author: rem
 * @Date: 2024/11/26/14:44
 * @Description:
 */
@Component("rule_stock")
@Slf4j
public class StockLogicTreeNode implements ILogicTreeNode {

    @Autowired
    private IStrategyArmoryDispatch dispatch;

    @Autowired
    private IStrategyRepository repository;

    @Override
    public TreeActionEntity execute(String userId, Long strategyId, Integer awardId, String ruleValue) {

        Boolean isDeduct = dispatch.deductAwardCount(strategyId, awardId);
        if (!isDeduct) {
            log.error("规则树过滤-扣减库存失败");
            return TreeActionEntity.builder()
                    .ruleLogicCheckTypeVO(RuleLogicCheckTypeVO.BLOCK)
                    .build();
        }
//        将奖品信息交给延迟队列 之后可在job中统一处理数据库中扣减库存操作
        repository.sendAwardStockDecrQueue(AwardStockDecrQueueVO.builder()
                .strategyId(strategyId)
                .awardId(awardId)
                .build());

        return TreeActionEntity.builder()
                .ruleLogicCheckTypeVO(RuleLogicCheckTypeVO.PASS)
                .logicAwardVO(LogicAwardVO.builder()
                        .awardId(awardId)
                        .ruleModel(ruleValue)
                        .build())
                .build();
    }


}
