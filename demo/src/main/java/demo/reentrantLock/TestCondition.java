package demo.reentrantLock;

import demo.util.Sleeper;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zhuliyang
 * @date 2020-04-06
 * @time 21:38
 **/
@Slf4j
public class TestCondition {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        Condition condition1 = lock.newCondition();
        Condition condition2 = lock.newCondition();
        new Thread(() ->{
            lock.lock();
            try {
                log.debug("开始等烟");
                condition1.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("开始抽烟");
            lock.unlock();
        }).start();
        new Thread(() ->{
            lock.lock();
            try {
                log.debug("开始等外卖");
                condition2.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("开始吃外卖");
            lock.unlock();
        }).start();
        Sleeper.sleep(3);
        lock.lock();
        log.debug("烟来了");
        condition1.signal();
        log.debug("外卖来了");
        condition2.signal();
        lock.unlock();
    }
}
