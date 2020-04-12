package demo.cas;

import demo.util.Sleeper;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhuliyang
 * @date 2020-04-09
 * @time 22:55
 **/
public class CastTest {
    public static void main(String[] args) {
        Inner inner = new Inner();
        for (int i = 0; i <10; i++) {
            new Thread(()->{inner.calculate(prev -> ((prev*2)+3)/2 );}).start();
        }
        Sleeper.sleep(1);
        System.out.println(inner.get());
    }

    static class Inner {
        AtomicInteger i = new AtomicInteger(1);
        int get() {
            return i.get();
        }
        void add(int inc) {
            while (true) {
                int except = this.i.get();
                int update = except + inc;
                if (i.compareAndSet(except, update)) {
                    return;
                }
            }
        }
        void calculate(Eval eval) {
            while (true) {
                int except = this.i.get();
                int update = eval.calculate(except);//通过接口计算后的值
                if (i.compareAndSet(except, update)) {
                    return;
                }
            }
        }
    }
}
