package demo.semaPhore;

import demo.util.Sleeper;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Semaphore;

/**
 * @author zhuliyang
 * @date 2020-04-24
 * @time 23:06
 **/
@Slf4j
public class SemaphoreTest {
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(3);
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    semaphore.acquire();
                    log.debug("开始");
                    Sleeper.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    log.debug("结束");
                    semaphore.release();
                }
            }).start();
        }
    }
}
