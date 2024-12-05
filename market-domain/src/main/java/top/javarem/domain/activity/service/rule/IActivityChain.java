package top.javarem.domain.activity.service.rule;

import top.javarem.domain.activity.service.rule.factory.DefaultActivityChainFactory;

/**
 * @Author: rem
 * @Date: 2024/12/02/14:23
 * @Description:
 */
public interface IActivityChain extends IActivityChainArmory {

    Boolean execute(DefaultActivityChainFactory.ActivityChainFilterVO chainFilterVO);

}
