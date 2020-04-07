package demo.designPatten;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zhuliyang
 * @date 2020-04-06
 * @time 22:56
 **/
public class PrintABCReentrantLock {
    int flag = 1;
    ReentrantLock lock = new ReentrantLock();
    Condition condition = lock.newCondition();

    public void print(int cur, int next, String str) {
        for (int i = 0; i < 5; i++) {
            try {
                lock.lock();
                while (flag!=cur){
                    condition.await();
                }
                System.out.print(str);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                condition.signalAll();
                flag = next;
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        PrintABCReentrantLock p = new PrintABCReentrantLock();
        new Thread(() ->{p.print(1,2 , "A");}).start();
        new Thread(() ->{p.print(2,3 , "B");}).start();
        new Thread(() ->{p.print(3,1 , "C");}).start();
    }
}
