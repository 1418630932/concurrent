package demo.reentrantLock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 死锁
 *
 * @author zhuliyang
 * @date 2020-04-05
 * @time 23:27
 **/
@Slf4j
public class DeadLock {
    //    public static void main(String[] args) {
//        Object o1 = new Object();
//        Object o2 = new Object();
//        new Thread(() ->{
//            synchronized (o1){
//                System.out.println("t1获得o1");
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                synchronized (o2){
//                    System.out.println("t1获得o2");
//                }
//            }
//        }).start();
//        new Thread(() ->{
//            synchronized (o2){
//                System.out.println("t2获得o2");
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                synchronized (o1){
//                    System.out.println("t2获得o1");
//                }
//            }
//        }).start();
//    }
    public static void main(String[] args) {
        ReentrantLock lock1 = new ReentrantLock();
        ReentrantLock lock2 = new ReentrantLock();
        new Thread(() -> {
            lock1.lock();
            log.info("loc1加锁");
            try {
                log.info("开始loc2加锁");
                if (!lock2.tryLock(3, TimeUnit.SECONDS)) {
                    log.info("loc2加锁失败");
                    return;
                }
                lock2.unlock();
                log.info("loc2解锁");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock1.lock();
            log.info("loc1解锁");
        }).start();
        new Thread(() -> {
            lock2.lock();
            log.info("loc2加锁");
            try {
                log.info("开始loc1加锁");
                if (!lock1.tryLock(3, TimeUnit.SECONDS)) {
                    log.info("loc1加锁失败");
                    return;
                }
                lock1.unlock();
                log.info("loc1解锁");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock2.lock();
            log.info("loc2解锁");
        }).start();
    }
}
