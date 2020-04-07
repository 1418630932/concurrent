package demo.designPatten;

import lombok.extern.slf4j.Slf4j;

import java.sql.Date;

/**
 * @author zhuliyang
 * @date 2020-04-04
 * @time 23:51
 **/
@Slf4j
public class ProtectedStop {
     static  Object res;
    public static void main(String[] args) {
        Object lock = new Object();
        int watiTime = 10000;
        new Thread(() -> {
            log.debug("开始线程");
            long before  = System.currentTimeMillis();
            synchronized (lock) {
                while (res == null) {
                    long after =System.currentTimeMillis();
                    long currentWaitTime = after-before;
                    if (currentWaitTime>=watiTime)break;
                    try {
                        lock.wait(watiTime-currentWaitTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("开始干活");
            }

        }).start();
        new Thread(() -> {
            synchronized (lock) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                res = new Object();
                lock.notify();
            }
        }).start();
    }
}
