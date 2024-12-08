package top.javarem.domain.activity.service.quota.rule;

import top.javarem.domain.activity.service.quota.rule.factory.DefaultActivityChainFactory;

/**
 * @Author: rem
 * @Date: 2024/12/02/14:23
 * @Description:
 */
public interface IActivityChain extends IActivityChainArmory {

    Boolean execute(DefaultActivityChainFactory.ActivityChainFilterVO chainFilterVO);

}
