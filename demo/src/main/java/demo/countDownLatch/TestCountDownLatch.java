package demo.countDownLatch;

import demo.util.Sleeper;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

/**
 * @author zhuliyang
 * @date 2020-04-25
 * @time 21:07
 **/
@Slf4j
public class TestCountDownLatch {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);
        log.debug("开始等待");
        new Thread(() ->{
            Sleeper.sleep(3);
            latch.countDown();
            log.debug("stop");
        }).start();
        new Thread(() ->{
            Sleeper.sleep(3);
            latch.countDown();
            log.debug("stop");
        }).start();
        new Thread(() ->{
            Sleeper.sleep(3);
            latch.countDown();
            log.debug("stop");
        }).start();
        new Thread(() ->{
            try {
                log.debug("等待1111");
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("等待结束1111");
        }).start();
        new Thread(() ->{
            try {
                log.debug("等待2222");
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("等待结束2222");
        }).start();
        latch.await();
        Sleeper.sleep(3);
        log.debug("等待结束");



    }
}
