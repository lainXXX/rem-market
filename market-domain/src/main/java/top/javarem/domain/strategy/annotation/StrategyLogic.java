package top.javarem.domain.strategy.annotation;

import top.javarem.domain.strategy.service.rule.factory.DefaultLogicFactory.LogicModel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: rem
 * @Date: 2024/11/22/11:01
 * @Description:
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface StrategyLogic {

    LogicModel logicModel();

}
