package top.javarem.domain.activity.service.quota.rule;

/**
 * @Author: rem
 * @Date: 2024/12/02/14:26
 * @Description:活动责任链抽象类
 */
public abstract class AbstractActivityChain implements IActivityChain {

    private IActivityChain next;

    @Override
    public IActivityChain next() {
        return this.next;
    }

    @Override
    public void appendNext(IActivityChain next) {
        this.next = next;
    }

}
