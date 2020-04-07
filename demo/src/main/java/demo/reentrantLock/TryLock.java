package demo.reentrantLock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @author zhuliyang
 * @date 2020-04-06
 * @time 18:24
 **/
@Slf4j
public class TryLock {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock(true);
        new Thread(() ->{
            if (!lock.tryLock()){
                log.info("获取锁失败");
                return;
            }
            try {
                log.info("加锁成功");
            }finally {
                lock.unlock();
                log.info("解锁成功");
            }
        }).start();


        new Thread(() ->{
            try {
                log.info("开始获取锁");
                if (!lock.tryLock(3, TimeUnit.SECONDS)){
                    log.info("获取锁失败");
                    return;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                lock.lock();
                log.info("加锁成功");
            }finally {
                lock.unlock();
                log.info("解锁成功");
            }
        }).start();
    }
}
