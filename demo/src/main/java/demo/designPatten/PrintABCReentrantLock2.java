package demo.designPatten;

import demo.util.Sleeper;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zhuliyang
 * @date 2020-04-06
 * @time 22:56
 **/
public class PrintABCReentrantLock2 {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        Condition condition1 = lock.newCondition();
        Condition condition2 = lock.newCondition();
        Condition condition3 = lock.newCondition();
        new Thread(() -> {
            lock.lock();
            for (int i = 0; i < 5; i++) {
                try {
                    condition1.await();
                    System.out.print("A");
                    condition2.signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            lock.unlock();
        }).start();
        new Thread(() -> {
            lock.lock();
            for (int i = 0; i < 5; i++) {
                try {
                    condition2.await();
                    System.out.print("B");
                    condition3.signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            lock.unlock();
        }).start();
        new Thread(() -> {
            lock.lock();
            for (int i = 0; i < 5; i++) {
                try {
                    condition3.await();
                    System.out.print("C");
                    condition1.signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            lock.unlock();
        }).start();
        Sleeper.sleep(1);
        lock.lock();
        condition1.signal();
        lock.unlock();
    }
}
