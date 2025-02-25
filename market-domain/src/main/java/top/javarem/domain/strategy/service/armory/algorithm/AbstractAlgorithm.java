package top.javarem.domain.strategy.service.armory.algorithm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import top.javarem.domain.strategy.repository.IStrategyRepository;

import java.security.SecureRandom;

/**
 * @Author: rem
 * @Date: 2025/02/03/20:12
 * @Description:抽奖策略算法抽象类
 */
public abstract class AbstractAlgorithm implements IAlgorithm {

    @Autowired
    protected IStrategyRepository repository;

    protected final SecureRandom secureRandom = new SecureRandom();

    @Getter
    @AllArgsConstructor
    public enum Algorithm{
        O1("o1Algorithm"),
        OLogN("oLogNAlgorithm"),
        ;

        private String key;
    }
}
