package demo.cas;

/**
 * @author zhuliyang
 * @date 2020-04-09
 * @time 23:35
 **/
@FunctionalInterface
public interface Eval {
    //接口的计算过程由实现类来实现
    int calculate(int prev);
}
