package top.javarem.domain.strategy.service.rule.chain;

/**
 * @Author: rem
 * @Date: 2024/11/25/21:13
 * @Description:责任链
 */
public abstract class AbstractStrategyLogicChain implements IStrategyLogicLogicChain{
//    定义下一节点  注意当前节点并不定义在调用AbstractStrategyLogicChain的任何方法中 所以这个类的属性是下一节点的属性
    private IStrategyLogicLogicChain next;

    @Override
    public IStrategyLogicLogicChain next() {
        return this.next;
    }

//    当一个节点调用该方法时 会把参数中的nextNode赋值给next
    @Override
    public IStrategyLogicLogicChain appendNode(IStrategyLogicLogicChain nextNode) {
        this.next = nextNode;
        return next;
    }

    protected abstract String getRuleModel();
}
