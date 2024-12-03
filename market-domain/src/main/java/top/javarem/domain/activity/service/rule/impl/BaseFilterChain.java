package top.javarem.domain.activity.service.rule.impl;

import org.springframework.stereotype.Component;
import top.javarem.domain.activity.service.rule.IActivityChain;

/**
 * @Author: rem
 * @Date: 2024/12/02/21:35
 * @Description:
 */
@Component("activity_base_filter")
public class BaseFilterChain implements IActivityChain {
    @Override
    public void execute() {

    }

    @Override
    public IActivityChain next() {
        return null;
    }

    @Override
    public void appendNext(IActivityChain next) {

    }
}
