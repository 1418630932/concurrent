package demo.testDemo;

import java.util.concurrent.atomic.AtomicInteger;

/**活锁
 * @author zhuliyang
 * @date 2020-04-05
 * @time 23:29
 **/
public class AliveLock {
    public static void main(String[] args) {
        AtomicInteger i = new AtomicInteger(50);
        new Thread(() ->{
            while (i.get() <100){
                System.out.println("t1"+i.get());
                i.getAndIncrement();
            }
        }).start();
        new Thread(() ->{
            while (i.get() >0){
                System.out.println("t2"+i.get());
                i.getAndDecrement();
            }
        }).start();
    }
}
