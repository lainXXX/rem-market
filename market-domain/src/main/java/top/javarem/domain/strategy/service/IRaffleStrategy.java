package top.javarem.domain.strategy.service;

import top.javarem.domain.strategy.model.entity.RaffleAwardEntity;
import top.javarem.domain.strategy.model.entity.RaffleFactorEntity;

/**
 * @Author: rem
 * @Date: 2024/11/21/17:52
 * @Description: 策略抽奖接口
 */
public interface IRaffleStrategy {

    RaffleAwardEntity performRaffle(RaffleFactorEntity factor);

}
